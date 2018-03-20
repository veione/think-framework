package com.think.core.net;

import com.google.protobuf.MessageLite;

import com.think.exception.IllegalProtocolException;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

@ChannelHandler.Sharable
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
        final MessageLite prototype = MessageManager.get(100);

        if (prototype == null) {
            throw new IllegalProtocolException();
        }
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
}
