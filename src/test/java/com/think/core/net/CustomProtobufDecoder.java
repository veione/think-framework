package com.think.core.net;

import com.google.protobuf.Message;
import com.google.protobuf.MessageLite;

import com.think.exception.IllegalProtocolException;

import java.util.List;
import java.util.zip.Adler32;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.handler.codec.MessageToMessageDecoder;

@Sharable
public class CustomProtobufDecoder extends MessageToMessageDecoder<ByteBuf> {
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

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        final byte[] array;
        final int offset;
        final int length = msg.readableBytes();
        final Message prototype = MessageManager.get(100);

        if (prototype == null) {
            throw new IllegalProtocolException();
        }

        if (msg.readableBytes() >= 10 && checksum(msg)) {

            Message message = prototype.newBuilderForType().mergeFrom(msg.array(),
                    msg.arrayOffset() + msg.readerIndex(),
                    msg.readableBytes() - 4).build();
        }
        //https://www.ibm.com/developerworks/cn/linux/l-cn-gpb/
        //https://github.com/chenshuo/muduo-protorpc/blob/cpp11/java/src/main/java/com/chenshuo/muduo/codec/ProtobufDecoder.java
        //http://blog.csdn.net/solstice/article/details/6300108
        //http://www.360doc.com/content/16/0129/15/15099545_531481464.shtml
        //https://www.cnblogs.com/sidesky/p/6913109.html
        //https://www.cnblogs.com/carl10086/p/6195568.html

        if (msg.hasArray()) {
            array = msg.array();
            offset = msg.arrayOffset() + msg.readerIndex();
        } else {
            array = new byte[length];
            msg.getBytes(msg.readerIndex(), array, 0, length);
            offset = 0;
        }

        if (HAS_PARSER) {
            out.add(prototype.getParserForType().parseFrom(array, offset, length));
        } else {
            out.add(prototype.newBuilderForType().mergeFrom(array, offset, length).build());
        }
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
