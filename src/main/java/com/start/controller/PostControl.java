package com.start.controller;


import com.start.entitle.Board;
import com.start.entitle.Post;
import com.start.entitle.Result;
import com.start.service.BoardService;
import com.start.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/post")
public class PostControl {
    @Autowired
    private PostService postService;

    @GetMapping
    public Result<List<Post>> list(){
        List<Post> cs= postService.display();
        return Result.success(cs);
    }
    @GetMapping("/perpost")
    public Result<List<Post>> perPost(){
        List<Post> cs= postService.findPostById();
        return Result.success(cs);
    }
    @GetMapping("/onepost")
    public Result<Post> getonepost(Integer id){
        Post cs= postService.getonepost(id);
        return Result.success(cs);
    }
    @GetMapping("/hotpost")
    public Result<List<Post>> gethotbapost(Integer id){
        List<Post> cs= postService.gethotbapost(id);
        return Result.success(cs);
    }

    @GetMapping("/bapost")
    public Result<List<Post>> getbapost(Integer id){
        List<Post> cs= postService.getbapost(id);
        return Result.success(cs);
    }
    @GetMapping("/percollect")
    public Result<List<Post>> findPersonalCollectPost(){
        List<Post> cs= postService.findPersonalCollectPost();
        return Result.success(cs);
    }
}
