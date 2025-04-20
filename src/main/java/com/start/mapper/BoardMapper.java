package com.start.mapper;

import com.start.entitle.Board;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
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
    @Select("SELECT * FROM \"board\" WHERE \"name\" LIKE #{sk}")
    List<Board> searchboard(String sk);


    @Insert("INSERT INTO \"board_follows\" (\"user_id\", \"board_id\") VALUES (#{uid}, #{id})")
    void doFocusBoard(Integer uid,Integer id);
    @Delete("DELETE FROM \"board_follows\" WHERE \"user_id\" = #{uid} AND \"board_id\" = #{id}")
    void cancelfocusboard(Integer uid, Integer id);
    @Select("select \"board_id\" from \"board_follows\" where \"user_id\"=#{uid}")
    List<Integer> findCurFocusB(Integer uid);
}
