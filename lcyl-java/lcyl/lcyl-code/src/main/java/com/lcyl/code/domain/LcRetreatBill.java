package com.lcyl.code.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LcRetreatBill {
    private Long id;
    private Long retreatId;          // 退住ID
    private String billJson;         // 账单json数据
    private String refundVoucherUrl; // 退款凭证URL
    private String tradingChannel;   // 支付渠道
    private Long elderId;            // 老人ID
    private Integer isRefund;        // 是否退款
    private BigDecimal refundAmount; // 退款金额
    private Date createTime;         // 创建时间
    private Date updateTime;         // 更新时间
    private Long createBy;           // 创建人
    private Long updateBy;           // 更新人
    private String remark;           // 备注

}