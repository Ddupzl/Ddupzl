package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.dto.Comp;
import com.example.entity.vo.response.CompGrade;
import com.example.entity.vo.response.Grade;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Date;
import java.util.List;

@Mapper
public interface CompMapper extends BaseMapper<Comp>{
    @Select("SELECT COUNT FROM db_competition WHERE competition_id=#{cid} ")
    Integer countComp(Integer cid);
    @Select("select competition_name from db_competition where competition_id=#{cid}")
    String Cname(Integer cid);
    @Select("SELECT  category_id FROM db_category WHERE SUBJECT=#{sub}")
    Integer selectCategory(String sub);
    @Select("SELECT  organizer_id FROM db_organizer WHERE sponsor=#{sponsor}")
    Integer selectOrganizer(String sponsor);
    @Select("SELECT family FROM db_bigcategory WHERE id=(SELECT bigtype FROM db_category WHERE category_id=#{id})")
    String selectBigTypeBySmall(Integer id);
    @Select("select url from db_competition where competition_id=#{id}")
    String getUrl(Integer id);
    @Select("SELECT a.`competition_id`,a.`competition_name`,b.`subject`,a.`competition_time`,a.`award_time` FROM db_competition AS a,db_category  AS b WHERE a.category_id=b.category_id AND a.award_time IS NOT NULL limit #{from},#{size}")
    List<CompGrade> selectGradeComp(int from, int size);
    @Select("select count(*) from db_competition where award_time IS NOT NULL")
    Integer countGradeComp();
    @Select("SELECT b.`name`,a.`grade`,a.`u_id`,c.`zp_url` FROM db_grade a LEFT JOIN db_registrations b ON a.`u_id`=b.`uid` AND a.`c_id`=b.`competition_id` \n" +
            "LEFT JOIN db_submit c ON c.`user_id`=a.`u_id` AND c.`competition_id`=a.`c_id` WHERE a.c_id=#{cid}")
    List<Grade> grade(Integer cid);
    @Delete("delete from db_grade where c_id=#{cid} and u_id=#{uid}")
    int deleteGrade(Integer cid,Integer uid);
    @Update("update db_grade set grade=#{grade} where c_id=#{cid} and u_id=#{uid} ")
    int updateGrade(Integer cid,Integer uid,Integer grade);
    @Select("SELECT a.competition_id,a.competition_name,a.competition_time,b.subject FROM db_competition a JOIN db_category b ON a.category_id=b.category_id\n" +
            "WHERE a.competition_id IN (SELECT competition_id FROM db_applications e JOIN db_approvals f ON e.application_id=f.approval_id WHERE result=1 AND teacher_id=#{uid}) LIMIT #{from},#{size}")
    List<CompGrade> selectGradeCompByUid(int uid,int from, int size);
    @Select("SELECT COUNT(*) FROM db_applications e JOIN db_approvals f ON e.application_id=f.approval_id WHERE result=1 AND teacher_id=#{uid}")
    Integer countGradeCompByUid(int uid); //因为正规的都是申请竞赛，所以直接在竞赛申请表中查询。
    @Select("SELECT b.uid,b.name,a.zp_url,c.grade FROM db_registrations b LEFT JOIN db_submit a ON a.user_id=b.uid AND b.competition_id= a.competition_id LEFT JOIN db_grade c ON c.u_id=b.uid AND b.competition_id=c.c_id WHERE b.competition_id=#{cid} ")
    List<Grade> djGrade (int cid);
    @Select("select award_time from db_competition where competition_id=#{cid}")
    Date award(Integer cid);
    @Update("update db_competition set award_time=#{time} where competition_id=#{cid}")
    Integer setAward(Date time,Integer cid);
}
