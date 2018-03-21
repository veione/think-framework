package com.think.core.net;

import com.google.protobuf.Message;
import com.google.protobuf.MessageLite;

import java.util.HashMap;
import java.util.Map;

public final class MessageManager {
    private static final Map<Integer, Message> MSG_MAP = new HashMap<>();

    /**
     * 从配置文件中加载消息
     */
    public static void load(String conf) {
        // TODO 从配置文件中加载消息
    }

    /**
     * 添加消息至集合中
     */
    public static void add(Integer msgId, Message msg) {
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
        return MSG_MAP.containsKey(msgId);
    }

    /**
     * 获取消息大小
     */
    public static int size() {
        return MSG_MAP.size();
    }

    /**
     * 根据消息号获取消息
     */
    public static Message get(int msgId) {
        return MSG_MAP.get(msgId);
    }
}
