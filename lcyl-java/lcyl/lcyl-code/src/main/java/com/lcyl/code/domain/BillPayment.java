package com.lcyl.code.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.lcyl.common.annotation.Excel;
import com.lcyl.common.core.domain.BaseEntity;

/**
 * 账单支付记录对象 lc_bill_payment
 * 
 * @author ruoyi
 * @date 2026-03-26
 */
public class BillPayment extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 支付记录编号 */
    @Excel(name = "支付记录编号")
    private String paymentNo;

    /** 账单ID */
    private Long billId;

    /** 账单编号 */
    @Excel(name = "账单编号")
    private String billNo;

    /** 支付人 */
    @Excel(name = "支付人")
    private String payerName;

    /** 支付人手机号 */
    private String payerPhone;

    /** 支付时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "支付时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date payTime;

    /** 支付渠道(1线上支付 2线下支付) */
    @Excel(name = "支付渠道(1线上支付 2线下支付)")
    private String payChannel;

    /** 支付方式(微信/支付宝/网银/现金/预缴款/其他) */
    @Excel(name = "支付方式(微信/支付宝/网银/现金/预缴款/其他)")
    private String payMethod;

    /** 微信支付订单号 */
    private String wechatOrderNo;

    /** 支付金额 */
    @Excel(name = "支付金额")
    private BigDecimal payAmount;

    /** 支付凭证 */
    private String voucherUrl;

    /** 支付备注 */
    private String payRemark;

    /** 操作人ID */
    private Long operatorId;

    /** 操作人姓名 */
    @Excel(name = "操作人姓名")
    private String operatorName;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setPaymentNo(String paymentNo) 
    {
        this.paymentNo = paymentNo;
    }

    public String getPaymentNo() 
    {
        return paymentNo;
    }

    public void setBillId(Long billId) 
    {
        this.billId = billId;
    }

    public Long getBillId() 
    {
        return billId;
    }

    public void setBillNo(String billNo) 
    {
        this.billNo = billNo;
    }

    public String getBillNo() 
    {
        return billNo;
    }

    public void setPayerName(String payerName) 
    {
        this.payerName = payerName;
    }

    public String getPayerName() 
    {
        return payerName;
    }

    public void setPayerPhone(String payerPhone) 
    {
        this.payerPhone = payerPhone;
    }

    public String getPayerPhone() 
    {
        return payerPhone;
    }

    public void setPayTime(Date payTime) 
    {
        this.payTime = payTime;
    }

    public Date getPayTime() 
    {
        return payTime;
    }

    public void setPayChannel(String payChannel) 
    {
        this.payChannel = payChannel;
    }

    public String getPayChannel() 
    {
        return payChannel;
    }

    public void setPayMethod(String payMethod) 
    {
        this.payMethod = payMethod;
    }

    public String getPayMethod() 
    {
        return payMethod;
    }

    public void setWechatOrderNo(String wechatOrderNo) 
    {
        this.wechatOrderNo = wechatOrderNo;
    }

    public String getWechatOrderNo() 
    {
        return wechatOrderNo;
    }

    public void setPayAmount(BigDecimal payAmount) 
    {
        this.payAmount = payAmount;
    }

    public BigDecimal getPayAmount() 
    {
        return payAmount;
    }

    public void setVoucherUrl(String voucherUrl) 
    {
        this.voucherUrl = voucherUrl;
    }

    public String getVoucherUrl() 
    {
        return voucherUrl;
    }

    public void setPayRemark(String payRemark) 
    {
        this.payRemark = payRemark;
    }

    public String getPayRemark() 
    {
        return payRemark;
    }

    public void setOperatorId(Long operatorId) 
    {
        this.operatorId = operatorId;
    }

    public Long getOperatorId() 
    {
        return operatorId;
    }

    public void setOperatorName(String operatorName) 
    {
        this.operatorName = operatorName;
    }

    public String getOperatorName() 
    {
        return operatorName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("paymentNo", getPaymentNo())
            .append("billId", getBillId())
            .append("billNo", getBillNo())
            .append("payerName", getPayerName())
            .append("payerPhone", getPayerPhone())
            .append("payTime", getPayTime())
            .append("payChannel", getPayChannel())
            .append("payMethod", getPayMethod())
            .append("wechatOrderNo", getWechatOrderNo())
            .append("payAmount", getPayAmount())
            .append("voucherUrl", getVoucherUrl())
            .append("payRemark", getPayRemark())
            .append("operatorId", getOperatorId())
            .append("operatorName", getOperatorName())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .toString();
    }
}
