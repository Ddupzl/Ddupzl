package com.example.service.impl;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.dto.Account;
import com.example.entity.dto.QQ;
import com.example.entity.vo.request.EmailRegister;
import com.example.entity.vo.request.ResetConfirm;
import com.example.entity.vo.request.ResetPassword;
import com.example.mapper.AccountMapper;
import com.example.service.AccountService;
import com.example.utils.Const;
import com.example.utils.FlowUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements AccountService {
    @Resource
    FlowUtils utils;
    @Resource
    AmqpTemplate amqpTemplate;
    @Resource
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    private ObjectMapper objectMapper = new ObjectMapper();
    @Resource
    PasswordEncoder passwordEncoder;
    @Resource
    AccountMapper mapper;
    //通过 id(也可能是replyId) 查找用户的username
    public String selectUsername(int id)
    {
        return this.baseMapper.selectById(id).getUsername();
    }
    //通过id查找用户的url
    public String selectUrl(int id)
    {
        return this.baseMapper.selectById(id).getUrl();
    }

    //把用户信息存到redis中: key为userInfo:token，value为用户信息
    public void storeUserInHash(String token, Object userInfo) {
        String key="userInfo:"+token;
        try {
            // 将用户信息对象转换为JSON格式的字符串
            String userInfoJson = objectMapper.writeValueAsString(userInfo);
            // 使用RedisTemplate将JSON字符串存储到Redis中
            redisTemplate.opsForValue().set(key,userInfoJson,7*24, TimeUnit.HOURS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //根据token获取用户信息
    public Object getUserInfoFromRedis(String token) {
        String key="userInfo:"+token;
        // 从Redis中获取JSON字符串
        String userInfoJson = redisTemplate.opsForValue().get(key);
        if (userInfoJson != null) {
            try {
                // 将JSON字符串转换回对象
                return objectMapper.readValue(userInfoJson, Account.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null; // 如果没有找到数据，返回null
    }

    @Override
    public int deleteUser(Integer uid) {
       return mapper.deleteUser(uid);
    }

    //根据token修改用户的信息(修改用户名,电话,邮箱)
    public String updateInfo(String token,Account account) {
        //从redis中取出的老数据b
        Account b= (Account) getUserInfoFromRedis(token);
        //首先查询用户名和邮箱是否跟修改之前一样,一样可以继续
       if (!Objects.equals(b.getUsername(), account.getUsername()) && this.existAccountByUsername(account.getUsername()) )
                 return "用户名重复，再想想吧";
        if (!Objects.equals(b.getEmail(), account.getEmail()) && this.existAccountByEmail(account.getEmail()) )
                return "此邮箱已被其他用户注册";

           if (mapper.updateInfo(account.getUsername(),account.getEmail(),account.getTel(),account.getUId()))
           {
               Account a=(Account) getUserInfoFromRedis(token);
               a.setTel(account.getTel());
               a.setUsername(account.getUsername());
               a.setEmail(account.getEmail());
               deleteUserInfoFromRedis(token);
               storeUserInHash(token,a);
               return "修改信息成功";
           }
           else
               return "修改信息时出错了";
    }
    //根据token修改redis中用户的图片地址
    public void updateUrlInRedis(String token,String url) {
        Account a=(Account) getUserInfoFromRedis(token);
        a.setUrl(url);
        deleteUserInfoFromRedis(token);
        storeUserInHash(token,a);
    }
    //根据token删除用户信息
    public void deleteUserInfoFromRedis(String token) {
        String key="userInfo:"+token;
        // 根据key删除Redis中的数据
        redisTemplate.delete(key);
    }
    //在数据库中更改用户头像路径
    public void updateTx(int u_id,String url){
       mapper.updateTx(u_id,url);
    }
    //邮箱请求时长限制
    private boolean verifyLimit(String ip){
        String key= Const.VERIFY_EMAIL_LIMIT+ip;
        return utils.limitOnceCheck(key,60); //再次请求的时长，不至于报请求频繁
    }
    //注册时查找邮箱存不存在
    private boolean existAccountByEmail(String email){
         return this.baseMapper.exists(Wrappers.<Account>query().eq("email",email));
//        return this.exists(Wrappers.<Account>query().eq("email",email));
    }
    private boolean existAccountByUsername(String username){
        return this.baseMapper.exists(Wrappers.<Account>query().eq("username",username));
    }
    public Account findAccountByEmail(String text){
        return this.query()
//                .eq("tel",text).or()
                .eq("email",text)
                .one();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Account account=this.findAccountByEmail(username);
        if (account==null)
            throw new UsernameNotFoundException("用户名或密码错误");
        return User
                .withUsername(username)
                .password(account.getPassword())
                .roles(account.getRole())
                .build();
    }
    @Override //从邮箱获取验证码，注册/重置密码
    public String registerEmailVerifyCode(String type, String email, String ip) {
        String key=Const.VERIFY_EMAIL_DATA+email;
        synchronized (ip.intern()) {
            if (!this.verifyLimit(ip))
                return "请求频繁，请一分钟后重试";
            if (this.existAccountByEmail(email) && type.equals("register")) return "此邮箱已被其他用户注册";
            String existingCode = stringRedisTemplate.opsForValue().get(key); //判断账号是否已发送过验证码,尝试从Redis中获取已有的验证码
            if (existingCode != null)
            {
                int exist=Integer.parseInt(existingCode);
                Map<String,Object> data=Map.of("type",type,"email",email,"code",exist);
                amqpTemplate.convertAndSend("mail",data);
                return null;
            }
            Random random=new Random();
            int code=random.nextInt(8999)+1000;
            Map<String,Object> data=Map.of("type",type,"email",email,"code",code);
            amqpTemplate.convertAndSend("mail",data);
            stringRedisTemplate.opsForValue()
                    .set(key,String.valueOf(code),5, TimeUnit.MINUTES);
            return null;
        }
    }
    @Override//邮箱注册  先验证码验证再用户名重复验证
    public String registerEmailAccount(EmailRegister vo) {
        String email=vo.getEmail();
        String username=vo.getUsername();
        String key=Const.VERIFY_EMAIL_DATA+email;
        String code=stringRedisTemplate.opsForValue().get(key);
        if (code==null) return "请先获取验证码";
        if (!code.equals(vo.getCode())) return "验证码输入错误，请重新输入";
        if (this.existAccountByUsername(username)) return "该用户名已被注册,再想一想吧";
        String password=passwordEncoder.encode(vo.getPassword());
        Account account=new Account(null,null,email,password, vo.getRole(), new Date(),username,null);
        if (this.save(account)){
            stringRedisTemplate.delete(key);
            return null;
        }
        else
            return "内部错误请联系管理员";
    }
    @Override//密码重置,第一步，先验证邮箱验证码是否正确
    public String resetConfirm(ResetConfirm vo) {
        String email = vo.getEmail();
        String code=stringRedisTemplate.opsForValue().get(Const.VERIFY_EMAIL_DATA+email);
        if (code==null) return "请先获取验证码";
        if (!code.equals(vo.getCode())) return "验证码输入错误，请重新输入";
        return null;
    }

    @Override//密码重置,第二步，验证码通过后改密码
    public String resetPassword(ResetPassword vo) {
        String email=vo.getEmail();
        String verify=this.resetConfirm(new ResetConfirm(email,vo.getCode()));
        if (verify!=null) return verify;
        String password=passwordEncoder.encode(vo.getPassword());
        boolean update=this.update().eq("email",email).set("password",password).update();
        if (update){
            stringRedisTemplate.delete(Const.VERIFY_EMAIL_DATA+email);
        }
        return null;
    }

}
