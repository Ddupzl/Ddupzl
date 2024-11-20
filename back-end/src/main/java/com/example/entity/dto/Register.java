package com.example.entity.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.util.Date;


@Data
@TableName("db_registrations")
public class Register {
    Integer registrationId;
    String name;
    String school;
    String dep;
    String email;
    String tel;
    Integer competitionId;
    Date registrationTime;
    Integer uid;
    @TableField(exist = false)
    String cName;
    @TableField(exist = false)
    Boolean hidden;
    @TableField(exist = false)
    String zpUrl;
}
