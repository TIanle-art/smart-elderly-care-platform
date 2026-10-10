package com.lcyl.code.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.lcyl.common.annotation.Excel;
import com.lcyl.common.core.domain.BaseEntity;

/**
 * 护理项目对象 lc_nursing_item
 * 
 * @author ruoyi
 * @date 2026-03-23
 */
public class NursingItem extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 护理项目名称（唯一） */
    @Excel(name = "护理项目名称", readConverterExp = "唯=一")
    private String itemName;

    /** 价格 */
    @Excel(name = "价格")
    private Double price;

    /** 单位（次/40分钟等） */
    @Excel(name = "单位", readConverterExp = "次=/40分钟等")
    private String unit;

    /** 排序号 */
    @Excel(name = "排序号")
    private Long sort;

    /** 护理项目图片URL */
    @Excel(name = "护理项目图片URL")
    private String imageUrl;

    /** 项目描述（50字以内） */
    private String itemDesc;

    /** 状态：1-启用，0-禁用 */
    @Excel(name = "状态：1-启用，0-禁用")
    private Long status;

    /** 创建人ID（关联nurse.id） */
    private Long creatorId;

    /** 创建人姓名（冗余） */
    @Excel(name = "创建人姓名", readConverterExp = "冗=余")
    private String creatorName;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setItemName(String itemName) 
    {
        this.itemName = itemName;
    }

    public String getItemName() 
    {
        return itemName;
    }

    public void setPrice(Double price) 
    {
        this.price = price;
    }

    public Double getPrice() 
    {
        return price;
    }

    public void setUnit(String unit) 
    {
        this.unit = unit;
    }

    public String getUnit() 
    {
        return unit;
    }

    public void setSort(Long sort) 
    {
        this.sort = sort;
    }

    public Long getSort() 
    {
        return sort;
    }

    public void setImageUrl(String imageUrl) 
    {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() 
    {
        return imageUrl;
    }

    public void setItemDesc(String itemDesc) 
    {
        this.itemDesc = itemDesc;
    }

    public String getItemDesc() 
    {
        return itemDesc;
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
            .append("itemName", getItemName())
            .append("price", getPrice())
            .append("unit", getUnit())
            .append("sort", getSort())
            .append("imageUrl", getImageUrl())
            .append("itemDesc", getItemDesc())
            .append("status", getStatus())
            .append("creatorId", getCreatorId())
            .append("creatorName", getCreatorName())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
