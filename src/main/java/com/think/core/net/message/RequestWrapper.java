package com.think.core.net.message;

import com.google.protobuf.MessageLite;

import com.think.core.net.session.Session;

import io.netty.channel.Channel;

/**
 * 请求包装类
 */
public class RequestWrapper implements NetMessage {
    public static final short MAGIC_WORD = 0x75;
    public static final int MAX_FRAME_SIZE = 10240;
    public static final int MIN_FRAME_SIZE = 4;
    private short requestId;
    private long accessTime;
    private Channel channel;
    private Session session;
    private MessageLite payload;

    public RequestWrapper(short requestId, long accessTime, MessageLite payload) {
        this.requestId = requestId;
        this.accessTime = accessTime;
        this.payload = payload;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public static RequestWrapper newBuilder() {
        return new RequestWrapper((short) 0, System.currentTimeMillis(), null);
    }

    public short getRequestId() {
        return requestId;
    }

    public void setRequestId(short requestId) {
        this.requestId = requestId;
    }

    public long getAccessTime() {
        return accessTime;
    }

    public void setAccessTime(long accessTime) {
        this.accessTime = accessTime;
    }

    public MessageLite getPayload() {
        return payload;
    }

    public void setPayload(MessageLite payload) {
        this.payload = payload;
    }

    @Override
    public String toString() {
        return "RequestWrapper{" +
                "requestId=" + requestId +
                ", accessTime=" + accessTime +
                ", payload=" + payload +
                '}';
    }
}
