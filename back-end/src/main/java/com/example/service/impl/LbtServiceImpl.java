package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.dto.Lbt;
import com.example.mapper.LbtMapper;
import com.example.service.LbtService;
import jakarta.annotation.Resource;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class LbtServiceImpl extends ServiceImpl<LbtMapper, Lbt> implements LbtService {
    @Resource
    LbtMapper mapper;

    /**
     * 文件物理存储位置
     */
    private static final String STORE_DIR = "C:\\Users\\HP\\Desktop\\SubjectContest\\back-end\\src\\main\\resources\\images\\p\\";

    @Override
    public List<Lbt> downloadList() {
        return mapper.selectList(null);
    }

    @Override
    public String upLoadLbt(MultipartFile file, String fileName) throws IOException {
        // 获取文件流
        InputStream is = file.getInputStream();
        // 在服务器中存储文件
        FileUtils.copyInputStreamToFile(is, new File(STORE_DIR + fileName));
        // 返回图片url
        Lbt a=new Lbt(null,fileName);
        mapper.insert(a);
        return fileName;
    }
    @Override
    public void downloadFile(String fileName, HttpServletRequest request, HttpServletResponse response) {
        // 获取真实的文件路径
        String filePath = STORE_DIR + fileName;
        try {
            response.setHeader("Cache-Control", "public, max-age=86400");
            // 下载文件
            // 设置响应头
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
            // 读取文件内容并写入输出流
            Files.copy(Paths.get(filePath), response.getOutputStream());
            response.getOutputStream().flush();
        } catch (IOException e) {
            response.setStatus(404);
        }
    }
    @Override
    public boolean deleteFile(String fileName) {
       if (mapper.deleteP(fileName)>=1)
       {
           // 获取真实的文件路径
           String filePath = STORE_DIR + fileName;
           File file = new File(filePath);
           return file.delete();
       }
       return false;
    }
}
