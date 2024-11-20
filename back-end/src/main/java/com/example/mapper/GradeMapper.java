package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.vo.response.Grade;
import com.example.entity.vo.response.GradeExcel;
import com.example.entity.vo.response.StuGrade;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface GradeMapper extends BaseMapper<Grade>{
    @Select("SELECT b.`name`,a.`grade`FROM db_grade a JOIN db_registrations b ON a.`u_id`=b.`uid` AND a.`c_id`=b.`competition_id` WHERE a.c_id=#{cid}")
    List<GradeExcel> gradeExcel(Integer cid);
    @Select("SELECT competition_name,competition_time,grade FROM db_competition a LEFT JOIN db_grade b ON a.`competition_id`=b.`c_id` WHERE b.`u_id`=#{uid}")
    List<StuGrade> getGrade(Integer uid);
}
