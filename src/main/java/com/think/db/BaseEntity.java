package com.think.db;

public abstract class BaseEntity<T> {
    protected Mark mark = Mark.NONE;

    public void mark(Mark mark) {
        this.mark = mark;
    }

    public enum Mark {
        NONE, UPDATE, DELETE, INSERT, QUERY
    }
}
