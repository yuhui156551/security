package com.yuhui;

import com.yuhui.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class DemoApplicationTests {
    @Resource
    private UserMapper userMapper;

    @Test
    void testMapper() {
        System.out.println(userMapper.selectList(null));
    }

}
