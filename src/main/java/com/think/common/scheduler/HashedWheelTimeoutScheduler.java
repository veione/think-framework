/**
 * Copyright 2012 Nikita Koksharov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Modified version of HashedWheelScheduler specially for timeouts handling.
 * Difference:
 * - handling old timeout with same key after adding new one
 * fixes multithreaded problem that appears in highly concurrent non-atomic sequence cancel() -> schedule()
 *
 * (c) Alim Akbashev, 2015-02-11
 *
 * Modified version of HashedWheelScheduler specially for timeouts handling.
 * Difference:
 * - handling old timeout with same key after adding new one
 * fixes multithreaded problem that appears in highly concurrent non-atomic sequence cancel() -> schedule()
 *
 * (c) Alim Akbashev, 2015-02-11
 */
/**
 * Modified version of HashedWheelScheduler specially for timeouts handling.
 * Difference:
 * - handling old timeout with same key after adding new one
 *   fixes multithreaded problem that appears in highly concurrent non-atomic sequence cancel() -> schedule()
 *
 * (c) Alim Akbashev, 2015-02-11
 */
package com.think.common.scheduler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.internal.PlatformDependent;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

public class HashedWheelTimeoutScheduler implements CancelableScheduler {

    private final ConcurrentMap<SchedulerKey, Timeout> scheduledFutures = PlatformDependent.newConcurrentHashMap();
    private final HashedWheelTimer executorService = new HashedWheelTimer();

    private volatile ChannelHandlerContext ctx;

    @Override
    public void update(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public void cancel(SchedulerKey key) {
        Timeout timeout = scheduledFutures.remove(key);
        if (timeout != null) {
            timeout.cancel();
        }
    }

    @Override
    public void schedule(final Runnable runnable, long delay, TimeUnit unit) {
        executorService.newTimeout(timeout -> runnable.run(), delay, unit);
    }

    @Override
    public void scheduleCallback(final SchedulerKey key, final Runnable runnable, long delay, TimeUnit unit) {
        Timeout timeout = executorService.newTimeout(timeout1 -> ctx.executor().execute(() -> {
            try {
                runnable.run();
            } finally {
                scheduledFutures.remove(key);
            }
        }), delay, unit);

        replaceScheduledFuture(key, timeout);
    }

    @Override
    public void schedule(final SchedulerKey key, final Runnable runnable, long delay, TimeUnit unit) {
        Timeout timeout = executorService.newTimeout(t -> {
            try {
                runnable.run();
            } finally {
                scheduledFutures.remove(key);
            }
        }, delay, unit);

        replaceScheduledFuture(key, timeout);
    }

    @Override
    public void shutdown() {
        executorService.stop();
    }

    private void replaceScheduledFuture(final SchedulerKey key, final Timeout newTimeout) {
        final Timeout oldTimeout;

        if (newTimeout.isExpired()) {
            // no need to put already expired timeout to scheduledFutures map.
            // simply remove old timeout
            oldTimeout = scheduledFutures.remove(key);
        } else {
            oldTimeout = scheduledFutures.put(key, newTimeout);
        }

        // if there was old timeout, cancel it
        if (oldTimeout != null) {
            oldTimeout.cancel();
        }
    }
}
