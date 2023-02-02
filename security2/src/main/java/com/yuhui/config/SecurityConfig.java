package com.yuhui.config;

import com.yuhui.handler.MyAuthenticationFailureHandler;
import com.yuhui.handler.MyAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

/**
 * 传统 Web开发
 */
//@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 官网建议set注入用resource，构造注入用autowired
     */
    private final MyUserDetailService myUserDetailService;

    @Autowired
    public SecurityConfig(MyUserDetailService myUserDetailService) {
        this.myUserDetailService = myUserDetailService;
    }

    // 默认全局配置 AuthenticationManager
    /*@Bean
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager userDetailsService = new InMemoryUserDetailsManager();
        userDetailsService.createUser(User.withUsername("aaa").password("{noop}123").roles("admin").build());
        return userDetailsService;
    }*/

    // 自定义 AuthenticationManager 并没有在工厂当中暴露出来
    @Autowired
    public void initialize(AuthenticationManagerBuilder builder) throws Exception {
         builder.userDetailsService(myUserDetailService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 认证配置
        http
                // 关闭csrf
                .csrf().disable()
                .authorizeRequests()
                .mvcMatchers("/login.html").permitAll()// 公共资源，直接放行
                .anyRequest().authenticated()// 其他所有资源都需要认证
                .and()
                .formLogin()// 表单认证
                .loginPage("/login.html")// 指定默认的登录页面
                .loginProcessingUrl("/doLogin")// 指定处理登录请求 url
                .usernameParameter("uname")
                .passwordParameter("passwd")
                .defaultSuccessUrl("/index.html", true)// 总是跳转到此页面
                .failureUrl("/login.html")// 重定向到登录页面
                .and()
                .logout()// 开启退出登录
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login.html");
//                .successHandler(new MyAuthenticationSuccessHandler())// 认证成功的处理
//                .failureHandler(new MyAuthenticationFailureHandler());// 认证失败的处理
    }

}
