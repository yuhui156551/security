package com.yuhui.filter;

import com.yuhui.exception.KaptchaNotMatchException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class KaptchaFilter extends UsernamePasswordAuthenticationFilter {

    public static final String KAPTCHA_KEY = "kaptcha";// 默认值
    private String kaptcha = KAPTCHA_KEY;

    public String getKaptcha() {
        return kaptcha;
    }

    public void setKaptcha(String kaptcha) {
        this.kaptcha = kaptcha;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        // 1、判断是否是 post 方式
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
        // 2、获取验证码
        // 如果没有赋值，拿到的就是默认值  如果赋值，拿到的就是set之后的值
        String kaptcha = request.getParameter(getKaptcha());
        // 3、输入的验证码和 session 中的验证码进行比较
        String sessionKaptcha = (String) request.getSession().getAttribute("kaptcha");
        if (!ObjectUtils.isEmpty(kaptcha) && !ObjectUtils.isEmpty(sessionKaptcha) &&
                kaptcha.equalsIgnoreCase(sessionKaptcha)) {
            return super.attemptAuthentication(request, response);
        }
        throw new KaptchaNotMatchException("验证码输入错误!");
    }
}