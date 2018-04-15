package com.think.common.timer;

/**
 * 监听器
 */
@FunctionalInterface
public interface ExpirationListener<E> {
    void expired(E expireObject);
}

