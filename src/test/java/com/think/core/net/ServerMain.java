package com.think.core.net;

import com.think.test.protobuf.PersonProtos;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

/**
 * ProtobufEncoder：非常简单，内部直接使用了message.toByteArray()将字节数据放入bytebuf中输出（out中，交由下一个encoder处理）。
 *
 * ProtobufVarint32LengthFieldPrepender：因为ProtobufEncoder只是将message的各个filed按照规则输出了，并没有serializedSize，所以socket无法判定package（封包）。这个Encoder的作用就是在ProtobufEncoder生成的字节数组前，prepender一个varint32数字，表示serializedSize。
 *
 * ProtobufVarint32FrameDecoder：这个decoder和Prepender做的工作正好对应，作用就是“成帧”，根据seriaziedSize读取足额的字节数组--一个完整的package。
 *
 * ProtobufDecoder：和ProtobufEncoder对应，这个Decoder需要指定一个默认的instance，decoder将会解析byteArray，并根据format规则为此instance中的各个filed赋值。
 */
public class ServerMain {
    public static void main(String[] args) {
        //bossGroup : NIO selector threadPool
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        //workerGroup : socket data read-write worker threadPool
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()./*addLast(new ProtobufVarint32FrameDecoder())
                                    .addLast(new ProtobufDecoder(PersonProtos.Person.getDefaultInstance()))
                                    .addLast(new ProtobufVarint32LengthFieldPrepender())
                                    .addLast(new ProtobufEncoder())*/
                                    addLast(new CustomProtobufDecoder())
                                    .addLast(new ProtobufServerHandler());//自定义handler
                        }
                    }).childOption(ChannelOption.TCP_NODELAY, true);
            System.out.println("begin");
            //bind到本地的18080端口
            ChannelFuture future = bootstrap.bind(18080).sync();
            //阻塞，直到channel.close
            future.channel().closeFuture().sync();
            System.out.println("end");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //辅助线程优雅退出
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
