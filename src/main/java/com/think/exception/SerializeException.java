package com.think.exception;

/**
 * 序列化异常
 * 
 * @author veione
 *
 */
public class SerializeException extends Exception {
  private static final long serialVersionUID = -5385210555516486845L;

  public SerializeException(String msg) {
    super(msg);
  }
}
