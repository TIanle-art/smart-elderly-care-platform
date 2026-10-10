package com.lcyl.code.domain.dto;

import lombok.Data;

/**
 * 审核操作参数（用于护理部长、结算部长、副院长）
 */
@Data
public class RetreatTaskCompletedDTO {
    private String taskId;      // Activiti任务ID
    private Integer action;     // 操作类型：1-通过，2-驳回
    private String opinion;     // 审核意见
    private Object businessData; // 业务数据，如合同上传、账单结算等

}
