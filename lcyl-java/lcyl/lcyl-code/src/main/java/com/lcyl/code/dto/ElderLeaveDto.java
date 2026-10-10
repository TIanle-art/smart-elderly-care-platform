package com.lcyl.code.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lcyl.common.annotation.Excel;
import lombok.Data;

import java.util.Date;

@Data
public class ElderLeaveDto {
    /** 单据编号 */
    @Excel(name = "单据编号")
    private String orderNo;

    /** 老人姓名（冗余） */
    @Excel(name = "老人姓名", readConverterExp = "冗=余")
    private String elderName;

    /** 老人身份证号 */
    @Excel(name = "老人身份证号")
    private String elderIdCard;

    /** 请假开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "请假开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date leaveStartTime;

    /** 预计返回时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "预计返回时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date plannedReturnTime;

    /** 是否已返回：0否，1是 */
    @Excel(name = "是否已返回：0否，1是")
    private Integer isReturned;

    /** 业务状态 */
    private String status;
}
