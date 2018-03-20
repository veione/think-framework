package com.think.util;

import java.lang.reflect.Method;
import java.util.Map;

public class Assert {

  public static void notNull(Class<?> clazz, String desc) {
    if (clazz == null) {
      throw new IllegalArgumentException(desc);
    }
  }

  public static void isTrue(boolean b, String desc) {
    if (!b) {
      throw new IllegalArgumentException(desc);
    }
  }

  public static void notNull(String name, String desc) {
    if (name == null) {
      throw new IllegalArgumentException(desc);
    }
  }

  public static void notNull(Method method, String desc) {
    if (method == null) {
      throw new IllegalArgumentException(desc);
    }
  }

  public static void notNull(Object target, String desc) {
    if (target == null) {
      throw new IllegalArgumentException(desc);
    }
  }

  public static void notEmpty(Map<String, Object> parameters, String desc) {
    if (parameters == null || parameters.isEmpty()) {
      throw new IllegalArgumentException(desc);
    }
  }

}
