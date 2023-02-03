package com.yuhui;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Base64Utils;

@SpringBootTest
class Security7rememberMeApplicationTests {

    @Test
    void contextLoads() {
        System.out.println(Base64Utils.decodeFromString(""));
    }

}
