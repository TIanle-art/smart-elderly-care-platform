package com.lcyl.code.domain;

import java.util.Date;
import lombok.Data;

/**
 * 老人请假销假/返院记录表
 * @TableName lc_elder_leave_back_record
 */
@Data
public class ElderLeaveBackRecord {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 请假单ID
     */
    private Long leaveId;

    /**
     * 实际返回时间
     */
    private Date actualReturnTime;

    /**
     * 实际请假天数
     */
    private Integer actualLeaveDays;

    /**
     * 返院状态：returned已返回、not_returned未返回、abnormal异常返院
     */
    private String backStatus;

    /**
     * 销假备注
     */
    private String backRemark;

    /**
     * 销假操作人ID
     */
    private Long operatorId;

    /**
     * 销假操作人姓名
     */
    private String operatorName;

    /**
     * 销假操作时间
     */
    private Date operateTime;

    /**
     * 创建时间
     */
    private Date createTime;
}