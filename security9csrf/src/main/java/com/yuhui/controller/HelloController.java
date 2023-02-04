package com.yuhui.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yuhui
 * @date 2023/2/4 16:36
 */
@RestController
public class HelloController{

    @GetMapping("/post")
//    @CrossOrigin(origins = "http://localhost:8081")
    public String post(){
        return "hello post";
    }
}


