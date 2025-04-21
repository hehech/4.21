package com.start.service;

import com.start.entitle.Post;

import java.util.List;

public interface PostService {

    List<Post> display();

    List<Post> findPostById();
    Post getonepost(Integer id);
    List<Post> gethotbapost(Integer id);
    List<Post> getbapost(Integer id);

    List<Post> findPersonalCollectPost();

    void addshoutie(Post p);
}
