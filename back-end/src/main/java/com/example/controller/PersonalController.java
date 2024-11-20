package com.example.controller;

import com.example.entity.RestBean;
import com.example.entity.dto.*;
import com.example.entity.vo.response.CompGrade;
import com.example.entity.vo.response.Grade;
import com.example.entity.vo.response.StuGrade;
import com.example.entity.vo.response.shSelect;
import com.example.mapper.*;
import com.example.service.AccountService;
import com.example.service.BmService;
import com.example.service.FileService;
import com.example.utils.JwtUtils;
import com.example.utils.PageUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

//有访问权限
@Validated
@RestController
@RequestMapping("/personal")
public class PersonalController {
    @Resource
    JwtUtils utils;
    @Resource
    FileService service;
    @Resource
    AccountService service2;
    @Resource
    BmService service3;
    @Resource
    CompetitionMapper mapper;
    @Resource
    CompMapper compMapper;
    @Resource
    SubmitMapper submitMapper;
    @Resource
    DbGradeMapper dbGradeMapper;
    @Resource
    GradeMapper gradeMapper;
    @Resource
    ApprovalMapper approvalMapper;
    @Resource
    BmMapper bmMapper;
     //文件物理存储位置
    private static final String STORE_DIR = "C:\\Users\\HP\\Desktop\\SubjectContest\\back-end\\src\\main\\resources\\images\\tx\\";
    private static final String STORE_DIR2 = "C:\\Users\\HP\\Desktop\\SubjectContest\\back-end\\src\\main\\resources\\images\\zuoping\\";
    static class Success {
        public final int code;
        public final Object data;
        public Success(String url) {
            this.code= 0;
            HashMap<String, String> map = new HashMap<>();
            map.put("url", url);
            this.data = map;
        }
    }
    public Boolean Authority(HttpServletRequest request){
        String token=utils.convertToken(request.getHeader("Authorization"));
        Account account=(Account) service2.getUserInfoFromRedis(token);
        return "teacher".equals(account.getRole());
    }
    @GetMapping("/getUserInfo")
    public RestBean getUserInfo(HttpServletRequest request){
        String token=utils.convertToken(request.getHeader("Authorization"));
        Account a=(Account) service2.getUserInfoFromRedis(token);
        if (a!=null)
            return RestBean.success(a);
        else
            return RestBean.failure(400,"token已过期，请重新登录");
    }
    @GetMapping("/getRole")
    public String getRole(HttpServletRequest request){
            String token=utils.convertToken(request.getHeader("Authorization"));
            Account a=(Account) service2.getUserInfoFromRedis(token);
            if (a!=null)
              return a.getRole();
            else
                return "token已过期，请重新登录";
    }
    @PostMapping("/updateInfo") //修改用户名,电话
    public RestBean updateInfo(@RequestBody Account account,HttpServletRequest request){
        String token=utils.convertToken(request.getHeader("Authorization"));
        String a=service2.updateInfo(token,account);
        if (Objects.equals(a, "用户名重复，再想想吧"))
            return RestBean.failure(400,a);
        else if (Objects.equals(a, "此邮箱已被其他用户注册"))
            return RestBean.failure(400,a);
         else if (Objects.equals(a, "修改信息时出错了"))
            return RestBean.failure(400,a);
        else
            return RestBean.success();
    }
    /**
     * 上传头像图片
     * @param file
     * @return
     */
    @RequestMapping("/file/upload")
    public Object uploadPict(@RequestParam("image") MultipartFile file,HttpServletRequest request) {
        try{
            String token=utils.convertToken(request.getHeader("Authorization"));
            Account a=(Account) service2.getUserInfoFromRedis(token);
            String fileName = UUID.randomUUID().toString().substring(0, 10);
            if (!Objects.equals(a.getUrl(), "5fa46133-f"))
              service.deleteFile(a.getUrl()); //先删除以前的头像图片
            service2.updateTx(a.getUId(),fileName);//根据id修改数据库中的头像地址
            service2.updateUrlInRedis(token,fileName);//根据token修改redis中的头像地址
            service.uploadFile(file,STORE_DIR + fileName);
            return new Success(fileName);
        }catch (IOException e) {
            // 处理异常，例如返回错误消息
            return e;
        }
    }
    /**
     * 图片下载
     * @param response
     */
    @GetMapping("/file/download")
    public void download(HttpServletRequest request, HttpServletResponse response) {
        String token=utils.convertToken(request.getHeader("Authorization"));
        Account a=(Account) service2.getUserInfoFromRedis(token);
        // 获取真实的文件路径
        String filePath = STORE_DIR + a.getUrl();
        service.downloadFile(filePath,response,a.getUrl());
    }
    @RequestMapping("/bmInfo")
    public RestBean bmInfo(@RequestParam("uid") Integer uid){
           List<Register> a=service3.bmInfo(uid);
           for (int i=0;i<a.size();i++)
           {
               Register register= a.get(i);
               register.setZpUrl(submitMapper.selectZpUrl(register.getCompetitionId(),uid));
               register.setCName(mapper.getDetails(register.getCompetitionId()).getCompetitionName());
               register.setHidden(true);
           }
        System.out.println(a);
           if (!a.isEmpty())
               return RestBean.success(a);
           return RestBean.failure(400,"查询不到报名信息");
    }
    //学生作品文件上传
    @RequestMapping("/zpUpload")
    public Object zpUploadt(@RequestParam("file") MultipartFile file,@RequestParam("cid") Integer cid,@RequestParam("uid") Integer uid) {
        try{
            //做一个判断该用户的该竞赛上传了作品之后不能再上传了
            if (submitMapper.selectZp(cid,uid)!=null)
                return RestBean.failure(400,"该用户已上传竞赛作品");
            String fileName = UUID.randomUUID().toString().substring(0, 10)+file.getOriginalFilename();
          if (service.uploadFile(file,STORE_DIR2 + fileName)!=null)
          {
              Submit a=new Submit(null,uid,cid,fileName);
              if (submitMapper.insert(a)==1)
                return new Success(fileName);
          }
            return RestBean.failure(400,"上传作品失败");
        }catch (IOException e) {
            return e;
        }
    }
    //学生作品下载
    @GetMapping("/zpDownload/{filename}")
    public void zpDownload(@PathVariable("filename") String filename, HttpServletResponse response) {
        String filePath = STORE_DIR2 + filename;
        System.out.println(filePath);
        service.downloadFile(filePath,response,filename);
    }
    //根据uid查找教师的竞赛
    @RequestMapping("/selectGradeComp")
    public RestBean selectGradeComp(HttpServletRequest request,@RequestParam("currentPage") int currentPage,
                                    @RequestParam("uid") int uid){
        if (!Authority(request))
            return RestBean.forbidden("您没有权限访问");
        if (compMapper.countGradeCompByUid(uid)==0)
            return RestBean.failure(400,"查询不到您已发布的竞赛");
        List<CompGrade> compGrades=compMapper.selectGradeCompByUid(uid,currentPage-1,1);
        for (int i=0;i<compGrades.size();i++)
            compGrades.get(i).setCount(bmMapper.countRegister(compGrades.get(i).getCompetitionId()));
        PageUtils<CompGrade> a=new PageUtils<>(compMapper.countGradeCompByUid(uid),1,compGrades);
        return RestBean.success(a);
    }
    //根据cid查找竞赛学生成绩
    @RequestMapping("/selectGrade")
    public RestBean selectGrade(HttpServletRequest request,@RequestParam("cid") int cid){
        if (!Authority(request))
            return RestBean.forbidden("您没有权限访问");
        List<Grade> a=compMapper.djGrade(cid);
        if (!a.isEmpty())
            return RestBean.success(a);
        else
            return RestBean.failure(400,"查询不到参加竞赛的学生");
    }
    // 先检查发过来的数据是否为空，如果为空直接返回false.然后根据竞赛id查询award_time字段为空吗，看发布没有。发布了就提示竞赛成绩已经发布，不可以重复发布。
    @RequestMapping("/submitGrade")  //教师发布竞赛成绩
    public RestBean submitGrade(HttpServletRequest request,@RequestBody List<Dbgrade> list,@RequestParam("cid") Integer cid)
    {
        if (!Authority(request))
            return RestBean.forbidden("您没有权限访问");
        if (compMapper.award(cid)!=null)
            return RestBean.failure(400,"竞赛成绩已发布，请勿重复操作");
        for (Dbgrade grade : list)
        {
            grade.setCId(cid);
            dbGradeMapper.insert(grade);
        }
        compMapper.setAward(new Date(),cid);
        return RestBean.success();
    }
    @RequestMapping("/shSelect")  //教师发布竞赛成绩
    public RestBean shSelect(HttpServletRequest request)
    {
        if (!Authority(request))
            return RestBean.forbidden("您没有权限访问");
        String token=utils.convertToken(request.getHeader("Authorization"));
        Account account=(Account) service2.getUserInfoFromRedis(token);
        List<shSelect> list =approvalMapper.shSelect(account.getUId());
        if (list.isEmpty())
            return RestBean.failure(400,"查询不到审核信息");
        return RestBean.success(list);
    }
    @RequestMapping("/getGrade")  //教师发布竞赛成绩
    public RestBean getGrade(HttpServletRequest request)
    {
        String token=utils.convertToken(request.getHeader("Authorization"));
        Account account=(Account) service2.getUserInfoFromRedis(token);
        List<StuGrade> a=gradeMapper.getGrade(account.getUId());
        if (!a.isEmpty())
           return RestBean.success(a);
        return RestBean.failure(400,"查询不到用户成绩");
    }
}
