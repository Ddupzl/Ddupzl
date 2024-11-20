package com.example.service.impl;

import com.alibaba.excel.EasyExcel;
import com.example.entity.vo.response.GradeExcel;
import com.example.service.AdminService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {
    @Override
    public void exportGrade(String compName,HttpServletResponse response, List<GradeExcel> results) throws IOException {
//        response.setHeader("Cache-Control", "public, max-age=86400");
        // 设置响应头，指定下载的文件名
        String fileName = URLEncoder.encode("竞赛成绩", "UTF-8");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String headerValue = "attachment; filename*=UTF-8''" + fileName + ".xlsx";
        response.setHeader("Content-Disposition", headerValue);
        // 使用EasyExcel将DTO列表写入Excel并输出到响应流
        EasyExcel.write(response.getOutputStream(), GradeExcel.class).sheet(compName+"竞赛成绩").doWrite(results);
    }
}
