package com.start.service;

import com.start.entitle.Post;

import java.util.List;

public interface PostService {

    List<Post> display();

    List<Post> findPostById();
}
