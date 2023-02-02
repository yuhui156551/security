package com.yuhui.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * 自定义验证码异常类，需继承 AuthenticationException 这样才能被当做认证异常处理
 */
public class KaptchaNotMatchException extends AuthenticationException {

    public KaptchaNotMatchException(String msg) {
        super(msg);
    }

    public KaptchaNotMatchException(String msg, Throwable cause) {
        super(msg, cause);
    }
}