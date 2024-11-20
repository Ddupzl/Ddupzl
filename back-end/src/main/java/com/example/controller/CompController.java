package com.example.controller;

import com.example.entity.RestBean;
import com.example.entity.dto.*;
import com.example.mapper.CompMapper;
import com.example.mapper.OrganizerMapper;
import com.example.service.AccountService;
import com.example.service.CompService;
import com.example.service.FileService;
import com.example.service.LbtService;
import com.example.utils.JwtUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

@Validated
@RestController
@RequestMapping("/competition")
public class CompController {
    @Resource
    JwtUtils utils;
    @Resource
    CompMapper mapper2;
    @Resource
    OrganizerMapper mapper3;
    @Resource
    CompService service4;
    @Resource
    FileService service;
    @Resource
    LbtService service3;
    @Resource
    AccountService service2;
    private static final String STORE_DIR2 = "C:\\Users\\HP\\Desktop\\SubjectContest\\back-end\\src\\main\\resources\\images\\p\\";
    //竞赛申请，添加竞赛   管理员和教师都可以申请
    @RequestMapping("/insertComp")
    public RestBean insertComp(@RequestBody Comp comp, HttpServletRequest request){
        String token=utils.convertToken(request.getHeader("Authorization"));
        Account account=(Account) service2.getUserInfoFromRedis(token);
        comp.setCategoryId( mapper2.selectCategory(comp.getCategoryName()) ); //查询出竞赛分类的id并写进comp对象中
        if (mapper2.selectOrganizer(comp.getOrganizerName())==null)  //获取竞赛组织信息，如果不存在就在organzier new一个出来并写进comp对象中
        {
            Organizer a=new Organizer(null,comp.getOrganizerName());
            mapper3.insert(a);
            comp.setOrganizerId(a.getOrganizer_id());
        }
        else
            comp.setOrganizerId(mapper2.selectOrganizer(comp.getOrganizerName()));
        Integer a=service4.insertAndGetId(comp); //上传到竞赛表并返回竞赛id
        if (a!=null)
        {
            //还要将申请人id和申请时间放进申请表中
            Application application=new Application(null,account.getUId(),a,new Date());
            Integer b=service4.insertApplication(application); //上传到竞赛申请表并返回申请id
            Integer c=service4.insertApprovals(new Approvals(b,null,null,null,null));//上传到竞赛审核表
            if (c!=null)//上传到竞赛申请表
              return RestBean.success();
            return RestBean.failure(400,"竞赛插入失败");
        }
        return RestBean.failure(400,"竞赛插入失败");
    }
    //上传竞赛图片
    @RequestMapping("/comp/upload")
    public Object uploadCPict(@RequestParam("image") MultipartFile file) {
        try{
            String fileName = UUID.randomUUID().toString().substring(0, 10);
            service.uploadFile(file,STORE_DIR2 + fileName);
            return new PersonalController.Success(fileName);
        }catch (IOException e) {
            // 处理异常，例如返回错误消息
            return e;
        }
    }
    //修改竞赛图片
    @RequestMapping("/comp/update")
    public Object updateCPict(@RequestParam("image") MultipartFile file,@RequestParam("id") Integer id)  {
       try {
           //删除旧图片，添加新图片
           if (service3.deleteFile(mapper2.getUrl(id)))
           {
               String fileName = UUID.randomUUID().toString().substring(0, 10);
               service.uploadFile(file,STORE_DIR2 + fileName);
               return new PersonalController.Success(fileName);
           }
           return RestBean.failure(400,"修改图片失败");
       }catch (IOException e) {
           // 处理异常，例如返回错误消息
           return e;
       }
    }
}

