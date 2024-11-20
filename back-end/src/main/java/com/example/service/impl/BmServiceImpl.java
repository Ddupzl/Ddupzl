package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.dto.Register;
import com.example.entity.vo.response.CompetitionTz;
import com.example.mapper.BmMapper;
import com.example.service.BmService;
import jakarta.annotation.Resource;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

@Service
public class BmServiceImpl extends ServiceImpl<BmMapper, Register> implements BmService {
    @Resource
    BmMapper mapper;
    @Resource
    AmqpTemplate amqpTemplate;
    @Resource
    private JdbcTemplate jdbcTemplate;

    @Override
    public boolean addRegister(Register register) {
        return this.save(register);
    }

    @Override
    public List<Register> bmInfo(Integer uid) {
        QueryWrapper<Register> queryWrapper = Wrappers.query();
        queryWrapper.eq("uid", uid);
        return mapper.selectList(queryWrapper);
    }
    // 查询今天比赛的用户邮箱并发送消息提醒
    public void checkTodayCompetitions() {
        System.out.println(mapper.checkTodayCompetitions());
        List<CompetitionTz> list=mapper.checkTodayCompetitions();
        if (!list.isEmpty())
        {
            for (CompetitionTz a : list) {
                Map<String, Object> data = Map.of("type", "remind", "email", a.getEmail(),"info",a.getCompetitionName(),"info2",a.getCompetitionTime(),"info3",a.getLocation());
                amqpTemplate.convertAndSend("mail", data);
            }
        }
    }

}
