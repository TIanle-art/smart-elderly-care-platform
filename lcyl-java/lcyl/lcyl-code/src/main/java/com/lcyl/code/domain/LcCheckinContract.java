package com.lcyl.code.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.lcyl.common.annotation.Excel;
import com.lcyl.common.core.domain.BaseEntity;

/**
 * 签约办理对象 lc_checkin_contract
 * 
 * @author ruoyi
 * @date 2026-03-23
 */
public class LcCheckinContract extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 关联申请ID */
    @Excel(name = "关联申请ID")
    private Long applyId;

    /** 合同编号 */
    @Excel(name = "合同编号")
    private String contractNo;

    /** 电子合同路径 */
    @Excel(name = "电子合同路径")
    private String contractPath;

    /** 签约时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "签约时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date signTime;

    /** 签约人（家属/老人） */
    @Excel(name = "签约人", readConverterExp = "家=属/老人")
    private String signPerson;

    /** 缴费状态（0未缴 1已缴） */
    @Excel(name = "缴费状态", readConverterExp = "0=未缴,1=已缴")
    private String payStatus;

    /** 实际入住时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "实际入住时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date checkinTime;

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

    public void setContractNo(String contractNo) 
    {
        this.contractNo = contractNo;
    }

    public String getContractNo() 
    {
        return contractNo;
    }

    public void setContractPath(String contractPath) 
    {
        this.contractPath = contractPath;
    }

    public String getContractPath() 
    {
        return contractPath;
    }

    public void setSignTime(Date signTime) 
    {
        this.signTime = signTime;
    }

    public Date getSignTime() 
    {
        return signTime;
    }

    public void setSignPerson(String signPerson) 
    {
        this.signPerson = signPerson;
    }

    public String getSignPerson() 
    {
        return signPerson;
    }

    public void setPayStatus(String payStatus) 
    {
        this.payStatus = payStatus;
    }

    public String getPayStatus() 
    {
        return payStatus;
    }

    public void setCheckinTime(Date checkinTime) 
    {
        this.checkinTime = checkinTime;
    }

    public Date getCheckinTime() 
    {
        return checkinTime;
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
            .append("contractNo", getContractNo())
            .append("contractPath", getContractPath())
            .append("signTime", getSignTime())
            .append("signPerson", getSignPerson())
            .append("payStatus", getPayStatus())
            .append("checkinTime", getCheckinTime())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .toString();
    }
}
