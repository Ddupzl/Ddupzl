package com.example.entity.vo.response;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class Grade {
    Integer uId;
    String name;
    Integer grade;
    String zpUrl;
}
