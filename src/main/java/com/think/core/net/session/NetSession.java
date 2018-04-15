package com.think.core.net.session;

import com.think.core.net.message.NetMessage;

import java.util.concurrent.TimeUnit;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelPromise;

/**
 * 网络会话对象实现类
 *
 * @author Gavin
 */
public class NetSession implements Session {
    // 网络通道
    private final transient Channel channel;
    // 会话ID
    private final long sessionId;
    // 上次访问时间,这个属性会根据玩家的操作自动修改
    private long accessTime;
    // 会话激活时间, 这个时间是玩家每次连接上服务器的时间
    private final long activeTime;
    // 服务器ID
    private final int serverId;
    // 玩家ID
    private final int userId = 0;

    /**
     * 实例化网络会话对象
     *
     * @param channel 网络通道
     */
    public NetSession(int serverId, long sessionId, Channel channel) {
        this.serverId = serverId;
        this.sessionId = sessionId;
        this.channel = channel;
        this.accessTime = System.currentTimeMillis();
        this.activeTime = System.currentTimeMillis();
    }

    @Override
    public Channel channel() {
        return this.channel;
    }

    @Override
    public void close() {
        if (channel != null) {
            channel.close();
        }
    }

    @Override
    public boolean isOpen() {
        if (channel != null) {
            return channel.isOpen();
        }
        return false;
    }

    @Override
    public boolean isActive() {
        if (channel != null) {
            return channel.isActive();
        }
        return false;
    }

    /**
     * 获取上次访问时间
     */
    public long getAccessTime() {
        return accessTime;
    }

    public void setAccessTime(long accessTime) {
        this.accessTime = accessTime;
    }

    /**
     * 获取该会话在线时长
     *
     * @return 当前时间减去会话激活时的时间之间的持续时间
     */
    @Override
    public long getOnlineTime() {
        return System.currentTimeMillis() - this.activeTime;
    }

    @Override
    public void send(NetMessage message) {
        send(message, null);
    }

    @Override
    public void send(NetMessage message, ChannelPromise promise) {
        send(message, promise, null);
    }

    @Override
    public void send(NetMessage message, ChannelPromise promise, ChannelFutureListener listener) {
        this.setAccessTime(System.currentTimeMillis());
        if (this.isOpen() && channel.isWritable()) {
            if (promise == null) {
                if (listener == null) {
                    channel.writeAndFlush(message, channel.voidPromise());
                } else {
                    channel.writeAndFlush(message, channel.voidPromise()).addListener(listener);
                }
            } else {
                if (listener != null) {
                    channel.writeAndFlush(message, promise).addListener(listener);
                } else {
                    channel.writeAndFlush(message, promise);
                }
            }
        } else {
            channel.eventLoop().schedule(() -> send(message, promise), 1L, TimeUnit.SECONDS);
        }
    }
}
