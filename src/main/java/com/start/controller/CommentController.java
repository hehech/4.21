package com.start.controller;

import com.start.entitle.Comment;
import com.start.entitle.Floor;
import com.start.entitle.Result;
import com.start.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("/onefloor")
    public Result<List<Comment>> findCommentsByFloorId(Integer id){
        List<Comment> cs= commentService.findCommentsByFloorId(id);
        for (Comment c:cs){
            System.out.println(c.getCreateTime());
        }
        return Result.success(cs);
    }

    @PostMapping("/addcomt")
    public Result addcomment(@RequestBody Comment comt) {
        System.out.println(comt.toString());
        commentService.addcomment(comt);
        return Result.success();

    }
}
