package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.dto.Post;
import com.example.entity.vo.request.PostRes;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public interface PostService extends IService<Post> {
    List<Post> postList(int from, int to);
    Post postInfo(int id);
    Integer posts(PostRes post);
}
