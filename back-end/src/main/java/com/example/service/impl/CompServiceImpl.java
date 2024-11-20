package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.dto.*;
import com.example.mapper.ApplicationsMapper;
import com.example.mapper.ApprovalMapper;
import com.example.mapper.CompMapper;
import com.example.mapper.OrganizerMapper;
import com.example.service.CompService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class CompServiceImpl extends ServiceImpl<CompMapper, Comp> implements CompService {
    @Resource
    CompMapper mapper;
    @Resource
    ApplicationsMapper mapper2;
    @Resource
    ApprovalMapper mapper3;
    @Resource
    OrganizerMapper mapper4;
    //修改竞赛信息
    @Override
    public Boolean updateCp(Comp2 comp2) {
        Integer a=mapper.selectCategory(comp2.getSubject());
        Integer b=mapper.selectOrganizer(comp2.getSponsor());
        if (b==null)
        {
            Organizer organizer=new Organizer(null,comp2.getSponsor());
            mapper4.insert(organizer);
            comp2.setOrganizerId(organizer.getOrganizer_id());
            b=comp2.getOrganizerId();
        }
        Comp comp=new Comp(comp2.getCompetitionId(),comp2.getCompetitionName(),a,b,comp2.getPublishTime(),comp2.getCompetitionTime(),
                comp2.getRegistrationTimeStart(),comp2.getRegistrationTimeEnd(),comp2.getLocation(),comp2.getStatus(),comp2.getIsHot(),
                comp2.getAwardTime(),comp2.getUrl(),comp2.getDescription(),comp2.getCount(),null,null);
        return mapper.updateById(comp) > 0;
    }
    //添加到竞赛表里
    public Integer insertAndGetId(Comp comp){
        mapper.insert(comp);
        return comp.getCompetitionId();
    }
    //添加到竞赛申请表里
    public Integer insertApplication(Application application)
    {
        mapper2.insert(application);
        return application.getApplicationId();
    }
    //添加到竞赛审核表里
    public Integer insertApprovals(Approvals approvals)
    {
       return mapper3.insert(approvals);
    }
}
