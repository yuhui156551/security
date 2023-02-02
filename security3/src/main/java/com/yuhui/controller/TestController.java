package com.yuhui.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yuhui
 * @date 2023/2/2 15:43
 */
@RestController
public class TestController {

    @RequestMapping("/test")
    public String testController(){
        return "test  ok!";
    }
}
