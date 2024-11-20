package com.example.controller;

import com.example.entity.RestBean;
import com.example.service.LbtService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Validated
@RestController
@RequestMapping("/lbt")
public class LbtController {
    @Resource
    LbtService service;
    @RequestMapping("/download/downloadList")
    public RestBean downloadList(){
        if (service.downloadList()!=null)
          return RestBean.success(service.downloadList());
        return RestBean.failure(400,"数据为空");
    }

    @RequestMapping("/uploadList")
    public RestBean uploadList(@RequestParam("imageList") List<MultipartFile> fileList) throws IOException {
        List<String> urlList = new ArrayList<>(fileList.size());
        for (MultipartFile file : fileList) {
            String url = service.upLoadLbt(file, UUID.randomUUID().toString().substring(0, 10)
                    + "-" + file.getOriginalFilename());
            urlList.add(url);
        }
        return RestBean.success(urlList);
    }
    @RequestMapping("/download/{fileName}")
    public void download(@PathVariable("fileName") String fileName, HttpServletRequest request, HttpServletResponse response) throws IOException {
        service.downloadFile(fileName, request, response);
    }
    /**
     * 删除接口
     */
    @RequestMapping("/delete")
    public RestBean deleteFile(@RequestParam("name") String fileName) {
        boolean flag = service.deleteFile(fileName);
        if (flag)
         return RestBean.success();
        return RestBean.failure(400,"删除图片失败");
    }
}
