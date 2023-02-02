package com.yuhui.service;

import com.yuhui.dao.UserDao;
import com.yuhui.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

/**
 * 自定义 UserDetailService 实现
 */
@Service
public class MyUserDetailService implements UserDetailsService {
    private final UserDao userDao;

    @Autowired
    public MyUserDetailService(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1、获取用户信息
        User user = userDao.loadUserByUsername(username);
        if (ObjectUtils.isEmpty(user)) throw new RuntimeException("用户不存在！");
        // 2、获取用户权限信息
        user.setRoles(userDao.getRolesByUid(user.getId()));
        return user;
    }
}
