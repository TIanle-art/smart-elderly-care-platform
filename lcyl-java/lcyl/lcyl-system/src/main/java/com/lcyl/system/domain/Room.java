package com.lcyl.system.domain;

import com.lcyl.common.annotation.Excel;
import com.lcyl.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;

/**
 * 房间表对象 lc_room
 * 
 * @author ruoyi
 * @date 2026-03-24
 */
public class Room extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 房间ID */
    private Long id;

    /** 房间编号 */
    @Excel(name = "房间编号")
    private String code;

    /** 排序号 */
    @Excel(name = "排序号")
    private Long sort;

    /** 所属楼层ID */
    @Excel(name = "所属楼层ID")
    private Long floorId;

    /** 房间价格 */
    @Excel(name = "房间价格")
    private BigDecimal price;

    /** 关联房型ID */
    @Excel(name = "关联房型ID")
    private Long roomTypeId;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setCode(String code) 
    {
        this.code = code;
    }

    public String getCode() 
    {
        return code;
    }

    public void setSort(Long sort) 
    {
        this.sort = sort;
    }

    public Long getSort() 
    {
        return sort;
    }

    public void setFloorId(Long floorId) 
    {
        this.floorId = floorId;
    }

    public Long getFloorId() 
    {
        return floorId;
    }

    public void setPrice(BigDecimal price) 
    {
        this.price = price;
    }

    public BigDecimal getPrice() 
    {
        return price;
    }

    public void setRoomTypeId(Long roomTypeId) 
    {
        this.roomTypeId = roomTypeId;
    }

    public Long getRoomTypeId() 
    {
        return roomTypeId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("code", getCode())
            .append("sort", getSort())
            .append("floorId", getFloorId())
            .append("price", getPrice())
            .append("createBy", getCreateBy())
            .append("updateBy", getUpdateBy())
            .append("remark", getRemark())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("roomTypeId", getRoomTypeId())
            .toString();
    }
}
