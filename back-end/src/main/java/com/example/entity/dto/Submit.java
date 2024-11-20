package com.example.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@TableName("db_submit")
@AllArgsConstructor
public class Submit {
    @TableId(type = IdType.AUTO)
    Integer submitId;
    Integer userId;
    Integer competitionId;
    String zpUrl;
}
