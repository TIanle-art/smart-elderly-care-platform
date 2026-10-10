package com.lcyl.code.vo;

import lombok.Data;

import java.util.Date;

/**
 * 主管待审批
 */
@Data
public class ElderLeaveTodoVo {

    /** 任务ID */
    private String taskId;

    /** 流程实例ID */
    private String processInstanceId;

    /** 当前任务定义Key */
    private String taskDefinitionKey;

    /** 当前任务名称 */
    private String taskName;

    /** 请假单ID */
    private Long leaveId;

    /** 单据编号 */
    private String orderNo;

    /** 老人姓名 */
    private String elderName;

    /** 请假开始时间 */
    private Date leaveStartTime;

    /** 预计返回时间 */
    private Date plannedReturnTime;

    /** 请假天数 */
    private Long leaveDays;

    /** 请假原因 */
    private String leaveReason;

    /** 提交时间 */
    private Date applyTime;

    /** 业务状态 */
    private String status;
}