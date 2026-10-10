package com.lcyl.code.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.lcyl.common.annotation.Excel;
import com.lcyl.common.core.domain.BaseEntity;

/**
 * 账单明细对象 lc_bill_item
 * 
 * @author ruoyi
 * @date 2026-03-26
 */
public class BillItem extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 账单ID */
    private Long billId;

    /** 账单编号 */
    private String billNo;

    /** 明细类型(1添加项 2扣减项) */
    @Excel(name = "明细类型(1添加项 2扣减项)")
    private String itemType;

    /** 费用项目 */
    @Excel(name = "费用项目")
    private String feeName;

    /** 服务内容 */
    @Excel(name = "服务内容")
    private String serviceContent;

    /** 金额 */
    @Excel(name = "金额")
    private BigDecimal amount;

    /** 排序 */
    private Integer sort;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setBillId(Long billId) 
    {
        this.billId = billId;
    }

    public Long getBillId() 
    {
        return billId;
    }

    public void setBillNo(String billNo) 
    {
        this.billNo = billNo;
    }

    public String getBillNo() 
    {
        return billNo;
    }

    public void setItemType(String itemType) 
    {
        this.itemType = itemType;
    }

    public String getItemType() 
    {
        return itemType;
    }

    public void setFeeName(String feeName) 
    {
        this.feeName = feeName;
    }

    public String getFeeName() 
    {
        return feeName;
    }

    public void setServiceContent(String serviceContent) 
    {
        this.serviceContent = serviceContent;
    }

    public String getServiceContent() 
    {
        return serviceContent;
    }

    public void setAmount(BigDecimal amount) 
    {
        this.amount = amount;
    }

    public BigDecimal getAmount() 
    {
        return amount;
    }

    public void setSort(Integer sort) 
    {
        this.sort = sort;
    }

    public Integer getSort() 
    {
        return sort;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("billId", getBillId())
            .append("billNo", getBillNo())
            .append("itemType", getItemType())
            .append("feeName", getFeeName())
            .append("serviceContent", getServiceContent())
            .append("amount", getAmount())
            .append("sort", getSort())
            .append("remark", getRemark())
            .toString();
    }
}
