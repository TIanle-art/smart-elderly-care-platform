package com.lcyl.code.domain;

import java.util.Date;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.lcyl.common.annotation.Excel;
import com.lcyl.common.core.domain.BaseEntity;
@Data
/**
 * 入住办理对象 check_in
 *
 * @author ruoyi
 * @date
 */
public class CheckIn extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 编号 */
    @Excel(name = "编号")
    private String checkInCode;

    /** 标题 */
    @Excel(name = "标题")
    private String title;

    /** 老人id */
    private Long elderId;

    /** 养老顾问 */
    private String counselor;

    /** 入住时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "入住时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date checkInTime;

    /** 原因 */
    private String reason;

    /** 申请人 */
    @Excel(name = "申请人")
    private String applicat;

    /** 部门编号 */
    private Long deptNo;

    /** 申请人id */
    private Long applicatId;

    /** 流程状态 */
    private Long flowStatus;

    /** 审核状态 */
    private Long status;

    /** 其他申请信息 */
    private String otherApplyInfo;

    /** 评估信息 */
    private String reviewInfo;

    /** 老人姓名 */
    private String elderName;

    /** 老人身份证号 */
    private String elderCardId;

    /** 当前任务id */
    private String currentTaskId;

    /** 当前流程id */
    private String processId;
    //评估详情
    private String evaluation;
    //评估师昵称
    private String evaluator;

    /** 当前任务节点名称 */
    private String currentTaskName;

    private String taskId;

    private String approver;

    private String singer;


    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }

    public void setCheckInCode(String checkInCode)
    {
        this.checkInCode = checkInCode;
    }

    public String getCheckInCode()
    {
        return checkInCode;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getTitle()
    {
        return title;
    }

    public void setElderId(Long elderId)
    {
        this.elderId = elderId;
    }

    public Long getElderId()
    {
        return elderId;
    }

    public void setCounselor(String counselor)
    {
        this.counselor = counselor;
    }

    public String getCounselor()
    {
        return counselor;
    }

    public void setCheckInTime(Date checkInTime)
    {
        this.checkInTime = checkInTime;
    }

    public Date getCheckInTime()
    {
        return checkInTime;
    }

    public void setReason(String reason)
    {
        this.reason = reason;
    }

    public String getReason()
    {
        return reason;
    }

    public void setApplicat(String applicat)
    {
        this.applicat = applicat;
    }

    public String getApplicat()
    {
        return applicat;
    }

    public void setDeptNo(Long deptNo)
    {
        this.deptNo = deptNo;
    }

    public Long getDeptNo()
    {
        return deptNo;
    }

    public void setApplicatId(Long applicatId)
    {
        this.applicatId = applicatId;
    }

    public Long getApplicatId()
    {
        return applicatId;
    }

    public void setFlowStatus(Long flowStatus)
    {
        this.flowStatus = flowStatus;
    }

    public Long getFlowStatus()
    {
        return flowStatus;
    }

    public void setStatus(Long status)
    {
        this.status = status;
    }

    public Long getStatus()
    {
        return status;
    }

    public void setOtherApplyInfo(String otherApplyInfo)
    {
        this.otherApplyInfo = otherApplyInfo;
    }

    public String getOtherApplyInfo()
    {
        return otherApplyInfo;
    }

    public void setReviewInfo(String reviewInfo)
    {
        this.reviewInfo = reviewInfo;
    }

    public String getReviewInfo()
    {
        return reviewInfo;
    }

    public void setElderName(String elderName)
    {
        this.elderName = elderName;
    }

    public String getElderName()
    {
        return elderName;
    }

    public void setElderCardId(String elderCardId)
    {
        this.elderCardId = elderCardId;
    }

    public String getElderCardId()
    {
        return elderCardId;
    }

    public void setCurrentTaskId(String currentTaskId)
    {
        this.currentTaskId = currentTaskId;
    }

    public String getCurrentTaskId()
    {
        return currentTaskId;
    }

    public void setProcessId(String processId)
    {
        this.processId = processId;
    }

    public String getProcessId()
    {
        return processId;
    }

    public void setCurrentTaskName(String currentTaskName)
    {
        this.currentTaskName = currentTaskName;
    }

    public String getCurrentTaskName()
    {
        return currentTaskName;
    }



    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("checkInCode", getCheckInCode())
                .append("title", getTitle())
                .append("elderId", getElderId())
                .append("counselor", getCounselor())
                .append("checkInTime", getCheckInTime())
                .append("reason", getReason())
                .append("remark", getRemark())
                .append("applicat", getApplicat())
                .append("deptNo", getDeptNo())
                .append("applicatId", getApplicatId())
                .append("createTime", getCreateTime())
                .append("flowStatus", getFlowStatus())
                .append("status", getStatus())
                .append("otherApplyInfo", getOtherApplyInfo())
                .append("reviewInfo", getReviewInfo())
                .append("updateTime", getUpdateTime())
                .append("elderName", getElderName())
                .append("elderCardId", getElderCardId())
                .append("currentTaskId", getCurrentTaskId())
                .append("processId", getProcessId())
                .append("currentTaskName", getCurrentTaskName())
                .toString();
    }

    public String getName() {
        if (otherApplyInfo == null) return null;
        JSONObject json = JSONObject.parseObject(otherApplyInfo);
        JSONObject basicForm = json.getJSONObject("basicForm");
        return basicForm != null ? basicForm.getString("name") : null;
    }

    public String getIdCardNo() {
        if (otherApplyInfo == null) return null;
        JSONObject json = JSONObject.parseObject(otherApplyInfo);
        JSONObject basicForm = json.getJSONObject("basicForm");
        return basicForm != null ? basicForm.getString("idCard") : null;
    }
}
