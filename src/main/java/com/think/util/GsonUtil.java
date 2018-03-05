package com.think.util;

import java.io.Reader;
import java.lang.reflect.Type;
import java.util.Map;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Gson工具类
 * 
 * @author veion
 *
 */
public class GsonUtil {
  private static final Gson GSON = new GsonBuilder().enableComplexMapKeySerialization()
      .setDateFormat("yyyy-MM-dd HH:mm:ss").create();

  public static <T> T fromJson(String json, Class<T> type) {
    return GSON.fromJson(json, type);
  }

  public static <T> T fromJson(Reader reader, Class<T> type) {
    return GSON.fromJson(reader, type);
  }

  public static String toJson(Object obj) {
    return GSON.toJson(obj);
  }

  public static Map<String, Object> toMap(String json) {
    Type type = new TypeToken<Map<String, Object>>() {
      private static final long serialVersionUID = 1L;
    }.getType();
    Map<String, Object> resultMap = GSON.fromJson(json, type);
    return resultMap;
  }
}
