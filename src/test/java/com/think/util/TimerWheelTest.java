package com.think.util;

import java.util.concurrent.TimeUnit; 
import java.util.concurrent.atomic.AtomicLong; 
import java.util.function.BooleanSupplier; 
 
import static org.hamcrest.MatcherAssert.assertThat; 
import static org.hamcrest.Matchers.*; 
import static org.junit.Assert.assertFalse; 
import static org.junit.Assert.assertTrue; 

import org.junit.Test;

import com.think.util.concurrent.NanoClock;

public class TimerWheelTest {
	private static final long ONE_MS_OF_NS = TimeUnit.MILLISECONDS.toNanos(1);

	private long controlTimestamp;

	private long getControlTimestamp() {
		return controlTimestamp;
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldExceptionOnNonPowerOf2TicksPerWheel() {
		new TimerWheel(100, TimeUnit.MILLISECONDS, 10);
	}

	@Test
	public void shouldBeAbleToCalculateDelayWithRealTime() {
		final TimerWheel wheel = new TimerWheel(100, TimeUnit.MILLISECONDS, 512);

		assertThat(wheel.computeDelayInMs(), allOf(greaterThanOrEqualTo(90L), lessThanOrEqualTo(110L)));
	}

	@Test
	public void shouldBeAbleToCalculateDelay() {
		controlTimestamp = 0;
		final TimerWheel wheel = new TimerWheel(this::getControlTimestamp, 1, TimeUnit.MILLISECONDS, 512);

		assertThat(wheel.computeDelayInMs(), is(1L));
	}

	@Test(timeout = 1000)
	public void shouldBeAbleToScheduleTimerOnEdgeOfTick() {
		controlTimestamp = 0;
		final AtomicLong firedTimestamp = new AtomicLong(-1);
		final TimerWheel wheel = new TimerWheel(this::getControlTimestamp, 1, TimeUnit.MILLISECONDS, 1024);
		final Runnable task = () -> firedTimestamp.set(wheel.clock().nanoTime());

		wheel.newTimeout(5000, TimeUnit.MICROSECONDS, task);

		processTimersUntil(wheel, ONE_MS_OF_NS, () -> firedTimestamp.get() != -1);

		// this is the first tick after the timer, so it should be on this edge
		assertThat(firedTimestamp.get(), is(TimeUnit.MILLISECONDS.toNanos(6)));
	}

	@Test(timeout = 1000)
	public void shouldHandleNonZeroStartTime() {
		controlTimestamp = TimeUnit.MILLISECONDS.toNanos(100);
		final AtomicLong firedTimestamp = new AtomicLong(-1);
		final TimerWheel wheel = new TimerWheel(this::getControlTimestamp, 1, TimeUnit.MILLISECONDS, 1024);
		final Runnable task = () -> firedTimestamp.set(wheel.clock().nanoTime());

		wheel.newTimeout(5000, TimeUnit.MICROSECONDS, task);

		processTimersUntil(wheel, ONE_MS_OF_NS, () -> firedTimestamp.get() != -1);

		// this is the first tick after the timer, so it should be on this edge
		assertThat(firedTimestamp.get(), is(TimeUnit.MILLISECONDS.toNanos(106))); // relative to start time
	}

	@Test
	public void shouldHandleNanoTimeUnitTimers() {
		controlTimestamp = 0;
		final AtomicLong firedTimestamp = new AtomicLong(-1);
		final TimerWheel wheel = new TimerWheel(this::getControlTimestamp, 1, TimeUnit.MILLISECONDS, 1024);
		final Runnable task = () -> firedTimestamp.set(wheel.clock().nanoTime());

		wheel.newTimeout(5000001, TimeUnit.NANOSECONDS, task);

		processTimersUntil(wheel, ONE_MS_OF_NS, () -> firedTimestamp.get() != -1);

		// this is the first tick after the timer, so it should be on this edge
		assertThat(firedTimestamp.get(), is(TimeUnit.MILLISECONDS.toNanos(6)));
	}

	@Test
	public void shouldHandleMultipleRounds() {
		controlTimestamp = 0;
		final AtomicLong firedTimestamp = new AtomicLong(-1);
		final TimerWheel wheel = new TimerWheel(this::getControlTimestamp, 1, TimeUnit.MILLISECONDS, 16);
		final Runnable task = () -> firedTimestamp.set(wheel.clock().nanoTime());

		wheel.newTimeout(63, TimeUnit.MILLISECONDS, task);

		processTimersUntil(wheel, ONE_MS_OF_NS, () -> firedTimestamp.get() != -1);

		// this is the first tick after the timer, so it should be on this edge
		assertThat(firedTimestamp.get(), is(TimeUnit.MILLISECONDS.toNanos(64))); // relative to start time
	}

	@Test
	public void shouldBeAbleToCancelTimer() {
		controlTimestamp = 0;
		final AtomicLong firedTimestamp = new AtomicLong(-1);
		final TimerWheel wheel = new TimerWheel(this::getControlTimestamp, 1, TimeUnit.MILLISECONDS, 256);
		final Runnable task = () -> firedTimestamp.set(wheel.clock().nanoTime());

		final TimerWheel.Timer timeout = wheel.newTimeout(63, TimeUnit.MILLISECONDS, task);

		processTimersUntil(wheel, ONE_MS_OF_NS, () -> wheel.clock().nanoTime() > TimeUnit.MILLISECONDS.toNanos(16));

		timeout.cancel();

		processTimersUntil(wheel, ONE_MS_OF_NS, () -> wheel.clock().nanoTime() > TimeUnit.MILLISECONDS.toNanos(128));

		assertThat(firedTimestamp.get(), is(-1L));
	}

	@Test
	public void shouldHandleExpiringTimersInPreviousTicks() {
		controlTimestamp = 0;
		final AtomicLong firedTimestamp = new AtomicLong(-1);
		final TimerWheel wheel = new TimerWheel(this::getControlTimestamp, 1, TimeUnit.MILLISECONDS, 256);
		final Runnable task = () -> firedTimestamp.set(wheel.clock().nanoTime());

		wheel.newTimeout(15, TimeUnit.MILLISECONDS, task);

		controlTimestamp += TimeUnit.MILLISECONDS.toNanos(32);

		processTimersUntil(wheel, ONE_MS_OF_NS, () -> wheel.clock().nanoTime() > TimeUnit.MILLISECONDS.toNanos(128));

		assertThat(firedTimestamp.get(), is(TimeUnit.MILLISECONDS.toNanos(32))); // time of first expireTimers call
	}

	@Test
	public void shouldHandleMultipleTimersInDifferentTicks() {
		controlTimestamp = 0;
		final AtomicLong firedTimestamp1 = new AtomicLong(-1);
		final AtomicLong firedTimestamp2 = new AtomicLong(-1);
		final TimerWheel wheel = new TimerWheel(this::getControlTimestamp, 1, TimeUnit.MILLISECONDS, 256);
		final Runnable task1 = () -> firedTimestamp1.set(wheel.clock().nanoTime());
		final Runnable task2 = () -> firedTimestamp2.set(wheel.clock().nanoTime());

		wheel.newTimeout(15, TimeUnit.MILLISECONDS, task1);
		wheel.newTimeout(23, TimeUnit.MILLISECONDS, task2);

		processTimersUntil(wheel, ONE_MS_OF_NS, () -> wheel.clock().nanoTime() > TimeUnit.MILLISECONDS.toNanos(128));

		assertThat(firedTimestamp1.get(), is(TimeUnit.MILLISECONDS.toNanos(16)));
		assertThat(firedTimestamp2.get(), is(TimeUnit.MILLISECONDS.toNanos(24)));
	}

	@Test
	public void shouldHandleMultipleTimersInSameTickSameRound() {
		controlTimestamp = 0;
		final AtomicLong firedTimestamp1 = new AtomicLong(-1);
		final AtomicLong firedTimestamp2 = new AtomicLong(-1);
		final TimerWheel wheel = new TimerWheel(this::getControlTimestamp, 1, TimeUnit.MILLISECONDS, 8);
		final Runnable task1 = () -> firedTimestamp1.set(wheel.clock().nanoTime());
		final Runnable task2 = () -> firedTimestamp2.set(wheel.clock().nanoTime());

		wheel.newTimeout(15, TimeUnit.MILLISECONDS, task1);
		wheel.newTimeout(15, TimeUnit.MILLISECONDS, task2);

		processTimersUntil(wheel, ONE_MS_OF_NS, () -> wheel.clock().nanoTime() > TimeUnit.MILLISECONDS.toNanos(128));

		assertThat(firedTimestamp1.get(), is(TimeUnit.MILLISECONDS.toNanos(16)));
		assertThat(firedTimestamp2.get(), is(TimeUnit.MILLISECONDS.toNanos(16)));
	}

	@Test
	public void shouldHandleMultipleTimersInSameTickDifferentRound() {
		controlTimestamp = 0;
		final AtomicLong firedTimestamp1 = new AtomicLong(-1);
		final AtomicLong firedTimestamp2 = new AtomicLong(-1);
		final TimerWheel wheel = new TimerWheel(this::getControlTimestamp, 1, TimeUnit.MILLISECONDS, 8);
		final Runnable task1 = () -> firedTimestamp1.set(wheel.clock().nanoTime());
		final Runnable task2 = () -> firedTimestamp2.set(wheel.clock().nanoTime());

		wheel.newTimeout(15, TimeUnit.MILLISECONDS, task1);
		wheel.newTimeout(23, TimeUnit.MILLISECONDS, task2);

		processTimersUntil(wheel, ONE_MS_OF_NS, () -> wheel.clock().nanoTime() > TimeUnit.MILLISECONDS.toNanos(128));

		assertThat(firedTimestamp1.get(), is(TimeUnit.MILLISECONDS.toNanos(16)));
		assertThat(firedTimestamp2.get(), is(TimeUnit.MILLISECONDS.toNanos(24)));
	}

	@Test
	public void shouldHandleRescheduledTimers() {
		controlTimestamp = 0;
		final AtomicLong firedTimestamp1 = new AtomicLong(-1);
		final AtomicLong firedTimestamp2 = new AtomicLong(-1);
		final TimerWheel wheel = new TimerWheel(this::getControlTimestamp, 1, TimeUnit.MILLISECONDS, 8);
		final Runnable task1 = () -> firedTimestamp1.set(wheel.clock().nanoTime());
		final Runnable task2 = () -> firedTimestamp2.set(wheel.clock().nanoTime());

		final TimerWheel.Timer timer = wheel.newTimeout(15, TimeUnit.MILLISECONDS, task1);

		processTimersUntil(wheel, ONE_MS_OF_NS, () -> wheel.clock().nanoTime() >= TimeUnit.MILLISECONDS.toNanos(50));

		assertTrue(timer.isExpired());
		assertFalse(timer.isActive());

		assertThat(firedTimestamp1.get(), is(TimeUnit.MILLISECONDS.toNanos(16)));
		assertThat(firedTimestamp2.get(), is(-1L));

		wheel.rescheduleTimeout(23, TimeUnit.MILLISECONDS, timer, task2);

		processTimersUntil(wheel, ONE_MS_OF_NS,
				() -> wheel.clock().nanoTime() >= TimeUnit.MILLISECONDS.toNanos(50 + 50));

		assertTrue(timer.isExpired());
		assertFalse(timer.isActive());

		assertThat(firedTimestamp1.get(), is(TimeUnit.MILLISECONDS.toNanos(16)));
		assertThat(firedTimestamp2.get(), is(TimeUnit.MILLISECONDS.toNanos(24 + 50)));
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldExceptionOnReschedulingActiveTimer() {
		controlTimestamp = 0;
		final TimerWheel wheel = new TimerWheel(this::getControlTimestamp, 1, TimeUnit.MILLISECONDS, 8);
		final Runnable task = wheel::clock;

		final TimerWheel.Timer timer = wheel.newTimeout(15, TimeUnit.MILLISECONDS, task);
		wheel.rescheduleTimeout(23, TimeUnit.MILLISECONDS, timer);
	}

	private long processTimersUntil(final TimerWheel wheel, final long increment, final BooleanSupplier condition) {
		final NanoClock clock = wheel.clock();
		final long startTime = clock.nanoTime();

		while (!condition.getAsBoolean()) {
			if (wheel.computeDelayInMs() > 0) {
				controlTimestamp += increment;
			}

			wheel.expireTimers();
		}

		return clock.nanoTime() - startTime;
	}
}
