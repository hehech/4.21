package com.start.service;

import com.start.entitle.Board;

import java.util.List;

public interface BoardService {
    //查询所有板块
    List<Board> display();

    String findBoardNameById(Integer id);

    Board findBoardinfoById(Integer id);

    List<Board> findinterBoards();

    void doFocusBoard(Integer id);

    void cancelfocusboard(Integer id);

    List<Integer> findCurFocusB();
}
