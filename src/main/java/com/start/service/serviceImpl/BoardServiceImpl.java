package com.start.service.serviceImpl;


import com.start.entitle.Board;
import com.start.mapper.BoardMapper;
import com.start.mapper.UserMapper;
import com.start.service.BoardService;
import com.start.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class BoardServiceImpl implements BoardService {

    @Autowired
    private BoardMapper boardMapper;


    //查询所有板块
    @Override
    public List<Board> display() {
        return boardMapper.findBoards();
    }

    @Override
    public String findBoardNameById(Integer id) {
        return boardMapper.findBoardNameById(id);
    }

    @Override
    public Board findBoardinfoById(Integer id) {
        return boardMapper.findBoardinfoById(id);
    }

    @Override
    public List<Board> findinterBoards() {
        Map<String,Object> map= ThreadLocalUtil.get();
        Integer id = (Integer) map.get("id");
        return boardMapper.findinterBoards(id);
    }

    @Override
    public void doFocusBoard(Integer id) {
        Map<String,Object> map= ThreadLocalUtil.get();
        Integer uid = (Integer) map.get("id");
        boardMapper.doFocusBoard(uid,id);
    }

    @Override
    public void cancelfocusboard(Integer id) {
        Map<String,Object> map= ThreadLocalUtil.get();
        Integer uid = (Integer) map.get("id");
        boardMapper.cancelfocusboard(uid,id);
    }

    @Override
    public List<Integer> findCurFocusB() {
        Map<String,Object> map= ThreadLocalUtil.get();
        Integer uid = (Integer) map.get("id");
        return boardMapper.findCurFocusB(uid);
    }
}
