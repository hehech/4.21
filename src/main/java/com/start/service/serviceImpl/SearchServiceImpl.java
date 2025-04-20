package com.start.service.serviceImpl;

import com.start.entitle.Board;
import com.start.entitle.Post;
import com.start.entitle.User;
import com.start.mapper.BoardMapper;
import com.start.mapper.PostMapper;
import com.start.mapper.UserMapper;
import com.start.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchServiceImpl implements SearchService {

        @Autowired
        private UserMapper userMapper;

        @Autowired
        private BoardMapper boardMapper;

        @Autowired
        private PostMapper postMapper;


        @Override
        public List<User> searchuser(String key) {
                String sk = "%" + key + "%";
                return userMapper.searchuser(sk);
        }

        @Override
        public List<Board> searchboard(String key) {
                String sk = "%" + key + "%";
                return boardMapper.searchboard(sk);
        }

        @Override
        public List<Post> searchpost(String key) {
                String sk = "%" + key + "%";
                return postMapper.searchpost(sk);
        }
}
