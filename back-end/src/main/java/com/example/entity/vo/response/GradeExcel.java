package com.example.entity.vo.response;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class GradeExcel {
    @ExcelProperty("姓名")
    private String name;
    @ExcelProperty("成绩")
    private Integer grade;
}
