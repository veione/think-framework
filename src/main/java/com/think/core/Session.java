package com.think.core;

import java.io.Serializable;
import java.util.concurrent.locks.ReentrantLock;

import com.think.common.DataMap;
import com.think.util.TimeUtil;

import io.netty.channel.Channel;

/**
 * 网络会话对象
 * 
 * @author veion
 *
 */
public final class Session implements Serializable {
  private static final long serialVersionUID = -4950161575479529827L;
  public int sessionId;
  public transient Channel ioChannel;
  public int userId;
  public int accountId;
  public String nickName;
  public String clientIP;
  public int serverId;
  public String publisher = "0";
  public String clientVersion = "0";
  public String logoutCase = "";
  public byte protocolVersion;
  private final ReentrantLock mainLock = new ReentrantLock();

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public String getNickName() {
    return nickName;
  }

  public void setNickName(String nickName) {
    this.nickName = nickName;
  }

  public String getClientIP() {
    return clientIP;
  }

  public void setClientIP(String clientIP) {
    this.clientIP = clientIP;
  }

  public int getServerId() {
    return serverId;
  }

  public void setServerId(int serverId) {
    this.serverId = serverId;
  }

  public String getPublisher() {
    return publisher;
  }

  public void setPublisher(String publisher) {
    this.publisher = publisher;
  }

  public String getClientVersion() {
    return clientVersion;
  }

  public void setClientVersion(String clientVersion) {
    this.clientVersion = clientVersion;
  }

  public ReentrantLock getMainLock() {
    return mainLock;
  }

  public DataMap toMap() {
    DataMap data = new DataMap();
    data.put("accountId", accountId).put("clientIP", clientIP).put("nickName", nickName)
        .put("publisher", publisher).put("serverId", serverId).put("userId", userId)
        .put("timestamp", TimeUtil.getNowDateTime());
    return data;
  }
}
