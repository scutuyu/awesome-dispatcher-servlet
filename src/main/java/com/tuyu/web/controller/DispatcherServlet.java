package com.tuyu.web.controller;

import com.tuyu.web.annotation.RequestMapping;
import com.tuyu.web.annotation.RequestMethod;
import com.tuyu.web.annotation.RestController;
import com.tuyu.web.support.RequestHandler;
import com.tuyu.web.util.ClassUtils;
import com.tuyu.web.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author tuyu
 * @date 2/13/19
 * Talk is cheap, show me the code.
 */
@WebServlet(urlPatterns = {"/tuyu"})
public class DispatcherServlet extends HttpServlet{

    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);

    private static Map<String, RequestHandler<?>> requestHandlerMap = new ConcurrentHashMap<>(10);

    @Override
    public void init() throws ServletException {
        super.init();
        initDispatcherServlet();
    }

    private void initDispatcherServlet() {
        Set<Class<?>> set = ClassUtils.getClassByAnnotation(RestController.class);
        for (Class cl : set) {
            Method[] declaredMethods = cl.getDeclaredMethods();

            RequestMapping annotation ;
            for (Method method : declaredMethods) {
                if ((annotation= method.getAnnotation(RequestMapping.class)) != null
                        && Modifier.isPublic(method.getModifiers())) {
                    String value = annotation.value();
                    try {
                        requestHandlerMap.put(value, createRequestHandler(cl, method, annotation));
                    } catch (Exception e) {
                        String msg = "初始化DispatcherServlet失败";
                        logger.error(msg, e);
                        throw new RuntimeException(msg);
                    }
                }
            }
        }
    }



    private <T> RequestHandler<T> createRequestHandler(Class<T> clazz, Method method,RequestMapping requestMapping) throws IllegalAccessException, InstantiationException {
        RequestHandler<T> handler = new RequestHandler<>();
        handler.setUri(requestMapping.value());
        handler.setHandlerClass(clazz);
        handler.setRequestMethod(requestMapping.method());
        handler.setMethod(method);
        handler.setHandler((T) clazz.newInstance());
        return handler;
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getMethod();
        String requestURI = req.getRequestURI();
        RequestHandler handler = requestHandlerMap.get(requestURI);
        resp.setCharacterEncoding("UTF-8");
        if (handler != null && methodEquals(method, handler.getRequestMethod())) {
            try {
                Object handle = handler.handle(new Object[0]);
                String characterEncoding = resp.getCharacterEncoding();
                System.out.println(characterEncoding);
                PrintWriter writer = resp.getWriter();
                String data = JsonUtils.Object2JsonString(handle);
                logger.info(data);
                writer.write(data);
                writer.flush();
            } catch (Exception e) {
                logger.error("调用{}类的{}方法失败", handler.getHandlerClass().getName(), handler.getMethod().getName(), e);
                throw new RuntimeException("servlet 异常");
            }
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private boolean methodEquals(String method, RequestMethod requestMethod) {
        if (method == null || requestMethod == null) {
            return false;
        }
        return method.equalsIgnoreCase(requestMethod.toString());
    }
}