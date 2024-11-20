package com.example.entity.vo.response;

import lombok.Data;

import java.util.Date;

@Data
public class CompetitionTz {
    private String email;
    private String competitionTime; // 假设你使用java.time.LocalDate
    private String competitionName;
    private String location;
}
