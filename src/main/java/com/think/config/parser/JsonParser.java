package com.think.config.parser;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import com.think.config.Config;
import com.think.config.impl.JsonConfigImpl;

public class JsonParser implements ConfigParser {

  @Override
  public Config parser(InputStream in) throws Exception {
    ByteArrayOutputStream bos = null;
    try {
      bos = new ByteArrayOutputStream();
      byte[] buf = new byte[1024];
      int length;
      while ((length = in.read(buf)) != -1) {
        bos.write(buf, 0, length);
      }
      return new JsonConfigImpl(bos.toString("UTF-8"));
    } finally {
      bos.flush();
      if (bos != null) {
        bos.close();
      }
    }
  }

}
