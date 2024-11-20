package com.example.service;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {
    /**
     * 上传图片, 返回url
     */
    String uploadFile(MultipartFile file, String fileName) throws IOException;

    /**
     * 下载图片
     */
    void downloadFile(String fileName,  HttpServletResponse response,String setHeader);

    /**
     * 删除图片
     */
    boolean deleteFile(String fileName);
}
