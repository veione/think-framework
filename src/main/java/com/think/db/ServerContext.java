package com.think.db;

import com.think.service.channel.ChannelManager;

public class ServerContext {
    public static EntityManager getEntityManager() {
        return new EntityManagerImpl();
    }

    public static ChannelManager getChannelManager() {
        return null;
    }
}
