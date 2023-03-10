package com.yuhui.config;

import com.yuhui.filter.JwtAuthenticationTokenFilter;
import com.yuhui.handler.MyAuthenticationFailureHandler;
import com.yuhui.handler.MyAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author yuhui
 * @date 2022/12/26 11:29
 */
//@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    // 自定义失败处理
    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;
    @Autowired
    private AccessDeniedHandler accessDeniedHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    /*@Bean
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager userDetailsService = new InMemoryUserDetailsManager();
        userDetailsService.createUser(User.withUsername("aaa").password("{noop}123").roles("admin").build());
        return userDetailsService;
    }*/

    /*@Autowired
    public void initialize(AuthenticationManagerBuilder builder) throws Exception {
         builder.userDetailsService(userDetailsService());
    }*/

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 认证配置
        http
                //关闭csrf
                .csrf().disable()
                //不通过Session获取SecurityContext
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                // 公共资源，直接放行
                .mvcMatchers("/login.html").permitAll()
                .and()
                .formLogin()
                .loginPage("/login.html")// 指定默认的登录页面
                .loginProcessingUrl("/doLogin")// 指定处理登录请求 url
                .usernameParameter("uname")
                .passwordParameter("passwd")
                .successForwardUrl("/index")//forward 跳转之后地址栏不变   注意:不会跳转到之前请求路径
                .successHandler(new MyAuthenticationSuccessHandler())// 认证成功的处理
                .failureHandler(new MyAuthenticationFailureHandler());// 认证失败的处理
//                .failureUrl("/login.html");
        // 对于登录接口 允许匿名访问(即放行) 携带token（说明是有身份者）反而不能访问
        // .anonymous()表达主要是指用户（登录与否）的状态。
        // 基本上，在用户通过“身份验证”之前，它是“匿名用户”。
        // 这就像每个人都有一个“默认角色”。
//                .antMatchers("/user/login").anonymous()
        // 除上面外的所有请求全部需要鉴权认证
//                .anyRequest().authenticated();

        // 把token校验过滤器添加到过滤器链中
        // 放在UsernamePasswordAuthenticationFilter的前面
        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);

        // 配置异常处理器
        http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint).
                accessDeniedHandler(accessDeniedHandler);

        // 允许跨域
        http.cors();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
