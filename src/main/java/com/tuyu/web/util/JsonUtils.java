package com.tuyu.web.util;

import com.alibaba.fastjson.JSON;

/**
 * json工具类
 *
 * @author tuyu
 * @date 2/13/19
 * Talk is cheap, show me the code.
 */
public final class JsonUtils {
    private JsonUtils() {
        throw new AssertionError("no JsonUtils instance for you!");
    }

    /**
     * 使用Fastjson将Object对象序列化为json字符串
     * @param obj
     *
     * @return
     */
    public static String Object2JsonString(Object obj) {
        return JSON.toJSONString(obj);
    }
}
