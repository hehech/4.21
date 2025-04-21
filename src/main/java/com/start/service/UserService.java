package com.start.service;

import com.start.entitle.Post;
import com.start.entitle.User;
import jakarta.validation.constraints.Pattern;
import org.springframework.stereotype.Service;

import java.util.List;


public interface UserService {

    //注册：添加用户
    void addUser(User user);
    //
    User findByAccount(String account);
    //获取当前登录用户信息
    User getCurrentUser();
    //
    User getuserinfobyid(Integer id);

    Integer findPerPost();

    List<User> findinterusers();

    List<User> findfans();

    User findUserByPhoneNumber(@Pattern(regexp = "^\\S{11,11}$") String phoneNumber);

    void addfocususer(Integer id);

    void cancelFocusUser(Integer id);

    List<Integer> findCurFocusU();

    Integer findotherpost(Integer id);

    List<Post> findotherPostinfo(Integer id);

    List<User> findotherfocususers(Integer id);

    List<User> findotherfans(Integer id);
}
