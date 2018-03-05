package com.think.util.concurrent;

/**
 * A {@link uk.co.real_logic.agrona.concurrent.NanoClock} the delegates to
 * {@link System#nanoTime()}.
 * 
 * Instances are threadsafe.
 */
public class SystemNanoClock implements NanoClock {
	public long nanoTime() {
		return System.nanoTime();
	}
}
