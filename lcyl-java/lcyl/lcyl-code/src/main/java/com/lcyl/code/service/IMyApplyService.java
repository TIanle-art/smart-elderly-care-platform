package com.lcyl.code.service;

import com.lcyl.code.domain.vo.MyApplyVo;

import java.util.List;

public interface IMyApplyService {
    List<MyApplyVo> getMyTodoList(Long userId, String userName, String nickName);
}
