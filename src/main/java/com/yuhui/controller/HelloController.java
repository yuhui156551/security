package com.yuhui.controller;

import com.yuhui.domain.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @RequestMapping("/hello")
//    @PreAuthorize("hasAuthority('system:dept:list')")
    @PreAuthorize("@yh.hasAuthority('system:dept:list')")
    public String hello() {
        Authentication authentication = SecurityContextHolder
                .getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        System.out.println("身份 :" + principal.getUserName());
        System.out.println("凭证 :" + authentication.getCredentials());
        System.out.println("权限 :" + authentication.getAuthorities());
        return "hello security";
    }
}