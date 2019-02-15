# awesome dispatcher servlet

自定义mvc框架，模仿spring的DispatcherServlet，做基本的请求分发

# 项目依赖

- javax.servlet-api 4.0.1
- fastjson 1.2.47
- log4j2 2.11.2
- lombok 1.16.12
- junit 4.12
- 本地tomcat容器 8.5.24

# Demonstration-演示
## 配置本地Tomcat容器，并运行Tomcat服务器

![image][img_tomcat_config1]
![image][img_tomcat_config2]
![image][img_tomcat_run]

## 测试

浏览器地址栏输入`localhost:8080`
![image][img_demo_index]
浏览器地址栏输入`localhost:8080/user/tuyu`
![image][img_demo_user_tuyu]


# Changelog

[Learn about the latest improvements][link_changelog].

[link_changelog]: https://github.com/scutuyu/awesome-dispatcher-servlet/blob/master/CHANGELOG.md

[img_tomcat_config1]: https://github.com/scutuyu/awesome-dispatcher-servlet/blob/master/images/tomcat_config1.png
[img_tomcat_config2]: https://github.com/scutuyu/awesome-dispatcher-servlet/raw/master/images/tomcat_config2.png
[img_tomcat_run]: https://github.com/scutuyu/awesome-dispatcher-servlet/raw/master/images/tomcat_run.png
[img_demo_index]: https://github.com/scutuyu/awesome-dispatcher-servlet/raw/master/images/demo_index.png
[img_demo_user_tuyu]: https://github.com/scutuyu/awesome-dispatcher-servlet/raw/master/images/demo_user_tuyu.png