package com.example.service;

import com.example.entity.dto.Application;
import com.example.entity.dto.Approvals;
import com.example.entity.dto.Comp;
import com.example.entity.dto.Comp2;

public interface CompService {
    //修改竞赛信息
    Boolean updateCp(Comp2 comp2);
    Integer insertAndGetId(Comp comp);
    Integer insertApplication(Application application);
    Integer insertApprovals(Approvals approvals);
}
