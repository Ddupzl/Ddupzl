package com.example.entity.vo.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

//竞赛页面查询类别
@Data
@AllArgsConstructor
public class SubjectType {
    String bigType;
    List<String> smallType;
}
