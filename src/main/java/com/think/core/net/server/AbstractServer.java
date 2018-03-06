package com.think.core.net.server;

import java.net.InetAddress;
import java.net.UnknownHostException;

import com.think.common.ThinkThreadFactory;
import com.think.core.net.Server;
import com.think.core.net.util.IOStatistics;
import com.think.util.EventLoopUtil;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.SocketChannel;

/**
 * 抽象服务器类
 * 
 * @author veione
 *
 */
public abstract class AbstractServer implements Server {
	protected ServerBootstrap severBoot;
	protected EventLoopGroup acceptorGroup;
	protected EventLoopGroup workerGroup;
	protected SocketChannel channel;
	protected volatile boolean isRunning = false;
	protected InetAddress inetAddress;
	protected int port;

	@Override
	public Server bind(InetAddress inet, int port) {
		this.inetAddress = inet;
		this.port = port;
		return this;
	}

	@Override
	public Server bind(String inet, int port) {
		try {
			return bind(InetAddress.getByName(inet), port);
		} catch (UnknownHostException e) {
			throw new IllegalArgumentException(inet);
		}
	}

	@Override
	public Server bind(int port) {
		return bind("localhost", port);
	}

	@Override
	public void restart() {
		// TODO Auto-generated method stub

	}

	@Override
	public void start() {
		isRunning = true;
		onStart();
		ThinkThreadFactory eventLoopThreadFactory = new ThinkThreadFactory("think-eventloop-thread-");
		ThinkThreadFactory acceptorEventLoopThreadFactory = new ThinkThreadFactory("think-acceptor-thread-");
		severBoot = new ServerBootstrap();
		acceptorGroup = EventLoopUtil.getTransportGroup(1, acceptorEventLoopThreadFactory);
		workerGroup = EventLoopUtil.getTransportGroup(EventLoopUtil.getCoreNum() * 2, eventLoopThreadFactory);

		try {
			this.severBoot.group(acceptorGroup, workerGroup).channel(EventLoopUtil.getServerSocketChannel())
					.option(ChannelOption.TCP_NODELAY, true)
					.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
					.option(ChannelOption.SO_REUSEADDR, true).option(ChannelOption.SO_TIMEOUT, 6000)
					.option(ChannelOption.SO_BACKLOG, 512).childOption(ChannelOption.CONNECT_TIMEOUT_MILLIS, 6000)
					.childOption(ChannelOption.SO_KEEPALIVE, true)
					.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
					.childHandler(newSocketHandler());
			ChannelFuture f = severBoot.bind(this.port).sync();
			f.addListener(new ChannelFutureListener() {
				@Override
				public void operationComplete(ChannelFuture cf) throws Exception {
					if (cf.isSuccess()) {
						
					}
				}
			});
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			if (acceptorGroup != null) {
				acceptorGroup.shutdownGracefully();
			}
			if (workerGroup != null) {
				workerGroup.shutdownGracefully();
			}
		}

		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				shutdown();
			}
		});
	}

	/**
	 * 当新客户端连接进来进行处理的方法,由其具体子类实现
	 * 
	 * @return
	 */
	public abstract ChannelHandler newSocketHandler();

	/**
	 * 服务器启动之前触发的方法
	 */
	public abstract void onStart();

	/**
	 * 服务器重启触发的方法
	 */
	public void onRestart() {
		// TODO
	}

	/**
	 * 服务器关闭时调用的方法
	 */
	public void shutdown() {
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
