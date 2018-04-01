package com.think.core.net.netty;

import com.google.common.collect.Lists;
import com.google.protobuf.MessageLite;

import com.think.core.net.message.RequestWrapper;
import com.think.core.net.message.ResponseWrapper;
import com.think.core.net.security.EncryptionDecryption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 协议编码器
 */
public class ProtocolEncoder extends MessageToByteEncoder<Object> {
    private final Logger logger = LoggerFactory.getLogger(ProtocolEncoder.class);
    private final EncryptionDecryption enc;
    private final boolean isEncryption;

    public ProtocolEncoder(boolean isEncryption) {
        if (isEncryption) {
            this.isEncryption = true;
            this.enc = new EncryptionDecryption();
        } else {
            this.isEncryption = false;
            this.enc = null;
        }
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        if (msg instanceof List) {
            List<ResponseWrapper> responseList = (List<ResponseWrapper>) msg;
            writeMessage(out, responseList);
        } else if (msg instanceof ResponseWrapper) {
            List<ResponseWrapper> responseList = Lists.newArrayList();
            responseList.add((ResponseWrapper) msg);
            writeMessage(out, responseList);
        }
    }

    /**
     * 写出消息
     *
     * @param out          消息缓冲对象
     * @param responseList 消息列表
     */
    private void writeMessage(ByteBuf out, List<ResponseWrapper> responseList) {
        int responseLength = responseList.size();
        out.writeShort(RequestWrapper.MAGIC_WORD);
        out.writeShort(responseLength);
        responseList.stream().forEach(r -> {
            out.writeShort(r.getResponseId());
            MessageLite payload = r.getPayload();
            byte[] data = payload.toByteArray();

            if (isEncryption) {
                data = enc.encrypt(data);
            }

            out.writeInt(data.length);
            if (data.length > 0) {
                out.writeBytes(data);
            }
        });
    }
}
