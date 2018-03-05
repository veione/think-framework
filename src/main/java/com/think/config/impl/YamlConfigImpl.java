package com.think.config.impl;

import org.yaml.snakeyaml.*;

import com.think.config.AbstractConfig;

public class YamlConfigImpl extends AbstractConfig {
  private String yaml;

  public YamlConfigImpl(String yaml) {
    this.yaml = yaml;
  }

  @Override
  public <T> T toBean(Class<T> type) {
    return new Yaml().loadAs(this.yaml, type);
  }
}
