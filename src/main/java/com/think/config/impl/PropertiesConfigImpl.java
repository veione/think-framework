package com.think.config.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Splitter;
import com.think.config.AbstractConfig;

public class PropertiesConfigImpl extends AbstractConfig {
  private Properties props;

  public PropertiesConfigImpl(Properties props) {
    this.props = props;
  }
  
  @Override
  public boolean containsKey(String key) {
    return this.props.containsKey(key);
  }
  
  @Override
  public String[] getStringArray(String key) {
    Optional<String> value = getString(key);
    if (value.isPresent()) {
      List<String> list = new ArrayList<>();
      String tmpValue = value.get();
      Splitter.on(",").split(tmpValue).forEach(s->list.add(s));
      return (String[]) list.toArray();
    }
    return null;
  }

  @Override
  public Optional<String> getString(String key) {
    Optional<String> result = Optional.empty();
    String value = this.props.getProperty(key);
    if (StringUtils.isNotEmpty(value)) {
      result = Optional.of(value);
    }
    return result;
  }

  @Override
  public Optional<Integer> getInteger(String key) {
    Optional<Integer> result = Optional.empty();
    Optional<String> value = getString(key);
    if (value.isPresent()) {
      result = Optional.of(Integer.parseInt(value.get()));
    }
    return result;
  }
  
  @Override
  public Optional<Long> getLong(String key) {
    Optional<Long> result = Optional.empty();
    Optional<String> value = getString(key);
    if (value.isPresent()) {
      result = Optional.of(Long.parseLong(value.get()));
    }
    return result;
  }
  
  @Override
  public Optional<Double> getDouble(String key) {
    Optional<Double> result = Optional.empty();
    Optional<String> value = getString(key);
    if (value.isPresent()) {
      result = Optional.of(Double.parseDouble(value.get()));
    }
    return result;
  }
  
  @Override
  public Optional<Boolean> getBoolean(String key) {
    Optional<Boolean> result = Optional.empty();
    Optional<String> value = getString(key);
    if (value.isPresent()) {
      result = Optional.of(Boolean.parseBoolean(value.get()));
    }
    return result;
  }
  
}
