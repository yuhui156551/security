package com.yuhui.controller;

import com.google.code.kaptcha.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;

@Controller
public class KaptchaController {
    private final Producer producer;

    @Autowired
    public KaptchaController(Producer producer) {
        this.producer = producer;
    }

    @GetMapping("/vc.jpg")
    public void getVerifyCode(HttpServletResponse response, HttpSession session) throws IOException {
        response.setContentType("image/png");
        String code = producer.createText();
        session.setAttribute("kaptcha", code);// 可以更换成 redis 实现
        BufferedImage bi = producer.createImage(code);
        ServletOutputStream os = response.getOutputStream();
        ImageIO.write(bi, "jpg", os);
    }
}