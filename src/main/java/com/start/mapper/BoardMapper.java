package com.start.mapper;

import com.start.entitle.Board;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface BoardMapper {

    //查询所有板块
    @Select("select * from \"board\"")
    List<Board> findBoards();

    @Select("select \"name\" from \"board\" where \"board_id\"=#{id}")
    String findBoardNameById(Integer id);

}
