package com.tuyu.web.util;

import java.util.Collection;

/**
 * 集合工具类
 *
 * @author tuyu
 * @date 2/11/19
 * Talk is cheap, show me the code.
 */
public class CollectionUtils {


    /**
     * 判断集合是否为空
     *
     * @param collection
     *
     * @return 集合为null或者为空则返回true，反之则false
     */
    public static boolean isEmpty(Collection<?> collection) {
        if (collection == null || collection.isEmpty()) {
            return true;
        }
        return false;
    }

    /**
     * 判断集合是否不为空
     *
     * @param collection
     *
     * @return 集合不为null且不为空则返回true，反之则false
     */
    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }
}
