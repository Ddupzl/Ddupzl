package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.dto.Comp;
import com.example.entity.dto.Comp2;
import com.example.entity.vo.response.Sh;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface Comp2Mapper extends BaseMapper<Comp2>{
    @Select("SELECT a.*,b.`sponsor`,c.`subject` FROM db_competition a JOIN db_organizer b ON a.`organizer_id`=b.`organizer_id` JOIN  db_category c ON a.`category_id`=c.`category_id` order by competition_id asc limit #{from},#{to}")
    List<Comp2> selectCompInfo(int from,int to);
    @Select("SELECT a.*,b.`sponsor`,c.`subject`,d.`application_time` FROM db_competition a JOIN db_organizer b ON a.`organizer_id`=b.`organizer_id` JOIN  db_category c ON a.`category_id`=c.`category_id` JOIN db_applications d ON d.`competition_id`=a.`competition_id` where status=0 order by competition_id asc limit #{from},#{to}")
    List<Sh> shCompInfo(int from, int to);
    @Select("SELECT count(*) FROM db_competition")
    int countCompInfo();
    @Select("SELECT count(*) FROM db_competition where status=0")
    int countShCompInfo();

}
