package com.think.core.net.service;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.google.protobuf.MessageLite;

import com.think.common.annotation.Handler;
import com.think.common.annotation.Mapping;
import com.think.core.net.message.ResponseWrapper;
import com.think.util.ClassScanner;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.LockSupport;

import io.netty.channel.Channel;

/**
 * 消息管理器
 */
public final class MessageManager {
    private static Logger logger = LoggerFactory.getLogger(MessageManager.class);
    private static final Map<Short, MessageLite> MESSAGES = new HashMap<>();
    private static final Queue<ResponseWrapper> msgQueue = new ConcurrentLinkedQueue();
    private static String packageName;

    /**
     * 加载消息
     */
    public static void load(String pkgName) {
        Objects.requireNonNull(pkgName);
        packageName = pkgName;
        StopWatch watch = new StopWatch();
        watch.start();
        Set<Class<?>> classes = ClassScanner.getClasses(packageName, clazz -> clazz.isAnnotationPresent(Handler.class));
        try {
            for (Class<?> clz : classes) {
                Object handler = clz.newInstance();
                Method[] methods = handler.getClass().getDeclaredMethods();
                for (Method method : methods) {
                    // 得到该类下面的RequestMapping注解
                    Mapping mapping = method.getAnnotation(Mapping.class);
                    if (mapping != null) {
                        Class<?> requestObject = mapping.msg();
                        Method invoke = requestObject.getMethod("getDefaultInstance");
                        MessageLite prototype;
                        if (invoke != null) {
                            prototype = (MessageLite) invoke.invoke(requestObject);
                            MESSAGES.put(mapping.requestId(), prototype);
                        }
                    }
                }
            }
            watch.stop();
            logger.debug("Load {} messages, total time consuming {}", MESSAGES.size(), watch.getTime());
            initMessageWorker();
        } catch (Exception e) {
            logger.error("Load message handler error", e);
        }
    }

    private static void initMessageWorker() {
        ExecutorService executor = Executors.newFixedThreadPool(6, new ThreadFactoryBuilder().setNameFormat("message-dispatcher-%d").build());
        for (int i = 1; i < 7; i++) {
            executor.execute(new Worker());
        }
    }

    /**
     * 获取消息映射对象
     */
    public static MessageLite getPrototype(Short requestId) {
        return MESSAGES.get(requestId);
    }

    /**
     * 重新加载消息
     */
    public static void reload() {
        MESSAGES.clear();
        load(packageName);
    }

    public static void write(Channel channel, List<ResponseWrapper> messages) {
        List<ResponseWrapper> realResponses = new ArrayList<>(messages.size());
        messages.stream().forEach(msg -> {
            if (msg.isReal()) { //是否为实时消息
                realResponses.add(msg);
            } else {
                msgQueue.offer(msg);
            }
        });
        // 写出实时消息
        if (!realResponses.isEmpty()) {
            channel.writeAndFlush(realResponses);
        }
    }

    static class Worker extends Thread {

        @Override
        public void run() {
            while (true) {
                synchronized (msgQueue) {
                    if (!msgQueue.isEmpty()) {
                        ResponseWrapper message = msgQueue.poll();
                        if (message != null) {
                            message.getSession().channel().writeAndFlush(message);
                        }
                    }
                    LockSupport.parkNanos(1000);
                }
            }
        }
    }
}
