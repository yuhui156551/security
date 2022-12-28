package com.yuhui.expression;

import com.yuhui.domain.LoginUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.http.SecurityHeaders;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author yuhui
 * @date 2022/12/28 21:04
 */

/**
 * 自定义权限校验
 */
@Component("yh")
public class YHExpressionRoot {
    public boolean hasAuthority(String authority){
        // 获取用户权限
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        List<String> permissions = loginUser.getPermissions();
        // 判断用户权限集合中是否存再authority
        return permissions.contains(authority);
    }
}
