package com.think.config;

import java.util.Optional;

public interface Config {

  boolean containsKey(String key);

  Optional<String> getString(String key);

  String[] getStringArray(String key);

  Optional<Integer> getInteger(String key);

  Optional<Long> getLong(String key);

  Optional<Double> getDouble(String key);

  Optional<Boolean> getBoolean(String key);


  <T> T toBean(Class<T> type);
}
