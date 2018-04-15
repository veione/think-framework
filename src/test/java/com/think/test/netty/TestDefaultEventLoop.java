package com.think.test.netty;

import org.junit.Test;

import io.netty.channel.DefaultEventLoop;
import io.netty.channel.DefaultEventLoopGroup;

public class TestDefaultEventLoop {

    @Test
    public void testDefaultEventLoop() {
        // 单线程处理所有任务
        DefaultEventLoop eventLoop = new DefaultEventLoop();
        eventLoop.execute(() -> System.out.println("Hello"));
        System.out.println(eventLoop.inEventLoop());
        eventLoop.executeAfterEventLoopIteration(() -> System.out.println("World"));
    }

    @Test
    public void testDefaultEventLoopGroup() {
        // 多线程处理所有任务
        DefaultEventLoopGroup eventLoopGroup = new DefaultEventLoopGroup();
        eventLoopGroup.execute(() -> System.out.println("Hello,Again"));
    }
}
