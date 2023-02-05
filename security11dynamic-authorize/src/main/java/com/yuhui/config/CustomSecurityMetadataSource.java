package com.yuhui.config;

import com.yuhui.entity.Menu;
import com.yuhui.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.Collection;
import java.util.List;

/**
 * @author yuhui
 * @date 2023/2/5 14:29
 */
@Component
public class CustomSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    private MenuService menuService;

    @Autowired
    public CustomSecurityMetadataSource(MenuService menuService) {
        this.menuService = menuService;
    }

    AntPathMatcher antPathMatcher = new AntPathMatcher();

    /**
     * 自定义动态资源权限 元数据信息
     */
    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        // 1、获取请求对象
        String requestURI = ((FilterInvocation) object).getRequest().getRequestURI();
        // 2、查询所有菜单
//         1	/admin/**	1	ROLE_ADMIN	系统管理员
//         2	/user/**	2	ROLE_USER	普通用户
//         3	/guest/**	2	ROLE_USER	普通用户
//         3	/guest/**	3	ROLE_GUEST	游客
        List<Menu> allMenu = menuService.getAllMenu();
        for (Menu menu : allMenu) {
            if (antPathMatcher.match(menu.getPattern(), requestURI)) {
                String[] roles = menu.getRoles().stream().map(r -> r.getName()).toArray(String[]::new);
                return SecurityConfig.createList(roles);
            }
        }
        return null;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }
}
