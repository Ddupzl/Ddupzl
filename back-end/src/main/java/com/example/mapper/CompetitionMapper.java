package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.vo.response.Competition;
import com.example.entity.vo.response.CompetitionVo;
import com.example.entity.vo.response.CompetitionVo2;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CompetitionMapper extends BaseMapper<CompetitionVo> {
    //根据id查找比赛的详细信息
    @Select("SELECT competition_name,sponsor,competition_time,registration_time_start,registration_time_end,location,url,description FROM db_competition a,db_organizer b WHERE a.`organizer_id`=b.`organizer_id` AND competition_id=#{id}")
    Competition getDetails(int id);
    //查找热门比赛信息
    @Select("SELECT competition_id,competition_name,sponsor,competition_time,url FROM db_competition a,db_organizer b WHERE is_hot=1 and a.`organizer_id`=b.`organizer_id` and status=1 limit #{from},#{to}")
    List<CompetitionVo> getCompetitionHot(int from,int to);
    //热门比赛信息总数
    @Select("SELECT count(*) FROM db_competition WHERE is_hot=1 and status=1 ")
    int getCompetitionCount();
    //查找最新比赛信息
    @Select("SELECT competition_id,competition_name,sponsor,competition_time,url FROM db_competition a,db_organizer b WHERE a.`organizer_id`=b.`organizer_id` and status=1 order by publish_time desc limit #{from},#{to}")
    List<CompetitionVo> getCompetitionLatest(int from,int to);
    //查找最新(按发布时间)比赛信息总数
    @Select("SELECT count(*) FROM db_competition where status=1 order by publish_time asc")
    int getLatestCount();
    //查找最新(按比赛时间)比赛信息总数
    @Select("SELECT count(*) FROM db_competition where status=1 order by competition_time asc")
    int getLatestCount2();
    //查找最新(按发布时间)比赛信息
    @Select("SELECT  competition_id,competition_name,sponsor,registration_time_start,registration_time_end,competition_time FROM db_competition a,db_organizer b WHERE a.`organizer_id`=b.`organizer_id` and status=1 order by publish_time desc limit #{from},#{to}")
    List<CompetitionVo2> getCompetitionLatest2(int from,int to);
    //查找最新(按比赛时间)比赛信息
    @Select("SELECT  competition_id,competition_name,sponsor,registration_time_start,registration_time_end,competition_time FROM db_competition a,db_organizer b WHERE a. `organizer_id`=b.`organizer_id` and status=1 order by competition_time desc limit #{from},#{to}")
    List<CompetitionVo2> getCompetitionLatest3(int from,int to);



    //查找全部比赛信息
    @Select("SELECT  competition_id,competition_name,sponsor,registration_time_start,registration_time_end,competition_time FROM db_competition a,db_organizer b WHERE a.`organizer_id`=b.`organizer_id` and status=1 limit #{from},#{to}")
    List<CompetitionVo2> getAllClassify(int from,int to);
    //查找全部比赛信息总数
    @Select("SELECT  count(*) FROM db_competition where status=1")
    int getAllCountClassify();
    //查找 竞赛大类别
    @Select("SELECT family FROM db_bigcategory")
    List<String> getSubjectBigType();
    //根据大类别寻找 竞赛小类别
    @Select("SELECT SUBJECT FROM db_category WHERE bigtype = (SELECT id FROM db_bigcategory WHERE family=#{bigType}) ")
    List<String> getSubjectSmallType(String bigType);

    //根据 小类别 查找比赛信息总数
    @Select("SELECT  count(*) FROM db_competition a WHERE status=1 and a.`category_id`=(SELECT category_id FROM db_category WHERE SUBJECT=#{type})" )
    int getCountBySmallType(String type);
    //根据 小类别 查找比赛信息
    @Select("SELECT  competition_id,competition_name,sponsor,registration_time_start,registration_time_end,competition_time FROM db_competition a,db_organizer b WHERE status=1 and a.`organizer_id`=b.`organizer_id` AND a.`category_id`=(SELECT category_id FROM db_category WHERE SUBJECT=#{type}) limit #{from},#{to}")
    List<CompetitionVo2> getSubjectBySmallType(String type,int from,int to);

    //根据 大类别 查找比赛信息总数
    @Select("SELECT COUNT(*) FROM db_competition a WHERE status=1 and a.`category_id` IN (SELECT category_id FROM db_category WHERE bigtype=(SELECT id FROM db_bigcategory WHERE family=#{type}))")
    int getCountByBigType(String type);
    //根据 大类别 查找比赛信息
    @Select("SELECT  competition_id,competition_name,sponsor,registration_time_start,registration_time_end,competition_time FROM db_competition a,db_organizer b WHERE status=1 and a.`organizer_id`=b.`organizer_id`AND a.`category_id` IN(SELECT category_id FROM db_category WHERE bigtype=(SELECT id FROM db_bigcategory WHERE family=#{type})) limit #{from},#{to} ")
    List<CompetitionVo2> getSubjectByBigType(String type, int from, int to);

}

