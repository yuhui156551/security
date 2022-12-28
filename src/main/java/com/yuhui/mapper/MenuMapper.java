package com.yuhui.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yuhui.domain.Menu;

import java.util.List;

/**
 * @author yuhui
 * @date 2022/12/28 14:21
 */
public interface MenuMapper extends BaseMapper<Menu> {

    /**
     * 根据id查询权限信息
     * @param id 用户id
     * @return
     */
    List<String> selectPermsByUserId(Long id);
}
