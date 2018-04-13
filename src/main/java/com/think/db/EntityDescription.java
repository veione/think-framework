package com.think.db;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 实体类描述类
 *
 * @author veione
 */
public class EntityDescription {
    private String tableName;
    private Object entity;
    private Class<?> entityClass;
    private PropertyDescriptor[] propertys;

    public EntityDescription() {
        this("");
    }

    public EntityDescription(String tableName) {
        this.tableName = tableName;
    }

    /**
     * 解析实体类
     *
     * @param object
     */
    public void parse(Object object) {
        Class<?> clss;
        if (object instanceof Class) {
            clss = (Class) object;
        } else {
            clss = object.getClass();
        }
        Table table = clss.getAnnotation(Table.class);
        if (table == null) {
            throw new IllegalArgumentException("Invalid class " + clss);
        }
        this.entity = object;
        this.tableName = table.table();
        this.propertys = getBeanInfo(clss);
        this.entityClass = clss;
    }

    public Class<?> getEntityClass() {
        return this.entityClass;
    }

    /**
     * 获取实体类的属性描述
     *
     * @param object
     * @return
     */
    private PropertyDescriptor[] getBeanInfo(Class<?> object) {
        BeanInfo beanInfo = null;
        try {
            beanInfo = Introspector.getBeanInfo(object);
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
        PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();
        return descriptors;

    }

    public Map<String, Object> getReadWriteProperties(Object object) {
        Map<String, Object> paramsMap = new HashMap<>();
        try {
            for (PropertyDescriptor desc : this.propertys) {
                String name = desc.getName();
                Object value = desc.getReadMethod().invoke(object, null);
                if (value != null && !name.equalsIgnoreCase("class")) {
                    paramsMap.put(name, value);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return paramsMap;
    }


    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public PropertyDescriptor[] getPropertys() {
        return propertys;
    }

    public void setPropertys(PropertyDescriptor[] propertys) {
        this.propertys = propertys;
    }
}
