package com.lcyl.system.domain;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 护理等级表实体
 */
@Data

public class NursingLevel {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 护理等级名称（唯一）
     */
    private String levelName;

    /**
     * 关联护理计划ID
     */
    private Long planId;

    /**
     * 每月护理费用
     */
    private BigDecimal monthlyFee;

    /**
     * 等级说明
     */
    private String levelDesc;

    /**
     * 状态：1-启用，0-禁用
     */
    private Integer status;

    /**
     * 创建人ID（关联nurse.id）
     */
    private Long creatorId;

    /**
     * 创建人姓名（冗余）
     */
    private String creatorName;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}