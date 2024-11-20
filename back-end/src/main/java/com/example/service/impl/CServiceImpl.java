package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.dto.Comp;
import com.example.entity.dto.Comp2;
import com.example.entity.vo.response.CompetitionVo;
import com.example.entity.vo.response.CompetitionVo2;
import com.example.mapper.Comp2Mapper;
import com.example.mapper.CompMapper;
import com.example.mapper.CompetitionMapper;
import com.example.service.CompetitionService;
import com.example.utils.PageUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CServiceImpl extends ServiceImpl<CompetitionMapper, CompetitionVo> implements CompetitionService {
    @Resource
    CompetitionMapper mapper;

    @Override
    public List<CompetitionVo> getCompetitionHot(int from,int to) {
        return mapper.getCompetitionHot(from,to);
    }
    @Override
    public int getCompetitionCount() {
        return mapper.getCompetitionCount();
    }

    @Override
    public List<CompetitionVo> getCompetitionLatest(int from, int to) {
        return mapper.getCompetitionLatest(from,to);
    }

    @Override
    public List<CompetitionVo2> getCompetitionLatest2(int from, int to) {
        return mapper.getCompetitionLatest2(from, to);
    }

    @Override
    public List<CompetitionVo2> getCompetitionLatest3(int from, int to) {
        return mapper.getCompetitionLatest3(from, to);
    }

    @Override
    public int getLatestCount() {
        return mapper.getLatestCount();
    }

    @Override
    public int getLatestCount2() {
        return mapper.getLatestCount2();
    }

    @Override
    public List<CompetitionVo2> getAllClassify(int from, int to) {
        return mapper.getAllClassify(from,to);
    }

    @Override
    public int getAllCountClassify() {
        return mapper.getAllCountClassify();
    }

    @Override
    public List<String> getSubjectBigType() {
        return mapper.getSubjectBigType();
    }

    @Override
    public List<String> getSubjectSmallType(String bigType) {
        return mapper.getSubjectSmallType(bigType);
    }

    //根据 大/小类别 查找比赛信息
    @Override
    public PageUtils<CompetitionVo2> getSubjectByType(String type, int from, int to) {
        List<String> a=mapper.getSubjectBigType();
        if (a.contains(type))
            return new PageUtils<>(mapper.getCountByBigType(type), 8, mapper.getSubjectByBigType(type, from, to));

        else
            return new PageUtils<>(mapper.getCountBySmallType(type), 8, mapper.getSubjectBySmallType(type, from, to));
    }

}
