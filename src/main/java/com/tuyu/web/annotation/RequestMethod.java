package com.tuyu.web.annotation;

/**
 * 请求方法
 *
 * @author tuyu
 * @date 2/11/19
 * Talk is cheap, show me the code.
 */
public enum RequestMethod {

    GET, POST;

    /**
     * 判断请求方法是否相等
     *
     * @param method
     * @param requestMethod
     *
     * @return
     */
    public static boolean methodEquals(String method, RequestMethod requestMethod) {
        if (method == null || requestMethod == null) {
            return false;
        }
        return method.equalsIgnoreCase(requestMethod.toString());
    }
}
