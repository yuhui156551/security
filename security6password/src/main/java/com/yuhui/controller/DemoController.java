package com.yuhui.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yuhui
 * @date 2023/2/3 16:24
 */
@RestController
public class DemoController {

    @RequestMapping("/demo")
    public String demoController(){
        return "demo ...ok!";
    }
}
