package com.lcyl.code.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.lcyl.common.annotation.Excel;
import com.lcyl.common.core.domain.BaseEntity;

/**
 * 审批发起人对象 lc_checkin_approve_initiator
 * 
 * @author ruoyi
 * @date 2026-03-23
 */
public class LcCheckinApproveInitiator extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 关联申请ID */
    @Excel(name = "关联申请ID")
    private Long applyId;

    /** 审批发起人ID */
    @Excel(name = "审批发起人ID")
    private Long initiatorId;

    /** 审批发起人姓名 */
    @Excel(name = "审批发起人姓名")
    private String initiatorName;

    /** 发起审批时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "发起审批时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date initiateTime;

    /** 删除标志 */
    private String delFlag;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setApplyId(Long applyId) 
    {
        this.applyId = applyId;
    }

    public Long getApplyId() 
    {
        return applyId;
    }

    public void setInitiatorId(Long initiatorId) 
    {
        this.initiatorId = initiatorId;
    }

    public Long getInitiatorId() 
    {
        return initiatorId;
    }

    public void setInitiatorName(String initiatorName) 
    {
        this.initiatorName = initiatorName;
    }

    public String getInitiatorName() 
    {
        return initiatorName;
    }

    public void setInitiateTime(Date initiateTime) 
    {
        this.initiateTime = initiateTime;
    }

    public Date getInitiateTime() 
    {
        return initiateTime;
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
            .append("id", getId())
            .append("applyId", getApplyId())
            .append("initiatorId", getInitiatorId())
            .append("initiatorName", getInitiatorName())
            .append("initiateTime", getInitiateTime())
            .append("delFlag", getDelFlag())
            .toString();
    }
}
