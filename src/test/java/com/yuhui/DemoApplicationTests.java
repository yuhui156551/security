package com.yuhui;

import com.yuhui.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.Resource;

@SpringBootTest
class DemoApplicationTests {
    @Resource
    private UserMapper userMapper;

    @Test
    void testMapper() {
        System.out.println(userMapper.selectList(null));
    }

    @Test
    void TestBCryptPasswordEncoder(){
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encode = bCryptPasswordEncoder.encode("1234");
        System.out.println(encode);
    }

}
