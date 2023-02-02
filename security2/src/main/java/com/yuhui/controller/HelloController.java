package com.yuhui.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @RequestMapping("/hello")
    public String hello() {
        // 默认是在内存中生成用户信息
        /*Authentication authentication = SecurityContextHolder
                .getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        System.out.println("身份 :" + principal.getUsername());
        System.out.println("凭证 :" + authentication.getCredentials());
        System.out.println("权限 :" + authentication.getAuthorities());*/
        return "hello security";
    }
}