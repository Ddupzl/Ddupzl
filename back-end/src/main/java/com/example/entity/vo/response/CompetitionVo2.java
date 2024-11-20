package com.example.entity.vo.response;

import lombok.Data;

//这是竞赛页面的
@Data
public class CompetitionVo2 {
    String competitionId;
    String competitionName;
    String sponsor;
    String registrationTimeStart;
    String registrationTimeEnd;
    String competitionTime;
}
