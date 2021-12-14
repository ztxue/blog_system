package com.itheima.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.model.domain.Comment;
import com.itheima.model.domain.User;
import com.itheima.dao.UserMapper;
import com.itheima.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("userService")
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;
    @Autowired
    private BCryptPasswordEncoder encoder;

    // 评论分页
    @Override
    public PageInfo<User> pageUser(int page, int count) {
        PageHelper.startPage(page, count);
        List<User> userList = userMapper.queryAll();
        PageInfo<User> userInfo = new PageInfo<>(userList);
        return userInfo;

    }

    @Override
    public User queryById(Integer id) {
        return null;
    }

    @Override
    public List<User> queryAll() {
        return userMapper.queryAll();
    }

    @Override
    public int insert(User user) {
        //加密
        user.setPassword(encoder.encode(user.getPassword()));
        return userMapper.insert(user);
    }
    @Override
    public User update(User tUser) {
        return null;
    }

    @Override
    public boolean deleteById(Integer id) {
        return false;
    }
}
