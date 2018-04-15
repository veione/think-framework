package com.think.common.timer;

import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * 时间槽
 */
public class TimeSlot<E> {

    private int id;

    private Set<E> elements = new ConcurrentSkipListSet<>();

    public TimeSlot(int id) {
        this.id = id;
    }

    public void add(E e) {
        elements.add(e);
    }

    public boolean remove(E e) {
        return elements.remove(e);
    }

    public Set<E> getElements() {
        return elements;
    }

    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }
        TimeSlot timeSlot = (TimeSlot) obj;
        if (timeSlot.id != id) {
            return false;
        }
        return true;
    }
}
