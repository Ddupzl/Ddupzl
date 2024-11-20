package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.dto.Submit;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SubmitMapper extends BaseMapper<Submit> {
    @Select("select * from db_submit where competition_id=#{cid} and user_id=#{uid}")
    Submit selectZp(Integer cid,Integer uid);
    @Select("select zp_url from db_submit where competition_id=#{cid} and user_id=#{uid}")
    String selectZpUrl(Integer cid,Integer uid);
}
