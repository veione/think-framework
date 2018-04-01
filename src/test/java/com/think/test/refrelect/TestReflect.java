package com.think.test.refrelect;

import com.esotericsoftware.reflectasm.MethodAccess;
import com.esotericsoftware.reflectasm.PublicConstructorAccess;
import com.think.common.annotation.Handler;
import com.think.util.ClassScanner;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;

public class TestReflect {

    @Test
    public void testReflect() throws Exception {
        // User user = new User();
        //使用reflectasm生成User访问类
        Class<?> clazz = Class.forName("com.think.test.refrelect.User");
        Object userObj = PublicConstructorAccess.get(clazz).newInstance();
        System.out.println("userObj = " + userObj);
        MethodAccess access = MethodAccess.get(userObj.getClass());
        int setNameIndex = access.getIndex("setName");
        int getNameIndex = access.getIndex("getName");
        Arrays.stream(access.getMethodNames()).forEach(System.out::println);
        System.out.println("setIndex = " + setNameIndex + " getNameIndex = " + getNameIndex);
        //invoke setName方法name值
        access.invoke(userObj, setNameIndex, "张三");
        // invoke getName方法获得值
        String name = (String) access.invoke(userObj, getNameIndex, null);
        System.out.println(name);
    }


    @Test
    public void testHandler() throws Exception {
        Set<Class<?>> classes = ClassScanner.getClasses("com.think", clazz -> clazz.isAnnotationPresent(Handler.class));
        for (Class<?> cls : classes) {
            Object obj = PublicConstructorAccess.get(cls).newInstance();
            System.out.println("obj = " + obj);
            MethodAccess access = MethodAccess.get(obj.getClass());
            Arrays.stream(access.getMethodNames()).forEach(System.out::println);
            int index= access.getIndex("listGPSData");
            System.out.println("index = " + index);
            Method method = Arrays.stream(obj.getClass().getDeclaredMethods()).filter(m->m.getName().equals("listGPSData")).findAny().get();
            long start = System.currentTimeMillis();
            for (int i = 0; i < 1000000; i++) {
                //access.invoke(obj, index, null, null);
                 method.invoke(obj, null, null);
            }
            System.out.println("Consume time : " + (System.currentTimeMillis() - start));
        }
    }

}

