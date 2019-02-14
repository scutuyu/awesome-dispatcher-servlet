package com.tuyu.web.support;

import com.tuyu.web.annotation.RequestMethod;
import lombok.Data;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 *  请求处理
 *
 * @author tuyu
 * @date 2/13/19
 * Talk is cheap, show me the code.
 */
@Data
public class RequestHandler<T> {
    /**
     * 请求的URI
     */
    private String uri;
    /**
     * 请求的方法
     */
    private RequestMethod requestMethod;
    /**
     * 与请求URI、请求方法相对应的处理器的处理方法
     */
    private Method method;
    /**
     * 处理器的Class
     */
    private Class<T> handlerClass;
    /**
     * 处理器实例
     */
    private T handler;

    /**
     * 通过反射调用处理器实例的相应方法，即处理请求
     *
     * @param args 参数数组
     *
     * @return 处理之后的结果，没有则为null
     *
     * @exception InvocationTargetException
     * @exception IllegalAccessException
     */
    public Object handle(Object ... args) throws InvocationTargetException, IllegalAccessException {
        Object invoke = method.invoke(handler, args);
        return invoke;
    }
}
