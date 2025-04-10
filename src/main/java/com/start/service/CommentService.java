package com.start.service;

import com.start.entitle.Comment;

import java.util.List;

public interface CommentService {
    List<Comment> findCommentsByFloorId(Integer id);

    void addcomment(Comment comt);
}
