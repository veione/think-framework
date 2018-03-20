package com.think.core;

import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.MessageLite;

/**
 * 抽象消息处理器
 *
 * @author veione
 */
public abstract class AbstractHandler implements Handler {
    protected MessageLite request;
    protected Session session;

    public void init(MessageLite request, Session session) {
        this.request = request;
        this.session = session;
    }

    @Override
    public void handler() {
        preHandler();
        onHandler();
        postHandler();
    }

    /**
     * 获取会话ID
     */
    public long getSessionId() {
        return this.session.sessionId;
    }

    /**
     * 业务逻辑处理器
     */
    public abstract void onHandler();

    /**
     * 业务逻辑处理之前调用
     */
    public void preHandler() {
        throw new UnsupportedOperationException();
    }

    /**
     * 业务逻辑处理之后调用
     */
    public void postHandler() {
        throw new UnsupportedOperationException();
    }
}
