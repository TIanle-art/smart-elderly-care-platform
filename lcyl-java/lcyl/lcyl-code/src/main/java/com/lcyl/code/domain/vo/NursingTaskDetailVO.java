package com.lcyl.code.domain.vo;

import lombok.Data;

import java.util.Date;

/**
 * @ClassName NursingTaskDetailVO
 * @Description TODO
 * @Author hfy
 * @Date 2026-03-26 16:08
 * @Version 1.0
 */
@Data
public class NursingTaskDetailVO {

    private Long id;

    // 老人姓名
    private String elderName;

    // 床位号
    private String bedNumber;

    // 护理项目
    private String projectName;

    // 项目类型 0-计划内 1-计划外
    private String itemType;

    // 护理员姓名
    private String nurseName;

    // 申请人
    private String applicantName;

    // 期望服务时间
    private String expectedServiceTime;

    // 创建时间
    private String createTime;

    // 订单编号
    private String orderNo;

    // 执行状态 0-待执行 1-已执行 2-已取消
    private String executeStatus;

    // ===================== 执行记录 =====================
    // 执行人
    private String executorName;

    // 执行时间
    private String executeTime;

    // 执行图片
    private String executeImage;

    // 执行记录
    private String executeRecord;

    // ===================== 取消记录 =====================
    // 取消时间
    private String cancelTime;

    // 取消原因
    private String cancelReason;

}
