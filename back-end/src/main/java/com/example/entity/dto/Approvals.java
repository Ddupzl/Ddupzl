package com.example.entity.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@TableName("db_approvals")
@AllArgsConstructor
public class Approvals {
    Integer approvalId;
    Integer approvverId;
    Integer result;
    Date approvalTime;
    String suggest;
}
