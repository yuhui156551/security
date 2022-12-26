package com.yuhui.service;

import com.yuhui.domain.ResponseResult;
import com.yuhui.domain.User;

/**
 * @author yuhui
 * @date 2022/12/26 13:47
 */
public interface LoginService {
    ResponseResult login(User user);
}
