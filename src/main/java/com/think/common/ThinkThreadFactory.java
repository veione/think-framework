package com.think.common;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程工程类
 * 
 * @author Administrator
 *
 */
public class ThinkThreadFactory implements ThreadFactory {
	private final String prefix;
	private final AtomicInteger threadCount = new AtomicInteger(0);

	public ThinkThreadFactory(String prefix) {
		this.prefix = prefix;
	}

	@Override
	public Thread newThread(Runnable runnable) {
		Thread t = new Thread(runnable, prefix + threadCount.getAndIncrement());
		t.setDaemon(false);
		return t;
	}

}
