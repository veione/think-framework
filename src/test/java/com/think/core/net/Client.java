package com.think.core.net;

import com.think.test.protobuf.PersonProtos;

import java.net.InetSocketAddress;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

public class Client {
    public static void main(String[] args) throws Exception{
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workerGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS,10000)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            System.out.println("initChannel");
                            ch.pipeline().addLast(new ProtobufVarint32FrameDecoder())
                                    .addLast(new ProtobufDecoder(PersonProtos.Person.getDefaultInstance()))
                                    .addLast(new ProtobufVarint32LengthFieldPrepender())
                                    .addLast(new ProtobufEncoder())
                                    .addLast(new ProtobufClientHandler());
                        }
                    });
            ChannelFuture future = bootstrap.connect(new InetSocketAddress("127.0.0.1", 18080));
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
