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
    private String uri;
    private RequestMethod requestMethod;
    private Method method;
    private Class<T> handlerClass;
    private T handler;

    public Object handle(Object ... args) throws InvocationTargetException, IllegalAccessException {
        Object invoke = method.invoke(handler, args);
        return invoke;
    }
}
