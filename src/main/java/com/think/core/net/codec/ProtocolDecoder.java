package com.think.net.codec;

import com.google.common.collect.Lists;
import com.google.protobuf.MessageLite;

import com.think.common.exception.IllegalProtocolException;
import com.think.net.message.RequestWrapper;
import com.think.net.security.EncryptionDecryption;
import com.think.net.service.MessageManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.zip.Adler32;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * 消息解码器
 */
public class ProtocolDecoder extends ByteToMessageDecoder {
    private final Logger logger = LoggerFactory.getLogger(ProtocolDecoder.class);
    private final EncryptionDecryption dec;
    private final boolean isEncryption;
    private static final boolean HAS_PARSER;

    static {
        boolean hasParser = false;
        try {
            // MessageLite.getParserForType() is not available until protobuf 2.5.0.
            MessageLite.class.getDeclaredMethod("getParserForType");
            hasParser = true;
        } catch (Throwable t) {
            // Ignore
        }

        HAS_PARSER = hasParser;
    }

    public ProtocolDecoder(boolean isEncryption) {
        if (isEncryption) {
            this.isEncryption = isEncryption;
            this.dec = new EncryptionDecryption();
        } else {
            this.isEncryption = false;
            this.dec = null;
        }
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // 标记当前缓冲区域的读索引位置
        in.markReaderIndex();

        //不可读情况
        int frameLength = in.readableBytes();
        if (!in.isReadable() || frameLength < RequestWrapper.MIN_FRAME_SIZE || frameLength > RequestWrapper.MAX_FRAME_SIZE) {
            ctx.channel().close();
            return;
        }

        short magicWord = in.readShort();
        if (magicWord != RequestWrapper.MAGIC_WORD) {
            in.discardReadBytes();
            return;
        }
        // 消息个数
        short messageNum = in.readShort();
        List<RequestWrapper> requests = Lists.newLinkedList();

        for (byte i = 0; i < messageNum; i++) {
            final short msgId = in.readShort();
            final int msgSize = in.readInt();
            final byte[] array;
            final int offset;
            final int length = in.readableBytes();

            if (msgSize <= 0 || length <= 0 || msgSize < length) {
                logger.debug("Invalid message size {} for message id {}", msgSize, msgId);
                return;
            }

            if (in.hasArray()) {
                array = in.array();
                offset = in.arrayOffset() + in.readerIndex();
            } else {
                array = new byte[length];
                in.getBytes(in.readerIndex(), array, 0, length);
                offset = 0;
            }
            // 跳过已经读取的字节
            in.skipBytes(length);

            final byte[] data;
            if (isEncryption) {
                data = decryption(array, offset, length);
            } else {
                data = array;
            }

            MessageLite prototype = MessageManager.getPrototype(msgId);
            MessageLite payload;
            if (prototype == null) {
                throw new IllegalProtocolException("Invalid message id {}" + msgId);
            }
            if (HAS_PARSER) {
                if (isEncryption) {
                    payload = prototype.getParserForType().parseFrom(data);
                } else {
                    payload = prototype.getParserForType().parseFrom(data, offset, length);
                }
            } else {
                payload = prototype.newBuilderForType().mergeFrom(data, offset, length).build();
            }

            RequestWrapper request = RequestWrapper.newBuilder();
            request.setRequestId(msgId);
            request.setPayload(payload);
            request.setChannel(ctx.channel());
            requests.add(request);
        }
        out.add(requests);
    }

    /**
     * 解密消息
     *
     * @param data 消息对象数据
     * @return 解密后数据
     */
    private byte[] decryption(byte[] data, int offset, int length) throws Exception {
        byte[] array = new byte[length];
        System.arraycopy(data, offset, array, 0, length);
        return dec.decrypt(array);
    }

    private boolean checksum(ByteBuf buf) {
        Adler32 adler32 = new Adler32();
        adler32.update(buf.array(), buf.arrayOffset() + buf.readerIndex(), buf.readableBytes() - 4);
        buf.markReaderIndex();
        buf.readerIndex(buf.writerIndex() - 4);
        int checksum = buf.readInt();
        buf.resetReaderIndex();
        return checksum == (int) adler32.getValue();
    }
}
