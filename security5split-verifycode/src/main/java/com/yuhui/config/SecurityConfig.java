package com.yuhui.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuhui.filter.LoginKaptchaFilter;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yuhui
 * @date 2023/2/3 11:58
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // Bcrypt 加密方式
    @Bean
    public PasswordEncoder bcryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(){
        InMemoryUserDetailsManager inMemoryUserDetailsManager = new InMemoryUserDetailsManager();
        // 老是忘记设置noop！！！
        inMemoryUserDetailsManager.createUser(User.withUsername("root").password("$2a$10$UK4tsZCvtd97CpWMR80/Ouv4nFjgcAk5jI2vcReV.MWqCEXtfDp/K").roles("admin").build());
        return inMemoryUserDetailsManager;
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

    @Bean
    public LoginKaptchaFilter loginKaptchaFilter() throws Exception {
        LoginKaptchaFilter loginKaptchaFilter = new LoginKaptchaFilter();
        // 1、认证 url
        loginKaptchaFilter.setFilterProcessesUrl("/doLogin");
        // 2、认证 接收参数
        loginKaptchaFilter.setUsernameParameter("uname");
        loginKaptchaFilter.setPasswordParameter("passwd");
        loginKaptchaFilter.setKaptchaParameter("kaptcha");
        // 3、指定认证管理器
        loginKaptchaFilter.setAuthenticationManager(authenticationManagerBean());
        // 4、认证成功处理
        loginKaptchaFilter.setAuthenticationSuccessHandler((req, resp, auh) -> {
            HashMap<String, Object> result = new HashMap<>();
            result.put("msg", "登录成功");
            result.put("用户信息", auh.getPrincipal());
            resp.setStatus(HttpStatus.OK.value());
            resp.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            // 转为 json 格式
            String s = new ObjectMapper().writeValueAsString(result);
            resp.getWriter().println(s);
        });
        // 5、认证失败处理
        loginKaptchaFilter.setAuthenticationFailureHandler((req, resp, ex) -> {
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("msg", "登录失败: " + ex.getMessage());
            resp.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            resp.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            String s = new ObjectMapper().writeValueAsString(result);
            resp.getWriter().println(s);
        });
        return loginKaptchaFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .mvcMatchers("/vc.png").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .and()
                // 异常处理
                .exceptionHandling()
                .authenticationEntryPoint((req, resp, ex) -> {
                    resp.setContentType("application/json;charset=UTF-8");
                    resp.setStatus(HttpStatus.UNAUTHORIZED.value());
                    resp.getWriter().println("必须认证之后才能访问!");
                })
                .and()
                .logout()
                .and()
                .csrf().disable();

        http.addFilterAt(loginKaptchaFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
