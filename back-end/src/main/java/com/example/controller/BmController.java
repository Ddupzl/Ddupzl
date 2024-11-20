package com.example.controller;

import com.example.entity.RestBean;
import com.example.entity.dto.Register;
import com.example.mapper.BmMapper;
import com.example.mapper.CompMapper;
import com.example.service.BmService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/bm")
public class BmController {
    @Resource
    BmService service;
    @Resource
    BmMapper bmMapper;
    @Resource
    CompMapper compMapper;

    @RequestMapping("/addRegister")
    public RestBean addRegister(@RequestBody Register register) {
        int cid=register.getCompetitionId();
        if (compMapper.countComp(cid)<=bmMapper.countRegister(cid))
            return RestBean.failure(400,"报名学生已满，再看看其他竞赛吧！");
        //根据传过来的uid和name去找有没有和传过来的cid相同的，如果有就已经报名.还需判断报名人数是否超过指定人数
        List<Integer> a=bmMapper.selectCidByName(register.getName());
        List<Integer> b=bmMapper.selectCidByUid(register.getUid());
        Integer c=register.getCompetitionId();
        if (a.contains(c)||b.contains(c))
            return RestBean.failure(400,"该用户或姓名已经报名了");
        //不然就增加到报名表中
        register.setRegistrationTime(new Date());
        if (service.addRegister(register))
            return RestBean.success();
        else
            return RestBean.failure(400,"报名失败");
        }


}

