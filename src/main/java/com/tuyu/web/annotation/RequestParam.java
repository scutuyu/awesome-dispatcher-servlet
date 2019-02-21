package com.tuyu.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 路径参数
 *
 * @author tuyu
 * @date 2/18/19
 * Talk is cheap, show me the code.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface RequestParam {
    /**
     * @return 参数名
     */
    String value();

    /**
     * @return 是否必须，默认true
     */
    boolean required() default true;

    String defaultValue();
}
