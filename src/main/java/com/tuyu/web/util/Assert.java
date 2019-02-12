package com.tuyu.web.util;

import java.util.Collection;

/**
 * 断言
 *
 * @author tuyu
 * @date 2/11/19
 * Talk is cheap, show me the code.
 */
public abstract class Assert {

    /**
     * 断言字符串不为空，如果为空则抛出非法参数异常
     *
     * @param value
     * @param msg
     */
    public static void notEmpty(String value, String msg) {
        if (StringUtils.isEmpty(value)) {
            throw new IllegalArgumentException(msg);
        }
    }

    /**
     * 断言集合不为空，如果为空则抛出非法参数异常
     *
     * @param collection
     * @param msg
     */
    public static void notEmpty(Collection<?> collection, String msg) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new IllegalArgumentException(msg);
        }
    }

    /**
     * 断言数组不为空，如果为空则抛出非法参数异常
     *
     * @param arr
     * @param msg
     * @param <T>
     */
    public static <T> void notEmpty(T[] arr, String msg) {
        if (ArrayUtils.isEmpty(arr)) {
            throw new IllegalArgumentException(msg);
        }
    }
}
