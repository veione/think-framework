package com.think.core.net.session;

import com.think.core.net.message.NetMessage;

import java.io.Serializable;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelPromise;

/**
 * 网络会话对象接口
 *
 * @author Gavin
 */
public interface Session extends Serializable, AutoCloseable {
    /**
     * 获取Netty Channel
     */
    Channel channel();

    /**
     * 关闭会话接口
     */
    void close();

    /**
     * 该会话是否处于打开状态
     */
    boolean isOpen();

    /**
     * 该会话是否处于激活状态
     */
    boolean isActive();

    /**
     * 获取该会话在线时长
     *
     * @return 当前时间减去会话激活时的时间之间的持续时间
     */
    long getOnlineTime();

    /**
     * 发送消息至客户端
     */
    void send(NetMessage message);

    /**
     * 发送消息至客户端
     *
     * @param message 消息对象
     */
    void send(NetMessage message, ChannelPromise promise);

    void send(NetMessage message, ChannelPromise promise, ChannelFutureListener listener);
}
