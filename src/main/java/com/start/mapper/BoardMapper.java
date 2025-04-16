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

    @Select("select * from \"board\" where \"board_id\"=#{id}")
    Board findBoardinfoById(Integer id);

    @Select("SELECT b.* FROM \"board\" b " +
            "INNER JOIN \"board_follows\" f ON b.\"board_id\" = f.\"board_id\" " +
            "WHERE f.\"user_id\" = #{id}")
    List<Board> findinterBoards(Integer id);
}
