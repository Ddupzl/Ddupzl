package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.dto.Post;
import com.example.entity.vo.request.PostRes;
import com.example.mapper.PostMapper;
import com.example.service.PostService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class PostServiceImpl extends ServiceImpl<PostMapper, Post> implements PostService {
    @Resource
    PostMapper mapper;
    @Override
    public List<Post> postList(int from, int to) {
      return mapper.postList(from,to);
    }

    @Override
    public Post postInfo(int id) {
        return mapper.postInfo(id);
    }

    @Override
    public Integer posts(PostRes post) {
//        System.out.println(post);
        return mapper.posts(post.getUserId(),post.getTitle(),post.getContent(),post.getPostDate());
    }
}
