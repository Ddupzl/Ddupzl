package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.dto.Account;
import com.example.entity.vo.request.EmailRegister;
import com.example.entity.vo.request.ResetConfirm;
import com.example.entity.vo.request.ResetPassword;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Map;


public interface AccountService extends IService<Account>, UserDetailsService {
    int deleteUser(Integer uid);
    String updateInfo(String token,Account account);//修改用户名、邮箱和电话
    void storeUserInHash(String token, Object userInfo);
    Object getUserInfoFromRedis(String token);
    void updateUrlInRedis(String token,String url);
    void deleteUserInfoFromRedis(String token);
    void updateTx(int id,String url);//更改用户头像路径
    Account findAccountByEmail(String text);//通过电话或邮件寻找账号，用于电话或邮件登录
    String registerEmailVerifyCode(String type,String email,String ip);//邮件注册申请验证码
    String registerEmailAccount(EmailRegister vo);//邮件注册账号，先判断验证码，再判断账号信心是否被注册
    String resetConfirm(ResetConfirm vo);//密码重置,第一步，先验证邮箱验证码是否正确
    String resetPassword(ResetPassword vo);//密码重置,第二步，验证码通过后改密码
}
