package com.yuhui.config;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.servlet.http.HttpServletRequest;

/**
 * 自定义记住我 services 实现类
 */
public class MyPersistentTokenBasedRememberMeServices extends PersistentTokenBasedRememberMeServices {
    public MyPersistentTokenBasedRememberMeServices(String key, UserDetailsService userDetailsService, PersistentTokenRepository tokenRepository) {
        super(key, userDetailsService, tokenRepository);
    }

    /**
     * 自定义前后端分离获取 remember-me 方式
     */
    @Override
    protected boolean rememberMeRequested(HttpServletRequest request, String parameter) {
        String paramValue = request.getAttribute(parameter).toString();
        // 下面的都是父类的实现，直接cv就行
        if (paramValue != null) {
            if (paramValue.equalsIgnoreCase("true") || paramValue.equalsIgnoreCase("on")
                    || paramValue.equalsIgnoreCase("yes") || paramValue.equals("1")) {
                return true;
            }
        }
        return false;
    }
}