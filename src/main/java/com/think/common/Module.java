package com.think.common;

public interface Module {
	void init();

	void register();

	void shutdown();

	void onShutdown();
}
