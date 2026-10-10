package com.lcyl.framework.interceptor.impl;

import com.lcyl.common.constant.Constants;
import com.lcyl.common.exception.base.BaseException;
import com.lcyl.common.utils.UserThreadLocal;
import com.lcyl.framework.web.service.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Slf4j
@Component
public class UserInterceptor implements HandlerInterceptor {
    @Autowired
    private TokenService tokenService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse
            response, Object handler) throws Exception {
        System.out.println("进入UserInterceptor拦截器");
        //从请求头获取token
        String token = request.getHeader("authorization");
        // 判断是否存在token
        log.info("开始解析 customer user token {}", token);
        if (ObjectUtils.isEmpty(token)) {
        //token失效
            throw new BaseException("小程序登录", "401", null, "没有权限,请登录");
        }
        // 存在token,解析token
        Map<String, Object> claims = tokenService.parseToken(token);
        // token是否非法
        if (ObjectUtils.isEmpty(claims)) {
            //token失效
            throw new BaseException("小程序登录", "401", null, "没有权限,请登录");
        }
        // 获取客户id
        Object userId = claims.get(Constants.JWT_USERID);
        if (ObjectUtils.isEmpty(userId)) {
            throw new BaseException("小程序登录", "401", null, "没有权限,请登录");
        }
        // 把客户id放到线程局部变量中
        UserThreadLocal.set(Long.valueOf(userId.toString()));
        return true;
    }
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse
            response, Object handler, Exception ex) throws Exception {
        UserThreadLocal.remove();
    }
}
