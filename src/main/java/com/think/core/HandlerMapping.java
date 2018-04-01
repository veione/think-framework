package com.think.core;

import com.esotericsoftware.reflectasm.MethodAccess;
import com.google.protobuf.GeneratedMessage;

import java.lang.reflect.Method;

/**
 * 消息处理实体类
 */
public class HandlerMapping {
    private Object handler;
    private Method method;
    private MethodAccess access;
    private short requestId;
    private int methodIndex;
    private Class<? extends GeneratedMessage> request;

    public int getMethodIndex() {
        return methodIndex;
    }

    public void setMethodIndex(int methodIndex) {
        this.methodIndex = methodIndex;
    }

    public short getRequestId() {
        return requestId;
    }

    public void setRequestId(short requestId) {
        this.requestId = requestId;
    }

    public Class<? extends GeneratedMessage> getRequest() {
        return request;
    }

    public void setRequest(Class<? extends GeneratedMessage> request) {
        this.request = request;
    }

    public MethodAccess getAccess() {
        return access;
    }

    public void setAccess(MethodAccess access) {
        this.access = access;
    }

    public Object getHandler() {
        return handler;
    }

    public void setHandler(Object handler) {
        this.handler = handler;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public static HandlerMapping newBuilder() {
        return new HandlerMapping();
    }
}

