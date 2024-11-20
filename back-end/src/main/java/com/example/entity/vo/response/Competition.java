package com.example.entity.vo.response;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
public class Competition {
    String competitionName;
    String sponsor;
    String competitionTime;
    String registrationTimeStart;
    String registrationTimeEnd;
    String location;
    String url;
    String description;
}
