package com.yuhui.config;

import com.yuhui.handler.MyAuthenticationFailureHandler;
import com.yuhui.handler.MyAuthenticationSuccessHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

/**
 * @author yuhui
 * @date 2022/12/26 11:29
 */
//@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {


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
                .mvcMatchers("/login.html").permitAll()// 公共资源，直接放行
                .anyRequest().authenticated()// 其他所有资源都需要认证
                .and()
                .formLogin()// 表单认证
                .loginPage("/login.html")// 指定默认的登录页面
                .loginProcessingUrl("/doLogin")// 指定处理登录请求 url
                .usernameParameter("uname")
                .passwordParameter("passwd")
                .successForwardUrl("/hello")//forward 跳转之后地址栏不变   注意:不会跳转到之前请求路径
                .successHandler(new MyAuthenticationSuccessHandler())// 认证成功的处理
                .failureHandler(new MyAuthenticationFailureHandler());// 认证失败的处理
    }

}
