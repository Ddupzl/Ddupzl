package com.example.entity.vo.request;

import lombok.Data;

import java.util.Date;

@Data
public class PostRes {
    Integer userId;
    String  title;
    String content;
    Date postDate;
}
