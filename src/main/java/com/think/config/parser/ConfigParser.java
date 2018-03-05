package com.think.config.parser;

import java.io.InputStream;

import com.think.config.Config;

@FunctionalInterface
public interface ConfigParser {
  
  Config parser(InputStream in) throws Exception;
}
