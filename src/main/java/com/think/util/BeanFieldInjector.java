package com.think.util;

import java.lang.reflect.Field;
import java.util.Map;
import org.apache.commons.lang3.ClassUtils;

/**
 * Bean注入(通过字段注入)工具。
 * 
 * @author 刘飞
 * 
 */
public class BeanFieldInjector {
  /**
   * 将指定参数[Map : parameters]注入到target实例化对象中并返回。
   * 
   * @param target 目标类字符串。
   * @param parameters 参数集合, 以字段名称为键。
   * @return 返回注入的实例结果。
   * @throws ClassNotFoundException
   * @throws LinkageError
   * @throws InstantiationException
   * @throws IllegalAccessException
   */
  public Object setBeanByField(String target, Map<String, Object> parameters)
      throws ClassNotFoundException, LinkageError, InstantiationException, IllegalAccessException {
    Assert.notNull(target, "target class must not be null");
    Assert.notEmpty(parameters, "parameters map must not be null");

    Class<?> targetClass = ClassUtils.getClass(BeanFieldInjector.class.getClassLoader(), target);

    Object targetBean = targetClass.newInstance();

    Field[] allFields = ReflectionUtils.getAllDeclaredFields(targetClass);

    for (Field field : allFields) {
      ReflectionUtils.makeAccessible(field);
      if (parameters.containsKey(field.getName()))
        ReflectionUtils.setField(field, targetBean,
            ReflectionUtils.simpleTypeValue(field.getType(), parameters.get(field.getName())));
    }
    return targetBean;
  }

  /**
   * 将指定参数[Map : parameters]注入到target实例化对象中并返回。
   * 
   * @param target 目标类。
   * @param parameters 参数集合, 以字段名称为键。
   * @return 返回注入的实例结果。
   * @throws ClassNotFoundException
   * @throws LinkageError
   * @throws InstantiationException
   * @throws IllegalAccessException
   */
  public Object setBeanByField(Class<?> target, Map<String, Object> parameters)
      throws InstantiationException, IllegalAccessException {
    Assert.notNull(target, "target class must not be null");
    Assert.notEmpty(parameters, "parameters map must not be null");

    Class<?> targetClass = target;

    Object targetBean = targetClass.newInstance();

    Field[] allFields = ReflectionUtils.getAllDeclaredFields(targetClass);

    for (Field field : allFields) {
      ReflectionUtils.makeAccessible(field);
      if (parameters.containsKey(field.getName()))
        ReflectionUtils.setField(field, targetBean,
            ReflectionUtils.simpleTypeValue(field.getType(), parameters.get(field.getName())));
    }
    return targetBean;
  }

  /**
   * 将指定参数[Map : parameters]注入到target实例化对象中并返回。
   * 
   * @param target 目标对象。
   * @param parameters 参数集合, 以字段名称为键。
   * @return 返回注入的实例结果。
   * @throws ClassNotFoundException
   * @throws LinkageError
   * @throws InstantiationException
   * @throws IllegalAccessException
   */
  public Object setBeanByField(Object target, Map<String, Object> parameters)
      throws InstantiationException, IllegalAccessException {
    Assert.notNull(target, "target class must not be null");
    Assert.notEmpty(parameters, "parameters map must not be null");

    Object object = target;

    Class<?> targetClass = object.getClass();

    Object targetBean = targetClass.newInstance();

    Field[] allFields = ReflectionUtils.getAllDeclaredFields(targetClass);

    for (Field field : allFields) {
      ReflectionUtils.makeAccessible(field);
      if (parameters.containsKey(field.getName()))
        ReflectionUtils.setField(field, targetBean,
            ReflectionUtils.simpleTypeValue(field.getType(), parameters.get(field.getName())));
    }
    return targetBean;
  }

  /**
   * 将指定参数[Map : parameters]注入到字段target对应的类型实例化对象中并返回。
   * 
   * @param target 目标字段。
   * @param parameters 参数集合, 以target字段名称为键和它对应的类型中的字段名称为键。
   * 
   *        如：User user = new User() ;通过user.name注入。
   * 
   * @return 返回注入的实例结果。
   * @throws ClassNotFoundException
   * @throws LinkageError
   * @throws InstantiationException
   * @throws IllegalAccessException
   */
  public Object setFieldBeanByField(Field target, Map<String, Object> parameters)
      throws InstantiationException, IllegalAccessException {

    Assert.notNull(target, "target class must not be null");
    Assert.notEmpty(parameters, "parameters map must not be null");

    String fieldName = target.getName();

    Class<?> targetClass = target.getType();

    Object targetBean = targetClass.newInstance();

    Field[] allFields = ReflectionUtils.getAllDeclaredFields(targetClass);

    for (Field field : allFields) {
      ReflectionUtils.makeAccessible(field);
      if (parameters.containsKey(fieldName + "." + field.getName()))
        ReflectionUtils.setField(field, targetBean, ReflectionUtils.simpleTypeValue(field.getType(),
            parameters.get(fieldName + "." + field.getName())));
    }
    return targetBean;

  }
}
