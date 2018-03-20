package com.think.core.net;

import com.google.protobuf.MessageLite;

import java.util.HashMap;
import java.util.Map;

public final class MessageManager {
    public static final Map<Integer, MessageLite> LITE_MAP = new HashMap<>();

    /**
     * 从配置文件中加载消息
     */
    public static void load(String conf) {
        // TODO 从配置文件中加载消息
    }

    /**
     * 添加消息至集合中
     */
    public static void add(Integer msgId, MessageLite msgLite) {
        // TODO 添加消息
    }

    /**
     * 从消息列表中移除该消息
     */
    public static void remove(Integer msgId) {
        // TODO 根据消息ID移除消息
    }

    /**
     * 消息列表中是否包含该消息
     */
    public static boolean contain(Integer msgId) {
        return LITE_MAP.containsKey(msgId);
    }

    /**
     * 获取消息大小
     */
    public static int size() {
        return LITE_MAP.size();
    }

    /**
     * 根据消息号获取消息
     */
    public static MessageLite get(int msgId) {
        return LITE_MAP.get(msgId);
    }
}
