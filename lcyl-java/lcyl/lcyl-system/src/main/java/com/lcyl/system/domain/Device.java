package com.lcyl.system.domain;

import com.lcyl.common.annotation.Excel;
import com.lcyl.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 设备管理对象 lc_device
 * 
 * @author ruoyi
 * @date 2026-03-27
 */
public class Device extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 设备ID */
    private Long id;

    /** 设备名称 */
    @Excel(name = "设备名称")
    private String deviceName;

    /** 备注名称 */
    @Excel(name = "备注名称")
    private String remarkName;

    /** 所属产品ID */
    @Excel(name = "所属产品ID")
    private Long productId;

    /** 所属产品名称 */
    @Excel(name = "所属产品名称")
    private String productName;

    /** 接入位置（房间/床位） */
    @Excel(name = "接入位置", readConverterExp = "房=间/床位")
    private String accessLocation;

    /** 节点类型 */
    @Excel(name = "节点类型")
    private String nodeType;

    /** 创建人 */
    @Excel(name = "创建人")
    private String creator;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setDeviceName(String deviceName) 
    {
        this.deviceName = deviceName;
    }

    public String getDeviceName() 
    {
        return deviceName;
    }

    public void setRemarkName(String remarkName) 
    {
        this.remarkName = remarkName;
    }

    public String getRemarkName() 
    {
        return remarkName;
    }

    public void setProductId(Long productId) 
    {
        this.productId = productId;
    }

    public Long getProductId() 
    {
        return productId;
    }

    public void setProductName(String productName) 
    {
        this.productName = productName;
    }

    public String getProductName() 
    {
        return productName;
    }

    public void setAccessLocation(String accessLocation) 
    {
        this.accessLocation = accessLocation;
    }

    public String getAccessLocation() 
    {
        return accessLocation;
    }

    public void setNodeType(String nodeType) 
    {
        this.nodeType = nodeType;
    }

    public String getNodeType() 
    {
        return nodeType;
    }

    public void setCreator(String creator) 
    {
        this.creator = creator;
    }

    public String getCreator() 
    {
        return creator;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("deviceName", getDeviceName())
            .append("remarkName", getRemarkName())
            .append("productId", getProductId())
            .append("productName", getProductName())
            .append("accessLocation", getAccessLocation())
            .append("nodeType", getNodeType())
            .append("creator", getCreator())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
