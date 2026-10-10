package com.lcyl.code.domain.dto;

public class ServiceOrderRefundFailDTO
{
    private Long refundId;
    private String failCode;
    private String failReason;

    public Long getRefundId()
    {
        return refundId;
    }

    public void setRefundId(Long refundId)
    {
        this.refundId = refundId;
    }

    public String getFailCode()
    {
        return failCode;
    }

    public void setFailCode(String failCode)
    {
        this.failCode = failCode;
    }

    public String getFailReason()
    {
        return failReason;
    }

    public void setFailReason(String failReason)
    {
        this.failReason = failReason;
    }
}
