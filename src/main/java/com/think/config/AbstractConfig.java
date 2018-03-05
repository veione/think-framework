package com.think.config;

import java.util.Optional;

public abstract class AbstractConfig implements Config {

  @Override
  public boolean containsKey(String key) {
    return false;
  }

  @Override
  public Optional<String> getString(String key) {
    throw new UnsupportedOperationException();
  }

  @Override
  public String[] getStringArray(String key) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Optional<Integer> getInteger(String key) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Optional<Long> getLong(String key) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Optional<Double> getDouble(String key) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Optional<Boolean> getBoolean(String key) {
    throw new UnsupportedOperationException();
  }

  @Override
  public <T> T toBean(Class<T> type) {
    throw new UnsupportedOperationException();
  }
}
