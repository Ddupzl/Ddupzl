package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.dto.Dbgrade;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DbGradeMapper extends BaseMapper<Dbgrade> {
}
