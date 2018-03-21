package com.think.core.net;

import com.google.protobuf.Message;

import com.think.core.Session;

/**
 * 请求上下文
 *
 * @author veione
 * @date 2018年3月21日20:48:54
 */
public class RequestWrapper {
    private Message request;
    private Session session;
    private int requestType;
    private long startTime;

    public RequestWrapper() {
        this.startTime = System.currentTimeMillis();
    }

    public RequestWrapper(int requestType, Session session, Message message) {
        this.requestType = requestType;
        this.session = session;
        this.request = message;
        this.startTime = System.currentTimeMillis();
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public Message getRequest() {
        return request;
    }

    public void setRequest(Message request) {
        this.request = request;
    }

    public long getStartTime() {
        return startTime;
    }

    public int getRequestType() {
        return requestType;
    }

    public void setRequestType(int requestType) {
        this.requestType = requestType;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Type=").append(this.requestType).append(" Content=").append(this.request);
        return builder.toString();
    }

    public static RequestWrapper newBuilder() {
        return new RequestWrapper();
    }
}
