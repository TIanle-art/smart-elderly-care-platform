package com.lcyl.code.domain.vo;

import com.lcyl.code.domain.ServiceOrder;
import com.lcyl.code.domain.ServiceOrderRefund;

public class WxMyOrderDetailVO
{
    private ServiceOrder orderInfo;
    private ServiceOrderExecutionVO executionInfo;
    private ServiceOrderRefund refundInfo;
    private String projectImage;

    public ServiceOrder getOrderInfo()
    {
        return orderInfo;
    }

    public void setOrderInfo(ServiceOrder orderInfo)
    {
        this.orderInfo = orderInfo;
    }

    public ServiceOrderExecutionVO getExecutionInfo()
    {
        return executionInfo;
    }

    public void setExecutionInfo(ServiceOrderExecutionVO executionInfo)
    {
        this.executionInfo = executionInfo;
    }

    public ServiceOrderRefund getRefundInfo()
    {
        return refundInfo;
    }

    public void setRefundInfo(ServiceOrderRefund refundInfo)
    {
        this.refundInfo = refundInfo;
    }

    public String getProjectImage()
    {
        return projectImage;
    }

    public void setProjectImage(String projectImage)
    {
        this.projectImage = projectImage;
    }
}
