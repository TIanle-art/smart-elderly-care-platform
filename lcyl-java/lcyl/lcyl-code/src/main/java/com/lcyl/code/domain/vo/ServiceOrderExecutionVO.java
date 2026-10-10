package com.lcyl.code.domain.vo;

import java.util.Date;

public class ServiceOrderExecutionVO
{
    private Long id;
    private Long orderId;
    private String executeStatus;
    private String cancelReason;
    private Date cancelTime;
    private Date executeTime;
    private String executeImage;
    private String executeRecord;
    private Long executorId;
    private String executorName;
    private Date createTime;
    private Date updateTime;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getOrderId()
    {
        return orderId;
    }

    public void setOrderId(Long orderId)
    {
        this.orderId = orderId;
    }

    public String getExecuteStatus()
    {
        return executeStatus;
    }

    public void setExecuteStatus(String executeStatus)
    {
        this.executeStatus = executeStatus;
    }

    public String getCancelReason()
    {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason)
    {
        this.cancelReason = cancelReason;
    }

    public Date getCancelTime()
    {
        return cancelTime;
    }

    public void setCancelTime(Date cancelTime)
    {
        this.cancelTime = cancelTime;
    }

    public Date getExecuteTime()
    {
        return executeTime;
    }

    public void setExecuteTime(Date executeTime)
    {
        this.executeTime = executeTime;
    }

    public String getExecuteImage()
    {
        return executeImage;
    }

    public void setExecuteImage(String executeImage)
    {
        this.executeImage = executeImage;
    }

    public String getExecuteRecord()
    {
        return executeRecord;
    }

    public void setExecuteRecord(String executeRecord)
    {
        this.executeRecord = executeRecord;
    }

    public Long getExecutorId()
    {
        return executorId;
    }

    public void setExecutorId(Long executorId)
    {
        this.executorId = executorId;
    }

    public String getExecutorName()
    {
        return executorName;
    }

    public void setExecutorName(String executorName)
    {
        this.executorName = executorName;
    }

    public Date getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }

    public Date getUpdateTime()
    {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime)
    {
        this.updateTime = updateTime;
    }
}
