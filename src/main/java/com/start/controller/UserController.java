package com.start.controller;

import com.aliyuncs.utils.StringUtils;
import com.start.entitle.Result;
import com.start.entitle.User;
import com.start.service.UserService;
import com.start.utils.JwtUtil;
import com.start.utils.Md5Util;
import com.start.utils.SMSUtils;
import com.start.utils.ValidateCodeUtils;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
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
    //根据userid找userinfo
    @GetMapping("/userinfo")
    public Result<User> getuserinfobyid(Integer id){
        User cs= userService.getuserinfobyid(id);
        return Result.success(cs);
    }
    @GetMapping("/perpostcount")
    public Result<Integer> getuserinfobyid(){
        Integer cs= userService.findPerPost();
        return Result.success(cs);
    }
    @GetMapping("/interusers")
    public Result<List<User>> findinterusers(){
        List<User> cs= userService.findinterusers();
        return Result.success(cs);
    }
    @GetMapping("/fans")
    public Result<List<User>> findfans(){
        List<User> cs= userService.findfans();
        return Result.success(cs);
    }


    @PostMapping("/sendMsg")
    public Result<String> sendMsg(@Pattern(regexp = "^\\S{11,11}$")String phoneNumber, HttpSession session){
//      1.获取手机号

        if(StringUtils.isEmpty(phoneNumber)){
            return Result.error("短信发送失败");
        }
//      2.随机生成四位验证码
        String code = ValidateCodeUtils.generateValidateCode(4).toString();

//      3.调用阿里云提供的短信服务
        SMSUtils.sendMessage("贺晨浩","",phoneNumber,code);

//      4.需要将生成的验证码保存到session中
        session.setAttribute(phoneNumber,code);

        return Result.success("验证码短信发送成功");
    }
}
