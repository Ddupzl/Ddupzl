package com.example.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.util.Date;

@Data
@TableName("db_post")
public class Post {
    @TableId(type = IdType.AUTO)
    Integer postId;
    Integer userId;
    String  title;
    String content;
    Date postDate;
    @TableField(exist = false)
    String username;  //发布帖子的人
    @TableField(exist = false)
    String url;  //发布帖子的人的头像

}
