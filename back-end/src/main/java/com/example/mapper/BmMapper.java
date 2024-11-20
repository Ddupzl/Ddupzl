package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.dto.Register;
import com.example.entity.vo.response.CompetitionTz;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface BmMapper extends BaseMapper<Register> {
    @Select("select competition_id from db_registrations where name=#{name}")
    List<Integer> selectCidByName(String name);
    @Select("select competition_id from db_registrations where uid=#{uid}")
    List<Integer> selectCidByUid(Integer uid);
    @Select("SELECT c.location,r.email, c.competition_time,c.competition_name FROM db_registrations r JOIN db_competition c ON r.competition_id = c.competition_id WHERE DATE(c.competition_time) = CURDATE()")
    List<CompetitionTz> checkTodayCompetitions();
    @Select("SELECT GetRegistrationCountForCompetition(#{cid})")
    Integer countRegister(Integer cid);
}
