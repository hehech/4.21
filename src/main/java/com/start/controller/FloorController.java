package com.start.controller;

import com.start.entitle.Floor;
import com.start.entitle.Post;
import com.start.entitle.Result;
import com.start.entitle.User;
import com.start.service.FloorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/floor")
public class FloorController {
    @Autowired
    private FloorService floorService;

    @GetMapping("/pfloor")
    public Result<List<Floor>> findfloorsbypostid(Integer id){
        List<Floor> cs= floorService.findfloorsbypostid(id);
        return Result.success(cs);
    }

    @PostMapping("/addfloor")
    public Result addfloor(@RequestBody Floor floor) {
        //查询帖子

            //没有占用
            System.out.println("查询成功");
            floorService.addfloor(floor);
            return Result.success();

    }
}
