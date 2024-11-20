package com.example.controller;

import com.example.config.LoginQQConstant;
import com.example.entity.RestBean;
import com.example.entity.dto.Account;
import com.example.entity.vo.response.AuthorizeVo;
import com.example.mapper.AccountMapper;
import com.example.service.AccountService;
import com.example.utils.JwtUtils;
import jakarta.servlet.ServletException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.io.IOException;


@RestController
@RequestMapping("/zq")
public class QQLoginController {
//    @Value("${spring.security.oauth2.client.registration.qq.client-id}")
//    String client_id;
//    @Value("${spring.security.oauth2.client.registration.qq.client-secret}")
//    String client_secret;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private AccountService accountService;
    @Autowired
    private RestTemplate restTemplate;

    @PostMapping("/callbackHandler")
    public RestBean callbackHandler(@RequestBody String code) throws IOException, ServletException {
        System.out.println("这是code::::::::::::::::::::::::::::::::" + code);
        if (code == null || code.trim().isEmpty()) {
            throw new RuntimeException("未获取到AuthorizationCode");
        }
        String urlForAccessToken = getUrlForAccessToken(code);
        String openid=getOpenId(urlForAccessToken);
        System.out.println("openid::::::::::::::::::"+openid);
        if (openid == null || openid.trim().isEmpty()) {
            throw new RuntimeException("未获取到openid");
        }
        Integer accountId = accountMapper.selectAccount(openid);
        if (accountId != null) {
            Account account = accountMapper.getInfo(accountId);
            String token = jwtUtils.createJwt2(account.getUId(), account.getUsername());//登录成功后把用户信息丢进jwt中并创建
            accountService.storeUserInHash(token, account);//把获取的用户信息存到redis中
            AuthorizeVo vo = new AuthorizeVo();
            vo.setToken(token);
            System.out.println(vo);
            return RestBean.success(vo);
        } else {
            return RestBean.failure(404, "该QQ号没有绑定用户");
        }
    }

    private String getUrlForAccessToken(String authorization_code) {
        String grant_type = "authorization_code";
        String client_id = LoginQQConstant.APP_ID;
        String client_secret = LoginQQConstant.APP_KEY;
        String redirect_uri = LoginQQConstant.CALLBACK_URL;

        String url = String.format(LoginQQConstant.GET_ACCESS_TOKEN +
                        "?grant_type=%s&client_id=%s&client_secret=%s&code=%s&redirect_uri=%s&need_openid=1",
                grant_type, client_id, client_secret, authorization_code, redirect_uri);

        return url;
    }

    private String getOpenId(String urlForAccessToken) throws IOException {
        String firstCallbackInfo = restTemplate.getForObject(urlForAccessToken, String.class);
        String[] params = firstCallbackInfo.split("&");
        String openid = null;
        for (String param : params) {
            String[] keyvalue = param.split("=");
            if (keyvalue[0].equals("openid")) {
                openid = keyvalue[1];
                break;
            }
        }
        return openid;
    }

}
