package com.think.common.service;

import com.think.exception.ServiceInitializationException;
import com.think.exception.ServiceNotFoundException;
import com.think.util.ClassScanner;

import java.lang.reflect.Modifier;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 一个默认实现了服务组件的提供管理类
 *
 * @author Gavin
 */
public class DefaultServiceProvider implements ServiceProvider, Iterable<Map<Class<?>, Service>> {
    /**
     * 服务组件集合
     */
    private final Map<Class<?>, Service> serviceMap = new ConcurrentHashMap<>();

    /**
     * 从指定的包名路径下加载服务组件
     *
     * @param pkg 包名
     */
    @Override
    public void loadService(String pkg) {
        Set<Class<?>> classSet = ClassScanner.getClasses(pkg, c -> Service.class.isAssignableFrom(c) && !Modifier.isAbstract(c.getModifiers()));
        Class<?> type = null;
        try {
            for (Class<?> clazz : classSet) {
                type = clazz;
                Service service = (Service) type.newInstance();
                service.start();
                serviceMap.put(clazz, service);
                service.ready();
            }
        } catch (InstantiationException | IllegalAccessException e) {
            throw new ServiceInitializationException(String.format("Service %s initialization failed.", type));
        }
    }

    @Override
    public <T extends Service> T getService(Class<?> type) {
        Objects.requireNonNull(type);
        T service = (T) serviceMap.get(type);
        if (service == null) {
            throw new ServiceNotFoundException("Service for " + type + " not found.");
        }
        return service;
    }

    @Override
    public void unloadService(Class<?> type) {
        Objects.requireNonNull(type);
        Service service = serviceMap.remove(type);
        if (service != null) {
            // 先暂停服务再停止服务
            service.pause();
            service.stop();
        }
    }

    @Override
    public Map<Class<?>, Service> getServiceMap() {
        return serviceMap;
    }

    @Override
    public Iterator<Map<Class<?>, Service>> iterator() {
        return new Iter(serviceMap.entrySet().iterator());
    }

    private class Iter implements Iterator {

        final Iterator<Map.Entry<Class<?>, Service>> mapIter;

        public Iter(Iterator<Map.Entry<Class<?>, Service>> mapIter) {
            this.mapIter = mapIter;
        }


        @Override
        public boolean hasNext() {
            return mapIter.hasNext();
        }

        @Override
        public Object next() {
            return mapIter.next();
        }

        @Override
        public void remove() {
            mapIter.remove();
        }
    }
}
