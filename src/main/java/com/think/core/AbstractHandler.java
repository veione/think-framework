package com.think.core;

import com.google.protobuf.GeneratedMessage;

/**
 * 抽象消息处理器
 * 
 * @author veione
 *
 * @param <T> 具体消息类
 */
public abstract class AbstractHandler<T extends GeneratedMessage> implements Handler {
  protected T request;
  protected Session session;

  public void init(T request, Session session) {
    this.request = request;
    this.session = session;
  }

  @Override
  public void handler() {
    preHandler();
    onHandler();
    postHandler();
  }

  public abstract void onHandler();

  public void preHandler() {
    throw new UnsupportedOperationException();
  }

  public void postHandler() {
    throw new UnsupportedOperationException();
  }
}
