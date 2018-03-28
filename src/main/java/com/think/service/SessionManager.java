package com.think.service;

import com.think.core.Session;

import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

import io.netty.channel.Channel;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import io.netty.util.internal.PlatformDependent;

/**
 * 会话管理器
 *
 * @author veione
 */
public final class SessionManager {
    /***
     * 会话计数器
     */
    private static final AtomicInteger sessionCounter = new AtomicInteger(0);
    /**
     * 会话键
     */
    private static final AttributeKey<Session> SESSION_KEY = AttributeKey.valueOf("session");
    /**
     * 会话存储对象
     */
    private static final ConcurrentMap<Channel, Session> SESSIONS = PlatformDependent.newConcurrentHashMap();

    /**
     * 新增会话对象
     *
     * @param channel 连接channel
     * @param session 会话对象
     */
    public boolean addSession(Channel channel, Session session) {
        Attribute<Session> sessionAttr = channel.attr(SESSION_KEY);

        boolean flag = sessionAttr.compareAndSet(null, session);
        if (flag) {
            sessionCounter.incrementAndGet();
            SESSIONS.put(channel, session);
        }
        return flag;
    }

    /**
     * 获取会话对象
     *
     * @param channel 连接channel
     */
    public Session getSession(Channel channel) {
        Session session = null;
        Attribute<Session> sessionAtr = channel.attr(SESSION_KEY);
        if (sessionAtr != null) {
            session = sessionAtr.get();
        }
        return session;
    }

    /**
     * 移除会话对象
     */
    public Session removeSession(Channel channel) {
        Session session = SESSIONS.remove(channel);
        if (session != null) {
            sessionCounter.decrementAndGet();
        }
        return session;
    }

    /**
     * 移除会话对象
     */
    public Session removeSession(Session session) {
        Session exitSession = SESSIONS.remove(session.channel());
        if (exitSession != null) {
            sessionCounter.decrementAndGet();
        }
        return exitSession;
    }

    /**
     * 获取所有的会话
     */
    public Map<Channel, Session> getSessions() {
        return SESSIONS;
    }

    /**
     * 获取会话总数
     */
    public int getSessionCount() {
        return sessionCounter.get();
    }

    /**
     * 清理所有的会话对象
     */
    public void clear() {
        SESSIONS.clear();
        sessionCounter.set(0);
    }

    /**
     * 获取实例对象
     */
    public static SessionManager getInstance() {
        return SessionManagerHolder.INSTANCE;
    }

    private static final class SessionManagerHolder {
        public static final SessionManager INSTANCE = new SessionManager();
    }
}
