package com.lcyl.system.domain;


import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 入住配置表实体
 */
@Data

public class CheckInConfig {

    /**
     * 主键
     */

    private Long id;

    /**
     * 老人ID
     */
    private Long elderId;

    /**
     * 入住开始时间
     */
    private Date checkInStartTime;

    /**
     * 入住结束时间
     */
    private Date checkInEndTime;

    /**
     * 护理等级ID
     */
    private Long nursingLevelId;

    /**
     * 床位号
     */
    private String bedNo;

    /**
     * 费用开始时间
     */
    private Date costStartTime;

    /**
     * 费用结束时间
     */
    private Date costEndTime;

    /**
     * 押金（元）
     */
    private BigDecimal depositAmount;

    /**
     * 护理费用（元/月）
     */
    private BigDecimal nursingCost;

    /**
     * 床位费用（元/月）
     */
    private BigDecimal bedCost;

    /**
     * 其他费用（元/月）
     */
    private BigDecimal otherCost;

    /**
     * 医保支付（元/月）
     */
    private BigDecimal medicalInsurancePayment;

    /**
     * 政府补贴（元/月）
     */
    private BigDecimal governmentSubsidy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 创建人id
     */
    private Long createBy;

    /**
     * 更新人id
     */
    private Long updateBy;

    /**
     * 备注
     */
    private String remark;

    /**
     * 床位ID
     */
    private Long bedId;
}