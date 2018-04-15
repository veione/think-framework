package com.think.core.net;

import com.think.exception.MessageHandlerException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@Sharable
public abstract class AbstractMessageHandler<I> extends SimpleChannelInboundHandler<I> {

    protected final Logger logger = LoggerFactory.getLogger(AbstractMessageHandler.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        onSessionActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        onSessionIdle(ctx);
    }

    /**
     * 当失去客户端会话时调用
     */
    protected abstract void onSessionIdle(ChannelHandlerContext ctx);

    /**
     * 当有客户端会话时调用
     */
    protected abstract void onSessionActive(ChannelHandlerContext ctx);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, I msg) {
        onMessage(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (cause instanceof IOException) {
            ctx.close();
            logger.error("Handler message exception {}", cause);
        } else {
            throw new MessageHandlerException(ctx, cause);
        }
    }

    /**
     * 当来自客户端消息时调用
     *
     * @param msg 消息对象
     */
    public abstract void onMessage(I msg);
}
