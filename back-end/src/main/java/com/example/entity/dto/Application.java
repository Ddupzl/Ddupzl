package com.example.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@TableName("db_applications")
@AllArgsConstructor
public class Application {
       @TableId(type = IdType.AUTO)
       Integer applicationId;
       Integer teacherId;
       Integer competitionId;
       Date applicationTime;
}
