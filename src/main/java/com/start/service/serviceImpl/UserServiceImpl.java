package com.start.service.serviceImpl;

import com.start.entitle.User;
import com.start.mapper.UserMapper;
import com.start.service.UserService;
import com.start.utils.Md5Util;
import com.start.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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

    @Override
    public User getuserinfobyid(Integer id) {
        return userMapper.getuserinfobyid(id);
    }

    @Override
    public Integer findPerPost() {
        Map<String,Object> map= ThreadLocalUtil.get();
        Integer id = (Integer) map.get("id");
        return userMapper.getPerPostCount(id);
    }

    @Override
    public List<User> findinterusers() {
        Map<String,Object> map= ThreadLocalUtil.get();
        Integer id = (Integer) map.get("id");
        return userMapper.findinterusers(id);
    }

    @Override
    public List<User> findfans() {
        Map<String,Object> map= ThreadLocalUtil.get();
        Integer id = (Integer) map.get("id");
        return userMapper.findfans(id);
    }

    @Override
    public User findUserByPhoneNumber(String phoneNumber) {
        return userMapper.findUserByPhoneNumber(phoneNumber);
    }

    @Override
    public void addfocususer(Integer id) {
        Map<String,Object> map= ThreadLocalUtil.get();
        Integer mid = (Integer) map.get("id");
        userMapper.addfocususer(mid,id);
    }

    @Override
    public void cancelFocusUser(Integer id) {
        Map<String,Object> map= ThreadLocalUtil.get();
        Integer mid = (Integer) map.get("id");
        userMapper.cancelFocusUser(mid,id);
    }

    @Override
    public List<Integer> findCurFocusU() {
        Map<String,Object> map= ThreadLocalUtil.get();
        Integer mid = (Integer) map.get("id");
        return userMapper.findCurFocusU(mid);
    }
}
