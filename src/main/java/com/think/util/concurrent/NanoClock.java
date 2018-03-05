package com.think.util.concurrent;

/**
 * Functional interface for return the current time as system wide monotonic
 * tick of 1 nanosecond precision.
 */
@FunctionalInterface
public interface NanoClock {
	/**
	 * The number of ticks in nanoseconds the clock has advanced since starting.
	 * 
	 * @return number of ticks in nanoseconds the clock has advanced since starting.
	 */
	long nanoTime();
}
