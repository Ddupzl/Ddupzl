//package com.example.filter;
//
//import com.example.entity.RestBean;
//import com.example.utils.Const;
//import jakarta.annotation.Resource;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpFilter;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.core.annotation.Order;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//import java.util.Optional;
//import java.util.concurrent.TimeUnit;
//
///**
// * 限流控制过滤器
// * 防止用户高频请求接口，借助Redis进行限流
// */
//@Slf4j
//@Component
//@Order(Const.ORDER_LIMIT)
//public class FlowLimitFilter extends HttpFilter {
//
//    @Resource
//    StringRedisTemplate template;
//
//    @Override
//    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
//        String address = request.getRemoteAddr();
//        if (this.tryCount(address))
//            chain.doFilter(request, response);
//        else
//            this.writeBlockMessage(response);
//    }
//
//    /**
//     * 尝试对指定IP地址请求计数，如果被限制则无法继续访问
//     * @param ip 请求IP地址
//     * @return 是否操作成功
//     */
//    private boolean tryCount(String ip) {
//        synchronized (ip.intern()) {
//            if(Boolean.TRUE.equals(template.hasKey(Const.FLOW_LIMIT_BLOCK +ip)))
//                return false;
//            return this.limitPeriodCheck(ip);
//        }
//    }
//    private boolean limitPeriodCheck(String ip)
//    {
//        if (Boolean.TRUE.equals(template.hasKey(Const.FLOW_LIMIT_COUNTER+ip))){
//            long increment= Optional.ofNullable(template.opsForValue().increment(Const.FLOW_LIMIT_COUNTER+ip)).orElse(0L);
//           if (increment>10){
//                 template.opsForValue().set(Const.FLOW_LIMIT_BLOCK+ip,"",30,TimeUnit.SECONDS);
//                 return false;
//           }
//        }
//        else
//            template.opsForValue().set(Const.FLOW_LIMIT_COUNTER+ip,"1",3, TimeUnit.SECONDS);
//        return true;
//    }
//    /**
//     * 为响应编写拦截内容，提示用户操作频繁
//     * @param response 响应
//     * @throws IOException 可能的异常
//     */
//    private void writeBlockMessage(HttpServletResponse response) throws IOException {
//        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//        response.setContentType("application/json;charset=utf-8");
//        response.getWriter().write(RestBean.forbidden("操作频繁，请稍后再试").asJsonString());
//    }
//}
