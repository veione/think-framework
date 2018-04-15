package com.think.common.timer;

import java.util.concurrent.TimeUnit;

import akka.actor.ActorSystem;
import akka.actor.Cancellable;
import akka.actor.Scheduler;

public class Test {
    public static void main(String[] args) {
        TimingWheel<Cancellable> timer = new TimingWheel(1024, 1, TimeUnit.SECONDS);
        timer.addExpirationListener(e -> System.out.println("e = [" + e + "]"));
        timer.start();

        final ActorSystem system = ActorSystem.create("think");

        Scheduler scheduler = system.scheduler();
        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            System.currentTimeMillis();
        }
        long end = System.currentTimeMillis();
        long result = end - start;
        System.out.println("result = [" + result + "]");

    }
}
