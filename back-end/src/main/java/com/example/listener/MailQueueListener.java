package com.example.listener;

import com.example.utils.Const;
import jakarta.annotation.Resource;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
@RabbitListener(queues = "mail")
public class MailQueueListener {
    @Resource
    JavaMailSender sender;

    @Value("${spring.mail.username}")
    String username;

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @RabbitHandler
    public void sendMailMessage(Map<String,Object> data){
        String email=(String) data.get("email");
        Integer code=(Integer) data.get("code");
        String type=(String) data.get("type");
        SimpleMailMessage message=switch(type){
            case "remind" -> createMessage("学科竞赛网站提醒您今天有比赛",
                    "比赛名称:"+data.get("info")+"\n比赛时间:"+data.get("info2")+"\n比赛地点:"+data.get("info3"),email);
            case "register" -> createMessage("欢迎注册我们的网站",
                    "您的邮件注册验证码为:"+code+",有效时间"+ stringRedisTemplate.getExpire(Const.VERIFY_EMAIL_DATA+email)/60+"分钟\n为了保障您的安全，请勿向他人泄露验证码信息",email);
            case "reset" ->createMessage("密码重置邮件",
                    "您正在进行重置密码操作\n验证码为:"+code+",有效时间为"+ stringRedisTemplate.getExpire(Const.VERIFY_EMAIL_DATA+email)/60+"分钟",email);
            default -> null;
        };
        if (message==null) return;
        sender.send(message);
    }
    private SimpleMailMessage createMessage(String title,String content,String email){
        SimpleMailMessage message=new SimpleMailMessage();
        message.setSubject(title);
        message.setText(content);
        message.setTo(email);
        message.setFrom(username);
        return message;
    }
}
