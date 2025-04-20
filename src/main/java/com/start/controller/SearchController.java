package com.start.controller;

import com.start.entitle.Board;
import com.start.entitle.Post;
import com.start.entitle.Result;
import com.start.entitle.User;
import com.start.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.directory.SearchResult;
import java.util.List;

@RestController
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private SearchService searchService;

    @GetMapping("/searchuser")
    public Result<List<User>> searchuser(String key){
        List<User> cs= searchService.searchuser(key);
        return Result.success(cs);
    }
    @GetMapping("/searchboard")
    public Result<List<Board>> searchboard(String key){
        List<Board> cs= searchService.searchboard(key);
        return Result.success(cs);
    }
    @GetMapping("/searchpost")
    public Result<List<Post>> searchpost(String key){
        List<Post> cs= searchService.searchpost(key);
        return Result.success(cs);
    }
}