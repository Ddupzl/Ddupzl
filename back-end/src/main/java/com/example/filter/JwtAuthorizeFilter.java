package com.example.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.utils.JwtUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthorizeFilter extends OncePerRequestFilter {
    @Resource
    JwtUtils utils;
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
//        String authorization=request.getHeader("Authorization");
//        DecodedJWT jwt=utils.resolveJwt(authorization);//解析jwt
//        if (jwt!=null){
//            UserDetails user;
//                  if ((user=utils.toUser(jwt))!=null){   //解析用户成user
//                      UsernamePasswordAuthenticationToken authentication=   //userNamePassword的token
//                              new UsernamePasswordAuthenticationToken(user,null,user.getAuthorities());
//                      authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                      SecurityContextHolder.getContext().setAuthentication(authentication);//直接把我们写好的验证信息往SecurityContex里面丢，然后通过验证
//                      request.setAttribute("id",utils.toId(jwt)); //从JWT中获取用户ID，并将其作为请求属性id设置到请求对象中。这可能在后续的请求处理中需要。
//                  }
//        }
//        filterChain.doFilter(request,response);
        String authorization=request.getHeader("Authorization");
        DecodedJWT jwt=utils.resolveJwt(authorization);//解析jwt
        if (jwt!=null){
            UserDetails user=utils.toUser(jwt); //解析用户成user
            UsernamePasswordAuthenticationToken authentication=   //userNamePassword的token
                    new UsernamePasswordAuthenticationToken(user,null,user.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);//直接把我们写好的验证信息往SecurityContex里面丢，然后通过验证
            request.setAttribute("id",utils.toId(jwt)); //从JWT中获取用户ID，并将其作为请求属性id设置到请求对象中。这可能在后续的请求处理中需要。
        }
        filterChain.doFilter(request,response);
    }
}
