package com.think.core.app;

import io.netty.channel.Channel;

/**
 * Manager for creating and obtaining channels. A {@link Channel} is a
 * communication group consisting of multiple client sessions and the
 * server.
 *
 * <p>A Channel is created with a {@link Delivery} guarantee. Messages
 * sent on a channel are delivered in a manner that satisfies the channel's
 * delivery guarantee. When possible, channel messages are delivered using
 * the most efficient means to satisfy the delivery guarantee. However, a
 * stronger delivery guarantee may be used to deliver the message if the
 * underlying protocol only supports stronger delivery guarantees. A
 * client session can not be joined to a channel if that client session
 * does not support a protocol satisfying the minimum requirements of the
 * channel's delivery guarantee.
 *
 * <p>The delivery guarantee of a channel cannot be changed. If different
 * delivery guarantees are needed, then different channels should be used
 * for communication.
 *
 * @see ServerContext#getChannelManager()
 */
public interface ChannelManager {

    Channel createChannel(Channel channel);

    Channel getChannel(String name);
}
