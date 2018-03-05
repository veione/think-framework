package com.think.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import com.think.config.factory.ParserFactory;
import com.think.config.impl.SystemConfigImpl;

/**
 * 配置文件启动器
 * 
 * @author veion
 *
 */
public final class ConfigLoader {
  /**
   * 默认加载系统配置
   * 
   * @param conf
   * @return
   */
  public static Config load() {
    return new SystemConfigImpl();
  }

  public static Config load(String conf) {
    ClassLoader loader = Thread.currentThread().getContextClassLoader();
    return load(loader, conf);
  }

  public static Config load(ClassLoader loader, String conf) {
    InputStream in = null;
    try {
      if (conf.startsWith("classpath:")) {
        conf = conf.substring("classpath:".length());
      }
      in = loader.getResourceAsStream(conf);
      return ParserFactory.getParser(conf).parser(in);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (in != null) {
        try {
          in.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    return null;
  }

  public static void main(String[] args) {
    Optional<Integer> value = ConfigLoader.load("classpath:app.properties").getInteger("core");
    System.out.println(value);
    Model name = ConfigLoader.load("app.json").toBean(Model.class);
    System.out.println(name.name);
  }
  
  static class Model {
    int id;
    String name;
    
  }
}
