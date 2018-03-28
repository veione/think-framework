package com.think.core;

import com.google.protobuf.GeneratedMessage;

import java.lang.reflect.Method;

/**
 * 消息处理实体类
 */
public class HandlerModel {
    private Object clazz;
    private String clazzDesc;
    private Method method;
    private String methodDesc;
    private GeneratedMessage request;
    private Class<? extends GeneratedMessage> message;
    private RequestContext context;

    public void execute() throws Exception {
        this.method.invoke(clazz, context, request);
    }

    public Class<? extends GeneratedMessage> getMessage() {
        return message;
    }

    public void setMessage(Class<? extends GeneratedMessage> message) {
        this.message = message;
    }

    public RequestContext getContext() {
        return context;
    }

    public void setContext(RequestContext context) {
        this.context = context;
    }

    public Object getClazz() {
        return clazz;
    }

    public void setClazz(Object clazz) {
        this.clazz = clazz;
    }

    public String getClazzDesc() {
        return clazzDesc;
    }

    public void setClazzDesc(String clazzDesc) {
        this.clazzDesc = clazzDesc;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public String getMethodDesc() {
        return methodDesc;
    }

    public void setMethodDesc(String methodDesc) {
        this.methodDesc = methodDesc;
    }

    public GeneratedMessage getRequest() {
        return request;
    }

    public void setRequest(GeneratedMessage request) {
        this.request = request;
    }

    @Override
    public String toString() {
        return "HandlerModel{" +
                "clazz=" + clazz +
                ", clazzDesc='" + clazzDesc + '\'' +
                ", method=" + method +
                ", methodDesc='" + methodDesc + '\'' +
                ", request=" + request +
                '}';
    }

    class HandlerMethod {

    }
}
