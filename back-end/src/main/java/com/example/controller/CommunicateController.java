package com.example.controller;

import com.example.entity.RestBean;
import com.example.entity.dto.Communicate;
import com.example.entity.dto.Post;
import com.example.service.CommunicateService;
import com.example.service.PostService;
import com.example.utils.PageUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

@RequestMapping("/comm")
@RestController
public class CommunicateController {
    @Resource
    private CommunicateService service;
    @Resource
    private PostService service2;
    private static final String STORE_DIR = "C:\\Users\\HP\\Desktop\\SubjectContest\\back-end\\src\\main\\resources\\images\\tx\\";

    @RequestMapping("/tx/{fileName}") //评论头像下载地址
    public void downloadTx(@PathVariable("fileName") String fileName, HttpServletResponse response) {
        // 获取真实的文件路径
        String filePath = STORE_DIR + fileName;
        try {
            // 下载文件
            // 设置响应头
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
//            response.setHeader("Cache-Control", "max-age=60*60");
            // 读取文件内容并写入输出流
            Files.copy(Paths.get(filePath), response.getOutputStream());
            response.getOutputStream().flush();
        } catch (IOException e) {
            e.printStackTrace();
            response.setStatus(404);
        }
    }

     // 返回树形结构评论数据      根据帖子id，也就是post_id,在db_comments表里查询相关的评论
    @RequestMapping("/list")
    public RestBean list(@RequestParam("postId") int id,HttpServletResponse response) throws IOException {
        response.setHeader("Cache-Control", "public, max-age=86400");
        List<Communicate> list = service.listTree(id);
        System.out.println(list);
        return RestBean.success(list);
    }

   //添加评论
    @RequestMapping("/save")
    public RestBean save(@RequestBody Communicate entity) {
        entity.setTime(new Date());
        service.save(entity);
        return RestBean.success();
    }
    //分页查询帖子,在表db_post里面查
    @RequestMapping("/postList")
    public PageUtils<Post> postList(@RequestParam("currentPage") int currentPage,HttpServletResponse response) {
        PageUtils<Post> results=new PageUtils<>((int)service2.count(),8,service2.postList((currentPage-1)*8,8) );
        return results;
    }
    //根据帖子id查询帖子信息
    @RequestMapping("/postInfo")
    public Post postInfo(@RequestParam("postId") int id) {
        return service2.postInfo(id);
    }
}
