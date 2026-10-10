package com.lcyl.code.domain.vo;

import lombok.Data;

import java.util.Date;

@Data
public class MyApplyVo {
    private String taskId;
    private String taskName;
    private String category;
    private String businessId;
    private String documentNo;
    private String title;
    private String applicant;
    private Date applyTime;
    private Date finishTime;
    private String flowStatus;
    private Boolean canCancel;
}

