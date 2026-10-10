package com.lcyl.code.domain.dto;

public class ServiceOrderRefundApplyDTO
{
    /** 订单ID */
    private Long orderId;

    /** 退款原因 */
    private String refundReason;

    /** 申请来源：1家属端 2后台 */
    private String applicantType;

    public Long getOrderId()
    {
        return orderId;
    }

    public void setOrderId(Long orderId)
    {
        this.orderId = orderId;
    }

    public String getRefundReason()
    {
        return refundReason;
    }

    public void setRefundReason(String refundReason)
    {
        this.refundReason = refundReason;
    }

    public String getApplicantType()
    {
        return applicantType;
    }

    public void setApplicantType(String applicantType)
    {
        this.applicantType = applicantType;
    }
}
