package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.dto.Comp;
import com.example.entity.dto.Comp2;
import com.example.entity.vo.response.CompetitionVo;
import com.example.entity.vo.response.CompetitionVo2;
import com.example.utils.PageUtils;
import java.util.List;

public interface CompetitionService extends IService<CompetitionVo> {
    //查找热门比赛信息
    List<CompetitionVo> getCompetitionHot(int from,int to);
    //查找热门比赛信息数量
    int getCompetitionCount();
    //查找最新比赛信息
    List<CompetitionVo> getCompetitionLatest(int from,int to);
    List<CompetitionVo2> getCompetitionLatest2(int from,int to);
    List<CompetitionVo2> getCompetitionLatest3(int from,int to);
    //查找最新比赛信息总数
    int getLatestCount();
    int getLatestCount2();
    List<CompetitionVo2> getAllClassify(int from, int to);
    int getAllCountClassify();
    //竞赛大类别
    List<String> getSubjectBigType();
    //根据大类别寻找竞赛小类别
    List<String> getSubjectSmallType(String bigType);

    //根据 类别(无论大小) 查找比赛信息
    PageUtils<CompetitionVo2> getSubjectByType(String type, int from, int to);

}
