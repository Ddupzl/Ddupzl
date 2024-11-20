package com.example.entity.vo.response;

import lombok.Data;

import java.util.Date;

@Data
public class CompGrade {
    Integer competitionId;
    String competitionName;
    String subject;
    Date competitionTime;
    Date awardTime;
    Integer count;
}
