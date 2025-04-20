package com.start.service;

import com.start.entitle.Board;
import com.start.entitle.Post;
import com.start.entitle.User;

import javax.naming.directory.SearchResult;
import java.util.List;

public interface SearchService {

    List<User> searchuser(String key);

    List<Board> searchboard(String key);

    List<Post> searchpost(String key);
}
