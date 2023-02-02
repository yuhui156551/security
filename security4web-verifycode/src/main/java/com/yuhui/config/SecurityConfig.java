package com.yuhui.config;

import com.yuhui.filter.KaptchaFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author yuhui
 * @date 2023/2/2 20:05
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager userDetailsManager = new InMemoryUserDetailsManager();
        userDetailsManager.createUser(User.withUsername("root").password("{noop}123").roles("admin").build());
        return userDetailsManager;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService());
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * 自定义 filter 进行干预
     */
    @Bean
    public KaptchaFilter kaptchaFilter() throws Exception {
        KaptchaFilter kaptchaFilter = new KaptchaFilter();
        // 指定接收验证码请求参数名
        kaptchaFilter.setKaptcha("kaptcha");
        // 指定认证管理器
        kaptchaFilter.setAuthenticationManager(authenticationManagerBean());
        // 指定认证成功和失败处理
        kaptchaFilter.setAuthenticationSuccessHandler((req, resp, auh) -> {
            resp.sendRedirect("/index.html");
        });
        kaptchaFilter.setAuthenticationFailureHandler((req, resp, ex) -> {
            resp.sendRedirect("/login.html");
        });
        // 指定处理登录
        kaptchaFilter.setFilterProcessesUrl("/doLogin");
        kaptchaFilter.setUsernameParameter("uname");
        kaptchaFilter.setPasswordParameter("passwd");
        return kaptchaFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .mvcMatchers("/login.html").permitAll()
                .mvcMatchers("/vc.jpg").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login.html")
                // KaptchaFilter配置了成功失败处理，对此处进行覆盖
//                .defaultSuccessUrl("/index.html", true)
//                .failureUrl("/login.html")
                .and()
                .logout()
                .logoutUrl("/logout")
                .and()
                .csrf().disable();

        http.addFilterAt(kaptchaFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
