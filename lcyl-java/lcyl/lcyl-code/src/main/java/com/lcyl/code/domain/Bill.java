package com.lcyl.code.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.lcyl.common.annotation.Excel;
import com.lcyl.common.core.domain.BaseEntity;

/**
 * 账单列表对象 lc_bill
 * 
 * @author ruoyi
 * @date 2026-03-25
 */
public class Bill extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 账单编号 */
    @Excel(name = "账单编号")
    private String billNo;

    /** 账单类型(1月度账单 2费用账单) */
    @Excel(name = "账单类型(1月度账单 2费用账单)")
    private String billType;

    /** 账单月份yyyy-MM */
    @Excel(name = "账单月份yyyy-MM")
    private String billMonth;

    /** 老人ID */
    private Long elderId;

    /** 老人姓名 */
    @Excel(name = "老人姓名")
    private String elderName;

    /** 老人身份证号 */
    @Excel(name = "老人身份证号")
    private String elderIdCard;

    /** 床位ID */
    private Long bedId;

    /** 床位号 */
    private String bedNo;

    /** 账单金额 */
    @Excel(name = "账单金额")
    private BigDecimal billAmount;

    /** 应付金额 */
    @Excel(name = "应付金额")
    private BigDecimal payableAmount;

    /** 交易状态(0待支付 1已支付 2已关闭) */
    @Excel(name = "交易状态(0待支付 1已支付 2已关闭)")
    private String tradeStatus;

    /** 支付截止时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "支付截止时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date payDeadline;

    /** 账单周期开始日期 */
    private Date startDate;

    /** 账单周期结束日期 */
    private Date endDate;

    /** 共计天数 */
    private Long daysCount;

    /** 创建人ID */
    private Long creatorId;

    /** 创建人姓名 */
    private String creatorName;

    /** 取消原因 */
    private String cancelReason;

    /** 取消时间 */
    private Date cancelTime;

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

    public void setBillNo(String billNo) 
    {
        this.billNo = billNo;
    }

    public String getBillNo() 
    {
        return billNo;
    }

    public void setBillType(String billType) 
    {
        this.billType = billType;
    }

    public String getBillType() 
    {
        return billType;
    }

    public void setBillMonth(String billMonth) 
    {
        this.billMonth = billMonth;
    }

    public String getBillMonth() 
    {
        return billMonth;
    }

    public void setElderId(Long elderId) 
    {
        this.elderId = elderId;
    }

    public Long getElderId() 
    {
        return elderId;
    }

    public void setElderName(String elderName) 
    {
        this.elderName = elderName;
    }

    public String getElderName() 
    {
        return elderName;
    }

    public void setElderIdCard(String elderIdCard) 
    {
        this.elderIdCard = elderIdCard;
    }

    public String getElderIdCard() 
    {
        return elderIdCard;
    }

    public void setBedId(Long bedId) 
    {
        this.bedId = bedId;
    }

    public Long getBedId() 
    {
        return bedId;
    }

    public void setBedNo(String bedNo) 
    {
        this.bedNo = bedNo;
    }

    public String getBedNo() 
    {
        return bedNo;
    }

    public void setBillAmount(BigDecimal billAmount) 
    {
        this.billAmount = billAmount;
    }

    public BigDecimal getBillAmount() 
    {
        return billAmount;
    }

    public void setPayableAmount(BigDecimal payableAmount) 
    {
        this.payableAmount = payableAmount;
    }

    public BigDecimal getPayableAmount() 
    {
        return payableAmount;
    }

    public void setTradeStatus(String tradeStatus) 
    {
        this.tradeStatus = tradeStatus;
    }

    public String getTradeStatus() 
    {
        return tradeStatus;
    }

    public void setPayDeadline(Date payDeadline) 
    {
        this.payDeadline = payDeadline;
    }

    public Date getPayDeadline() 
    {
        return payDeadline;
    }

    public void setStartDate(Date startDate) 
    {
        this.startDate = startDate;
    }

    public Date getStartDate() 
    {
        return startDate;
    }

    public void setEndDate(Date endDate) 
    {
        this.endDate = endDate;
    }

    public Date getEndDate() 
    {
        return endDate;
    }

    public void setDaysCount(Long daysCount) 
    {
        this.daysCount = daysCount;
    }

    public Long getDaysCount() 
    {
        return daysCount;
    }

    public void setCreatorId(Long creatorId) 
    {
        this.creatorId = creatorId;
    }

    public Long getCreatorId() 
    {
        return creatorId;
    }

    public void setCreatorName(String creatorName) 
    {
        this.creatorName = creatorName;
    }

    public String getCreatorName() 
    {
        return creatorName;
    }

    public void setCancelReason(String cancelReason) 
    {
        this.cancelReason = cancelReason;
    }

    public String getCancelReason() 
    {
        return cancelReason;
    }

    public void setCancelTime(Date cancelTime) 
    {
        this.cancelTime = cancelTime;
    }

    public Date getCancelTime() 
    {
        return cancelTime;
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
            .append("billNo", getBillNo())
            .append("billType", getBillType())
            .append("billMonth", getBillMonth())
            .append("elderId", getElderId())
            .append("elderName", getElderName())
            .append("elderIdCard", getElderIdCard())
            .append("bedId", getBedId())
            .append("bedNo", getBedNo())
            .append("billAmount", getBillAmount())
            .append("payableAmount", getPayableAmount())
            .append("tradeStatus", getTradeStatus())
            .append("payDeadline", getPayDeadline())
            .append("startDate", getStartDate())
            .append("endDate", getEndDate())
            .append("daysCount", getDaysCount())
            .append("creatorId", getCreatorId())
            .append("creatorName", getCreatorName())
            .append("cancelReason", getCancelReason())
            .append("cancelTime", getCancelTime())
            .append("remark", getRemark())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
