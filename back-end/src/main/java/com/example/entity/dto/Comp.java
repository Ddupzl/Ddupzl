package com.example.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Data
@AllArgsConstructor
@TableName("db_competition")
public class Comp {
    @TableId(type = IdType.AUTO)
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
    @TableField("url")
    String url2;
    String description;
    Integer count;

    @TableField(exist = false)
    String categoryName;
    @TableField(exist = false)
    String organizerName;
}
