package com.tuyu.web.util;

/**
 * 数组工具类
 *
 * @author tuyu
 * @date 2/11/19
 * Talk is cheap, show me the code.
 */
public class ArrayUtils {

    /**
     * 判断数组是否为空
     *
     * @param arr
     * @param <T>
     *
     * @return 数组为null或者为空则返回true，反之则false
     */
    public static <T> boolean isEmpty(T[] arr){
        if (arr == null || arr.length == 0) {
            return true;
        }
        return false;
    }

    /**
     * 判断数组是否不为空
     *
     * @param arr
     * @param <T>
     *
     * @return 数组不为null且不为空则返回true，反之则false
     */
    public static <T> boolean isNotEmpty(T[] arr) {
        return !isEmpty(arr);
    }
}
