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

    /**
     * 合并两个数组的数据，返回一个新的数组
     *
     * @param arr1
     * @param arr2
     * @param <T>
     *
     * @return
     */
    public static <T> T[] combine(T[] arr1, T[] arr2) {
        if (arr1 == null || arr1.length == 0) {
            return arr2;
        } else if (arr2 == null || arr2.length == 0) {
            return arr1;
        } else {
            T[] result = (T[]) new Object[arr1.length + arr2.length];
            System.arraycopy(arr1, 0, result, 0, arr1.length);
            System.arraycopy(arr2, 0, result, arr1.length, arr2.length);
            return result;
        }
    }
}
