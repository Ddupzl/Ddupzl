package com.example.service;

import com.example.entity.vo.response.GradeExcel;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public interface AdminService {
   void exportGrade(String compName,HttpServletResponse response, List<GradeExcel> results) throws IOException;
}
