package com.lcyl.code.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 结算员结算参数
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RetreatSettleBillDTO {
    private Long retreatId;
    private BigDecimal amount;   // 结算金额
    private String remark;       // 备注
}
