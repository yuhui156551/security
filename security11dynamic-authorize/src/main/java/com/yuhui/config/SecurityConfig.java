package com.yuhui.config;

import com.yuhui.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.UrlAuthorizationConfigurer;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final CustomSecurityMetadataSource customSecurityMetadataSource;
    private final UserService userService;

    @Autowired
    public SecurityConfig(CustomSecurityMetadataSource customSecurityMetadataSource, UserService userService) {
        this.customSecurityMetadataSource = customSecurityMetadataSource;
        this.userService = userService;
    }

    @Override// 设置数据库数据源
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 1、获取工厂对象
        ApplicationContext applicationContext = http.getSharedObject(ApplicationContext.class);
        // 2、设置自定义 url 权限处理
        http.apply(new UrlAuthorizationConfigurer<>(applicationContext))
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O object) {
                        object.setSecurityMetadataSource(customSecurityMetadataSource);
                        // 是否拒绝公共资源访问
                        object.setRejectPublicInvocations(false);
                        return object;
                    }
                });
        http.formLogin()
                .and()
                .csrf().disable();
    }
}