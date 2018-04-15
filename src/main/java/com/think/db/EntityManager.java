package com.think.db;

import java.util.List;
import java.util.Map;

/**
 * 实体类管理器接口类
 *
 * @author Gavin
 */
public interface EntityManager {
    /**
     * 加载单条数据
     */
    <T> T load(Object object);

    /**
     * 加载多条数据
     */
    <T> List<T> load(Object object, Map<String, Object> wheres);

    /**
     * 加载多条数据并指定查询条件和查询数量
     *
     * @param object 加载对象
     * @param wheres where条件
     * @param limit  查询数量
     */
    <T> List<T> load(Object object, Map<String, Object> wheres, int limit);

    /**
     * 插入数据
     */
    int insert(Object object);

    /**
     * 更新数据
     */
    int update(Object object);

    /**
     * 删除数据
     */
    int delete(Object object);

    /**
     * 关闭服务
     */
    void shutdown();
}
