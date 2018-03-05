package com.think.config.impl;

import com.think.config.AbstractConfig;
import com.think.util.GsonUtil;

public class JsonConfigImpl extends AbstractConfig {
  private String json;

  public JsonConfigImpl(String json) {
    this.json = json;
  }

  @Override
  public <T> T toBean(Class<T> type) {
    return GsonUtil.fromJson(this.json, type);
  }
}
