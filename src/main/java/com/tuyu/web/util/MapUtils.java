package com.tuyu.web.util;

import java.util.Map;

/**
 * Map工具类
 *
 * @author tuyu
 * @date 2/21/19
 * Talk is cheap, show me the code.
 */
public final class MapUtils {

    private MapUtils() {
        throw new AssertionError("no MapUtils instance for you!");
    }

    /**
     * 判断Map是否为空
     *
     * @param map
     * @param <K>
     * @param <V>
     *
     * @return map为null或者size为0则返回true，否则返回false
     */
    public static <K, V> boolean isEmpty(Map<K, V> map){
        if (map == null || map.size() == 0) {
            return true;
        }
        return false;
    }
}
