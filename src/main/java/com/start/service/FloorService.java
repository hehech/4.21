package com.start.service;

import com.start.entitle.Floor;
import com.start.entitle.PageBean;

import java.util.List;

public interface FloorService {


    List<Floor> findfloorsbypostid(Integer id);

    Floor findfloorbyid(Long floorId);

    void addfloor(Floor floor);

    PageBean<Floor> findfloorsbypostidpage(Integer pageNum, Integer pageSize, Integer id);
}
