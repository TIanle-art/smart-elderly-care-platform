package com.lcyl.code.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName LoginVo
 * @Description TODO
 * @Author GuiGui
 * @Date 2026-03-24 21:08
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginVo {
    private String name;
    private String token;
}
