package com.example.controller;

import com.example.entity.RestBean;
import com.example.entity.dto.Post;
import com.example.entity.vo.request.PostRes;
import com.example.service.PostService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;


@RestController
@RequestMapping("/post")
public class PostController {
    @Resource
    PostService service;

    @RequestMapping("/posts")
    public RestBean post(@RequestBody PostRes post){
        post.setPostDate(new Date());
        service.posts(post);
        return RestBean.success();
    }
}
