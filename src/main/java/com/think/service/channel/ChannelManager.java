package com.think.service.channel;

import io.netty.channel.Channel;

public interface ChannelManager {

    void createChannel(String name, Delivery delivery);

    Channel getChannel(String name);
}
