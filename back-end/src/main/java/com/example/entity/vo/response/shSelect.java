package com.example.entity.vo.response;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.Date;

@Data
public class shSelect {
    Integer applicationId;
    Integer competitionId;
    String competitionName;
    Date applicationTime;
    Integer approvverId;
    Integer result;
    Date approvalTime;
    String suggest;
}
