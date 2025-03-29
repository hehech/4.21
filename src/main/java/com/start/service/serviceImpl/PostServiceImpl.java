package com.start.service.serviceImpl;

import com.start.mapper.PostMapper;
import com.start.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostMapper postMapper;


}
