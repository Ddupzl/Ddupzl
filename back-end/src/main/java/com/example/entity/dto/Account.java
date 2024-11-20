package com.example.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@TableName("db_account")
//@AllArgsConstructor
public class Account {
        @TableId(type = IdType.AUTO)
        Integer uId;
        String tel;
        String email;
        String password;
        String role;
//       mybatis plus会把registerTime映射成register_time，会把大写映射成"_"
        Date registerTime;
        String username;
        String url;//图片地址
        // 使用@JsonCreator注解标记构造器
        @JsonCreator
        public Account(@JsonProperty("uId") Integer uId, @JsonProperty("tel") String tel,@JsonProperty("email") String email,
                       @JsonProperty("password") String password,@JsonProperty("role") String role, @JsonProperty("registerTime") Date registerTime,
                       @JsonProperty("username") String username,@JsonProperty("url") String url) {
                this.uId = uId;
                this.tel = tel;
                this.email = email;
                this.password = password;
                this.role = role;
                this.registerTime = registerTime;
                this.username = username;
                this.url = url;
        }
}
