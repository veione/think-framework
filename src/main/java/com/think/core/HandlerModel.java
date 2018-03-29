package com.think.core;

import com.google.protobuf.Descriptors;
import com.google.protobuf.GeneratedMessage;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 消息处理实体类
 */
public class HandlerModel {
    private Object clazz;
    private String clazzDesc;
    private Method method;
    private String methodDesc;
    private int requestId;
    private GeneratedMessage request;
    private Class<? extends GeneratedMessage> message;
    private RequestContext context;
    private MessageMapping msgMapping;

    public MessageMapping getMsgMapping() {
        return msgMapping;
    }

    public void setMsgMapping(MessageMapping msgMapping) {
        this.msgMapping = msgMapping;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
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

    public static class MessageMapping {
        private Descriptors.Descriptor descriptor;
        private Object msgBuilderCls;
        private Field[] msgFields;

        public Descriptors.Descriptor getDescriptor() {
            return descriptor;
        }

        public void setDescriptor(Descriptors.Descriptor descriptor) {
            this.descriptor = descriptor;
        }

        public Object getMsgBuilderCls() {
            return msgBuilderCls;
        }

        public void setMsgBuilderCls(Object msgBuilderCls) {
            this.msgBuilderCls = msgBuilderCls;
        }

        public Field[] getMsgFields() {
            return msgFields;
        }

        public void setMsgFields(Field[] msgFields) {
            this.msgFields = msgFields;
        }
    }

}

