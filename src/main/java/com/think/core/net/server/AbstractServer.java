package com.think.core.net.server;

import java.net.InetAddress;
import java.net.UnknownHostException;

import com.think.common.ThinkThreadFactory;
import com.think.core.net.Server;
import com.think.core.net.util.IOStatistics;
import com.think.util.EventLoopUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * 抽象服务器类
 *
 * @author veione
 */
public abstract class AbstractServer implements Server {
    protected final Logger logger = LoggerFactory.getLogger(AbstractServer.class);
    protected ServerBootstrap severBoot;
    protected EventLoopGroup acceptorGroup;
    protected EventLoopGroup workerGroup;
    protected SocketChannel channel;
    protected volatile boolean isRunning = false;
    protected InetAddress inetAddress;
    protected int port;

    @Override
    public Server bind(InetAddress inert, int port) {
        this.inetAddress = inert;
        this.port = port;
        return this;
    }

    @Override
    public Server bind(String inert, int port) {
        try {
            return bind(InetAddress.getByName(inert), port);
        } catch (UnknownHostException e) {
            throw new IllegalArgumentException(inert);
        }
    }

    @Override
    public Server bind(int port) {
        return bind("localhost", port);
    }

    @Override
    public void restart() {
        start();
    }

    @Override
    public void start() {
        isRunning = true;
        onStart();
        ThinkThreadFactory workerThreadFactory = new ThinkThreadFactory("io-thread-");
        ThinkThreadFactory acceptorThreadFactory = new ThinkThreadFactory("acceptor-thread-");
        severBoot = new ServerBootstrap();
        acceptorGroup = EventLoopUtil.getTransportGroup(1, acceptorThreadFactory);
        workerGroup = EventLoopUtil.getTransportGroup(EventLoopUtil.getCoreNum() * 2, workerThreadFactory);

        try {
            this.severBoot.group(acceptorGroup, workerGroup).channel(EventLoopUtil.getServerSocketChannel())
                    .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                    .option(ChannelOption.SO_REUSEADDR, true)
                    .option(ChannelOption.SO_BACKLOG, 512)
                    .childOption(ChannelOption.CONNECT_TIMEOUT_MILLIS, 6000)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(newSocketHandler());
            ChannelFuture f = severBoot.bind(this.port).sync();
            f.addListener(cf -> {
                if (cf.isSuccess()) {
                    logger.info("Server start successfully on por {}", port);
                    // 添加服务器关闭钩子
                    Runtime.getRuntime().addShutdownHook(new Thread(() -> shutdown()));
                }
            });
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            logger.error("Server start failure", e);
        } finally {
            if (acceptorGroup != null) {
                acceptorGroup.shutdownGracefully();
            }
            if (workerGroup != null) {
                workerGroup.shutdownGracefully();
            }
        }
    }

    /**
     * 当新客户端连接进来进行处理的方法,由其具体子类实现
     */
    public abstract ChannelHandler newSocketHandler();

    /**
     * 服务器启动之前触发的方法
     */
    public void onStart() {
        logger.info("==================================");
        logger.info("           Server start           ");
        logger.info("==================================");
    }

    /**
     * 服务器重启触发的方法
     */
    public void onRestart() {
        logger.info("====================================");
        logger.info("           Server restart           ");
        logger.info("====================================");
    }

    /**
     * 服务器关闭时调用的方法
     */
    public void shutdown() {
        logger.info("=====================================");
        logger.info("           Server shutdown           ");
        logger.info("=====================================");
        if (acceptorGroup != null) {
            acceptorGroup.shutdownGracefully();
        }
        if (workerGroup != null) {
            workerGroup.shutdownGracefully();
        }
        if (channel != null) {
            channel.close();
            channel = null;
        }
    }

    @Override
    public IOStatistics getIOStatistics() {
        return new IOStatistics();
    }

    @Override
    public ServerConfig getServerConfig() {
        // TODO Auto-generated method stub
        return null;
    }
}
