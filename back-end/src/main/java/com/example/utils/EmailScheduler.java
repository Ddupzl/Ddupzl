package com.example.utils;

import com.example.service.BmService;
import jakarta.annotation.Resource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class EmailScheduler {

    @Resource
    BmService service;

    @Scheduled(cron = "0 18 16 * * ?") // 每天上午8点执行
    public void sendEmailsAt8AM() {
            service.checkTodayCompetitions();
    }
}
