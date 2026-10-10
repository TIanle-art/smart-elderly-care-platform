package com.lcyl.code.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.lcyl.common.annotation.Excel;
import com.lcyl.common.core.domain.BaseEntity;

/**
 * 入住申请对象 lc_checkin_apply
 * 
 * @author ruoyi
 * @date 2026-03-23
 */
public class LcCheckinApply extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 申请ID */
    private Long applyId;

    /** 申请编号 */
    @Excel(name = "申请编号")
    private String applyNo;

    /** 申请人ID */
    @Excel(name = "申请人ID")
    private Long applyUserId;

    /** 申请人姓名 */
    @Excel(name = "申请人姓名")
    private String applyUserName;

    /** 申请状态（01=基础信息待填写） */
    @Excel(name = "申请状态", readConverterExp = "0=1=基础信息待填写")
    private String applyStatus;

    /** Activiti流程实例ID */
    @Excel(name = "Activiti流程实例ID")
    private String processInstanceId;

    /** 驳回原因 */
    @Excel(name = "驳回原因")
    private String rejectReason;

    /** 删除标志（0正常 1删除） */
    private String delFlag;

    public void setApplyId(Long applyId) 
    {
        this.applyId = applyId;
    }

    public Long getApplyId() 
    {
        return applyId;
    }

    public void setApplyNo(String applyNo) 
    {
        this.applyNo = applyNo;
    }

    public String getApplyNo() 
    {
        return applyNo;
    }

    public void setApplyUserId(Long applyUserId) 
    {
        this.applyUserId = applyUserId;
    }

    public Long getApplyUserId() 
    {
        return applyUserId;
    }

    public void setApplyUserName(String applyUserName) 
    {
        this.applyUserName = applyUserName;
    }

    public String getApplyUserName() 
    {
        return applyUserName;
    }

    public void setApplyStatus(String applyStatus) 
    {
        this.applyStatus = applyStatus;
    }

    public String getApplyStatus() 
    {
        return applyStatus;
    }

    public void setProcessInstanceId(String processInstanceId) 
    {
        this.processInstanceId = processInstanceId;
    }

    public String getProcessInstanceId() 
    {
        return processInstanceId;
    }

    public void setRejectReason(String rejectReason) 
    {
        this.rejectReason = rejectReason;
    }

    public String getRejectReason() 
    {
        return rejectReason;
    }

    public void setDelFlag(String delFlag) 
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag() 
    {
        return delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("applyId", getApplyId())
            .append("applyNo", getApplyNo())
            .append("applyUserId", getApplyUserId())
            .append("applyUserName", getApplyUserName())
            .append("applyStatus", getApplyStatus())
            .append("processInstanceId", getProcessInstanceId())
            .append("rejectReason", getRejectReason())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
