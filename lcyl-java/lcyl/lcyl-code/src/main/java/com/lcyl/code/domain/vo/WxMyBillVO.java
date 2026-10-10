package com.lcyl.code.domain.vo;

import java.math.BigDecimal;
import java.util.Date;

public class WxMyBillVO
{
    private Long id;
    private String billNo;
    private String billType;
    private Long elderId;
    private String elderName;
    private String billMonth;
    private BigDecimal payableAmount;
    private String tradeStatus;
    private Date payDeadline;
    private Date createTime;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getBillNo()
    {
        return billNo;
    }

    public void setBillNo(String billNo)
    {
        this.billNo = billNo;
    }

    public String getBillType()
    {
        return billType;
    }

    public void setBillType(String billType)
    {
        this.billType = billType;
    }

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

    public String getBillMonth()
    {
        return billMonth;
    }

    public void setBillMonth(String billMonth)
    {
        this.billMonth = billMonth;
    }

    public BigDecimal getPayableAmount()
    {
        return payableAmount;
    }

    public void setPayableAmount(BigDecimal payableAmount)
    {
        this.payableAmount = payableAmount;
    }

    public String getTradeStatus()
    {
        return tradeStatus;
    }

    public void setTradeStatus(String tradeStatus)
    {
        this.tradeStatus = tradeStatus;
    }

    public Date getPayDeadline()
    {
        return payDeadline;
    }

    public void setPayDeadline(Date payDeadline)
    {
        this.payDeadline = payDeadline;
    }

    public Date getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }
}
