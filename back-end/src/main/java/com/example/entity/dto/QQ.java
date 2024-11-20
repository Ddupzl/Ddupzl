package com.example.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("db_qq")
public class QQ {
    @TableId(type = IdType.AUTO)
    Integer id;
    String openId;
    Integer account;
}
