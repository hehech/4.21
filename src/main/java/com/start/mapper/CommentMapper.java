package com.start.mapper;

import com.start.entitle.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author hao
* @description 针对表【comment】的数据库操作Mapper
* @createDate 2025-04-10 17:21:39
* @Entity generator.domain.Comment
*/
@Mapper
public interface CommentMapper {

    void insert(Comment record);



    @Select("select * from \"comment\" where \"floor_id\"=#{id}")
    List<Comment> findCommentsByFloorId(Integer id);
}
