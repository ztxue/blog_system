package com.itheima.dao;

import com.itheima.model.domain.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface UserMapper {

    User queryById(Integer id);

    List<User> queryAll();

    int insert(User user);

    int update(User user);

    int deleteById(Integer id);

}

