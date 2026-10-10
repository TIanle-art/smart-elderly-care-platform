package com.lcyl.code.domain;

import com.lcyl.common.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName Member
 * @Description TODO
 * @Author GuiGui
 * @Date 2026-03-24 19:43
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Member  extends BaseEntity {
    private Integer id;
    private String phone;
    private String name;
    private String avatar;
    private String openId;
    private String gender;
}
