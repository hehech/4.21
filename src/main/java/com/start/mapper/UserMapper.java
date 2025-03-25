package com.start.mapper;

import com.start.entitle.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
* @author hao
* @description 针对表【user】的数据库操作Mapper
* @createDate 2025-03-20 13:08:11
* @Entity generator.domain.User
*/
@Mapper
public interface UserMapper {

    void addUser(User user);

    User findByAccount(String account);
}
