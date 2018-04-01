package com.think.core.net.netty;

import com.think.core.net.ProtobufClientHandler;
import com.think.service.HandlerManager;
import com.think.service.MessageManager;

import java.net.InetSocketAddress;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class Client {
    public static void main(String[] args) throws Exception {
        MessageManager.load("com.think");
        HandlerManager.load("com.think");

        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workerGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            System.out.println("initChannel");
                            ch.pipeline()
                                    .addLast(new ProtocolDecoder(true))
                                    .addLast(new ProtocolEncoder(true))
                                    .addLast(new ProtobufClientHandler());
                        }
                    });
            ChannelFuture future = bootstrap.connect(new InetSocketAddress("127.0.0.1", 9090));
            System.out.println("begin");
            future.channel().closeFuture().sync();
            System.out.println("Closed");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
        }

    }
}
