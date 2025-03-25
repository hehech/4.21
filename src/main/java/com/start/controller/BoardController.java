package com.start.controller;


import com.start.entitle.Board;
import com.start.entitle.Result;
import com.start.mapper.BoardMapper;
import com.start.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/board")
public class BoardController {

    @Autowired
    private BoardService boardService;

    //查询所有板块
    @GetMapping
    public Result<List<Board>> list(){
        List<Board> cs= boardService.display();
        return Result.success(cs);
    }

}
