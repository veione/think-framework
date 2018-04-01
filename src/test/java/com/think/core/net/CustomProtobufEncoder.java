package com.think.core.net;

import com.google.common.collect.Lists;

import com.think.core.net.message.ResponseWrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

@Sharable
public class CustomProtobufEncoder extends MessageToByteEncoder<Object> {
    private final Logger logger = LoggerFactory.getLogger(CustomProtobufDecoder.class);

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        if (msg instanceof List) {
            List<com.think.core.net.message.ResponseWrapper> responseList = (List) msg;
            sendMessage(ctx, out, responseList);
        } else if (msg instanceof com.think.core.net.message.ResponseWrapper) {
            List<com.think.core.net.message.ResponseWrapper> responseList = Lists.newArrayList();
            responseList.add((com.think.core.net.message.ResponseWrapper) msg);
            sendMessage(ctx, out, responseList);
        }
    }

    /**
     * 发送消息
     *
     * @param ctx
     * @param out          消息存储对象
     * @param responseList 待发送消息列表
     */
    private void sendMessage(ChannelHandlerContext ctx, ByteBuf out, List<ResponseWrapper> responseList) {
        List<ByteBuf> respBufList = Lists.newArrayList();
        responseList.forEach((ResponseWrapper resp) -> {
            byte[] responseBody = resp.getPayload().toByteArray();
            ByteBuf buf = ctx.alloc().buffer();
            if (responseBody.length >= 10240) {
                logger.warn(">>>>>> The message {} is too large, please check <<<<<<", resp.getPayload().getParserForType().getClass().getName());
            }
            buf.writeShort(resp.getResponseId());
            buf.writeBytes(responseBody);
            respBufList.add(buf);
        });
    }


}
