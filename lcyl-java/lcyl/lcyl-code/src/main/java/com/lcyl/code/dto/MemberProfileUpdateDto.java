package com.lcyl.code.dto;

import lombok.Data;

@Data
public class MemberProfileUpdateDto {

    /**
     * 昵称
     */
    private String name;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 性别
     */
    private String gender;
}