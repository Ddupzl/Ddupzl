package com.example.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@TableName("db_grade")
public class Dbgrade {
    @TableId(type = IdType.AUTO)
    Integer id;
    Integer grade;
    Integer cId;
    Integer uId;
}
