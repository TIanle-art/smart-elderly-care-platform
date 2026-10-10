package com.lcyl.code.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.lcyl.common.annotation.Excel;
import com.lcyl.common.core.domain.BaseEntity;

/**
 * 护理等级对象 lc_nursing_level
 * 
 * @author ruoyi
 * @date 2026-03-24
 */
public class LcNursingLevel extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 护理等级名称（唯一） */
    @Excel(name = "护理等级名称", readConverterExp = "唯=一")
    private String levelName;

    /** 关联护理计划ID */
    @Excel(name = "关联护理计划ID")
    private Long planId;
    /** 护理计划名称（新增，用于前端显示） */
    private String planName;

    /** 每月护理费用 */
    @Excel(name = "每月护理费用")
    private Double monthlyFee;

    /** 等级说明 */
    @Excel(name = "等级说明")
    private String levelDesc;

    /** 状态：1-启用，0-禁用 */
    @Excel(name = "状态：1-启用，0-禁用")
    private Long status;

    /** 创建人ID（关联nurse.id） */
    private Long creatorId;

    /** 创建人姓名（冗余） */
    @Excel(name = "创建人姓名", readConverterExp = "冗=余")
    private String creatorName;


    public void setPlanName(String planName) {
        this.planName = planName;
    }
    public String getPlanName() {
        return planName;
    }

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setLevelName(String levelName) 
    {
        this.levelName = levelName;
    }

    public String getLevelName() 
    {
        return levelName;
    }

    public void setPlanId(Long planId) 
    {
        this.planId = planId;
    }

    public Long getPlanId() 
    {
        return planId;
    }

    public void setMonthlyFee(Double monthlyFee) 
    {
        this.monthlyFee = monthlyFee;
    }

    public Double getMonthlyFee() 
    {
        return monthlyFee;
    }

    public void setLevelDesc(String levelDesc) 
    {
        this.levelDesc = levelDesc;
    }

    public String getLevelDesc() 
    {
        return levelDesc;
    }

    public void setStatus(Long status) 
    {
        this.status = status;
    }

    public Long getStatus() 
    {
        return status;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("levelName", getLevelName())
            .append("planId", getPlanId())
            .append("monthlyFee", getMonthlyFee())
            .append("levelDesc", getLevelDesc())
            .append("status", getStatus())
            .append("creatorId", getCreatorId())
            .append("creatorName", getCreatorName())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
