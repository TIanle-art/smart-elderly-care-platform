package com.lcyl.code.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.lcyl.common.annotation.Excel;
import com.lcyl.common.core.domain.BaseEntity;

/**
 * 护理计划-项目关联对象 lc_nursing_plan_item
 * 
 * @author ruoyi
 * @date 2026-03-23
 */
public class NursingPlanItem extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 护理计划ID */
    @Excel(name = "护理计划ID")
    private Long planId;

    /** 护理项目ID */
    @Excel(name = "护理项目ID")
    private Long itemId;

    /** 期望服务时长（分钟） */
    @Excel(name = "期望服务时间")
    private String expectedTime;

    /** 执行周期：每日/每周/每月 */
    @Excel(name = "执行周期：每日/每周/每月")
    private String executeCycle;

    /** 执行次数（1-7次） */
    @Excel(name = "执行次数", readConverterExp = "1=-7次")
    private Long executeTimes;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setPlanId(Long planId) 
    {
        this.planId = planId;
    }

    public Long getPlanId() 
    {
        return planId;
    }
    public void setItemId(Long itemId) 
    {
        this.itemId = itemId;
    }

    public Long getItemId() 
    {
        return itemId;
    }
    public void setExpectedTime(String expectedTime)
    {
        this.expectedTime = expectedTime;
    }

    public String getExpectedTime()
    {
        return expectedTime;
    }
    public void setExecuteCycle(String executeCycle) 
    {
        this.executeCycle = executeCycle;
    }

    public String getExecuteCycle() 
    {
        return executeCycle;
    }
    public void setExecuteTimes(Long executeTimes) 
    {
        this.executeTimes = executeTimes;
    }

    public Long getExecuteTimes() 
    {
        return executeTimes;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("planId", getPlanId())
            .append("itemId", getItemId())
            .append("expectedTime", getExpectedTime())
            .append("executeCycle", getExecuteCycle())
            .append("executeTimes", getExecuteTimes())
            .toString();
    }
}
