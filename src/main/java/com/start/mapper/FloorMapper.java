package com.start.mapper;

import com.start.entitle.Floor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author hao
* @description 针对表【floor】的数据库操作Mapper
* @createDate 2025-04-09 18:29:32
* @Entity generator.domain.Floor
*/
@Mapper
public interface FloorMapper {

    @Select("select * from \"floor\" where \"post_id\"=#{id}")
    List<Floor> findfloorsbypostid(Integer id);

    @Select("select * from \"floor\" where \"floor_id\"=#{floorId}")
    Floor findfloorbyid(Long floorId);

    void addfloor(Floor floor);
}
