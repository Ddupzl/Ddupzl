package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.dto.Lbt;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface LbtMapper extends BaseMapper<Lbt> {
    @Delete("delete from db_lbt where image=#{name}")
    Integer deleteP(String name);

}
