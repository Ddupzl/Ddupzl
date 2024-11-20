package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.dto.Communicate;

import java.util.List;

public interface CommunicateService extends IService<Communicate> {
    /**
     * 返回树形评论数据
     * @return
     */
    List<Communicate> listTree(int id);
}
