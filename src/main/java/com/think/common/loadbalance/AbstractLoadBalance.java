package com.think.common.loadbalance;

import java.util.List;

public abstract class AbstractLoadBalance implements LoadBalance {
    @Override
    public <T> T select(List<T> shards, String seed) {
        if (shards == null || shards.size() == 0) {
            return null;
        }

        if (shards.size() == 1) {
            shards.get(0);
        }

        return doSelect(shards, seed);
    }

    abstract <T> T doSelect(List<T> shards, String seed);
}
