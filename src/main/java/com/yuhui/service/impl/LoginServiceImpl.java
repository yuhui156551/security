package com.yuhui.service.impl;

import com.yuhui.domain.LoginUser;
import com.yuhui.domain.ResponseResult;
import com.yuhui.domain.User;
import com.yuhui.service.LoginService;
import com.yuhui.utils.JwtUtil;
import com.yuhui.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Objects;

/**
 * @author yuhui
 * @date 2022/12/26 13:47
 */
@Service
public class LoginServiceImpl implements LoginService {

    @Resource
    private AuthenticationManager authenticationManager;
    @Autowired
    private RedisCache redisCache;

    /**
     * 馀灰流程梳理：
     * 放行login-->配置类中配置AuthenticationManager注入容器，执行authenticate方法进行用户认证
     * -->认证失败，抛出异常，security自带捕获异常流程捕获异常
     * -->认证成功，获取LoginUser，存入redis，根据userid使用jwt工具类生成token相应给前端
     * @param user
     * @return
     */
    @Override
    public ResponseResult login(User user) {
        // AuthenticationManager进行用户认证,返回LoginUser对象，debug一下就明白了
        UsernamePasswordAuthenticationToken authenticationToken = new
                UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        if (Objects.isNull(authenticate)) {
            throw new RuntimeException("用户名或密码错误");
        }
        // 使用userid生成token
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(userId);
        // authenticate存入redis
        redisCache.setCacheObject("login:" + userId, loginUser);
        // 把token响应给前端
        HashMap<String, String> map = new HashMap<>();
        map.put("token", jwt);
        return new ResponseResult(200, "登陆成功", map);
    }
}
