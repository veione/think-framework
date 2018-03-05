package com.think.core;

public abstract class Event<T> {
  protected String source;
  protected String dest;
  protected T body;

  public Event(String source, String dest, T body) {
    this.source = source;
    this.dest = dest;
    this.body = body;
  }

  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public String getDest() {
    return dest;
  }

  public void setDest(String dest) {
    this.dest = dest;
  }

  public T getBody() {
    return body;
  }

  public void setBody(T body) {
    this.body = body;
  }
}
