package com.example.config;

import com.example.entity.RestBean;
import com.example.entity.dto.Account;
import com.example.entity.vo.response.AuthorizeVo;
import com.example.filter.JwtAuthorizeFilter;
import com.example.service.AccountService;
import com.example.utils.JwtUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.io.PrintWriter;

@Configuration
public class SecurityConfiguration {
    @Resource
    JwtUtils utils;
    @Resource
    JwtAuthorizeFilter jwtAuthorizeFilter;
    @Resource
    AccountService service;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(conf->conf
                        .requestMatchers("/zl/**","/zq/**","/error","/info/**","/comm/**","/lbt/download/**").permitAll()        //拦截
                        .anyRequest().authenticated()
                )
                .formLogin(conf->conf
                        .loginProcessingUrl("/zl/login")
                        .failureHandler(this::onAuthenticationFailure)  //登录
                        .successHandler(this::onAuthenticationSuccess)
                )
                .logout(conf -> conf
                        .logoutUrl("/zl/logout")
                        .logoutSuccessHandler(this::onLogoutSuccess)   //登出
                )
                .exceptionHandling(conf->conf
                        .authenticationEntryPoint(this::onUnauthorized)//没有登录
                        .accessDeniedHandler(this::onAccessDeny) //没有权限访问
                )
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(conf->conf
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthorizeFilter, UsernamePasswordAuthenticationFilter.class)
                .headers(headers -> headers
                        .cacheControl(HeadersConfigurer.CacheControlConfig::disable) // 禁用默认的缓存控制
                )
                .build();
    }

    public void onAccessDeny(HttpServletRequest request,
                             HttpServletResponse response,
                             AccessDeniedException exception) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(RestBean.forbidden(exception.getMessage()).asJsonString());
    }
    //登录成功
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        response.setContentType("application/json;charset=utf-8");//设置HTTP响应头,包含了关于响应的一些附加信息，如Content-Type（指定响应体的
        //媒体类型）、Content-Length（指定响应体的长度）等。以告知客户端（通常是Web浏览器或其他HTTP客户端）,返回的响应体中的内容类型(json)和字符编码
        User user = (User) authentication.getPrincipal();
        Account account=service.findAccountByEmail(user.getUsername());//用springSecurity自带的user类获取用户名
        String token=utils.createJwt2(account.getUId(),account.getUsername());//登录成功后把用户信息丢进jwt中并创建
        service.storeUserInHash(token,account);//把获取的用户信息存到redis中
        AuthorizeVo vo=new AuthorizeVo();
        vo.setToken(token);
        response.getWriter().write(RestBean.success(vo).asJsonString());
    }
    //登录失败
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(RestBean.unauthorized(exception.getMessage()).asJsonString());
    }
    //退出登录
    public void onLogoutSuccess(HttpServletRequest request,
                                HttpServletResponse response,
                                Authentication authentication) throws IOException, ServletException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter writer = response.getWriter();
        String authorization=request.getHeader("Authorization");
        if (utils.invalidJwt(authorization)){  //退出登录，把token加入黑名单，并且登录后生成的用户信息表也根据token删除掉
            service.deleteUserInfoFromRedis(utils.convertToken(authorization));
            writer.write(RestBean.success().asJsonString());
        }else {
            writer.write(RestBean.failure(400,"退出登录失败").asJsonString());
        }
    }
    //未登录
    public void onUnauthorized(HttpServletRequest request,
                               HttpServletResponse response,
                               AuthenticationException exception) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(RestBean.unauthorized(exception.getMessage()).asJsonString());
    }
}
