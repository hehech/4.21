package com.start.intercepter;

import com.start.utils.JwtUtil;
import com.start.utils.ThreadLocalUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

@Component
public class LoginIntercepter implements HandlerInterceptor {

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //令牌验证
        String token = request.getHeader("Authorization");
        try {
            System.out.println("接受到请求");
            //从redis中获取相同的token
            ValueOperations<String, String> ops=redisTemplate.opsForValue();
            String redisToken = ops.get(token);
            if(redisToken==null){
                throw  new RuntimeException();
            }

            Map<String,Object> claims= JwtUtil.parseToken(token);
            //将信息放到threadLocal变量中去
            ThreadLocalUtil.set(claims);
            //放行
            return true;
        } catch (Exception e) {
            //http响应状态码为401
            response.setStatus(401);
            //不放行
            return false;
        }
    }
    //结束后清除Thread变量
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        ThreadLocalUtil.remove();
    }
}
