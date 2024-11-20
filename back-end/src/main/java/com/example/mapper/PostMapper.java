package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.dto.Post;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

@Mapper
public interface PostMapper extends BaseMapper<Post> {

    @Select("SELECT post_id,username,url,title,content,post_date FROM db_post a,db_account b WHERE a.`user_id`=b.`u_id` order by post_date desc limit #{from},#{to}")
    List<Post> postList(int from,int to);
    @Select("select title,content,post_date from db_post where post_id=#{id}")
    Post postInfo(int id);
//    @Select("CALL AddNewPost(#{userId}, #{title}, #{content}, #{date})")
//    Integer posts(Integer userId, String title, String content, Date date);
@Select("insert into db_post(user_id,title,content,post_date) values(#{userId}, #{title}, #{content}, #{date})")
Integer posts(Integer userId, String title, String content, Date date);
}
