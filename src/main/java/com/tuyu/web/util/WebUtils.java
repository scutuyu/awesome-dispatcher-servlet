package com.tuyu.web.util;

/**
 * web 相关的工具类
 *
 * @author tuyu
 * @date 2/18/19
 * Talk is cheap, show me the code.
 */
public final class WebUtils {
    private WebUtils() {
        throw new AssertionError("no WebUtils instance for you!");
    }

    /**
     * 合并URI
     * <pre>
     *     eg: /user + /tuyu = /user/tuyu
     *     eg: /user + tuyu = /user/tuyu
     *     eg: user + /tuyu = /user/tuyu
     *     eg: user + tuyu = /user/tuyu
     *     eg: "" + /tuyu = /tuyu
     *     eg: "" + tuyu = /tuyu
     * </pre>
     * @param firstUri 第一个URI
     * @param secondUri 第二个URI
     *
     * @return 合并后的URI
     */
    public static String combineUri(String firstUri, String secondUri) {
        final String uriSeparator = "/";
        if (StringUtils.isEmpty(firstUri)) {
            return secondUri.startsWith(uriSeparator) ? secondUri : uriSeparator + secondUri;
        }
        String uri = "";
        if (firstUri.startsWith(uriSeparator)) {
            uri += firstUri;
            if (!secondUri.startsWith(uriSeparator)) {
                uri += uriSeparator;
            }
            uri += secondUri;
        } else {
            uri += uriSeparator;
            uri += firstUri;
            if (!secondUri.startsWith(uriSeparator)) {
                uri += uriSeparator;
            }
            uri += secondUri;
        }
        if (uri.startsWith("//")) {
            uri = uriSeparator.substring(1, uri.length());
        }
        return uri;
    }
}
