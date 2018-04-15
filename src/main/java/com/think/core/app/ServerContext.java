package com.think.core.app;

/**
 * 应用上下文管理对象
 */
public final class ServerContext {

    private ServerContext() {
    }

    public static ChannelManager getChannelManager() {

        return null;
    }

    public static SessionManager getSessionManager() {

        return null;
    }

    public static DataManager getDataManager() {

        return null;
    }

    public static TaskManager getTaskManager() {

        return null;
    }

    public static <T> T getManager(Class<T> type) {

        return null;
    }
}
