package com.tuyu.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * POST 请求映射，和spring mvc提供的注解类似
 *
 * @author tuyu
 * @date 2/11/19
 * Talk is cheap, show me the code.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface PostMapping {

    /**
     * @return 映射路径
     */
    String value();

    /**
     * @return 请求的方法
     */
    RequestMethod method() default RequestMethod.POST;
}
