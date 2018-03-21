package com.think.core.net;

import com.google.common.collect.Lists;

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
            List<ResponseWrapper> responseList = (List) msg;
            sendMessage(ctx, out, responseList);
        } else if (msg instanceof ResponseWrapper) {
            List<ResponseWrapper> responseList = Lists.newArrayList();
            responseList.add((ResponseWrapper) msg);
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
            byte[] responseBody = resp.getResponseBody();
            ByteBuf buf = ctx.alloc().buffer();
            if (responseBody.length >= 10240) {
                logger.warn(">>>>>> The message {} is too large, please check <<<<<<", resp.getResponse().getDescriptorForType().getFullName());
            }
            buf.writeInt(resp.getResponseType());
            buf.writeBytes(responseBody);
            respBufList.add(buf);
        });
    }


}
