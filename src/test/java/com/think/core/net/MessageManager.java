package com.think.core.net;

import com.google.protobuf.Message;

import com.think.config.Config;
import com.think.config.ConfigLoader;

import java.util.HashMap;
import java.util.Map;

/**
 * 消息管理器
 *
 * @author veione
 * @date 2018年3月21日21:01:30
 */
public final class MessageManager {
    private static final Map<Integer, Message> MSG_MAP = new HashMap<>();

    private static class MessageMappingModel {
        private short msgId;
        private String clzz;

        public MessageMappingModel(short msgId, String clzz) {
            this.msgId = msgId;
            this.clzz = clzz;
        }

        public short getMsgId() {
            return msgId;
        }

        public void setMsgId(short msgId) {
            this.msgId = msgId;
        }

        public String getClzz() {
            return clzz;
        }

        public void setClzz(String clzz) {
            this.clzz = clzz;
        }

        @Override
        public String toString() {
            return "MessageMappingModel{" +
                    "msgId=" + msgId +
                    ", clzz='" + clzz + '\'' +
                    '}';
        }
    }

    /**
     * 从配置文件中加载消息
     */
    public static void load(String conf) {
        // TODO 从配置文件中加载消息
        Config config = ConfigLoader.load(conf);
        MessageMappingModel model = config.toBean(MessageMappingModel.class);

    }

    /**
     * 添加消息至集合中
     */
    public static Message register(Integer msgId, Message msg) {
        return MSG_MAP.put(msgId, msg);
    }

    /**
     * 从消息列表中移除该消息
     */
    public static Message remove(Integer msgId) {
        return MSG_MAP.remove(msgId);
    }

    /**
     * 消息列表中是否包含该消息
     */
    public static boolean contains(Integer msgId) {
        //MSG_MAP.values().stream().mapToInt(v->v.getSerializedSize()).max();
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
