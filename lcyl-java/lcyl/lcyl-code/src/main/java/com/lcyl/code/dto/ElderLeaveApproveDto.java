package com.lcyl.code.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ElderLeaveApproveDto {

    /** 任务ID */
    @NotBlank(message = "任务ID不能为空")
    private String taskId;

    /** 请假单ID */
    @NotNull(message = "请假单ID不能为空")
    private Long leaveId;

    /** 审批结果：approved通过、rejected拒绝、back驳回 */
    @NotBlank(message = "审批结果不能为空")
    private String approveResult;

    /** 审批意见 */
    private String approveOpinion;
}
