package com.lcyl.code.domain.dto;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 支付账单参数对象
 *
 * @author ruoyi
 * @date 2026-03-26
 */
public class BillPayDTO
{
    /** 账单ID */
    private Long billId;

    /** 账单编号 */
    private String billNo;

    /** 支付记录编号 */
    private String paymentNo;

    /** 支付时间 */
    private Date payTime;

    /** 支付渠道(1线上支付 2线下支付) */
    private String payChannel;

    /** 支付方式 */
    private String payMethod;

    /** 微信支付订单号 */
    private String wechatOrderNo;

    /** 支付金额 */
    private BigDecimal payAmount;

    /** 支付凭证 */
    private String voucherUrl;

    /** 支付备注 */
    private String payRemark;

    /** 操作人ID */
    private Long operatorId;

    /** 操作人姓名 */
    private String operatorName;

    /** 创建者 */
    private String createBy;

    /** 创建时间 */
    private Date createTime;

    public Long getBillId()
    {
        return billId;
    }

    public void setBillId(Long billId)
    {
        this.billId = billId;
    }

    public String getBillNo()
    {
        return billNo;
    }

    public void setBillNo(String billNo)
    {
        this.billNo = billNo;
    }

    public String getPaymentNo()
    {
        return paymentNo;
    }

    public void setPaymentNo(String paymentNo)
    {
        this.paymentNo = paymentNo;
    }

    public Date getPayTime()
    {
        return payTime;
    }

    public void setPayTime(Date payTime)
    {
        this.payTime = payTime;
    }

    public String getPayChannel()
    {
        return payChannel;
    }

    public void setPayChannel(String payChannel)
    {
        this.payChannel = payChannel;
    }

    public String getPayMethod()
    {
        return payMethod;
    }

    public void setPayMethod(String payMethod)
    {
        this.payMethod = payMethod;
    }

    public String getWechatOrderNo()
    {
        return wechatOrderNo;
    }

    public void setWechatOrderNo(String wechatOrderNo)
    {
        this.wechatOrderNo = wechatOrderNo;
    }

    public BigDecimal getPayAmount()
    {
        return payAmount;
    }

    public void setPayAmount(BigDecimal payAmount)
    {
        this.payAmount = payAmount;
    }

    public String getVoucherUrl()
    {
        return voucherUrl;
    }

    public void setVoucherUrl(String voucherUrl)
    {
        this.voucherUrl = voucherUrl;
    }

    public String getPayRemark()
    {
        return payRemark;
    }

    public void setPayRemark(String payRemark)
    {
        this.payRemark = payRemark;
    }

    public Long getOperatorId()
    {
        return operatorId;
    }

    public void setOperatorId(Long operatorId)
    {
        this.operatorId = operatorId;
    }

    public String getOperatorName()
    {
        return operatorName;
    }

    public void setOperatorName(String operatorName)
    {
        this.operatorName = operatorName;
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
}
