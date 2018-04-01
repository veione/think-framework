package com.think.service;

import com.google.protobuf.MessageLite;

import com.esotericsoftware.reflectasm.MethodAccess;
import com.think.common.annotation.Handler;
import com.think.core.HandlerMapping;
import com.think.common.annotation.Mapping;
import com.think.core.Session;
import com.think.util.ClassScanner;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * 处理器管理类
 *
 * @author veione
 */
public final class HandlerManager {
    private static Logger logger = LoggerFactory.getLogger(HandlerManager.class);
    private static final Map<Short, HandlerMapping> HANDLERS = new HashMap<>();

    /**
     * 注册处理器
     *
     * @param requestId 消息请求id
     * @param model     处理实体类
     */
    public static HandlerMapping register(Short requestId, HandlerMapping model) {
        Objects.requireNonNull(requestId);
        Objects.requireNonNull(model);
        return HANDLERS.put(requestId, model);
    }

    /**
     * 获取处理器实体
     *
     * @param requestId 请求消息Id
     */
    public static HandlerMapping getHandler(Short requestId) {
        Objects.requireNonNull(requestId);
        HandlerMapping model = HANDLERS.get(requestId);
        if (model == null) {
            throw new IllegalArgumentException("Invalid request message id " + requestId);
        }
        return model;
    }

    /**
     * 从指定包下加载处理器
     *
     * @param packageName 包名
     */
    public static void load(String packageName) {
        StopWatch watch = new StopWatch();
        watch.start();
        Set<Class<?>> classes = ClassScanner.getClasses(packageName, c -> c.isAnnotationPresent(Handler.class));
        try {
            for (Class<?> clz : classes) {
                Object handler = clz.newInstance();
                Method[] methods = handler.getClass().getDeclaredMethods();
                MethodAccess access = MethodAccess.get(handler.getClass());
                for (Method method : methods) {
                    // 得到该类下面的RequestMapping注解
                    Mapping mapping = method.getAnnotation(Mapping.class);
                    if (mapping != null) {
                        HandlerMapping handlerMapping = HandlerMapping.newBuilder();

                        int methodIndex = access.getIndex(method.getName());
                        handlerMapping.setHandler(handler);
                        handlerMapping.setMethod(method);
                        handlerMapping.setMethodIndex(methodIndex);
                        handlerMapping.setRequest(mapping.msg());
                        handlerMapping.setRequestId(mapping.requestId());
                        handlerMapping.setAccess(access);

                        register(handlerMapping.getRequestId(), handlerMapping);
                    }
                }
            }
            watch.stop();
            logger.debug("Load {} handlers, total time consuming {}", HANDLERS.size(), watch.getTime());
        } catch (Exception e) {
            logger.error("Load handler error", e);
        }
    }

    public static Map<Short, HandlerMapping> getHandlers() {
        return HANDLERS;
    }

    public static void execute(Short requestId, Session session, MessageLite message) {
        HandlerMapping mapping = HandlerManager.getHandler(requestId);
        if (mapping != null) {
            mapping.getAccess().invoke(mapping.getHandler(), mapping.getMethodIndex(), session, message);
        }
    }
}
