package com.example.service;

import com.example.entity.dto.Lbt;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface LbtService {
    List<Lbt> downloadList();
    String upLoadLbt(MultipartFile file, String fileName) throws IOException;
    void downloadFile(String fileName, HttpServletRequest request, HttpServletResponse response);
    public boolean deleteFile(String fileName);
}
