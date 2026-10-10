package com.lcyl.code.controller;

import com.lcyl.code.domain.vo.MyApplyVo;
import com.lcyl.code.service.IMyApplyService;
import com.lcyl.common.core.domain.model.LoginUser;
import com.lcyl.common.utils.Result;
import com.lcyl.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MyApplyController {
    @Autowired
    private IMyApplyService myApplyService;
    @GetMapping("/my/apply/todoList")
    public Result<?> getMyTodoList() {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        Long userId = loginUser.getUserId();
        String userName = loginUser.getUsername();
        String nickName = loginUser.getUser().getNickName();
        List<MyApplyVo> list = myApplyService.getMyTodoList(userId, userName, nickName);
        return Result.success(list);
    }
}
