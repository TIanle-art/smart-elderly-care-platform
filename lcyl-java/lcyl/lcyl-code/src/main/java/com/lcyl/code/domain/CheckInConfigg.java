package com.lcyl.code.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.lcyl.common.annotation.Excel;
import com.lcyl.common.core.domain.BaseEntity;

@Data
/**
 * 入住配置表对象 check_in_config
 *
 * @author ruoyi
 * @date
 */
public class CheckInConfigg extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 入住ID（业务ID） */
    private Long checkInId;

    /** 老人ID */
    @Excel(name = "老人ID")
    private Long elderId;

    /** 入住开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "入住开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date checkInStartTime;

    /** 入住结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "入住结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date checkInEndTime;

    /** 护理等级ID */
    @Excel(name = "护理等级ID")
    private Long nursingLevelId;

    /** 床位号（展示用，如 1楼101房间101床位） */
    @Excel(name = "床位号")
    private String bedNo;

    /** 床位ID（用于提交后更新床位状态为1） */
    private Long bedId;

    /** 费用开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "费用开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date costStartTime;

    /** 费用结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "费用结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date costEndTime;

    /** 押金（元） */
    @Excel(name = "押金", readConverterExp = "元=")
    private BigDecimal depositAmount;

    /** 护理费用（元/月） */
    @Excel(name = "护理费用", readConverterExp = "元=/月")
    private BigDecimal nursingCost;

    /** 床位费用（元/月） */
    @Excel(name = "床位费用", readConverterExp = "元=/月")
    private BigDecimal bedCost;

    /** 其他费用（元/月） */
    @Excel(name = "其他费用", readConverterExp = "元=/月")
    private BigDecimal otherCost;

    /** 医保支付（元/月） */
    @Excel(name = "医保支付", readConverterExp = "元=/月")
    private BigDecimal medicalInsurancePayment;

    /** 政府补贴（元/月） */
    @Excel(name = "政府补贴", readConverterExp = "元=/月")
    private BigDecimal governmentSubsidy;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }

    public void setElderId(Long elderId)
    {
        this.elderId = elderId;
    }

    public Long getElderId()
    {
        return elderId;
    }

    public void setCheckInStartTime(Date checkInStartTime)
    {
        this.checkInStartTime = checkInStartTime;
    }

    public Date getCheckInStartTime()
    {
        return checkInStartTime;
    }

    public void setCheckInEndTime(Date checkInEndTime)
    {
        this.checkInEndTime = checkInEndTime;
    }

    public Date getCheckInEndTime()
    {
        return checkInEndTime;
    }

    public void setNursingLevelId(Long nursingLevelId)
    {
        this.nursingLevelId = nursingLevelId;
    }

    public Long getNursingLevelId()
    {
        return nursingLevelId;
    }

    public void setBedNo(String bedNo)
    {
        this.bedNo = bedNo;
    }

    public String getBedNo()
    {
        return bedNo;
    }

    public void setBedId(Long bedId)
    {
        this.bedId = bedId;
    }

    public Long getBedId()
    {
        return bedId;
    }

    public void setCostStartTime(Date costStartTime)
    {
        this.costStartTime = costStartTime;
    }

    public Date getCostStartTime()
    {
        return costStartTime;
    }

    public void setCostEndTime(Date costEndTime)
    {
        this.costEndTime = costEndTime;
    }

    public Date getCostEndTime()
    {
        return costEndTime;
    }

    public void setDepositAmount(BigDecimal depositAmount)
    {
        this.depositAmount = depositAmount;
    }

    public BigDecimal getDepositAmount()
    {
        return depositAmount;
    }

    public void setNursingCost(BigDecimal nursingCost)
    {
        this.nursingCost = nursingCost;
    }

    public BigDecimal getNursingCost()
    {
        return nursingCost;
    }

    public void setBedCost(BigDecimal bedCost)
    {
        this.bedCost = bedCost;
    }

    public BigDecimal getBedCost()
    {
        return bedCost;
    }

    public void setOtherCost(BigDecimal otherCost)
    {
        this.otherCost = otherCost;
    }

    public BigDecimal getOtherCost()
    {
        return otherCost;
    }

    public void setMedicalInsurancePayment(BigDecimal medicalInsurancePayment)
    {
        this.medicalInsurancePayment = medicalInsurancePayment;
    }

    public BigDecimal getMedicalInsurancePayment()
    {
        return medicalInsurancePayment;
    }

    public void setGovernmentSubsidy(BigDecimal governmentSubsidy)
    {
        this.governmentSubsidy = governmentSubsidy;
    }

    public BigDecimal getGovernmentSubsidy()
    {
        return governmentSubsidy;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("elderId", getElderId())
                .append("checkInStartTime", getCheckInStartTime())
                .append("checkInEndTime", getCheckInEndTime())
                .append("nursingLevelId", getNursingLevelId())
                .append("bedNo", getBedNo())
                .append("costStartTime", getCostStartTime())
                .append("costEndTime", getCostEndTime())
                .append("depositAmount", getDepositAmount())
                .append("nursingCost", getNursingCost())
                .append("bedCost", getBedCost())
                .append("otherCost", getOtherCost())
                .append("medicalInsurancePayment", getMedicalInsurancePayment())
                .append("governmentSubsidy", getGovernmentSubsidy())
                .append("createTime", getCreateTime())
                .append("updateTime", getUpdateTime())
                .append("createBy", getCreateBy())
                .append("updateBy", getUpdateBy())
                .append("remark", getRemark())
                .toString();
    }

    private Integer status;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
