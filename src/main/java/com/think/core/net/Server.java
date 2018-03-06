package com.think.core.net;

import java.net.InetAddress;

import com.think.core.net.server.ServerConfig;
import com.think.core.net.util.IOStatistics;

/**
 * Server接口类
 * 
 * @author veione
 *
 */
public interface Server {
	/**
	 * 绑定IP和端口号
	 * 
	 * @param ip
	 *            IP地址
	 * @param port
	 *            端口号
	 * @return
	 */
	Server bind(String ip, int port);

	/**
	 * 绑定指定的IP地址和端口号
	 * 
	 * @param inet
	 *            IP地址
	 * @param port
	 *            端口号
	 * @return
	 */
	Server bind(InetAddress inet, int port);

	/**
	 * 绑定指定的端口号
	 * 
	 * @param port
	 *            端口号
	 * @return
	 */
	Server bind(int port);

	/**
	 * 重启服务器
	 */
	void restart();

	/**
	 * 启动服务器
	 */
	void start();

	/**
	 * 关闭服务器
	 */
	void shutdown();

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
