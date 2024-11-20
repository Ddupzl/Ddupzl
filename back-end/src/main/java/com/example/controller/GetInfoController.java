package com.example.controller;

import com.example.entity.vo.response.Competition;
import com.example.entity.vo.response.CompetitionVo;
import com.example.entity.vo.response.CompetitionVo2;
import com.example.entity.vo.response.SubjectType;
import com.example.mapper.CompetitionMapper;
import com.example.service.CompetitionService;
import com.example.utils.PageUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Validated
@RestController
@RequestMapping("/info")
public class GetInfoController {
    /**
     * 文件物理存储位置
     */
    private static final String STORE_DIR = "C:\\Users\\HP\\Desktop\\SubjectContest\\back-end\\src\\main\\resources\\images\\p\\";
    @Resource
    CompetitionService service;
    @Resource
    CompetitionMapper mapper;
    @GetMapping("/getDetails")
    public Competition getDetails(@RequestParam("id") int id){
        System.out.println(mapper.getDetails(id));
        return mapper.getDetails(id);
    }
    /**
     * 文件下载接口,下载图片
     * @param fileName 文件名
     * @param response
     */
    @GetMapping("/file/download/{fileName}")
    public void download(@PathVariable("fileName") String fileName, HttpServletResponse response) {
        // 获取真实的文件路径
        String filePath = STORE_DIR + fileName;
        try {
            // 下载文件
            // 设置响应头
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
            // 读取文件内容并写入输出流
            Files.copy(Paths.get(filePath), response.getOutputStream());
            response.getOutputStream().flush();
        } catch (IOException e) {
            response.setStatus(404);
        }
    }
    @GetMapping("/getHot")
    public PageUtils<CompetitionVo> getCompetitionInfoHot(@RequestParam("currentPage") int currentPage){
        PageUtils<CompetitionVo> results=new PageUtils<>(service.getCompetitionCount(),8,service.getCompetitionHot((currentPage-1)*8,8));
        return results;                                                                                 //分页查询信息
    }
    @GetMapping("/getLatest")
    public PageUtils<CompetitionVo> getCompetitionInfoLatest(@RequestParam("currentPage") int currentPage){
        PageUtils<CompetitionVo> results=new PageUtils<>(service.getLatestCount(),8,service.getCompetitionLatest((currentPage-1)*8,8));
        return results;                                                                           //分页查询信息
    }
    @GetMapping("/getLatest2")
    public PageUtils<CompetitionVo2> getCompetitionInfoLatest2(@RequestParam("currentPage") int currentPage){
        PageUtils<CompetitionVo2> results=new PageUtils<>(service.getLatestCount(),8,service.getCompetitionLatest2((currentPage-1)*8,8));
        return results;                                                                           //分页查询信息
    }
    @GetMapping("/getLatest3")
    public PageUtils<CompetitionVo2> getCompetitionInfoLatest3(@RequestParam("currentPage") int currentPage){
        PageUtils<CompetitionVo2> results=new PageUtils<>(service.getLatestCount2(),8,service.getCompetitionLatest3((currentPage-1)*8,8));
        return results;                                                                           //分页查询信息
    }
    @GetMapping("/getAllClassify")  //竞赛分类，查询所有
    public PageUtils<CompetitionVo2> getAllClassify(@RequestParam("currentPage") int currentPage){
        PageUtils<CompetitionVo2> results=new PageUtils<>(service.getAllCountClassify(),8,service.getAllClassify((currentPage-1)*8,8));
        return results;                                                                           //分页查询信息
    }

    @GetMapping("/getSubType") //竞赛分类，根据大类查询出小类
    public List<SubjectType> getSubjectName(){
        List<String> a=service.getSubjectBigType();
        List<SubjectType> c = new ArrayList<>();
        System.out.println(a);
        for(int i=0;i<a.size();i++)
        {
            SubjectType b=new SubjectType(a.get(i),service.getSubjectSmallType(a.get(i)));
            c.add(b);
        }
        return c;
    }
    @GetMapping("/getSubByType") //根据 大/小类别 查找比赛信息
        public PageUtils<CompetitionVo2> getSubjectByType(@RequestParam("type") String type,
                                                          @RequestParam("currentPage") int currentPage){
       PageUtils<CompetitionVo2> results= service.getSubjectByType(type,(currentPage-1)*8,8);
       return results;
    }

}
