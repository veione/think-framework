package com.think.db;

/**
 * 实体基类抽象类
 *
 * @author Gavin 2018年4月13日21:27:30
 */
public abstract class BaseEntity {
    /**
     * 默认数据状态为空闲状态
     */
    protected Mark mark = Mark.IDLE;

    /**
     * 标记数据状态
     *
     * @param mark 标记枚举
     */
    public void mark(Mark mark) {
        this.mark = mark;
    }

    /**
     * 对象标记枚举
     */
    public enum Mark {
        IDLE, UPDATE, DELETE, INSERT, QUERY
    }
}
