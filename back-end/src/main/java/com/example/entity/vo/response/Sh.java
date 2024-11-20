package com.example.entity.vo.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class Sh {
    Integer competitionId;
    String competitionName;
    Integer categoryId;
    Integer organizerId;
    Date competitionTime;
    Date registrationTimeStart;
    Date registrationTimeEnd;
    Date applicationTime;
    String location;
    String url;
    String description;
    Integer count;

    String subject;
    String  sponsor;
}
