package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.dto.Application;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Date;

@Mapper
public interface ApplicationsMapper extends BaseMapper<Application> {
//    @Insert("insert into db_applications(teacher_id,competition_id,application_time) values (#{tid},#{cid},#{time}) ")
//    Integer insertApplication(Integer tid, Integer cid, Date time);
}
