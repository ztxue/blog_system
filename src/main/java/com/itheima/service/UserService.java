package com.itheima.service;


import com.github.pagehelper.PageInfo;

import com.itheima.model.domain.User;

import java.util.List;

public interface UserService {

    // 分页
    PageInfo<User> pageUser(int page, int count);

    User queryById(Integer id);

    List<User> queryAll();

    int insert(User User);

    User update(User tUser);

    boolean deleteById(Integer id);

}
