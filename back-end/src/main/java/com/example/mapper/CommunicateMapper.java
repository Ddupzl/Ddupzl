package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.dto.Communicate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CommunicateMapper extends BaseMapper<Communicate> {
    @Select("select comment_id,user_id,reply_id,pid,content,time from db_comments where post_id=#{id} order by time desc")
    List<Communicate> selectCommentsByPostId(int id);
}
