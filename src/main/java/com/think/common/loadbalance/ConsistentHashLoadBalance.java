package com.think.common.loadbalance;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class ConsistentHashLoadBalance extends AbstractLoadBalance {
    @Override
    <T> T doSelect(List<T> shards, String seed) {
        if (seed == null || seed.length() == 0) {
            seed = "HASH-".concat(String.valueOf(ThreadLocalRandom.current().nextInt()));
        }
        ConsistentHashSelector<T> selector = new ConsistentHashSelector<T>(shards);
        return selector.selectForKey(seed);
    }
}
