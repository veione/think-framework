package com.think.service;

import com.esotericsoftware.reflectasm.MethodAccess;
import com.esotericsoftware.reflectasm.PublicConstructorAccess;
import com.google.protobuf.*;
import com.think.core.annotation.Handler;
import com.think.core.HandlerMapping;
import com.think.core.MessageMapping;
import com.think.core.annotation.Mapping;
import com.think.util.ClassScanner;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MessageManager {
    private static final Map<Integer, GeneratedMessage> MESSAGES = new HashMap<>();

    public void load() {
        Map<Integer, HandlerMapping> handlerMap = HandlerManager.getHandlers();

        for (Map.Entry<Integer, HandlerMapping> entry : handlerMap.entrySet()) {
            HandlerMapping handler = entry.getValue();
            handler.getMethod();

        }
        Set<Class<?>> clsses = ClassScanner.getClasses(pkg, clazz -> clazz.isAnnotationPresent(Handler.class));
        try {
            for (Class clz : clsses) {
                Object obj = PublicConstructorAccess.get(clz.getClass());
                Arrays.stream(obj.getClass().getDeclaredMethods()).filter(m -> m.isAnnotationPresent(Mapping.class)).forEach(m -> {
                    Class<?> msg = m.getAnnotation(Mapping.class).msg();

                    MessageMapping mapping = new MessageMapping();
                    Method buildMethod = msg.getMethod("newBuilder", null);
                    Object buildObject = buildMethod.invoke(null);
                    Field[] buildFields = buildObject.getClass().getDeclaredFields();
                    Arrays.stream(buildFields).forEach(b -> b.setAccessible(true));


                    Method m = clazz.getMethod("getDescriptor", null);
                    Object object = m.invoke(null);
                    Descriptors.Descriptor descriptor = (Descriptors.Descriptor) object;

                    mapping.setDescriptor(descriptor);
                    mapping.setMsgBuilderCls(buildObject);
                    mapping.setMsgFields(buildFields);
                    model.setMsgMapping(mapping);
                });

            }
        } catch (Exception e) {

        }

    }


    private static Descriptors.Descriptor getDescriptor(Integer requestId) {
        return getHandler(requestId).getMsgMapping().getDescriptor();
    }

    public static Message parseMessage(Integer requestId, byte[] buf) throws InvalidProtocolBufferException {
        DynamicMessage dynamicMessage = DynamicMessage.parseFrom(getDescriptor(handler.getRequestId()), buf);


    }

    /**
     * 解析消息
     *
     * @param handler 请求消息id
     * @param buf     字节缓冲
     */
    public static Message parseMessage(HandlerMapping handler, byte[] buf) throws InvalidProtocolBufferException {
        DynamicMessage dynamicMessage = DynamicMessage.parseFrom(getDescriptor(handler.getRequestId()), buf);
        DynamicMessage.Builder builder = dynamicMessage.toBuilder();
        Map<Descriptors.FieldDescriptor, Object> fieldsMap = builder.getAllFields();

        Field[] buildFields = handler.getMsgMapping().getMsgFields();
        Object buildObject = handler.getMsgMapping().getMsgBuilderCls();

        fieldsMap.forEach((k, v) -> {
            String fieldKey = k.getJsonName();
            Arrays.stream(buildFields).forEach(c -> {
                if (fieldKey.equals(c.getName().replace("_", ""))) {
                    try {
                        c.set(buildObject, v);
                    } catch (IllegalAccessException e) {
                        // ignore
                    }
                }
            });
        });

        GeneratedMessage.Builder msgBuilder = (GeneratedMessage.Builder) buildObject;
        Message message = msgBuilder.build();
        return message;
    }
}
