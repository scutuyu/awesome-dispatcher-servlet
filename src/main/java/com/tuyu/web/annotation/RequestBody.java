package com.tuyu.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 从请求体中读取数据的注解
 *
 * @author tuyu
 * @date 2/18/19
 * Talk is cheap, show me the code.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface RequestBody {
}
