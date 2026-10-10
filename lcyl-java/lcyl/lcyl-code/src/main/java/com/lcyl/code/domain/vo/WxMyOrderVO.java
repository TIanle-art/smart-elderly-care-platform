package com.lcyl.code.domain.vo;

import java.math.BigDecimal;
import java.util.Date;

public class WxMyOrderVO
{
    private Long id;
    private String orderNo;
    private Long elderId;
    private String elderName;
    private String bedNo;
    private String projectName;
    private String projectImage;
    private BigDecimal orderAmount;
    private Date expectedServiceTime;
    private String orderStatus;
    private String tradeStatus;
    private Date createTime;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getOrderNo()
    {
        return orderNo;
    }

    public void setOrderNo(String orderNo)
    {
        this.orderNo = orderNo;
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

    public String getBedNo()
    {
        return bedNo;
    }

    public void setBedNo(String bedNo)
    {
        this.bedNo = bedNo;
    }

    public String getProjectName()
    {
        return projectName;
    }

    public void setProjectName(String projectName)
    {
        this.projectName = projectName;
    }

    public BigDecimal getOrderAmount()
    {
        return orderAmount;
    }

    public String getProjectImage()
    {
        return projectImage;
    }

    public void setProjectImage(String projectImage)
    {
        this.projectImage = projectImage;
    }

    public void setOrderAmount(BigDecimal orderAmount)
    {
        this.orderAmount = orderAmount;
    }

    public Date getExpectedServiceTime()
    {
        return expectedServiceTime;
    }

    public void setExpectedServiceTime(Date expectedServiceTime)
    {
        this.expectedServiceTime = expectedServiceTime;
    }

    public String getOrderStatus()
    {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus)
    {
        this.orderStatus = orderStatus;
    }

    public String getTradeStatus()
    {
        return tradeStatus;
    }

    public void setTradeStatus(String tradeStatus)
    {
        this.tradeStatus = tradeStatus;
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
