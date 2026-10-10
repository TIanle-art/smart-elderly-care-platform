package com.lcyl.code.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class ElderLeaveSubmitDto {

    /** 老人ID */
    @NotNull(message = "老人ID不能为空")
    private Long elderId;

    /** 陪同人类型：家属、护理人员、其他、无 */
    @NotBlank(message = "陪同人类型不能为空")
    private String companionType;

    /** 陪同人姓名 */
    private String companionName;

    /** 陪同人联系方式 */
    private String companionPhone;

    /** 请假开始时间 */
    @NotNull(message = "请假开始时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date leaveStartTime;

    /** 预计返回时间 */
    @NotNull(message = "预计返回时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date plannedReturnTime;

    /** 请假原因 */
    @NotBlank(message = "请假原因不能为空")
    private String leaveReason;
}