package com.think.common;

import java.net.InetAddress;

import com.think.core.net.server.IOStatistics;

public interface Server {

	Server bind(String inet, int port);

	Server bind(InetAddress inet, int port);

	Server bind(int port);

	void restart();

	void start();

	void onShutdown();

	/**
	 * IO监控对象
	 * 
	 * @return
	 */
	IOStatistics getIOStatistics();

	/**
	 * 获取服务器配置
	 * 
	 * @return
	 */
	ServerConfig getServerConfig();
}
