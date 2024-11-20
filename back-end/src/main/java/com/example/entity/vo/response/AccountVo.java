package com.example.entity.vo.response;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class AccountVo {
    @TableId(type = IdType.AUTO)
    Integer uId;
    String tel;
    String email;
    Date registerTime;
    String username;
    String url;//图片地址
}
