package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.dto.Communicate;
import com.example.mapper.CommunicateMapper;
import com.example.service.AccountService;
import com.example.service.CommunicateService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommunicateServiceImpl extends ServiceImpl<CommunicateMapper, Communicate> implements CommunicateService {
    @Resource
    AccountServiceImpl service;
    @Resource
    CommunicateMapper mapper;
    /**
     * 返回树形评论数据
     *
     * @return
     */
    @Override
    public List<Communicate> listTree(int id) {
        System.out.println(this.list());
        List<Communicate> list = mapper.selectCommentsByPostId(id);
        for (Communicate communicate :list)
        {
            communicate.setUsername(service.selectUsername(communicate.getUserId()));
            communicate.setUrl( service.selectUrl(communicate.getUserId()));
            if (communicate.getReplyId()!=null)//不空的话就设置replyUsername，不然爆null
               communicate.setReplyUsername(service.selectUsername(communicate.getReplyId()));
        }
        // 映射id->index
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int index = 0; index < list.size(); index++) {
            map.put(list.get(index).getCommentId(), index);
        }
        // 遍历寻找父节点
        for (Communicate communicate : list) {
            Integer pid = communicate.getPid();
            // 有父节点
            if (pid != null) {
                // 获取父节点id
                Integer indexFather = map.get(pid);
                Communicate father = list.get(indexFather);
                if (father.getChildren() == null) {
                    father.setChildren(new ArrayList<>());
                }
                // 在父节点上添加当前节点
                father.getChildren().add(communicate);
            }
        }
        // 过滤出一级节点
        List<Communicate> ans = list.stream().filter(child -> child.getPid() == null).collect(Collectors.toList());
        return ans;
    }
}
