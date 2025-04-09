package com.start.service;

import com.start.entitle.Floor;

import java.util.List;

public interface FloorService {


    List<Floor> findfloorsbypostid(Integer id);

    Floor findfloorbyid(Long floorId);

    void addfloor(Floor floor);
}
