package com.lcyl.code.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.lcyl.common.annotation.Excel;
import com.lcyl.common.core.domain.BaseEntity;

/**
 * 入住配置对象 lc_checkin_config
 * 
 * @author ruoyi
 * @date 2026-03-23
 */
public class LcCheckinConfig extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 关联申请ID */
    @Excel(name = "关联申请ID")
    private Long applyId;

    /** 床位号 */
    @Excel(name = "床位号")
    private String bedNo;

    /** 护理员ID */
    @Excel(name = "护理员ID")
    private Long nurseId;

    /** 护理员姓名 */
    @Excel(name = "护理员姓名")
    private String nurseName;

    /** 护理费（元/月） */
    @Excel(name = "护理费", readConverterExp = "元=/月")
    private BigDecimal careFee;

    /** 床位费（元/月） */
    @Excel(name = "床位费", readConverterExp = "元=/月")
    private BigDecimal roomFee;

    /** 总费用（元/月） */
    @Excel(name = "总费用", readConverterExp = "元=/月")
    private BigDecimal totalFee;

    /** 配置时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "配置时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date configTime;

    /** 删除标志 */
    private String delFlag;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setApplyId(Long applyId) 
    {
        this.applyId = applyId;
    }

    public Long getApplyId() 
    {
        return applyId;
    }

    public void setBedNo(String bedNo) 
    {
        this.bedNo = bedNo;
    }

    public String getBedNo() 
    {
        return bedNo;
    }

    public void setNurseId(Long nurseId) 
    {
        this.nurseId = nurseId;
    }

    public Long getNurseId() 
    {
        return nurseId;
    }

    public void setNurseName(String nurseName) 
    {
        this.nurseName = nurseName;
    }

    public String getNurseName() 
    {
        return nurseName;
    }

    public void setCareFee(BigDecimal careFee) 
    {
        this.careFee = careFee;
    }

    public BigDecimal getCareFee() 
    {
        return careFee;
    }

    public void setRoomFee(BigDecimal roomFee) 
    {
        this.roomFee = roomFee;
    }

    public BigDecimal getRoomFee() 
    {
        return roomFee;
    }

    public void setTotalFee(BigDecimal totalFee) 
    {
        this.totalFee = totalFee;
    }

    public BigDecimal getTotalFee() 
    {
        return totalFee;
    }

    public void setConfigTime(Date configTime) 
    {
        this.configTime = configTime;
    }

    public Date getConfigTime() 
    {
        return configTime;
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
            .append("applyId", getApplyId())
            .append("bedNo", getBedNo())
            .append("nurseId", getNurseId())
            .append("nurseName", getNurseName())
            .append("careFee", getCareFee())
            .append("roomFee", getRoomFee())
            .append("totalFee", getTotalFee())
            .append("configTime", getConfigTime())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .toString();
    }
}
