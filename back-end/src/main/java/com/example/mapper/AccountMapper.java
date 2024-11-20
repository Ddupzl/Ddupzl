package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.dto.Account;
import com.example.entity.dto.QQ;
import com.example.entity.vo.response.AccountVo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface AccountMapper extends BaseMapper<Account> {
    @Update("update db_account set url=#{url} where u_id=#{id}")
    boolean updateTx(int id,String url);//更改用户头像路径
    @Update("UPDATE db_account SET username=#{username},email=#{email},tel=#{tel} WHERE u_id=#{id} ")//修改用户名和电话
    boolean updateInfo(String username,String email,String tel,int id);
    @Select("select count(*) from db_account where role='teacher'")
    int getTeacherCount();
    @Select("select count(*) from db_account where role='student'")
    int getStudentCount();
    @Select("select u_id,tel,email,register_time,username,url from db_account where role='teacher' limit #{from},#{to}")
    List<AccountVo> getTeacherInfo(int from,int to);
    @Select("select u_id,tel,email,register_time,username,url from db_account where role='student' limit #{from},#{to} ")
    List<AccountVo> getStudentInfo(int from,int to);
    @Delete("delete from db_account where u_id=#{uid}")
    int deleteUser(Integer uid);
    @Select("select account from db_account where openId= #{openId}")
    Integer selectAccount(String openId);
    @Select("select * from where u_id=#{uid}")
    Account getInfo(Integer uid);

}
