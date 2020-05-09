package com.elk.elkkafka.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Auther: Mr.Kong
 * @Date: 2020/5/9 11:15
 * @Description: 拦截器
 */
@EnableWebMvc
@Configuration
public class WebConfigurer implements WebMvcConfigurer {

    @Autowired
    private MethodInterceptor methodInterceptor;

    /**
     * 资源拦截
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
    }

    /**
     * API拦截
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //注册第两个个拦截器，excludePathPatterns是取消拦截的路径
        registry.addInterceptor(methodInterceptor).addPathPatterns("/**").excludePathPatterns("/login");
    }
}
