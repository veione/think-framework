package com.think.util;

/**
 * 类扫描过滤器
 */
@FunctionalInterface
public interface ClassFilter {

    /**
     * 是否满足条件
     */
    boolean accept(Class<?> clazz);
}  