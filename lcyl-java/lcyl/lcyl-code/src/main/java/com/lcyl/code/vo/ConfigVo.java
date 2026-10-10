package com.lcyl.code.vo;

import lombok.Data;

import java.util.Date;

/**
 * @ClassName ConfigVo
 * @Description TODO
 * @Author LiYang
 * @Date 2026年02月04日 14:08
 * @Version 1.0
 */
@Data
public class ConfigVo {
    private String nursingLevel;
    private Double nursingCost;
    private String roomType;
    private Double bedCost;
    private Double otherCost;
    private Double medicalPayment;
    private Integer monthTotalDays;
    private Double governmentSubsidy;
    private Double billAmount;
    private Double payableAmount;
    private Integer totalDays;
    private Double firstSum;
    private Double secondSum;
    private Double monthlyPayment;
    private Double currentCost;
    private Date startTime;
    private Date endTime;
    private String billNo;
    private Integer billType;
    private String billMonth;
    private Integer elderId;
    private Double prepaidAmount;
    private Double depositAmount;
    private Date paymentDeadline;
    private Integer transactionStatus;
    private Integer createBy;
}
