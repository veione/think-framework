package com.think.core;

import com.google.common.collect.Lists;

import java.io.Serializable;
import java.net.InetSocketAddress;
import java.util.List;

import com.think.common.DataMap;
import com.think.core.net.message.ResponseWrapper;
import com.think.service.MessageManager;
import com.think.service.SessionManager;
import com.think.util.TimeUtil;

import io.netty.channel.Channel;

/**
 * 网络会话对象
 *
 * @author veion
 */
public final class Session implements Serializable {
    private static final long serialVersionUID = -4950161575479529827L;
    public long sessionId;
    public transient Channel channel;
    public int userId;
    public int accountId;
    public String nickName;
    public String clientIP;
    public int serverId;
    public long accessTime;
    public DataMap params;

    public Session(Channel channel) {
        this.sessionId = SessionManager.getInstance().nextId();
        this.channel = channel;
        this.userId = 0;
        this.clientIP = ((InetSocketAddress) channel.remoteAddress()).getAddress().getHostAddress();
        this.accessTime = System.currentTimeMillis();
        this.params = new DataMap();
    }

    public static Session newSession(Channel channel) {
        return new Session(channel);
    }

    public void close() {
        channel.close();
    }


    public void write(ResponseWrapper message) {
        List<ResponseWrapper> messages = Lists.newArrayList();
        messages.add(message);
        write(messages);
    }

    public void write(List<ResponseWrapper> messages) {
        MessageManager.write(channel, messages);
    }

    public DataMap toMap() {
        DataMap data = new DataMap();
        data.put("accountId", accountId).put("clientIP", clientIP).put("nickName", nickName)
                .put("serverId", serverId).put("userId", userId)
                .put("timestamp", TimeUtil.getNowDateTime());
        return data;
    }

}
