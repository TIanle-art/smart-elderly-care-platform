package com.lcyl.code.domain.vo;

import lombok.Data;

import java.util.Date;

/**
 * @ClassName MyVisitVo
 * @Description TODO
 * @Author GuiGui
 * @Date 2026-03-30 15:43
 * @Version 1.0
 */
@Data
public class MyVisitVo {
    private Integer id;
    private String phone;
    private String name;
    private String olderName;
    private Integer type;
    private Date appointmentTime;
    private Integer status;
    private Date visitTime;
    private String greeting;
}
