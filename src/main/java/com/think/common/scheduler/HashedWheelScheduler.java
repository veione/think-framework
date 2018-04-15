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
 */
package com.think.common.scheduler;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.internal.PlatformDependent;

public class HashedWheelScheduler implements CancelableScheduler {

    private final Map<SchedulerKey, Timeout> scheduledFutures = PlatformDependent.newConcurrentHashMap();
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
    public void scheduleCallback(final SchedulerKey key, Runnable task, long delay, TimeUnit unit) {
        Timeout timeout = executorService.newTimeout(t -> ctx.executor().execute(() -> {
            try {
                task.run();
            } finally {
                scheduledFutures.remove(key);
            }
        }), delay, unit);

        if (!timeout.isExpired()) {
            scheduledFutures.put(key, timeout);
        }
    }

    @Override
    public void schedule(final Runnable task, long delay, TimeUnit unit) {
        executorService.newTimeout(t -> task.run(), delay, unit);
    }

    @Override
    public void schedule(final SchedulerKey key, Runnable task, long delay, TimeUnit unit) {
        Timeout timeout = executorService.newTimeout(t -> {
            try {
                task.run();
            } finally {
                scheduledFutures.remove(key);
            }
        }, delay, unit);

        if (!timeout.isExpired()) {
            scheduledFutures.put(key, timeout);
        }
    }

    @Override
    public void shutdown() {
        executorService.stop();
    }
}
