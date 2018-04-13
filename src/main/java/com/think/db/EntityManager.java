package com.think.db;

import java.util.List;
import java.util.Map;

/**
 * 实体类管理器接口类
 */
public interface EntityManager {
    /**
     * 加载单条数据
     *
     * @param object
     * @param <T>
     * @return
     */
    <T> T load(Object object);

    /**
     * 加载多条数据
     *
     * @param object
     * @param wheres
     * @param <T>
     * @return
     */
    <T> List<T> load(Object object, Map<String, Object> wheres);

    <T> List<T> load(Object object, Map<String, Object> wheres, int limit);

    /**
     * 插入数据
     *
     * @param object
     * @return
     */
    int insert(Object object);

    /**
     * 更新数据
     *
     * @param object
     * @return
     */
    int update(Object object);

    /**
     * 删除数据
     *
     * @param object
     * @return
     */
    int delete(Object object);

    /**
     * 关闭服务
     */
    void shutdown();
}
