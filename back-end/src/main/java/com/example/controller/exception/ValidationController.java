package com.example.controller.exception;

import com.example.entity.RestBean;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//自定义处理，邮件请求验证码时邮件乱输时的异常,跟springmvc一样
@Slf4j  //日志
@RestControllerAdvice
public class ValidationController {
    @ExceptionHandler(ValidationException.class)
    public RestBean<Void> validateException(ValidationException exception){
        log.warn("Resolve [{}: {}]",exception.getClass().getName(),exception.getMessage());
        return RestBean.failure(400,"请求参数有误");
    }
}
