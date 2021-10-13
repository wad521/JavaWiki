package com.jiawa.wiki.config;

/**
 * @author:yxl
 **/
import com.jiawa.wiki.interceptor.LogInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration
public class SpringMvcConfig implements WebMvcConfigurer {


    //将拦截器注入进来
    @Resource
    LogInterceptor logInterceptor;

    //给拦截器添加方法
    public void addInterceptors(InterceptorRegistry registry) {
        //excludePathPatterns排除某些请求，不将他们拦截
        // “/**”代表所有请求

        registry.addInterceptor(logInterceptor)
                .addPathPatterns("/**").excludePathPatterns("/login");
    }
}
