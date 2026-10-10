package com.lcyl.code.domain.dto;

import lombok.Data;

import java.sql.Date;

/**
 * 发起退住申请时前端提交的参数
 */
@Data
public class RetreatStartDTO {
    private Long elderId;      // 老人ID
    private String reason;     // 退住原因
    private Date checkOutTime; // 期望退住时间
}
