package com.example.controller;

import com.example.entity.RestBean;
import com.example.entity.dto.Account;
import com.example.entity.dto.Approvals;
import com.example.entity.dto.Comp2;
import com.example.entity.vo.response.Grade;
import com.example.entity.vo.response.*;
import com.example.mapper.*;
import com.example.service.AccountService;
import com.example.service.AdminService;
import com.example.service.CompService;
import com.example.utils.JwtUtils;
import com.example.utils.PageUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Validated
@RestController
@RequestMapping("/admin")
public class AdminController {
    @Resource
    AccountMapper mapper;
    @Resource
    Comp2Mapper mapper2;
    @Resource
    CompMapper mapper3;
    @Resource
    ApprovalMapper approvalMapper;
    @Resource
    CompetitionMapper mapper4;
    @Resource
    JwtUtils utils;
    @Resource
    AccountService service;
    @Resource
    CompService service2;
    @Resource
    AdminService adminService;
    @Resource
    GradeMapper gradeMapper;
    public Boolean Authority(HttpServletRequest request){
        String token=utils.convertToken(request.getHeader("Authorization"));
        Account account=(Account) service.getUserInfoFromRedis(token);
        return "admin".equals(account.getRole());
    }

    @GetMapping("/getTeacherInfo")
    public RestBean getTeacherInfo(HttpServletRequest request,@RequestParam("currentPage") int currentPage){
        if (!Authority(request))
            return RestBean.forbidden("您没有权限访问");
        PageUtils<AccountVo> ab=new PageUtils<>(mapper.getTeacherCount(),7,mapper.getTeacherInfo((currentPage-1)*7,7));
        if (ab.getSize()!=0)
           return  RestBean.success(ab);
        else
            return RestBean.failure(400,"没有查询到用户信息");

    }
    @GetMapping("/getStudentInfo")
    public RestBean getStudentInfo(HttpServletRequest request,@RequestParam("currentPage") int currentPage){
        if (!Authority(request))
            return RestBean.forbidden("您没有权限访问");
        PageUtils<AccountVo> ab=new PageUtils<>(mapper.getStudentCount(),7,mapper.getStudentInfo((currentPage-1)*7,7));
        if (ab.getSize()!=0)
            return  RestBean.success(ab);
        else
            return RestBean.failure(400,"没有查询到用户信息");
    }
    @GetMapping("/getCp")
    public RestBean getCInfo(HttpServletRequest request,@RequestParam("currentPage") int currentPage){
        if (!Authority(request))
            return RestBean.forbidden("您没有权限访问");
        PageUtils<Comp2> ab=new PageUtils<>(mapper2.countCompInfo(),7,mapper2.selectCompInfo((currentPage-1)*7,7));
        if (ab.getSize()!=0)
            return  RestBean.success(ab);
        else
            return RestBean.failure(400,"没有查询到比赛信息");
    }
    @GetMapping("/shCp")
    public RestBean shCInfo(HttpServletRequest request,@RequestParam("currentPage") int currentPage){
        if (!Authority(request))
            return RestBean.forbidden("您没有权限访问");
        PageUtils<Sh> ab=new PageUtils<>(mapper2.countShCompInfo(),7,mapper2.shCompInfo((currentPage-1)*7,7));
        if (ab.getSize()!=0)
            return  RestBean.success(ab);
        else
            return RestBean.failure(400,"没有查询到比赛信息");
    }
    @RequestMapping("/sh")
    public RestBean shComp(HttpServletRequest request, @RequestBody Approvals approvals,@RequestParam("c") int id){
        if (!Authority(request))
            return RestBean.forbidden("您没有权限访问");
        int a=approvalMapper.selectApprovalId(id);
        if (approvalMapper.updateApprovals(a,approvals.getApprovverId(),approvals.getResult(),new Date(),approvals.getSuggest()))
            return RestBean.success();
        return RestBean.failure(400,"审核提交失败");
    }
    @GetMapping("/deleteCp")
    public RestBean deleteCp(@RequestParam("id") Integer id,HttpServletRequest request){
        if (!Authority(request))
            return RestBean.forbidden("您没有权限访问");
        if (mapper3.deleteById(id)==1)
            return  RestBean.success();
        else
            return RestBean.failure(400,"删除失败");
    }
    @RequestMapping("/updateCp")
    public RestBean updateCp(@RequestBody Comp2 comp2,HttpServletRequest request) {
        if (!Authority(request))
            return RestBean.forbidden("您没有权限访问");
       if (service2.updateCp(comp2))
         return RestBean.success("修改信息成功");
       else
           return RestBean.failure(400,"修改信息失败");
    }
    @RequestMapping("/selectBigType")
    public RestBean selectBigTypeBySmall(@RequestParam("s") Integer a,HttpServletRequest request){
        if (!Authority(request))
            return RestBean.forbidden("您没有权限访问");
        return RestBean.success(mapper3.selectBigTypeBySmall(a));
    }
    @RequestMapping("/selectBigType2")
    public RestBean selectBigTypeBySmall2(HttpServletRequest request){
        if (!Authority(request))
            return RestBean.forbidden("您没有权限访问");
        return RestBean.success(mapper4.getSubjectBigType());
    }
    @GetMapping("/export-results")
    public void exportResults(HttpServletResponse response,@RequestParam("id") int id) throws IOException {
        String cname=mapper3.Cname(id); //根据竞赛id查找竞赛名称
        List<GradeExcel> results =gradeMapper.gradeExcel(id); //根据id查找竞赛成绩信息
        adminService.exportGrade(cname,response, results);
    }
    @RequestMapping("/selectGradeComp")
    public RestBean selectGradeComp(HttpServletRequest request,@RequestParam("currentPage") int currentPage){
        if (!Authority(request))
            return RestBean.forbidden("您没有权限访问");
        PageUtils<CompGrade> a=new PageUtils<>(mapper3.countGradeComp(),1,mapper3.selectGradeComp(currentPage-1,1));
        if (mapper3.countGradeComp()!=0)
          return RestBean.success(a);
        else
            return RestBean.failure(400,"查询不到已公布的竞赛成绩");
    }
    @RequestMapping("/selectGrade")
    public RestBean selectGrade(HttpServletRequest request,@RequestParam("cid") int cid){
        if (!Authority(request))
            return RestBean.forbidden("您没有权限访问");
        List<Grade> a=mapper3.grade(cid);
        if (a.size()!=0)
            return RestBean.success(a);
        else
            return RestBean.failure(400,"查询不到已公布的竞赛成绩");
    }
    @GetMapping("/deleteGrade")
    public RestBean deleteGrade(@RequestParam("cid") Integer cid,@RequestParam("uid") Integer uid,HttpServletRequest request){
        if (!Authority(request))
            return RestBean.forbidden("您没有权限访问");
        if (mapper3.deleteGrade(cid,uid)==1)
            return  RestBean.success();
        else
            return RestBean.failure(400,"删除失败");
    }
    @RequestMapping("/updateGrade")
    public RestBean updateGrade(@RequestParam("cid") Integer cid,@RequestBody Grade grade,HttpServletRequest request){
        if (!Authority(request))
            return RestBean.forbidden("您没有权限访问");
        if (mapper3.updateGrade(cid,grade.getUId(),grade.getGrade())==1)
            return  RestBean.success();
        else
            return RestBean.failure(400,"修改失败");
    }
    @RequestMapping("/deleteUser")
    public RestBean deleteUser(@RequestParam("id") Integer uid,HttpServletRequest request){
        if (!Authority(request))
            return RestBean.forbidden("您没有权限访问");
        if (service.deleteUser(uid)>0)
            return RestBean.success();
        return RestBean.failure(400,"删除用户信息失败");
    }
    @RequestMapping("/pldelete")
    public RestBean pldelete(@RequestBody int[] a, HttpServletRequest request){
        if (!Authority(request))
            return RestBean.forbidden("您没有权限访问");
        for (int j : a)
            service.deleteUser(j);
        return RestBean.success();
    }
}
