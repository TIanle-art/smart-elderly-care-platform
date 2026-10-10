package com.lcyl.code.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 结算部长最终清算参数
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RetreatFinalDTO {
    private BigDecimal finalAmount;
    private String refundMethod;    // 退款方式
    private String refundVoucher;  // 退款凭证
    private String refundRemark;   // 退款备注

}
