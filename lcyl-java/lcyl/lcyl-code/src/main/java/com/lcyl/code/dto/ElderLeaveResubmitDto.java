package com.lcyl.code.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 驳回后重新提交
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ElderLeaveResubmitDto {

    @NotNull(message = "请假单ID不能为空")
    private Long leaveId;
    private String taskId;

    @NotBlank(message = "陪同人类型不能为空")
    private String companionType;
    private String companionName;
    private String companionPhone;

    @NotNull(message = "请假开始时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date leaveStartTime;

    @NotNull(message = "预计返回时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date plannedReturnTime;

    @NotBlank(message = "请假原因不能为空")
    private String leaveReason;
}
