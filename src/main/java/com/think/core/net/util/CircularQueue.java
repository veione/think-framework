package com.think.core.net.util;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class CircularQueue<T> {
    private BlockingQueue<T> buffer;

    public CircularQueue(int size) {
        this.buffer = new ArrayBlockingQueue<>(size);
    }

    public T take() throws InterruptedException {
        return this.buffer.take();
    }

    public List<T> pollBulk() {
        List<T> lists = new LinkedList<>();
        this.buffer.drainTo(lists);
        return lists;
    }

    public List<T> pollBulk(int maxItem) {
        List<T> lists = new LinkedList<>();
        this.buffer.drainTo(lists, maxItem);
        return lists;
    }

    public boolean offer(T obj) {
        return this.buffer.offer(obj);
    }

    public int size() {
        return this.buffer.size();
    }
}
