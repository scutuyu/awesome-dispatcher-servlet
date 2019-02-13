package com.tuyu.web.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
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

    private static final String BASE_PACKAGE_NAME = "com.tuyu.web";

    private static final Set<Class<?>> CLASS_SET = new HashSet<Class<?>>();

    /**
     * ClassUtils类初始化时扫描默认包下的所有类
     */
    static {
        scanPackage(BASE_PACKAGE_NAME);
    }

    /**
     * 扫描包，将被指定注解注释的类缓存起来
     *
     * @param packageName 包名
     *
     * @return
     */
    private static void scanPackage(String packageName) {
        Assert.notEmpty(packageName, "初始化ClassUtils类扫描包时，包名不能为空");
        String basePath = packageName.replace(".", "/");
        try {
//            Enumeration<URL> systemResources = ClassLoader.getSystemResources(basePath);
            Enumeration<URL> systemResources = getClassLoader().getResources(basePath);
            while (systemResources.hasMoreElements()) {
                URL url = systemResources.nextElement();

                File file = new File(url.getFile());
                File[] files = file.listFiles();
                for (File f : files) {
                    loadClass(packageName, f, CLASS_SET);
                }
            }
        } catch (IOException e) {
            logger.error("无法扫描指定的包：{}", packageName, e);
        }

    }

    private static ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    private static void loadClass(String pkn, File file, Set<Class<?>> set) {
        if (file != null && file.exists()) {
            if (file.isDirectory()) {
                String subName = file.getName();
                File[] files = file.listFiles();
                for (File f : files) {
                    loadClass(pkn + "." + subName, f, set);
                }
            } else if (file.isFile() && isClassFile(file.getName())) {
                String name = pkn + "." + file.getName();
                try {
                    Class<?> c = Class.forName(name.substring(0, name.indexOf(".class")), true, getClassLoader());
                    set.add(c);
                } catch (ClassNotFoundException e) {
                    logger.error("加载类: {} 失败", name, e);
                }
            }
        }
    }

    private static boolean isClassFile(String fileName) {
        return fileName.indexOf(".class") != -1;
    }

    public static Set<Class<?>> getClassByAnnotation(Class<? extends Annotation> annotation) {
        Set<Class<?>> set = new HashSet<Class<?>>();
        for (Class<?> cl : CLASS_SET) {
            if (cl.isAnnotationPresent(annotation)) {
                set.add(cl);
            }
        }
        return set;
    }
}
