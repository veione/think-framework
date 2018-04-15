package com.think.common.loadbalance;

import java.util.List;

/**
 * RoundRobin轮询策略表示每次都取下一个服务器，
 * 比如一共有5台服务器，第1次取第1台，第2次取第2台，第3次取第3台，以此类推：
 */
public class RoundbinLoadBalance extends AbstractLoadBalance {
    @Override
    <T> T doSelect(List<T> shards, String seed) {
        return null;
    }
}
