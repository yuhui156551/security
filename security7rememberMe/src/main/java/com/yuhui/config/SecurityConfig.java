package com.yuhui.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuhui.filter.LoginFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.InMemoryTokenRepositoryImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author yuhui
 * @date 2023/2/2 14:52
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean// 这里图简单，使用 inMemoryUserDetailsManager
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager inMemoryUserDetailsManager = new InMemoryUserDetailsManager();
        inMemoryUserDetailsManager.createUser(User.withUsername("root").password("{noop}123").roles("admin").build());
        return inMemoryUserDetailsManager;
    }

    // 自定义 AuthenticationManager
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 配置 authenticationManager 使用自定义UserDetailService
        auth.userDetailsService(userDetailsService());
    }

    // 暴露出 AuthenticationManager
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    // 自定义 filter 交给工厂管理
    @Bean
    public LoginFilter loginFilter() throws Exception {
        LoginFilter loginFilter = new LoginFilter();
        loginFilter.setFilterProcessesUrl("/doLogin");// 设置认证 url
        loginFilter.setUsernameParameter("uname");// 指定接收 json 用户名 key
        loginFilter.setPasswordParameter("passwd");
        loginFilter.setAuthenticationManager(authenticationManagerBean());
        loginFilter.setRememberMeServices(rememberMeServices());// 设置认证成功时使用自定义 rememberMeServices
        // 自定义成功处理
        loginFilter.setAuthenticationSuccessHandler((request, response, authentication) -> {
            Map<String, Object> result = new HashMap<>();
            result.put("msg", "登录成功");
//            result.put("status", 200);
            response.setStatus(HttpStatus.OK.value());
            result.put("用户信息", authentication.getPrincipal());
            response.setContentType("application/json;charset=UTF-8");
            // 转为 json 格式
            String s = new ObjectMapper().writeValueAsString(result);
            response.getWriter().println(s);
        });
        // 自定义失败处理
        loginFilter.setAuthenticationFailureHandler((request, response, exception) -> {
            Map<String, Object> result = new HashMap<>();
            result.put("msg", "登录失败: " + exception.getMessage());
//            result.put("status", 500);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setContentType("application/json;charset=UTF-8");
            String s = new ObjectMapper().writeValueAsString(result);
            response.getWriter().println(s);
        });
        return loginFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .and()
                .rememberMe()// 开启记住我功能  1、认证成功保存记住我 cookie 到客户端  2、只要 cookie 写入客户端成功才能实现自动登录
                .rememberMeServices(rememberMeServices())// 设置自动登录使用哪个 rememberMeServices
                .and()
                // 异常处理
                .exceptionHandling()
                .authenticationEntryPoint((req, resp, ex) -> {
                    resp.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
                    resp.setStatus(HttpStatus.UNAUTHORIZED.value());
                    resp.getWriter().println("请先进行认证！");
                })
                .and()
                .csrf().disable();

        http.addFilterAt(loginFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public RememberMeServices rememberMeServices(){
        return new MyPersistentTokenBasedRememberMeServices(UUID.randomUUID().toString(),
                userDetailsService(),
                new InMemoryTokenRepositoryImpl());
    }
}
