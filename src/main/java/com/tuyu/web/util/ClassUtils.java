package com.tuyu.web.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

/**
 * Class工具类
 *
 * @author tuyu
 * @date 2/11/19
 * Talk is cheap, show me the code.
 */
public class ClassUtils {

    private static final Logger logger = LoggerFactory.getLogger(ClassUtils.class);
//    private static final Logger logger = LogManager.getLogger();

    private static final String BASE_PACKAGE_NAME = "com.tuyu.web";

    private static final Set<Class<?>> CLASS_SET = new HashSet<Class<?>>();

    /**
     * ClassUtils类初始化时扫描默认包下的所有类
     */
    static {
        loadClasses(BASE_PACKAGE_NAME);
    }

    /**
     * 扫描包，将被指定注解注释的类缓存起来
     *
     * @param packageName 包名
     *
     * @return
     */
    private static void loadClasses(String packageName) {
        Assert.notEmpty(packageName, "初始化ClassUtils类扫描包时，包名不能为空");
        String basePath = packageName.replace(".", "/");
        try {
            Enumeration<URL> systemResources = ClassLoader.getSystemResources(basePath);
            while (systemResources.hasMoreElements()) {
                URL url = systemResources.nextElement();

                File file = new File(url.getFile());
                logger.info("{}", file.exists());
                logger.info("{}", file.isDirectory());
            }
        } catch (IOException e) {
            logger.error("无法扫描指定的包：{}", packageName, e);
        }

    }

    public static Set<Class<?>> getClassByAnnotation(Class<?> annotation) {
        Set<Class<?>> set = new HashSet<Class<?>>();

        return set;
    }
}
