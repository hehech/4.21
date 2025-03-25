package com.start.service;

import com.start.entitle.User;
import org.springframework.stereotype.Service;


public interface UserService {

    //注册：添加用户
    void addUser(User user);
    //
    User findByAccount(String account);
    //获取当前登录用户信息
    User getCurrentUser();
}
