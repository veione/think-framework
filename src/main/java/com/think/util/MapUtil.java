package com.think.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Map工具类
 *
 * @author Gavin
 */
public final class MapUtil {
    /**
     * Map集合转对象
     *
     * @param type 需要转换的对象
     * @param map  map集合对象
     */
    public static <T> T mapToBean(Class<T> type, Map map) {
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(type);
            T obj = (T) type.newInstance();

            PropertyDescriptor[] pps = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor desc : pps) {
                String fieldName = desc.getName();
                if (map.containsKey(fieldName)) {
                    Object value = map.get(fieldName);
                    desc.getWriteMethod().invoke(obj, value);
                }
            }
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 对象转Map集合
     *
     * @param bean 对象
     * @return Map集合
     */
    public static Map<String, Object> objectToMap(Object bean) throws InvocationTargetException, IllegalAccessException, IntrospectionException, InstantiationException {
        Objects.requireNonNull(bean);
        final Map<String, Object> description = new HashMap<>();
        final PropertyDescriptor[] descriptors = getPropertyDescriptors(bean);
        for (PropertyDescriptor descriptor : descriptors) {
            final String name = descriptor.getName();
            final Object value = descriptor.getReadMethod().invoke(bean);
            if (value != null) {
                description.put(name, value);
            }
        }
        return description;
    }

    /**
     * 获取对象的属性数组
     *
     * @param object 给定的对象
     * @return 属性数组
     */
    public static PropertyDescriptor[] getPropertyDescriptors(Object object) throws IntrospectionException, IllegalAccessException, InstantiationException {
        Class<?> clazz;
        if (object instanceof Class) {
            clazz = (Class) object;
        } else {
            clazz = object.getClass();
        }
        BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
        PropertyDescriptor[] pps = beanInfo.getPropertyDescriptors();
        return pps;
    }
}
