package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.dto.Approvals;
import com.example.entity.vo.response.shSelect;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Date;
import java.util.List;

@Mapper
public interface ApprovalMapper extends BaseMapper<Approvals> {
    @Select("select application_id from db_applications where competition_id=#{id} ")
    Integer selectApprovalId(int id);
    @Update("update db_approvals set approvver_id=#{uid},result=#{result},approval_time=#{time},suggest=#{suggest} where approval_id=#{aid} ")
    Boolean updateApprovals(int aid, int uid, int result, Date time,String suggest);
    @Select("SELECT a.*,b.*,c.competition_name\n" +
            "FROM db_applications a  \n" +
            "LEFT JOIN db_approvals b ON a.application_id = b.approval_id JOIN db_competition c ON c.competition_id=a.competition_id  WHERE teacher_id=#{uid};")
    List<shSelect> shSelect(Integer uid);
}
