package com.lcyl.code.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.lcyl.common.annotation.Excel;
import com.lcyl.common.core.domain.BaseEntity;

/**
 * 评估报告对象 lc_checkin_evaluate_report
 * 
 * @author ruoyi
 * @date 2026-03-23
 */
public class LcCheckinEvaluateReport extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 关联申请ID */
    @Excel(name = "关联申请ID")
    private Long applyId;

    /** 评估报告内容 */
    @Excel(name = "评估报告内容")
    private String reportContent;

    /** 报告编号 */
    @Excel(name = "报告编号")
    private String reportNo;

    /** 报告生成时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "报告生成时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date reportTime;

    /** 医生签字 */
    @Excel(name = "医生签字")
    private String doctorSign;

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

    public void setReportContent(String reportContent) 
    {
        this.reportContent = reportContent;
    }

    public String getReportContent() 
    {
        return reportContent;
    }

    public void setReportNo(String reportNo) 
    {
        this.reportNo = reportNo;
    }

    public String getReportNo() 
    {
        return reportNo;
    }

    public void setReportTime(Date reportTime) 
    {
        this.reportTime = reportTime;
    }

    public Date getReportTime() 
    {
        return reportTime;
    }

    public void setDoctorSign(String doctorSign) 
    {
        this.doctorSign = doctorSign;
    }

    public String getDoctorSign() 
    {
        return doctorSign;
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
            .append("reportContent", getReportContent())
            .append("reportNo", getReportNo())
            .append("reportTime", getReportTime())
            .append("doctorSign", getDoctorSign())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .toString();
    }
}
