package com.think.common.loadbalance;

import java.util.List;

/**
 * BestAvailableRule策略用来选取最少并发量请求的服务器：
 */
public class BestAvailableLoadBalance extends AbstractLoadBalance {
    @Override
    <T> T doSelect(List<T> shards, String seed) {
        return null;
    }
}
