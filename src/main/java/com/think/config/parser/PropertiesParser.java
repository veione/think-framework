package com.think.config.parser;

import java.io.InputStream;
import java.util.Properties;

import com.think.config.Config;
import com.think.config.impl.PropertiesConfigImpl;

/**
 * Properties解析工具类
 * 
 * @author veion
 *
 */
public class PropertiesParser implements ConfigParser {

  @Override
  public Config parser(InputStream in) throws Exception {
    Properties props = new Properties();
    props.load(in);
    return new PropertiesConfigImpl(props);
  }

}
