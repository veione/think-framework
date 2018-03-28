package com.think.core;

import com.google.protobuf.GeneratedMessage;

import java.util.HashMap;
import java.util.Map;

public final class HandlerManager {
    private static final Map<Class<? extends GeneratedMessage>, HandlerModel> handlerMap = new HashMap<>();

    /**
     * 注册处理器
     *
     * @param request 消息请求对象
     * @param model   处理实体类
     */
    public static HandlerModel register(Class<? extends GeneratedMessage> request, HandlerModel model) {
        return handlerMap.put(request, model);
    }
}
