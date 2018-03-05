package com.think.common;

import java.net.InetAddress;

public interface Server {

	Server bind(String inet, int port);

	Server bind(InetAddress inet, int port);

	Server bind(int port);

	void restart();

	void start();

	void onShutdown();
}
