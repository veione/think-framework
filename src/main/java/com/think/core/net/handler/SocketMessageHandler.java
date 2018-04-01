package com.think.core.net.handler;

import com.think.core.Session;
import com.think.core.net.message.RequestWrapper;
import com.think.service.SessionManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import akka.actor.ActorRef;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.ChannelHandler.Sharable;

@Sharable
public final class SocketMessageHandler extends SimpleChannelInboundHandler<List<RequestWrapper>> {
    private final Logger logger = LoggerFactory.getLogger(SocketMessageHandler.class);
    private final ActorRef dispatcherActor;

    public SocketMessageHandler(ActorRef dispatcherActor) {
        this.dispatcherActor = dispatcherActor;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        Session session = SessionManager.getInstance().getSession(ctx.channel());
        if (session == null) {
            session = Session.newSession(ctx.channel());
            SessionManager.getInstance().addSession(session);
        }
        logger.debug("Current session size {}", SessionManager.getInstance().getSessionCount());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        SessionManager.getInstance().removeSession(ctx.channel());
        logger.debug("Current session size {}", SessionManager.getInstance().getSessionCount());
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
        if (logger.isDebugEnabled()) {
            logger.debug("handler user triggered");
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, List<RequestWrapper> msg) {
        if (logger.isDebugEnabled()) {
            logger.debug("handler channel channelRead0");
        }
        dispatcherActor.tell(msg, dispatcherActor);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        SessionManager.getInstance().removeSession(ctx.channel());
        logger.error("Handler channel exception", cause);
    }
}
