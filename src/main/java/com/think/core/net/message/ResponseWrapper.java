package com.think.net.message;

import com.google.protobuf.MessageLite;

import com.think.net.session.Session;

/**
 * 消息响应包装器
 */
public class ResponseWrapper implements NetMessage {
    private short responseId;
    private MessageLite payload;
    private long accessTime;
    private boolean isReal;
    private Session session;

    public ResponseWrapper(short responseId, long accessTime, boolean isReal, MessageLite payload) {
        this.responseId = responseId;
        this.accessTime = accessTime;
        this.isReal = isReal;
        this.payload = payload;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public static ResponseWrapper newBuilder() {
        return new ResponseWrapper((short) 0, System.currentTimeMillis(), false, null);
    }

    public int getResponseId() {
        return responseId;
    }

    public void setResponseId(short responseId) {
        this.responseId = responseId;
    }

    public MessageLite getPayload() {
        return payload;
    }

    public void setPayload(MessageLite payload) {
        this.payload = payload;
    }

    public long getAccessTime() {
        return accessTime;
    }

    public void setAccessTime(long accessTime) {
        this.accessTime = accessTime;
    }

    public boolean isReal() {
        return isReal;
    }

    public void setReal(boolean real) {
        isReal = real;
    }

    @Override
    public String toString() {
        return "ResponseWrapper{" +
                "responseId=" + responseId +
                ", payload=" + payload +
                ", accessTime=" + accessTime +
                ", isReal=" + isReal +
                '}';
    }
}
