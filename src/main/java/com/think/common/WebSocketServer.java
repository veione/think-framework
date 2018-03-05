package com.think.common;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class WebSocketServer extends AbstractServer {

	@Override
	public void onStart() {
		// TODO Auto-generated method stub

	}

	@Override
	public ChannelInitializer<SocketChannel> newSocketHandler() {
		return new ChannelInitializer<SocketChannel>() {

			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				ChannelPipeline p = ch.pipeline();

			}
		};
	}
}
