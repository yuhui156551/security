package com.yuhui.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yuhui
 * @date 2023/2/4 13:02
 */
@RestController
public class DemoController {

    @GetMapping("/")
    public String demo(){
        return "demo...ok!";
    }
}
