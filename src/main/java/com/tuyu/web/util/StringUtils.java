package com.tuyu.web.util;

import com.alibaba.fastjson.PropertyNamingStrategy;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    /**
     * 解析HttpServletRequest URL后面的参数
     * <pre>
     *     eg: http://localhost:8081/user?name=tuyu&age=12
     *
     *     [name=tuyu,age=12]
     * </pre>
     *
     * @param queryString
     *
     * @return
     */
    public static Map<String, String[]> parseUrlQueryString(String queryString) {
        Map<String, String[]> map = new HashMap<>();
        if (isEmpty(queryString)) {
            return map;
        }

        final String paramSeparator = "&";
        final String keyValueSeparator = "=";
        final String valueSeparator = ",";

        String[] params = queryString.split(paramSeparator);
        if (params.length == 0) {
            return map;
        }

        for (String param : params) {
            String[] kvs = param.split(keyValueSeparator);
            if (kvs.length == 0) {
                continue;
            }
            String name = kvs[0];
            if (isNotEmpty(name)) {
                // 保证小驼峰命名和下划线命名都可用
                String[] values = split(kvs[1], valueSeparator);
                String underscore = underscore2CamelCase(name);
                String camelCase = camelCase2Underscore(name);
                setKeyValueForMap(map, underscore2CamelCase(name), values);
                if (!underscore.equals(camelCase)) {
                    setKeyValueForMap(map, camelCase2Underscore(name), values);
                }
            }
        }
        return map;
    }

    private static void setKeyValueForMap(Map<String, String[]> map, String key, String[] value) {
        if (map.containsKey(key)) {
            map.put(key, ArrayUtils.combine(map.get(key), value));
        } else {
            map.put(key, value);
        }
    }

    private static String[] split(String string, String separator) {
        if (isEmpty(string)) {
            return new String[0];
        }
        return string.split(separator);
    }

    /**
     * 下划线转驼峰
     * <pre>
     *     eg: user_name -> userName
     * </pre>
     *
     * @param underscore 下划线格式的字符串
     *
     * @return 小驼峰格式的字符串
     */
    public static String underscore2CamelCase(String underscore) {
        final String pattern = "_(.)";
        Pattern compile = Pattern.compile(pattern);
        Matcher matcher = compile.matcher(underscore);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String group1 = matcher.group(1);
            matcher.appendReplacement(sb, group1.toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 驼峰转下划线
     *
     * @param camelCase 小驼峰格式的字符串
     *
     * @return 下划线格式的字符串
     */
    public static String camelCase2Underscore(String camelCase) {
        String translate = PropertyNamingStrategy.SnakeCase.translate(camelCase);
        return translate;
    }
}
