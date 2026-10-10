package com.lcyl.code.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.lcyl.common.annotation.Excel;
import com.lcyl.common.core.domain.BaseEntity;

/**
 * 余额对象 balance
 * 
 * @author ruoyi
 * @date 2026-03-27
 */
public class Balance extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 预交款余额 */
    @Excel(name = "预交款余额")
    private BigDecimal prepaidBalance;

    /** 押金金额 */
    @Excel(name = "押金金额")
    private BigDecimal depositAmount;

    /** 欠费金额（元） */
    @Excel(name = "欠费金额", readConverterExp = "元=")
    private BigDecimal arrearsAmount;

    /** 支付截止时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "支付截止时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date paymentDeadline;

    /** 状态（0：正常，1：退住  2：入住未缴押金） */
    private Long status;

    /** 老人ID */
    private Long elderId;

    /** 是否只查询欠费老人(1是) */
    private String arrearsOnly;

    /** 老人姓名 */
    @Excel(name = "老人姓名")
    private String elderName;

    /** 床位号 */
    @Excel(name = "床位号")
    private String bedNo;

    /** 删除标志(0存在 2删除) */
    private String delFlag;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setPrepaidBalance(BigDecimal prepaidBalance) 
    {
        this.prepaidBalance = prepaidBalance;
    }

    public BigDecimal getPrepaidBalance() 
    {
        return prepaidBalance;
    }

    public void setDepositAmount(BigDecimal depositAmount) 
    {
        this.depositAmount = depositAmount;
    }

    public BigDecimal getDepositAmount() 
    {
        return depositAmount;
    }

    public void setArrearsAmount(BigDecimal arrearsAmount) 
    {
        this.arrearsAmount = arrearsAmount;
    }

    public BigDecimal getArrearsAmount() 
    {
        return arrearsAmount;
    }

    public void setPaymentDeadline(Date paymentDeadline) 
    {
        this.paymentDeadline = paymentDeadline;
    }

    public Date getPaymentDeadline() 
    {
        return paymentDeadline;
    }

    public void setStatus(Long status) 
    {
        this.status = status;
    }

    public Long getStatus() 
    {
        return status;
    }

    public void setElderId(Long elderId) 
    {
        this.elderId = elderId;
    }

    public Long getElderId() 
    {
        return elderId;
    }

    public void setArrearsOnly(String arrearsOnly)
    {
        this.arrearsOnly = arrearsOnly;
    }

    public String getArrearsOnly()
    {
        return arrearsOnly;
    }

    public void setElderName(String elderName) 
    {
        this.elderName = elderName;
    }

    public String getElderName() 
    {
        return elderName;
    }

    public void setBedNo(String bedNo) 
    {
        this.bedNo = bedNo;
    }

    public String getBedNo() 
    {
        return bedNo;
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
            .append("prepaidBalance", getPrepaidBalance())
            .append("depositAmount", getDepositAmount())
            .append("arrearsAmount", getArrearsAmount())
            .append("paymentDeadline", getPaymentDeadline())
            .append("status", getStatus())
            .append("elderId", getElderId())
            .append("arrearsOnly", getArrearsOnly())
            .append("elderName", getElderName())
            .append("bedNo", getBedNo())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("createBy", getCreateBy())
            .append("updateBy", getUpdateBy())
            .append("remark", getRemark())
            .append("delFlag", getDelFlag())
            .toString();
    }
}
