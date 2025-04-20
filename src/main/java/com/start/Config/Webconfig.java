package com.start.Config;

import com.start.intercepter.LoginIntercepter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class Webconfig implements WebMvcConfigurer {

    @Autowired
    private LoginIntercepter loginIntercepter;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(loginIntercepter).excludePathPatterns("/user/login","/user/regist",
                "/user/sendMsg","/user/loginbyphone");
    }
}
