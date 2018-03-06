package com.think.core.net.util;

import java.util.concurrent.atomic.AtomicLong;

/**
 * IO统计类
 * 
 * @author veione
 *
 */
public final class IOStatistics {
	public final AtomicLong inMsgs = new AtomicLong();
	public final AtomicLong outMsgs = new AtomicLong();
	public final AtomicLong inBytes = new AtomicLong();
	public final AtomicLong outBytes = new AtomicLong();

	public long getInMessages() {
		return this.inMsgs.get();
	}

	public long getOutMessages() {
		return this.outMsgs.get();
	}

	public long getInBytes() {
		return this.inBytes.get();
	}

	public long getOutBytes() {
		return this.outBytes.get();
	}
}
