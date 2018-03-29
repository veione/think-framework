package com.think.service;

import com.google.protobuf.Descriptors;
import com.google.protobuf.DynamicMessage;
import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;

import com.think.core.HandlerModel;
import com.think.core.HandlerModel.MessageMapping;
import com.think.core.annotation.Handler;
import com.think.core.annotation.Mapping;
import com.think.protocol.GPSDataProto;
import com.think.protocol.Gps;
import com.think.util.ClassScanner;

import org.apache.commons.lang3.time.StopWatch;
import org.apache.logging.log4j.core.config.Configurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * 处理器管理类
 *
 * @author veione
 */
public final class HandlerManager {
    private static Logger logger = LoggerFactory.getLogger(HandlerManager.class);
    private static final Map<Integer, HandlerModel> HANDLERS = new HashMap<>();

    /**
     * 注册处理器
     *
     * @param request 消息请求对象
     * @param model   处理实体类
     */
    public static HandlerModel register(Integer requestId, HandlerModel model) {
        Objects.requireNonNull(requestId);
        Objects.requireNonNull(model);
        return HANDLERS.put(requestId, model);
    }

    /**
     * 获取处理器实体
     *
     * @param requestId 请求消息Id
     */
    public static HandlerModel getHandler(Integer requestId) {
        Objects.requireNonNull(requestId);
        HandlerModel model = HANDLERS.get(requestId);
        if (model == null) {
            throw new IllegalArgumentException("Invalid request message id " + requestId);
        }
        return model;
    }

    /**
     * 执行调用操作
     */
    public static void execute(Integer requestId, byte[] buf) throws Exception {
        HandlerModel model = getHandler(requestId);
        model.getMethod().invoke(model.getClazz(), model.getContext(), parseMessage(model, buf));
    }

    /**
     * 从指定包下加载处理器
     *
     * @param packageName 包名
     */
    public static void load(String packageName) {
        StopWatch watch = new StopWatch();
        watch.start();
        Set<Class<?>> classes = ClassScanner.getClasses(packageName, clazz -> clazz.isAnnotationPresent(com.think.core.annotation.Handler.class));
        try {
            for (Class<?> clz : classes) {
                Object clazz = clz.newInstance();
                Method[] methods = clazz.getClass().getDeclaredMethods();
                for (Method method : methods) {
                    // 得到该类下面的RequestMapping注解
                    Mapping mapping = method.getAnnotation(Mapping.class);
                    if (mapping != null) {
                        HandlerModel model = new HandlerModel();
                        model.setClazz(clazz);
                        model.setClazzDesc(clazz.getClass().getAnnotation(Handler.class).desc());
                        model.setMethodDesc(method.getAnnotation(Mapping.class).desc());
                        model.setRequestId(method.getAnnotation(Mapping.class).requestId());
                        model.setMethod(method);
                        model.setMessage(method.getAnnotation(Mapping.class).msg());
                        processMessageMapping(model);
                        register(model.getRequestId(), model);
                    }
                }
            }
            watch.stop();
            logger.debug("Load {} handlers, total time consuming {}", HANDLERS.size(), watch.getTime());
        } catch (Exception e) {
            logger.error("Load handler error", e);
        }
    }

    private static void processMessageMapping(HandlerModel model) throws Exception {
        MessageMapping mapping = new MessageMapping();
        Class<?> clazz = model.getMessage();
        Method buildMethod = clazz.getMethod("newBuilder", null);
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
    }

    private static Descriptors.Descriptor getDescriptor(Integer requestId) {
        return getHandler(requestId).getMsgMapping().getDescriptor();
    }

    public static Message parseMessage(Integer requestId, byte[] buf) throws InvalidProtocolBufferException {
        return parseMessage(getHandler(requestId), buf);
    }

    /**
     * 解析消息
     *
     * @param handler 请求消息id
     * @param buf     字节缓冲
     */
    public static Message parseMessage(HandlerModel handler, byte[] buf) throws InvalidProtocolBufferException {
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


    public static void main(String[] args) throws Exception {
        Configurator.initialize("Log4j2", "classpath:" + "log4j2.xml");
        HandlerManager.load("com.think");

        HandlerModel handlerModel = HandlerManager.getHandler(100);
        System.out.println("handlerModel = [" + handlerModel + "]");
        System.out.println("getName = [" + GPSDataProto.gps_data.class.getName() + "]");


        // Message message = HandlerManager.parseMessage(100, gps_builder.build().toByteArray());
        StopWatch watch = new StopWatch();
        watch.start();
        //HandlerManager.execute(100, gps_builder.build().toByteArray());
        Gps.gps_data.Builder gps_builder = Gps.gps_data.newBuilder();
        gps_builder.setAltitude(1);
        gps_builder.setDataTime("2017-12-17 16:21:44");
        gps_builder.setGpsStatus(1);
        gps_builder.setLat(39.123);
        gps_builder.setLon(120.112);
        gps_builder.setDirection(30.2F);

        Gps.gps_data gd = null;
        try {
            gd = Gps.gps_data.parseFrom(gps_builder.build().toByteArray());
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        System.out.print(gd.toString());
        watch.stop();
        System.out.println("Consume time = [" + watch.getTime() + "]");
        //System.out.println("message = [" + message + "]");
    }
}
