package com.start.service.serviceImpl;

import com.start.entitle.Comment;
import com.start.mapper.CommentMapper;
import com.start.service.CommentService;
import com.start.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentMapper commentMapper;

    @Override
    public List<Comment> findCommentsByFloorId(Integer id) {
        return commentMapper.findCommentsByFloorId(id);
    }

    @Override
    public void addcomment(Comment comt) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Long userId = ((Number) map.get("id")).longValue();
        comt.setUserId(userId);
        comt.setCreateTime(new Date());
        commentMapper.insert(comt);
    }
}
