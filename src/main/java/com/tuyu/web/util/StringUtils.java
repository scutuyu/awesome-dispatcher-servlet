package com.tuyu.web.util;

/**
 * String工具类
 *
 * @author tuyu
 * @date 2/11/19
 * Talk is cheap, show me the code.
 */
public class StringUtils {

    /**
     * 判断字符串是否为空
     *
     * @param value
     *
     * @return 字符串为null或者空串都返回true，反之返回false
     */
    public static boolean isEmpty(String value) {
        if (isNull(value) || "".equals(value)) {
            return true;
        }
        return false;
    }

    /**
     * 判断字符串是否不为空
     * @param value
     *
     * @return 字符串不为null且不是空串则返回true，反之则false
     */
    public static boolean isNotEmpty(String value) {
        return !isEmpty(value);
    }

    /**
     * 判断字符串是否为null
     * @param value
     *
     * @return 字符串为null则返回true，反之返回false
     */
    public static boolean isNull(String value) {
        return value == null;
    }

    /**
     * 判断字符串是否不为null
     *
     * @param value
     *
     * @return 字符串不为null则返回true，反之则false
     */
    public static boolean isNotNull(String value) {
        return !isNull(value);
    }

}
