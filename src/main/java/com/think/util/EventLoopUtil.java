package com.think.util;

import java.util.concurrent.ThreadFactory;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class EventLoopUtil {

	public static boolean epollIsAvailable() {
		return Epoll.isAvailable();
	}

	public static int getCoreNum() {
		return Runtime.getRuntime().availableProcessors();
	}

	public static EventLoopGroup getTransportGroup() {
		return getTransportGroup(0);
	}

	public static EventLoopGroup getTransportGroup(int numOfThread) {
		return getTransportGroup(numOfThread, null);
	}

	public static EventLoopGroup getTransportGroup(int numOfThread, ThreadFactory threadFactory) {
		if (epollIsAvailable()) {
			return threadFactory != null ? new EpollEventLoopGroup(numOfThread, threadFactory)
					: new EpollEventLoopGroup(numOfThread);
		} else {
			return threadFactory != null ? new NioEventLoopGroup(numOfThread, threadFactory)
					: new NioEventLoopGroup(numOfThread);
		}
	}

	public static Class<? extends ServerSocketChannel> getServerSocketChannel() {
		if (epollIsAvailable()) {
			return EpollServerSocketChannel.class;
		} else {
			return NioServerSocketChannel.class;
		}
	}
}
