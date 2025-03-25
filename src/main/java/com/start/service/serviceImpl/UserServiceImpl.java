package com.start.service.serviceImpl;

import com.start.entitle.User;
import com.start.mapper.UserMapper;
import com.start.service.UserService;
import com.start.utils.Md5Util;
import com.start.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    //根据account找用户
    @Override
    public User findByAccount(String account) {
        return userMapper.findByAccount(account);
    }

    @Override
    public User getCurrentUser() {
        Map<String,Object> map= ThreadLocalUtil.get();
        String account = (String) map.get("account");
        User user = userMapper.findByAccount(account);
        return user;
    }

    //注册用户
    @Override
    public void addUser(User user) {
        String md5String = Md5Util.getMD5String(user.getPassword());
        user.setPassword(md5String);
        if(user.getAvaterUrl()==null){
            user.setAvaterUrl("pic url");
        }
       // user.setLastLoginTime(new Date());
        if(user.getBio()==null){
            user.setBio("这个人很神秘~");
        }
        userMapper.addUser(user);
    }


}
