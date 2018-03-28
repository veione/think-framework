package com.think.core;

import com.think.core.annotation.Handler;
import com.think.core.annotation.Mapping;
import com.think.protocol.GPSDataProto;
import com.think.protocol.GPSDataProto.gps_data;
import com.think.util.ClassFilter;
import com.think.util.ClassScanner;

import java.lang.reflect.Method;
import java.util.Set;

@Handler(desc = "商店处理器")
public class StoreHandler {

    @Mapping(request = gps_data.class, desc = "处理GPS")
    public void handleGPSData(RequestContext context, gps_data request) {
        System.out.println("context = [" + context + "], request = [" + request + "]");
    }


    public static void main(String[] args) throws Exception {
        Set<Class<?>> classes = ClassScanner.getClasses("com.think", clazz -> clazz.isAnnotationPresent(Handler.class));
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
                    model.setMethod(method);
                    model.setMessage(method.getAnnotation(Mapping.class).request());
                    model.setContext(new RequestContext());
                    model.setRequest(GPSDataProto.gps_data.newBuilder().build());
                    System.out.println("model = [" + model + "]");
                    System.out.println("method = [" + method + "]");
                    method.invoke(clazz, new RequestContext(), gps_data.newBuilder().build());
                    model.execute();
                }
            }
        }
        System.out.println("args = [" + classes.size() + "]");
    }
}
