package com.example.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class Comp2 {
    Integer competitionId;
    String competitionName;
    Integer categoryId;
    Integer organizerId;
    Date publishTime;
    Date competitionTime;
    Date registrationTimeStart;
    Date registrationTimeEnd;
    String location;
    String status;
    String isHot;
    Date awardTime;
    String url;
    String description;
    Integer count;

    String subject;
    String  sponsor;
}
