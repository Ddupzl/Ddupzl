package com.example.service.impl;

import com.example.service.FileService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

@Service
public class FileServiceImpl implements FileService {
    private static final String STORE_DIR = "C:\\Users\\HP\\Desktop\\SubjectContest\\back-end\\src\\main\\resources\\images\\tx\\";
    /**
     * 上传图片, 返回url
     */
    @Override
    public String uploadFile(MultipartFile file, String fileName) throws IOException {
        InputStream is = file.getInputStream();
        // 在服务器中存储文件
        FileUtils.copyInputStreamToFile(is, new File(fileName));//文件输入流存到指定的位置
        // url
        return fileName;
    }

    /**
     * 下载图片
     */
    @Override
    public void downloadFile(String fileName,  HttpServletResponse response,String setFileName) {
        try {
            response.setHeader("Cache-Control", "public, max-age=86400");
            response.setContentType("application/octet-stream");
            String encodedFileName = Base64.getUrlEncoder().encodeToString(setFileName.getBytes(StandardCharsets.UTF_8));
            String headerValue = "attachment; filename*=UTF-8''" + encodedFileName;
            response.setHeader("Content-Disposition", headerValue);
            // 读取文件内容并写入输出流
            Files.copy(Paths.get(fileName), response.getOutputStream());
            response.flushBuffer(); // 使用flushBuffer而不是getOutputStream().flush()
        } catch (IOException e) {
            response.setStatus(404);
        }
    }
    /**
     * 删除图片
     */
    @Override
    public boolean deleteFile(String fileName) {
        String filePath = STORE_DIR + fileName;
        File file = new File(filePath);
        return file.delete();
    }
}
//            // 下载文件
//            // 设置响应头
//            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
////            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
//            // 使用filename*参数并指定UTF-8编码
//            String headerValue = "attachment; filename=\"" + "aaa" + "\"; filename*=UTF-8''" + java.net.URLEncoder.encode(fileName, "UTF-8");
//            response.setHeader("Content-Disposition", headerValue);
//            // 读取文件内容并写入输出流
//            Files.copy(Paths.get(fileName), response.getOutputStream());
//            response.getOutputStream().flush();
// 设置响应头
