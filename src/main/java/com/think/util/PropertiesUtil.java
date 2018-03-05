package com.think.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Properties工具类
 * 
 * @author veione
 *
 */
public class PropertiesUtil {
  private Properties props;
  private String conf;
  
  public PropertiesUtil(String conf) {
    this.conf = conf;
    load(conf);  
  }

  /**
   * 根据文件名读取配置文件
   * 
   * @param conf 文件名
   */
  private void load(String conf) {
    try {
      this.props = new Properties();
      InputStream in = new BufferedInputStream(new FileInputStream(conf));
      this.props.load(in);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public String getProperty(String key) {
    return this.props.getProperty(key);
  }

  /**
   * 获取所有的属性集合
   * 
   * @return 所有的属性集合Map
   */
  public Map<String, String> getAllProperty() {
    Map<String, String> propMap = new HashMap<>();
    Enumeration<?> enumeration = this.props.propertyNames();
    while (enumeration.hasMoreElements()) {
      String key = (String) enumeration.nextElement();
      String value = this.props.getProperty(key);
      propMap.put(key, value);
    }
    return propMap;
  }

  /**
   * 打印所有属性
   */
  public void printProperties() {
    this.props.list(System.out);
  }

  /**
   * 写出属性
   * 
   * @param key 属性名
   * @param value 属性值
   */
  public void writeProperty(String key, String value) {
    OutputStream os;
    try {
      os = new BufferedOutputStream(new FileOutputStream(this.conf));
      this.props.setProperty(key, value);
      this.props.store(os, "Update key : " + key);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
