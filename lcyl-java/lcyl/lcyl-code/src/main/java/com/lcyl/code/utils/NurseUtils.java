package com.lcyl.code.utils;

import com.lcyl.code.domain.Nurse;
import com.lcyl.code.service.INurseService;
import com.lcyl.common.core.domain.model.LoginUser;
import com.lcyl.common.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class NurseUtils {

    private static INurseService nurseService;

    @Autowired
    public void setNurseService(INurseService nurseService) {

        NurseUtils.nurseService = nurseService;
    }

    public static Nurse getCurrentNurse() {
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = loginUser.getUserId();
        Nurse nurse = nurseService.selectNurseByUserId(userId);
        if (nurse == null) {
            throw new ServiceException("当前登录用户未绑定护理员信息");
        }
        return nurse;
    }
}