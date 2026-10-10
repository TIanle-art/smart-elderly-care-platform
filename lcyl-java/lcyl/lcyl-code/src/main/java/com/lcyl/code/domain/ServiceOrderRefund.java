package com.lcyl.code.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.lcyl.common.annotation.Excel;
import com.lcyl.common.core.domain.BaseEntity;

/**
 * 退款管理对象 lc_service_order_refund
 * 
 * @author ruoyi
 * @date 2026-03-21
 */
public class ServiceOrderRefund extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 退款编号 */
    @Excel(name = "退款编号")
    private String refundNo;

    /** 订单ID */
    private Long orderId;

    /** 订单编号(冗余) */
    @Excel(name = "订单编号(冗余)")
    private String orderNo;

    /** 退款金额 */
    @Excel(name = "退款金额")
    private BigDecimal refundAmount;

    /** 退款状态(0处理中 1成功 2失败) */
    @Excel(name = "退款状态(0处理中 1成功 2失败)")
    private String refundStatus;

    /** 申请人ID */
    private Long applicantId;

    /** 申请人姓名 */
    @Excel(name = "申请人姓名")
    private String applicantName;

    /** 申请人类型(1家属端 2后台用户) */
    private String applicantType;

    /** 申请时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "申请时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date applyTime;

    /** 退款时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "退款时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date refundTime;

    /** 退款原因 */
    private String refundReason;

    /** 退款渠道 */
    private String refundChannel;

    /** 退款方式(微信/支付宝/现金等) */
    private String refundMethod;

    /** 失败状态码 */
    private String failCode;

    /** 失败原因 */
    private String failReason;

    /** 删除标志(0存在 2删除) */
    private String delFlag;
    /** 订单状态 */
    private String orderStatus;

    public String getOrderStatus()
    {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus)
    {
        this.orderStatus = orderStatus;
    }


    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setRefundNo(String refundNo) 
    {
        this.refundNo = refundNo;
    }

    public String getRefundNo() 
    {
        return refundNo;
    }

    public void setOrderId(Long orderId) 
    {
        this.orderId = orderId;
    }

    public Long getOrderId() 
    {
        return orderId;
    }

    public void setOrderNo(String orderNo) 
    {
        this.orderNo = orderNo;
    }

    public String getOrderNo() 
    {
        return orderNo;
    }

    public void setRefundAmount(BigDecimal refundAmount) 
    {
        this.refundAmount = refundAmount;
    }

    public BigDecimal getRefundAmount() 
    {
        return refundAmount;
    }

    public void setRefundStatus(String refundStatus) 
    {
        this.refundStatus = refundStatus;
    }

    public String getRefundStatus() 
    {
        return refundStatus;
    }

    public void setApplicantId(Long applicantId) 
    {
        this.applicantId = applicantId;
    }

    public Long getApplicantId() 
    {
        return applicantId;
    }

    public void setApplicantName(String applicantName) 
    {
        this.applicantName = applicantName;
    }

    public String getApplicantName() 
    {
        return applicantName;
    }

    public void setApplicantType(String applicantType) 
    {
        this.applicantType = applicantType;
    }

    public String getApplicantType() 
    {
        return applicantType;
    }

    public void setApplyTime(Date applyTime) 
    {
        this.applyTime = applyTime;
    }

    public Date getApplyTime() 
    {
        return applyTime;
    }

    public void setRefundTime(Date refundTime) 
    {
        this.refundTime = refundTime;
    }

    public Date getRefundTime() 
    {
        return refundTime;
    }

    public void setRefundReason(String refundReason) 
    {
        this.refundReason = refundReason;
    }

    public String getRefundReason() 
    {
        return refundReason;
    }

    public void setRefundChannel(String refundChannel) 
    {
        this.refundChannel = refundChannel;
    }

    public String getRefundChannel() 
    {
        return refundChannel;
    }

    public void setRefundMethod(String refundMethod) 
    {
        this.refundMethod = refundMethod;
    }

    public String getRefundMethod() 
    {
        return refundMethod;
    }

    public void setFailCode(String failCode) 
    {
        this.failCode = failCode;
    }

    public String getFailCode() 
    {
        return failCode;
    }

    public void setFailReason(String failReason) 
    {
        this.failReason = failReason;
    }

    public String getFailReason() 
    {
        return failReason;
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
            .append("refundNo", getRefundNo())
            .append("orderId", getOrderId())
            .append("orderNo", getOrderNo())
            .append("refundAmount", getRefundAmount())
            .append("refundStatus", getRefundStatus())
            .append("applicantId", getApplicantId())
            .append("applicantName", getApplicantName())
            .append("applicantType", getApplicantType())
            .append("applyTime", getApplyTime())
            .append("refundTime", getRefundTime())
            .append("refundReason", getRefundReason())
            .append("refundChannel", getRefundChannel())
            .append("refundMethod", getRefundMethod())
            .append("failCode", getFailCode())
            .append("failReason", getFailReason())
            .append("remark", getRemark())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
