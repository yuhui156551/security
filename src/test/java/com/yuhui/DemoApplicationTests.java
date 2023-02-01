package com.yuhui;

import com.yuhui.mapper.MenuMapper;
import com.yuhui.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.Resource;

@SpringBootTest
class DemoApplicationTests {
    @Resource
    private UserMapper userMapper;
    @Resource
    private MenuMapper menuMapper;

    @Test
    void testMapper() {
//        System.out.println(userMapper.selectList(null));
        System.out.println(menuMapper.selectPermsByUserId(1L));
    }

    @Test
    void TestBCryptPasswordEncoder(){
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        // $2a$10$pOpv0jeqUBRxhFhDlhzdAuuqJh0CHy4RnwCbCIzzbfN4jj0N7tDK.
        String encode = bCryptPasswordEncoder.encode("123");
        System.out.println(encode);
    }

}
