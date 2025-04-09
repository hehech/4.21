package com.start.mapper;

import com.start.entitle.Post;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author hao
* @description 针对表【post(帖子/一楼正文：存储帖子信息、一楼正文信息)】的数据库操作Mapper
* @createDate 2025-03-29 11:53:56
* @Entity generator.domain.Post
*/


@Mapper
public interface PostMapper {


    @Select("select * from \"post\"")
    List<Post> findPosts();
    @Select("select * from \"post\" where \"user_id\"=#{id}")
    List<Post> findPostById(Integer id);
    @Select("select * from \"post\" where \"board_id\"=#{id}")
    List<Post> getBapost(Integer id);
    @Select("select * from \"post\" where \"post_id\"=#{id}")
    Post getonepost(Integer id);
    @Select("select * from \"post\" where \"board_id\"=#{id}")
    List<Post> gethotbapost(Integer id);
    @Select("select * from \"post\" where \"board_id\"=#{id}")
    List<Post> getbapost(Integer id);
}
