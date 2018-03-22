package com.think.protocol;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.DynamicMessage;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import org.apache.commons.beanutils.BeanUtils;

public class TestGpsProtobuf {

    public static void main(String[] args) throws Exception {
        Map<Integer, Descriptor> descriptorMap = new HashMap<>();
        descriptorMap.put(100, Gps.gps_data.getDescriptor());


        System.out.println("===== 构建一个GPS模型开始 =====");
        Gps.gps_data.Builder gps_builder = Gps.gps_data.newBuilder();
        gps_builder.setAltitude(1);
        gps_builder.setDataTime("2017-12-17 16:21:44");
        gps_builder.setGpsStatus(1);
        gps_builder.setLat(39.123);
        gps_builder.setLon(120.112);
        gps_builder.setDirection(30.2F);
        gps_builder.setId(100L);


        Class<?> clazz = Class.forName("com.think.protocol.Gps$gps_data");
        Method buildMethod = clazz.getMethod("newBuilder", null);
        Object buildObject = buildMethod.invoke(null);
        GeneratedMessage.Builder builder = (GeneratedMessage.Builder) buildObject;
        Method[] fieldMethods = builder.getClass().getDeclaredMethods();
        Field[] fields = builder.getClass().getDeclaredFields();
        Arrays.stream(fields).forEach(m -> System.out.println(m.getName()));
        //System.out.println("builder = [" + builder.getAllFields() + "]");
        Method[] methods = builder.getClass().getDeclaredMethods();
        //Arrays.stream(methods).forEach(m -> System.out.println(m.getName()));


        Method m = clazz.getMethod("getDescriptor", null);
        //DynamicMessage dynamicMessage = (DynamicMessage) constructor.newInstance();
        Object object = m.invoke(null);
        Descriptor objDescriptor = (Descriptor) object;

        DynamicMessage dynamicMessage = DynamicMessage.parseFrom(objDescriptor, gps_builder.build().toByteArray());
        DynamicMessage.Builder builder1 = dynamicMessage.toBuilder();
        System.out.println("builder1 = [" + builder1 + "]");
        Map<Descriptors.FieldDescriptor, Object> fieldsMap = builder1.getAllFields();


        fieldsMap.forEach((k, v) -> {
            String value = "set" + upperCase(k.getJsonName());
            Arrays.stream(methods).forEach(method -> {
                if (value.contains(method.getName())) {
                    try {
                        BeanUtils.copyProperties(builder, builder1);
                        BeanUtils.setProperty(builder, k.getJsonName()+"_", v);
                        System.out.println("method : " + k.getJsonName() + " value =  [ " + BeanUtils.getProperty(builder, k.getJsonName()) + " ]");
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                }
            });
        });


        System.out.println("clazz = [" + objDescriptor + "]");
        List<Descriptors.FieldDescriptor> fieldDescriptors = objDescriptor.getFields();
        fieldDescriptors.forEach(f -> System.out.println("field = [" + f.getFullName() + "]"));


        Gps.gps_data gps_data = gps_builder.build();
        System.out.println(gps_data.toString());
        System.out.println("===== 构建GPS模型结束 =====");

        System.out.println("===== gps Byte 开始=====");
        for (byte b : gps_data.toByteArray()) {
            System.out.print(b);
        }
        System.out.println("\n" + "bytes长度" + gps_data.toByteString().size());
        System.out.println("===== gps Byte 结束 =====");

        System.out.println("===== 使用gps 反序列化生成对象开始 =====");
        Gps.gps_data gd = null;
        try {
            gd = Gps.gps_data.parseFrom(gps_data.toByteArray());
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        System.out.print(gd.toString());
        System.out.println("===== 使用gps 反序列化生成对象结束 =====");

        Descriptor desc = descriptorMap.get(100);

        DynamicMessage msg = DynamicMessage.parseFrom(desc, gps_data.toByteArray());
        Descriptor desc2 = msg.getDescriptorForType();

        System.out.println("===================");
        System.out.println(msg);
        System.out.println("===================");

        System.out.println("===== 使用gps 转成json对象开始 =====");

        String jsonFormatM = "";
        try {
            jsonFormatM = JsonFormat.printer().print(gd);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(jsonFormatM.toString());
        System.out.println("json数据大小：" + jsonFormatM.getBytes().length);
        System.out.println("===== 使用gps 转成json对象结束 =====");
    }


    public static String upperCase(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
