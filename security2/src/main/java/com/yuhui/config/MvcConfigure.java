package com.yuhui.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 对 SpringMVC 进行自定义配置
 */
@Configuration
public class MvcConfigure implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // 有如下配置，就无需为每一个视图去开发一个控件
        registry.addViewController("login.html").setViewName("login");
        registry.addViewController("index.html").setViewName("index");
    }
}
