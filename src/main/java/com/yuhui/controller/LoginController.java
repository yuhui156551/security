package com.yuhui.controller;

import com.yuhui.domain.ResponseResult;
import com.yuhui.domain.User;
import com.yuhui.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yuhui
 * @date 2022/12/26 13:47
 */
@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("/user/login")
    public ResponseResult login(@RequestBody User user){
        return loginService.login(user);
    }
}
