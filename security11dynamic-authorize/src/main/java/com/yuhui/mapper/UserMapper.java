package com.yuhui.mapper;

import com.yuhui.entity.Role;
import com.yuhui.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author yuhui
 * @date 2023/2/5 13:19
 */
@Mapper
public interface UserMapper {

    /**
     * 根据用户名查询用户信息
     */
    User loadUserByUsername(String username);

    /**
     * 根据用户id获取角色信息
     */
    List<Role> getUserRoleByUid(Integer uid);
}
