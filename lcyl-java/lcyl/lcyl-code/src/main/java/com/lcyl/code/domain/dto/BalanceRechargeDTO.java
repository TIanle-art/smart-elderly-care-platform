package com.lcyl.code.domain.dto;

import java.math.BigDecimal;
import java.util.Date;

public class BalanceRechargeDTO
{
    private Long elderId;
    private String elderName;
    private String bedNo;
    private String rechargeNo;
    private String rechargeMethod;
    private BigDecimal rechargeAmount;
    private String voucherUrl;
    private String rechargeRemark;
    private String status;
    private String createBy;
    private Date createTime;
    private String updateBy;
    private Date updateTime;
    private String delFlag;
    private String remark;

    public Long getElderId()
    {
        return elderId;
    }

    public void setElderId(Long elderId)
    {
        this.elderId = elderId;
    }

    public String getElderName()
    {
        return elderName;
    }

    public void setElderName(String elderName)
    {
        this.elderName = elderName;
    }

    public String getBedNo()
    {
        return bedNo;
    }

    public void setBedNo(String bedNo)
    {
        this.bedNo = bedNo;
    }

    public String getRechargeNo()
    {
        return rechargeNo;
    }

    public void setRechargeNo(String rechargeNo)
    {
        this.rechargeNo = rechargeNo;
    }

    public String getRechargeMethod()
    {
        return rechargeMethod;
    }

    public void setRechargeMethod(String rechargeMethod)
    {
        this.rechargeMethod = rechargeMethod;
    }

    public BigDecimal getRechargeAmount()
    {
        return rechargeAmount;
    }

    public void setRechargeAmount(BigDecimal rechargeAmount)
    {
        this.rechargeAmount = rechargeAmount;
    }

    public String getVoucherUrl()
    {
        return voucherUrl;
    }

    public void setVoucherUrl(String voucherUrl)
    {
        this.voucherUrl = voucherUrl;
    }

    public String getRechargeRemark()
    {
        return rechargeRemark;
    }

    public void setRechargeRemark(String rechargeRemark)
    {
        this.rechargeRemark = rechargeRemark;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getCreateBy()
    {
        return createBy;
    }

    public void setCreateBy(String createBy)
    {
        this.createBy = createBy;
    }

    public Date getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }

    public String getUpdateBy()
    {
        return updateBy;
    }

    public void setUpdateBy(String updateBy)
    {
        this.updateBy = updateBy;
    }

    public Date getUpdateTime()
    {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime)
    {
        this.updateTime = updateTime;
    }

    public String getDelFlag()
    {
        return delFlag;
    }

    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag;
    }

    public String getRemark()
    {
        return remark;
    }

    public void setRemark(String remark)
    {
        this.remark = remark;
    }
}
