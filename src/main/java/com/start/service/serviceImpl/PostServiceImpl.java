package com.start.service.serviceImpl;

import com.start.entitle.Post;
import com.start.mapper.PostMapper;
import com.start.service.PostService;
import com.start.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostMapper postMapper;


    @Override
    public List<Post> display() {
        return postMapper.findPosts();
    }

    @Override
    public List<Post> findPostById() {
        Map<String,Object> map= ThreadLocalUtil.get();
        Integer id = (Integer) map.get("id");
        return postMapper.findPostById(id);
    }

    @Override
    public Post getonepost(Integer id) {
        return postMapper.getonepost(id);
    }

    @Override
    public List<Post> gethotbapost(Integer id) {
        //要在这里实现排序的逻辑
        return postMapper.gethotbapost(id);
    }

    @Override
    public List<Post> getbapost(Integer id) {
        return postMapper.getbapost(id);
    }

    @Override
    public List<Post> findPersonalCollectPost() {
        Map<String,Object> map= ThreadLocalUtil.get();
        Integer id = (Integer) map.get("id");
        return postMapper.findPersonalCollectPost(id);
    }
}
