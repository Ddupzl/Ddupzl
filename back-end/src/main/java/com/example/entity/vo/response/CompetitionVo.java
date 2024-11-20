package com.example.entity.vo.response;

import lombok.Data;


//这是首页的
@Data
public class CompetitionVo {
    String competitionId;
    String competitionName;
    String sponsor;
    String competitionTime;
    String url;
}
