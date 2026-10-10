package com.lcyl.code.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.lcyl.common.annotation.Excel;
import com.lcyl.common.core.domain.BaseEntity;

/**
 * 审批记录对象 lc_checkin_approve_record
 * 
 * @author ruoyi
 * @date 2026-03-23
 */
public class LcCheckinApproveRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 关联申请ID */
    @Excel(name = "关联申请ID")
    private Long applyId;

    /** 审批人ID */
    @Excel(name = "审批人ID")
    private Long approverId;

    /** 审批人姓名 */
    @Excel(name = "审批人姓名")
    private String approverName;

    /** 审批节点（初审/终审/院长审批） */
    @Excel(name = "审批节点", readConverterExp = "初=审/终审/院长审批")
    private String approveNode;

    /** 审批结果（pass/ reject） */
    @Excel(name = "审批结果", readConverterExp = "p=ass/,r=eject")
    private String approveResult;

    /** 审批意见 */
    @Excel(name = "审批意见")
    private String approveOpinion;

    /** 审批时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "审批时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date approveTime;

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

    public void setApproverId(Long approverId) 
    {
        this.approverId = approverId;
    }

    public Long getApproverId() 
    {
        return approverId;
    }

    public void setApproverName(String approverName) 
    {
        this.approverName = approverName;
    }

    public String getApproverName() 
    {
        return approverName;
    }

    public void setApproveNode(String approveNode) 
    {
        this.approveNode = approveNode;
    }

    public String getApproveNode() 
    {
        return approveNode;
    }

    public void setApproveResult(String approveResult) 
    {
        this.approveResult = approveResult;
    }

    public String getApproveResult() 
    {
        return approveResult;
    }

    public void setApproveOpinion(String approveOpinion) 
    {
        this.approveOpinion = approveOpinion;
    }

    public String getApproveOpinion() 
    {
        return approveOpinion;
    }

    public void setApproveTime(Date approveTime) 
    {
        this.approveTime = approveTime;
    }

    public Date getApproveTime() 
    {
        return approveTime;
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
            .append("approverId", getApproverId())
            .append("approverName", getApproverName())
            .append("approveNode", getApproveNode())
            .append("approveResult", getApproveResult())
            .append("approveOpinion", getApproveOpinion())
            .append("approveTime", getApproveTime())
            .append("delFlag", getDelFlag())
            .toString();
    }
}
