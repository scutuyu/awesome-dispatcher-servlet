package com.tuyu.web.servlet.support;

import com.alibaba.fastjson.JSON;
import com.tuyu.web.annotation.PathVariable;
import com.tuyu.web.annotation.RequestBody;
import com.tuyu.web.annotation.RequestParam;
import com.tuyu.web.util.*;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.math.BigDecimal;
import java.util.*;

/**
 * 数据绑定类
 *
 * @author tuyu
 * @date 2/18/19
 * Talk is cheap, show me the code.
 */
@Slf4j
public class DataBinder {

    private Method handleMethod;
    private HttpServletRequest request;
    private Object[] params;
    private Map<String, String[]> requestBodyCacheMap;
    private Map<String, String[]> queryStringCacheMap;
    private String requestBodyStringCache;

    public DataBinder(Method handleMethod, HttpServletRequest request) {
        Assert.notNull(handleMethod, "处理方法不能为null");
        Assert.notNull(request, "请求不能为null");
        this.handleMethod = handleMethod;
        this.request = request;
    }

    /**
     * 获取绑定后的参数数组
     *
     * @return
     */
    public Object[] getParams() {
        Object[] param = this.params;
        if (param == null) {
            this.params = param = bindParams();
        }
        return param;
    }

    /**
     * 正真执行参数绑定的逻辑
     *
     * @return
     */
    private Object[] bindParams() {
        Parameter[] parameters = this.handleMethod.getParameters();
        List<Object> list = new ArrayList<>(parameters.length);

        for (Parameter pa : parameters) {
            // 如果参数的来类型是简单类型，就执行简单类型绑定逻辑
            if (isSimpleClass(pa.getType())) {
                bindSimpleType(pa, list);
                continue;
            }
            // 否则，就默认参数类型是自定义类型

            // 如果参数加了RequestBody注解，就从请求体中获取数据
            if (parameterHasAnnotation(pa, RequestBody.class)) {
                handleRequestBodyParams(pa, this.request);

            } else {
                // 否则，就从url后面获取参数，或者表单中获取参数
                Map<String, String[]> urlMap = getQueryStringCacheMap(getQueryString(request));
                Map<String, String[]> formMap = request.getParameterMap();
                bindCustomType(pa, urlMap, formMap, list);
            }
        }

        return list.toArray();
    }

    /**
     * 判断类型是否为简单类型
     *
     * @param clazz
     *
     * @return
     */
    private boolean isSimpleClass(Class clazz) {

        if (Objects.equals(clazz, Integer.class)
                || Objects.equals(clazz, Long.class)
                || Objects.equals(clazz, Boolean.class)
                || Objects.equals(clazz, BigDecimal.class)
                || Objects.equals(clazz, String.class)
                || Objects.equals(clazz, Short.class)
                || Objects.equals(clazz, Float.class)
                || Objects.equals(clazz, Double.class)
                || Objects.equals(clazz, Character.class)
                || Objects.equals(clazz, Byte.class)) {
            return true;
        }

        return false;
    }

    /**
     * 为简单类型的参数绑定数据
     *
     * @param pa 处理方法中的参数
     * @param list 绑定后的参数值，所有的值都是经过数据类型转换后的值
     */
    private void bindSimpleType(Parameter pa, List<Object> list) {
        Object obj = null;
        if (parameterHasAnnotation(pa, RequestBody.class)) {
            // 处理方法的参数使用了@RequestBody
            obj = handleRequestBodyParams(pa, this.request);
        } else if (parameterHasAnnotation(pa, PathVariable.class)) {
            // 处理方法的参数使用了@PathVariable TODO: 暂时不支持
            obj = handlePathVariableParams(pa, this.request);
        } else if (parameterHasAnnotation(pa, RequestParam.class)) {
            // 处理方法的参数使用了@RequestParam
            obj = handleRequestParamParams(pa, this.request);
        } else if (pa.getAnnotations().length == 0) {
            // 处理方法的参数没有使用注解
            obj = handleDefaultParams(pa, this.request);
        }
        if (obj == null) {
            String parameterName = pa.getName();
            log.error("不能绑定参数{}", parameterName);
            throw new RuntimeException("参数绑定异常: " + parameterName);
        }
        list.add(typeTranslate(pa, obj));
    }

    /**
     * 绑定自定义参数类型，并将绑定后的参数值保存到list中
     *
     * @param pa
     * @param urlMap
     * @param formMap
     * @param list
     */
    private void bindCustomType(Parameter pa, Map<String, String[]> urlMap, Map<String, String[]> formMap, List<Object> list) {
        Class clazz = pa.getType();
        String paramName = pa.getName();
        try {
            Object o = clazz.newInstance();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                String fieldName = field.getName();
                // 静态属性不需要绑定值
                if (Modifier.isStatic(field.getModifiers())) {
                    continue;
                }
                String[] value = urlMap.get(StringUtils.camelCase2Underscore(fieldName));
                if (value == null) {
                    value = urlMap.get(StringUtils.underscore2CamelCase(fieldName));
                }
                if (value == null) {
                    value = formMap.get(StringUtils.camelCase2Underscore(fieldName));
                }
                if (value == null) {
                    value = formMap.get(StringUtils.underscore2CamelCase(fieldName));
                }
                field.setAccessible(true);
                // 将参数值转换为正确的类型后设置到对象o中
                field.set(o, typeTranslate(field.getType(), value));
            }
            list.add(o);
        } catch (InstantiationException e) {
            log.error("绑定参数：{}异常", paramName, e);
            throw new RuntimeException("绑定参数异常");
        } catch (IllegalAccessException e) {
            log.error("绑定参数：{}异常", paramName, e);
            throw new RuntimeException("绑定参数异常");
        }
    }

    /**
     * 参数类型转换
     *
     * @param parameter 处理方法的参数
     * @param value 参数值，默认是String[]
     *
     * @return
     */
    private Object typeTranslate(Parameter parameter, Object value) {
        return typeTranslate(parameter.getType(), value);
    }

    /**
     * 数据类型转换
     *
     * @param clazz 要转换的类型
     * @param value 参数值，默认是String[]
     *
     * @return
     */
    private Object typeTranslate(Class clazz, Object value) {
        if (value == null) {
            return null;
        }

        String[] arr = (String[]) value;
        if (clazz.equals(Integer.class)) {
            return Integer.valueOf(arr[0]);
        } else if (clazz.equals(String.class)) {
            return arr[0];
        }
        return value;
    }

    /**
     * 判断处理方法的参数是否有指定的注解
     *
     * @param parameter
     * @param anClass
     *
     * @return
     */
    private boolean parameterHasAnnotation(Parameter parameter, Class<? extends Annotation> anClass) {
        return parameter.isAnnotationPresent(anClass);
    }

    /**
     * 从请求体中读取数据，并缓存起来
     * <p>
     *     如果请求头是application/json就将请求体中的数据转为指定的POJO对象
     *     否则就认为数据格式形如: user_name=tuyu&user_age=12&hoby=reading,eating，并解析为Map缓存起来
     * </p>
     *
     * @param parameter
     * @param request
     *
     * @return 根据参数
     */
    private Object handleRequestBodyParams(Parameter parameter, HttpServletRequest request) {
        String data = this.requestBodyStringCache;
        if (data == null) {
            try {
                this.requestBodyStringCache = data = StreamUtils.getString(request.getInputStream());
            } catch (IOException e) {
                String msg = "从获取Http请求中获取输入流失败";
                throw new RuntimeException(msg, e);
            }
        } else {
            data = this.requestBodyStringCache;
        }

        String header = request.getHeader("Content-Type");
        if (StringUtils.isNotEmpty(header) && header.indexOf("application/json") != -1) {
            Object object = JSON.parseObject(data, parameter.getType());
            return object;
        }

        Map<String, String[]> map = this.requestBodyCacheMap;
        if (map == null) {
            this.requestBodyCacheMap = map = StringUtils.parseUrlQueryString(data);
        }
        return map.get(parameter.getName());
    }

    /**
     * 从路径中获取路径参数，目前不支持，以后支持 TODO:
     *
     * @param parameter
     * @param request
     *
     * @return
     */
    private Object handlePathVariableParams(Parameter parameter, HttpServletRequest request) {
        return null;
    }

    /**
     * 从请求的url后面获取参数，以Map的形式缓存并返回
     *
     * @param queryString
     *
     * @return
     */
    private Map<String, String[]> getQueryStringCacheMap(String queryString) {
        Map<String, String[]> map = this.queryStringCacheMap;
        if (map == null) {
            this.queryStringCacheMap = map = StringUtils.parseUrlQueryString(queryString);
        }
        return map;
    }

    /**
     * 处理方法的参数添加了@RequestParam注解，获取该种类型参数的值，并返回
     *
     * @param parameter 处理方法的参数
     * @param request
     *
     * @return
     */
    private Object handleRequestParamParams(Parameter parameter, HttpServletRequest request) {
        Map<String, String[]> map = getQueryStringCacheMap(getQueryString(request));
        if (MapUtils.isEmpty(map)) {
            map = request.getParameterMap();
        }
        RequestParam an = parameter.getAnnotation(RequestParam.class);
        return map.get(an.value());
    }

    /**
     * 从请求URL中获取字符串类型的参数表示
     *
     * @param request
     *
     * @return
     */
    private String getQueryString(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        final String queryStringSeparator = "\\?";
        if (requestURI.indexOf(queryStringSeparator) != -1) {
            String[] arr = requestURI.split(queryStringSeparator);
            return arr[1];
        }
        return "";
    }

    /**
     * 获取默认类型的参数值，即如果参数没有添加任何注解，需要此方法解析并返回
     *
     * @param parameter 处理方法的参数
     * @param request
     *
     * @return
     */
    private Object handleDefaultParams(Parameter parameter, HttpServletRequest request) {
        Map<String, String[]> map = getQueryStringCacheMap(getQueryString(request));
        String[] paramValue = null;
        if (MapUtils.isEmpty(map) && (paramValue = map.get(parameter.getName())) == null) {
            map = request.getParameterMap();
        }
        String paramName = parameter.getName();
        paramValue = map.get(paramName);
        return paramValue == null ? map.get(StringUtils.camelCase2Underscore(paramName)) : paramValue;
    }

}
