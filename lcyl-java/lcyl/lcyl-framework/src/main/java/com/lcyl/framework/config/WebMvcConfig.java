package com.lcyl.framework.config;

import com.lcyl.framework.interceptor.impl.UserInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @ClassName WebMvcConfig.java
 * @Description webMvc高级配置
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Autowired
    UserInterceptor userInterceptor;
    //拦截的时候过滤掉swagger相关路径和登录相关接口
    private static final String[] EXCLUDE_PATH_PATTERNS = new String[]{
            "/swagger-ui.html",
            "/webjars/**",
            "/swagger-resources",
            "/v2/api-docs",
// 登录接口
            "/wxLogin/phoneLogin"
    };

    /**
     * @Description 拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
// 小程序端接口鉴权拦截器
        registry.addInterceptor(userInterceptor)
                .excludePathPatterns(EXCLUDE_PATH_PATTERNS)
                .addPathPatterns("/wxLogin/**");
    }
}