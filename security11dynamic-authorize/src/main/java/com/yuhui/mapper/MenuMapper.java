package com.yuhui.mapper;

import com.yuhui.entity.Menu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MenuMapper {
    /**
     * 获取所有菜单
     */
    List<Menu> getAllMenu();
}