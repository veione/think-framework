package com.think.test.netty;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

import io.netty.util.HashedWheelTimer;

public class TestHashedWheelTimer {

    @Test
    public void testHashWheelTimer() {
        HashedWheelTimer timer = new HashedWheelTimer();
        timer.newTimeout(timeout -> System.out.println("timeout = [" + timeout.timer() + "]"), 1, TimeUnit.SECONDS);
        timer.newTimeout(timeout -> System.out.println("timeout = [" + timeout + "]"), 1, TimeUnit.SECONDS);
        timer.newTimeout(timeout -> System.out.println("timeout = [" + timeout + "]"), 1, TimeUnit.SECONDS);
        timer.newTimeout(timeout -> System.out.println("timeout = [" + timeout + "]"), 1, TimeUnit.SECONDS);
        timer.newTimeout(timeout -> System.out.println("timeout = [" + timeout + "]"), 1, TimeUnit.SECONDS);
        timer.newTimeout(timeout -> System.out.println("timeout = [" + timeout + "]"), 1, TimeUnit.SECONDS);
        timer.newTimeout(timeout -> System.out.println("timeout = [" + timeout + "]"), 1, TimeUnit.SECONDS);
        timer.newTimeout(timeout -> System.out.println("timeout = [" + timeout + "]"), 1, TimeUnit.SECONDS);
        timer.newTimeout(timeout -> System.out.println("timeout = [" + timeout + "]"), 1, TimeUnit.SECONDS);
        timer.newTimeout(timeout -> System.out.println("timeout = [" + timeout + "]"), 1, TimeUnit.SECONDS);
        timer.newTimeout(timeout -> System.out.println("timeout = [" + timeout + "]"), 1, TimeUnit.SECONDS);
        timer.newTimeout(timeout -> System.out.println("timeout = [" + timeout + "]"), 1, TimeUnit.SECONDS);
        timer.newTimeout(timeout -> System.out.println("timeout = [" + timeout + "]"), 1, TimeUnit.SECONDS);
        timer.newTimeout(timeout -> System.out.println("timeout = [" + timeout + "]"), 1, TimeUnit.SECONDS);
        timer.newTimeout(timeout -> System.out.println("timeout = [" + timeout + "]"), 1, TimeUnit.SECONDS);
        timer.newTimeout(timeout -> System.out.println("timeout = [" + timeout + "]"), 1, TimeUnit.SECONDS);
        timer.newTimeout(timeout -> System.out.println("timeout = [" + timeout + "]"), 1, TimeUnit.SECONDS);
        timer.newTimeout(timeout -> System.out.println("timeout = [" + timeout + "]"), 1, TimeUnit.SECONDS);
        timer.newTimeout(timeout -> System.out.println("timeout = [" + timeout + "]"), 1, TimeUnit.SECONDS);

        //timer.stop();
        //timer.start();
    }
}
