package com.lcyl.code.domain.dto;

import lombok.Data;

/**
 * @ClassName UserLoginRequestDto
 * @Description TODO
 * @Author GuiGui
 * @Date 2026-03-24 19:57
 * @Version 1.0
 */
@Data
public class UserLoginRequestDto {
    //private String nickName; // 昵称
    private String code; // 登录临时凭证
    private String phoneCode; //
}
