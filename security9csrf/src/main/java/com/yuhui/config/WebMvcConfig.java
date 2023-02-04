package com.yuhui.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author yuhui
 * @date 2023/2/4 16:59
 */
/*@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 处理的请求地址
                .allowedMethods("*")
                .allowedOrigins("*")
                .allowedHeaders("*")
                .allowCredentials(false)// 不需要传递任何凭证
                .exposedHeaders("")
                .maxAge(3600);
    }
}*/
