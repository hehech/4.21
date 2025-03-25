package com.start.controller;

import com.start.entitle.Result;
import com.start.entitle.User;
import com.start.service.UserService;
import com.start.utils.JwtUtil;
import com.start.utils.Md5Util;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    //注册
    @PostMapping("/regist")
    public Result RegistControl(@RequestBody User user) {

        //查询用户
        User u = userService.findByAccount(user.getAccount());
        if (u == null) {
            //没有占用
            //注册
            userService.addUser(user);
            return Result.success();
        } else {
            //占用
            return Result.error("用户名已被占用！");
        }
    }

    //登录
    @PostMapping("/login")
    public Result<String> login(@Pattern(regexp = "^\\S{8,16}$") String account, @Pattern(regexp = "^\\S{8,16}$") String password) {
        System.out.println("访问了");
        User loginUser = userService.findByAccount(account);
        if (loginUser == null) {
            return Result.error("此用户不存在");
        }
        //判断密码是否正确；
        if (Md5Util.getMD5String(password).equals(loginUser.getPassword())) {
            //登录成功
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", loginUser.getUserId());
            claims.put("account", loginUser.getAccount());
            String token = JwtUtil.genToken(claims);
            //把token放到------中
            return Result.success(token);
        } else {
            return Result.error("密码错误");
        }
    }

    //返回当前登录用户信息
    @GetMapping("currentuser")
    public Result<User> getCurrentUser(){
        return Result.success(userService.getCurrentUser());
    }
}
