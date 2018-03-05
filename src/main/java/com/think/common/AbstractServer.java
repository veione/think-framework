package com.think.common;

import java.net.InetAddress;
import java.net.UnknownHostException;

import com.think.core.net.server.IOStatistics;
import com.think.util.EventLoopUtil;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.SocketChannel;

public abstract class AbstractServer implements Server {
	protected ServerBootstrap severBoot;
	protected EventLoopGroup acceptorEventLoopGroup;
	protected EventLoopGroup eventLoopGroup;
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
		acceptorEventLoopGroup = EventLoopUtil.getTransportGroup(1, acceptorEventLoopThreadFactory);
		eventLoopGroup = EventLoopUtil.getTransportGroup(EventLoopUtil.getCoreNum() * 2, eventLoopThreadFactory);

		try {
			this.severBoot.group(acceptorEventLoopGroup, eventLoopGroup).channel(EventLoopUtil.getServerSocketChannel())
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
			if (acceptorEventLoopGroup != null) {
				acceptorEventLoopGroup.shutdownGracefully();
			}
			if (eventLoopGroup != null) {
				eventLoopGroup.shutdownGracefully();
			}
		}

		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				onShutdown();
			}
		});
	}

	public abstract ChannelHandler newSocketHandler();

	public abstract void onStart();

	public void onRestart() {
		// TODO
	}

	public void onShutdown() {
		if (acceptorEventLoopGroup != null) {
			acceptorEventLoopGroup.shutdownGracefully();
		}
		if (eventLoopGroup != null) {
			eventLoopGroup.shutdownGracefully();
		}
		if (channel != null) {
			channel.close();
			channel = null;
		}
	}
	
	@Override
	public IOStatistics getIOStatistics() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public ServerConfig getServerConfig() {
		// TODO Auto-generated method stub
		return null;
	}
}
