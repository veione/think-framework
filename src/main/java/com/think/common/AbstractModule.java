package com.think.common;

import java.util.Map;

import io.netty.util.internal.PlatformDependent;

public abstract class AbstractModule implements Module {
	private Map<String, Module> moduleMap = PlatformDependent.newConcurrentHashMap();

	public void register(String moduleName, Module module) {
		moduleMap.put(moduleName, module);
	}

	public Module remove(String moduleName) {
		Module module = moduleMap.get(moduleName);
		if (module != null) {
			module.onShutdown();
		}
		return module;
	}

	@Override
	public void shutdown() {
		onShutdown();
	}

	public void reload(String moduleName) {
		// 清理资源后再加载资源
		shutdown();
		init();
	}
}
