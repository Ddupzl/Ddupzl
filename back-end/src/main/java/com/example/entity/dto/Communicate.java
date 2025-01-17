package com.example.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@TableName("db_comments")
public class Communicate implements Serializable {
    @TableId(type = IdType.AUTO)
    Integer commentId;
    Integer postId;
    Integer userId;
    Integer replyId;
    Integer pid;
    String content;
    Date time;

    @TableField(exist = false)
    String replyUsername;
    @TableField(exist = false)
    String url;
    @TableField(exist = false)
    String username;
    @TableField(exist = false)
    private List<Communicate> children;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
