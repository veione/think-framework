package com.think.service;

import com.think.core.Session;
import com.think.util.IdWorker;

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
    /**
     * 会话键
     */
    public static final AttributeKey<Session> SESSION_KEY = AttributeKey.valueOf("sessionId");
    /**
     * 会话ID生成器
     */
    private IdWorker idWorker;
    /***
     * 会话计数器
     */
    private static final AtomicInteger sessionCounter = new AtomicInteger(0);
    /**
     * 会话存储对象
     */
    private static final ConcurrentMap<Channel, Session> SESSIONS = PlatformDependent.newConcurrentHashMap();

    public void setIdWorker(final IdWorker worker) {
        this.idWorker = worker;
    }

    /**
     * 新增会话对象
     *
     * @param session 会话对象
     */
    public boolean addSession(Session session) {
        Attribute<Session> sessionAttr = session.channel.attr(SESSION_KEY);
        boolean flag = sessionAttr.compareAndSet(null, session);
        if (flag) {
            sessionCounter.incrementAndGet();
            SESSIONS.put(session.channel, session);
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
            session.close();
            sessionCounter.decrementAndGet();
        }
        return session;
    }

    /**
     * 移除会话对象
     */
    public Session removeSession(Session session) {
        Session exitSession = SESSIONS.remove(session.channel);
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

    public long nextId() {
        if (this.idWorker == null) {
            throw new NullPointerException("Please initialization id generator object.");
        }
        return idWorker.nextId();
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
