package com.think.exception;

import io.netty.channel.ChannelHandlerContext;

public class MessageHandlerException extends WorldException {
    public MessageHandlerException() {
    }

    public MessageHandlerException(String message) {
        super(message);
    }

    public MessageHandlerException(String message, Throwable cause) {
        super(message, cause);
    }

    public MessageHandlerException(ChannelHandlerContext ctx, Throwable cause) {
        super("Message handler exception", cause);
        ctx.close();
    }
}
