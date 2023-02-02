package com.yuhui.dao;

import com.yuhui.entity.Role;
import com.yuhui.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserDao {
    /**
     * 根据用户名查询用户
     */
    User loadUserByUsername(String username);

    /**
     * 根据用户id查询角色
     */
    List<Role> getRolesByUid(Integer uid);
}