package com.think.common.annotations;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.think.common.constant.http.HttpConstants;
import com.think.common.constant.http.RequestMethod;

@Documented
@Retention(RUNTIME)
@Target({ TYPE, METHOD })
public @interface RequestMapping {
	String value();
    RequestMethod method() default RequestMethod.GET;
    String consumes() default HttpConstants.HEADER_CONTENT_TYPE_JSON;
    String produces() default HttpConstants.HEADER_CONTENT_TYPE_TEXT;
}
