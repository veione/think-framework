package com.think.common;

public class StoreModule extends AbstractModule {

	@Override
	public void init() {
		// 初始化加载资源
	}

	@Override
	public void register() {
		super.register("StoreModule", this);
	}

	@Override
	public void onShutdown() {
		// 清理资源
	}
}
