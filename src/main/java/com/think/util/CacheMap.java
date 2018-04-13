package com.think.util;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

/**
 * 一个可缓存的Map集合数据
 *
 * @param <K>
 * @param <V>
 */
public class CacheMap<K, V> {
    private final Map<K, Value<K, V>> cacheMap = new HashMap<>();
    private final ReferenceQueue<V> queue = new ReferenceQueue<>();
    private final long entryTimeout;

    public CacheMap() {
        this(0);
    }

    public CacheMap(long entryTimeout) {
        if (entryTimeout < 0) {
            throw new IllegalArgumentException("entryTimeout is negative");
        }
        this.entryTimeout = entryTimeout;
    }

    public V put(K key, V value) {
        processQueue();
        Value<K, V> oldValue =
                cacheMap.put(key, new Value<>(key, value, queue, entryTimeout));
        return oldValue != null ? oldValue.get() : null;
    }

    public boolean containsKey(K key) {
        processQueue();
        return get(key) != null;

    }

    public V get(K key) {
        processQueue();
        Value<K, V> value = cacheMap.get(key);
        if (value == null) {
            return null;
        } else if (value.isExpired()) {
            cacheMap.remove(key);
            return null;
        } else {
            return value.get();
        }
    }

    public V remove(K key) {
        processQueue();
        Value<K, V> oldValue = cacheMap.remove(key);
        return oldValue != null ? oldValue.get() : null;
    }

    /**
     * Removes all associations from this map.
     */
    public void clear() {
        processQueue();
        cacheMap.clear();
    }

    private void processQueue() {
        while (true) {
            /* No way to say that the queue holds Values */
            @SuppressWarnings("unchecked")
            Value<K, V> value = (Value<K, V>) queue.poll();
            if (value != null) {
                cacheMap.remove(value.getKey());
            } else {
                break;
            }
        }
    }


    private static final class Value<K, V> extends SoftReference<V> {
        /**
         * The value's associated key.
         */
        private final K key;
        /**
         * The value's expiration time.
         */
        private final long expirationTime;

        /**
         * Creates an instance of this class and registers it with the
         * specified reference queue.
         *
         * @param key     the key
         * @param value   the value (held softly)
         * @param queue   the reference queue
         * @param timeout a timeout (0 = infinite)
         */
        Value(K key, V value, ReferenceQueue<V> queue, long timeout) {
            super(value, queue);
            this.key = key;
            this.expirationTime =
                    timeout > 0 ?
                            System.currentTimeMillis() + timeout :
                            Long.MAX_VALUE;
        }

        /**
         * Returns {@code true} if this value has expired, otherwise
         * returns {@code false}.
         */
        boolean isExpired() {
            return expirationTime <= System.currentTimeMillis();
        }

        /**
         * Returns this value's associated key.
         */
        K getKey() {
            return key;
        }
    }
}
