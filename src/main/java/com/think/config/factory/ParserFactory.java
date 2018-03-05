package com.think.config.factory;

import com.think.config.parser.ConfigParser;
import com.think.config.parser.JsonParser;
import com.think.config.parser.PropertiesParser;
import com.think.config.parser.YamlParser;

/**
 * 解析工厂
 * 
 * @author Ricky Fung
 */
public class ParserFactory {

  /**
   * 根据配置文件返回具体解析类型
   * 
   * @param ext 配置文件后缀
   * @return 真正解析配置文件的类型
   */
  public static ConfigParser getParser(String conf) {
    ConfigParser parser = null;
    if (conf.contains("json")) {
      parser = new JsonParser();
    } else if (conf.contains("yaml")) {
      parser = new YamlParser();
    } else if (conf.contains("properties")) {
      parser = new PropertiesParser();
    } else {
      throw new UnsupportedOperationException(String.format("%s format unsupported.", conf));
    }
    return parser;
  }
}
