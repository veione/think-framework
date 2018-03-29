package com.think.core.annotation;

import com.google.protobuf.GeneratedMessage;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Mapping {
    /**
     * 消息请求ID
     */
    int requestId() default 0;

    /**
     * 方法说明
     */
    String desc() default "";

    /**
     * 指定消息请求类
     */
    Class<? extends GeneratedMessage> msg();
}
