package com.start.mapper;

import com.start.entitle.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

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
    @Select("select * from \"user\" where \"user_id\"=#{id}")
    User getuserinfobyid(Integer id);
    @Select("SELECT COUNT(*) FROM \"post\" WHERE \"user_id\" = #{id}")
    Integer getPerPostCount(Integer id);
    @Select("SELECT u.* FROM \"user\" u " +
            "INNER JOIN \"user_follows\" f ON u.\"user_id\" = f.\"followee_id\" " +
            "WHERE f.\"follower_id\" = #{id}")
    List<User> findinterusers(Integer id);
    @Select("SELECT u.* FROM \"user\" u " +
            "INNER JOIN \"user_follows\" f ON u.\"user_id\" = f.\"follower_id\" " +
            "WHERE f.\"followee_id\" = #{id}")
    List<User> findfans(Integer id);
    @Select("select * from \"user\" where \"phone_number\"=#{phoneNumber}")
    User findUserByPhoneNumber(String phoneNumber);
}
