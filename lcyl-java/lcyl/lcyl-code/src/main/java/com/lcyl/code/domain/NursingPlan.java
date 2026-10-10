package com.lcyl.code.domain;

import java.util.List;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.lcyl.common.annotation.Excel;
import com.lcyl.common.core.domain.BaseEntity;

/**
 * 护理计划对象 lc_nursing_plan
 * 
 * @author ruoyi
 * @date 2026-03-23
 */
public class NursingPlan extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 护理计划名称（唯一） */
    @Excel(name = "护理计划名称", readConverterExp = "唯=一")
    private String planName;

    /** 状态：1-启用，0-禁用 */
    @Excel(name = "状态：1-启用，0-禁用")
    private Long status;

    /** 创建人ID（关联nurse.id） */
    private Long creatorId;

    /** 创建人姓名（冗余） */
    @Excel(name = "创建人姓名", readConverterExp = "冗=余")
    private String creatorName;

    private Integer isBound;

    /** 护理计划-项目关联信息 */
    private List<NursingPlanItem> nursingPlanItemList;

    public Integer getIsBound() {
        return isBound;
    }
    public void setIsBound(Integer isBound) {
        this.isBound = isBound;
    }
    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setPlanName(String planName) 
    {
        this.planName = planName;
    }

    public String getPlanName() 
    {
        return planName;
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

    public List<NursingPlanItem> getNursingPlanItemList()
    {
        return nursingPlanItemList;
    }

    public void setNursingPlanItemList(List<NursingPlanItem> nursingPlanItemList)
    {
        this.nursingPlanItemList = nursingPlanItemList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("planName", getPlanName())
            .append("status", getStatus())
            .append("creatorId", getCreatorId())
            .append("creatorName", getCreatorName())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("nursingPlanItemList", getNursingPlanItemList())
            .toString();
    }
}
