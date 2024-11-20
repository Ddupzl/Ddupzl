package com.example.controller;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.RestBean;
import com.example.entity.dto.Account;
import com.example.entity.vo.request.EmailRegister;
import com.example.entity.vo.request.ResetConfirm;
import com.example.entity.vo.request.ResetPassword;
import com.example.entity.vo.response.AuthorizeVo;
import com.example.mapper.AccountMapper;
import com.example.mapper.BmMapper;
import com.example.service.AccountService;
import com.example.utils.JwtUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

@Validated
@RestController
@RequestMapping("/zl")
public class YzController {
    @Resource
    AccountService service;
    private RestBean<Void> messageHandle(Supplier<String> action){
        String message=action.get();
        return message==null ? RestBean.success() : RestBean.failure(400,message);
    }
    @PostMapping("/register")
    public RestBean<Void> register(@RequestBody @Valid EmailRegister vo){
        return this.messageHandle(()->service.registerEmailAccount(vo));
    }
    private <T> RestBean<Void> messageHandle(T vo, Function<T,String> function){
        return messageHandle(()->function.apply(vo));
    }
    @GetMapping("/ask-code")
    public RestBean<Void> askVerifyCode(@RequestParam @Email String email,
                                        @RequestParam @Pattern(regexp = "(register|reset)") String type,
                                        HttpServletRequest request){
//        String message=service.registerEmailVerifyCode(type,email,request.getRemoteAddr());
//        return message == null ? RestBean.success() : RestBean.failure(400,message);
        return this.messageHandle(()->service.registerEmailVerifyCode(type,email,request.getRemoteAddr()));
    }

    @PostMapping("/reset-confirm")
    public RestBean<Void> restConfirm(@RequestBody @Valid ResetConfirm vo){
//        return this.messageHandle(()->service.resetConfirm(vo));
        return this.messageHandle(vo,service::resetConfirm);
    }
    @PostMapping("/reset-password")
    public RestBean<Void> restPassword(@RequestBody @Valid ResetPassword vo){
//        return this.messageHandle(()->service.resetPassword(vo));
        return this.messageHandle(vo,service::resetPassword);
    }

}

