package com.think.common.service;

import java.util.Map;

/**
 * 服务提供者接口类
 *
 * @author Gavin
 */
public interface ServiceProvider {
    /**
     * 从指定的包位置下加载服务组件
     *
     * @param pkg 包名
     */
    void loadService(String pkg);

    /**
     * 根据服务组件的类型获取服务组件类
     *
     * @param type 服务组件类对象
     * @return 服务组件实例对象
     */
    <T extends Service> T getService(Class<?> type);

    /**
     * 当服务不可用时,卸载服务
     *
     * @param type 服务类组件对象
     */
    void unloadService(Class<?> type);

    /**
     * 获取所有服务组件
     */
    Map<Class<?>, Service> getServiceMap();
}
