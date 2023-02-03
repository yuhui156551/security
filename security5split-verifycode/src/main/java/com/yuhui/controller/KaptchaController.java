package com.yuhui.controller;

import com.google.code.kaptcha.Producer;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;

@RestController
public class KaptchaController {
    private final Producer producer;

    @Autowired
    public KaptchaController(Producer producer) {
        this.producer = producer;
    }

    @GetMapping("/vc.png")
    public String getVerifyCode(HttpSession session) throws IOException {
        // 1、生成验证码
        String code = producer.createText();
        System.out.println(code);
        session.setAttribute("kaptcha", code);// 可以更换成 redis 实现
        BufferedImage bi = producer.createImage(code);
        // 2、将图片转换为内存中的byte数组
        FastByteArrayOutputStream fos = new FastByteArrayOutputStream();
        ImageIO.write(bi, "png", fos);
        // 3、生成 base64
        return Base64.encodeBase64String(fos.toByteArray());
    }
}