package com.think.core.net.server;

import com.think.core.actor.DispatcherActor;
import com.think.core.net.Server;
import com.think.core.net.codec.ProtocolDecoder;
import com.think.core.net.codec.ProtocolEncoder;
import com.think.core.net.handler.SocketMessageHandler;
import com.think.service.HandlerManager;
import com.think.service.MessageManager;
import com.think.service.SessionManager;
import com.think.util.IdWorker;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class SocketServer extends AbstractServer {
    private ActorSystem actorSystem;
    private ActorRef dispatcherActor;

    @Override
    public void onStart() {
        actorSystem = ActorSystem.create("think");
        dispatcherActor = actorSystem.actorOf(DispatcherActor.props());
    }

    @Override
    public void shutdown() {
        // TODO Auto-generated method stub

    }

    @Override
    public ChannelInitializer<SocketChannel> newSocketHandler() {
        return new ChannelInitializer<SocketChannel>() {

            @Override
            protected void initChannel(SocketChannel ch) {
                ChannelPipeline pipeline = ch.pipeline();
                // Decoders
                //pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(1048576, 0, 4, 0, 4));
                pipeline.addLast("protobufDecoder", new ProtocolDecoder(true));
                // Encoder
                // pipeline.addLast("frameEncoder", new LengthFieldPrepender(4));
                pipeline.addLast("protobufEncoder", new ProtocolEncoder(true));
                // Handler
                pipeline.addLast("businessHandler", new SocketMessageHandler(dispatcherActor));
            }
        };
    }

    public static void main(String[] args) {
        // 设置log4j2为完全异步模式
        System.setProperty("Log4jContextSelector", "org.apache.logging.log4j.core.async.AsyncLoggerContextSelector");
        SessionManager.getInstance().setIdWorker(new IdWorker(12));
        MessageManager.load("com.think");
        HandlerManager.load("com.think");
        Server server = new SocketServer();
        server.bind(9090);
        server.start();
    }
}
