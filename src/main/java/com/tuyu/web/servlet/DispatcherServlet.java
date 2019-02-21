package com.tuyu.web.servlet;

import com.tuyu.web.annotation.RequestMapping;
import com.tuyu.web.annotation.RequestMethod;
import com.tuyu.web.servlet.support.DataBinder;
import com.tuyu.web.support.RequestHandler;
import com.tuyu.web.util.ClassUtils;
import com.tuyu.web.util.JsonUtils;
import com.tuyu.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 自己实现的前置控制器
 * <p>
 *     拦截所有请求(/*)，将请求分发给具体的处理器处理，将返回的数据简单处理后响应请求
 * </p>
 *
 * @author tuyu
 * @date 2/13/19
 * Talk is cheap, show me the code.
 */
@WebServlet(urlPatterns = {"/*"})
public class DispatcherServlet extends HttpServlet{

    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);

    /**
     * 缓存处理器信息的map，key为请求URI，value为处理该请求的处理器信息（com.tuyu.web.support.RequestHandler）
     */
    private static Map<String, RequestHandler<?>> requestHandlerMap = new ConcurrentHashMap<>(10);

    /**
     * servlet初始化方法，会初始化requestHandlerMap
     * @exception ServletException
     */
    @Override
    public void init() throws ServletException {
        super.init();
        initDispatcherServlet();
    }

    /**
     * DispatcherServlet具体的初始化逻辑
     */
    private void initDispatcherServlet() {
        final Class<RequestMapping> requestMappingClass = RequestMapping.class;
        Set<Class<?>> set = ClassUtils.getClassByAnnotation(requestMappingClass);
        for (Class cl : set) {
            RequestMapping classAnnotation = (RequestMapping) cl.getAnnotation(requestMappingClass);
            String classUri = classAnnotation == null ? "" : classAnnotation.value();
            Method[] declaredMethods = cl.getDeclaredMethods();

            RequestMapping annotation ;
            for (Method method : declaredMethods) {
                if ((annotation= method.getAnnotation(requestMappingClass)) != null
                        && Modifier.isPublic(method.getModifiers())) {
                    String value = annotation.value();
                    try {
                        requestHandlerMap.put(WebUtils.combineUri(classUri, value),
                                RequestHandler.createRequestHandler(cl, method, annotation));
                    } catch (Exception e) {
                        String msg = "初始化DispatcherServlet失败";
                        logger.error(msg, e);
                        throw new RuntimeException(msg);
                    }
                }
            }
        }
    }

    /**
     * 具体的请求分发逻辑
     *
     * @param req
     * @param resp
     *
     * @exception ServletException
     * @exception IOException
     */
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 处理静态资源（首页，根目录）
        if (req.getRequestURI().equals("/")) {
            URL resource = this.getClass().getClassLoader().getResource("../../index.html");
            File file = new File(resource.getFile());
            FileInputStream inputStream = new FileInputStream(file);
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes);
            ServletOutputStream outputStream = resp.getOutputStream();
            outputStream.write(bytes);
            inputStream.close();
            outputStream.flush();
            outputStream.close();
            return;
        }
        // 请求分发
        doDispatch(req, resp);


    }

    /**
     * 过滤静态资源请求后，执行真正的请求分发
     *
     * @param request
     * @param response
     */
    protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Type", "application/json");
        // 获取handler
        RequestHandler handler = getRequestHandler(request.getRequestURI());
        if (handler == null || !RequestMethod.methodEquals(request.getMethod(), handler.getRequestMethod())) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }

        // 绑定参数 TODO:
        DataBinder dataBinder = new DataBinder(handler.getMethod(), request);
        Object[] params = dataBinder.getParams();

        try {
            // 反射调用处理器方法
            Object handle = handler.handle(params);
            PrintWriter writer = response.getWriter();
            String data = JsonUtils.Object2JsonString(handle);
            logger.info(data);
            writer.write(data);
            writer.flush();
        } catch (Exception e) {
            logger.error("调用{}类的{}方法失败", handler.getHandlerClass().getName(), handler.getMethod().getName(), e);
            throw new RuntimeException("servlet 异常");
        }
    }

    /**
     * 根据请求uri查询对应的处理器
     * <p>
     *     暂时不支持路径参数，以后会支持
     * </p>
     *
     * @param reqRui 请求的URI
     *
     * @return
     */
    private RequestHandler<?> getRequestHandler(String reqRui) {
        RequestHandler<?> rh = requestHandlerMap.get(reqRui);
        return rh;
    }
}
