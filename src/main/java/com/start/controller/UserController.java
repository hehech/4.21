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
import org.springframework.data.redis.core.StringRedisTemplate;
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
    @Autowired
    private StringRedisTemplate redisTemplate;

    // 验证码前缀，用于Redis key
    private static final String CODE_PREFIX = "login:code:";
    // 验证码有效期(分钟)
    private static final long CODE_EXPIRE_MINUTES = 5;
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
            //把token放到redis中
            ValueOperations<String, String> ops=redisTemplate.opsForValue();
            ops.set(token,token,1,TimeUnit.HOURS);

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
    public Result<String> sendMsg(@RequestParam @Pattern(regexp = "^\\S{11,11}$") String phoneNumber) {
        // 1. 校验手机号
        if(StringUtils.isEmpty(phoneNumber)) {
            return Result.error("手机号不能为空");
        }

        // 2. 检查是否已发送且未过期(防刷)
        String existCode = redisTemplate.opsForValue().get(CODE_PREFIX + phoneNumber);
        if (existCode != null) {
            return Result.error("验证码已发送，请稍后再试");
        }

        // 3. 生成4位验证码
        String code = ValidateCodeUtils.generateValidateCode(4).toString();

        // 5. 调用短信服务(实际项目中)
        try {
            SMSUtils.sendMessage("贺晨浩", "", phoneNumber, code);
            // 4. 存入Redis，设置5分钟过期
            redisTemplate.opsForValue().set(
                    CODE_PREFIX + phoneNumber,
                    code,
                    CODE_EXPIRE_MINUTES,
                    TimeUnit.MINUTES
            );
            return Result.success("验证码发送成功");
        } catch (Exception e) {
            return Result.error("验证码发送失败，请重试");
        }
    }

    //手机验证码登录
    @PostMapping("/loginbyphone")
    public Result<String> loginbyphone(
            @RequestParam @Pattern(regexp = "^\\S{11,11}$") String phoneNumber,
            @RequestParam @Pattern(regexp = "^\\S{4,4}$") String code) {

        // 1. 从Redis获取验证码
        String cacheCode = redisTemplate.opsForValue().get(CODE_PREFIX + phoneNumber);
        if (cacheCode == null) {
            return Result.error("验证码已过期，请重新获取");
        }

        // 2. 比对验证码
        if (!cacheCode.equals(code)) {
            return Result.error("验证码不正确");
        }

        // 3. 查询用户(不存在则自动注册)
        User loginUser = userService.findUserByPhoneNumber(phoneNumber);
        if (loginUser == null) {
            return Result.error("该手机号码无绑定账号！");
        }

        // 4. 生成token
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", loginUser.getUserId());
        claims.put("account", loginUser.getAccount());
        String token = JwtUtil.genToken(claims);

        // 5. 把token存入Redis
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        ops.set(token, token, 1, TimeUnit.HOURS);

        // 6. 登录成功后删除Redis中的验证码(防止重复使用)
        redisTemplate.delete(CODE_PREFIX + phoneNumber);

        return Result.success(token);
    }
}
