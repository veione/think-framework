package com.think.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间工具类
 * 
 * @author veion
 *
 */
public class TimeUtil {
  public static final String DATE_TIME_PATTERN = "YYYY-MM-dd HH:mm:ss";
  public static final String DATE_PATTERN = "YYYY-MM-dd";
  public static final String TIME_PATTERN = "HH:mm:ss";
  private static final SimpleDateFormat FORMAT = new SimpleDateFormat(DATE_TIME_PATTERN);

  /**
   * 获取现在的时间戳
   * 
   * @return 现在距离1900-01-01的毫秒数
   */
  public static long getCurTimeSeconds() {
    return System.currentTimeMillis();
  }

  /**
   * 根据指定的时间格式返回年月日时分秒
   * 
   * @param patten 时间日期格式
   */
  public static String getNowDateTime(String patten) {
    SimpleDateFormat format = new SimpleDateFormat(patten);
    String result = format.format(new Date());
    return result;
  }

  /**
   * 返回年月日时分秒 格式：yyyy-MM-dd HH:mm:ss
   */
  public static String getNowDateTime() {
    return FORMAT.format(new Date());
  }
}
