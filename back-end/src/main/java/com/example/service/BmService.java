package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.dto.Register;


import java.util.List;

public interface BmService extends IService<Register> {
    boolean addRegister(Register register);
    List<Register> bmInfo(Integer uid);
    void checkTodayCompetitions();
}
