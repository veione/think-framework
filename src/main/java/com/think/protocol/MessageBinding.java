package com.think.protocol;

import com.google.protobuf.Descriptors;
import com.google.protobuf.DynamicMessage;

import java.io.Serializable;

/**
 * 消息绑定对象
 *
 * @author veione
 * @date 2018年3月22日22:06:13
 */
public class MessageBinding implements Serializable {
    private short msgId;
    private int actionId;
    private String clsStr;
    private Descriptors.Descriptor descriptor;
    private DynamicMessage dynamicMessage;
    private DynamicMessage.Builder builder;

    private MessageBinding() {
    }

    public static MessageBinding newBuilder() {
        return new MessageBinding();
    }

    public short getMsgId() {
        return msgId;
    }

    public void setMsgId(short msgId) {
        this.msgId = msgId;
    }

    public int getActionId() {
        return actionId;
    }

    public void setActionId(int actionId) {
        this.actionId = actionId;
    }

    public String getClsStr() {
        return clsStr;
    }

    public void setClsStr(String clsStr) {
        this.clsStr = clsStr;
    }

    public Descriptors.Descriptor getDescriptor() {
        return descriptor;
    }

    public void setDescriptor(Descriptors.Descriptor descriptor) {
        this.descriptor = descriptor;
    }

    public DynamicMessage getDynamicMessage() {
        return dynamicMessage;
    }

    public void setDynamicMessage(DynamicMessage dynamicMessage) {
        this.dynamicMessage = dynamicMessage;
    }

    public DynamicMessage.Builder getBuilder() {
        return builder;
    }

    public void setBuilder(DynamicMessage.Builder builder) {
        this.builder = builder;
    }

    @Override
    public String toString() {
        return "MessageBinding{" +
                "msgId=" + msgId +
                ", actionId=" + actionId +
                ", clsStr='" + clsStr + '\'' +
                ", descriptor=" + descriptor +
                ", dynamicMessage=" + dynamicMessage +
                ", builder=" + builder +
                '}';
    }
}
