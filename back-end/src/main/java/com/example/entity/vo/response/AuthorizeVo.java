package com.example.entity.vo.response;

import lombok.Data;
import java.util.Date;

//这是响应给前端的信息类
@Data
public class AuthorizeVo {
//    String username;
//    String role;
    String token;
//    Date expire;
//    String url;//图片地址
    int id;//用户id
}
