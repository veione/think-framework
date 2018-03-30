package com.think.core;

import com.google.protobuf.Descriptors;

import java.lang.reflect.Field;

public class MessageMapping {
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
