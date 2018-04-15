package com.think.common.loadbalance;

import java.util.List;

/**
 * 负载均衡有好几种实现策略，常见的有：
 *
 * 随机 (Random)
 * 轮询 (RoundRobin)
 * 一致性哈希 (ConsistentHash)
 * 哈希 (Hash)
 * 加权（Weighted）
 */
public interface LoadBalance {

    <T> T select(List<T> shards, String seed);
}
