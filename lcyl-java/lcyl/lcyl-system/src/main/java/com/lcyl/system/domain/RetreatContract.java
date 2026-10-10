package com.lcyl.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lcyl.common.annotation.Excel;
import com.lcyl.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 解除合同对象 lc_retreat_contract
 * 
 * @author tyf
 * @date 2026-03-26
 */
public class RetreatContract extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 退住id */
    @Excel(name = "退住id")
    private Long retreatId;

    /** 解除合同url */
    @Excel(name = "解除合同url")
    private String contractUrl;

    /** 解除合同名称 */
    @Excel(name = "解除合同名称")
    private String contractName;

    /** 解除日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "解除日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date terminateDate;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setRetreatId(Long retreatId) 
    {
        this.retreatId = retreatId;
    }

    public Long getRetreatId() 
    {
        return retreatId;
    }

    public void setContractUrl(String contractUrl) 
    {
        this.contractUrl = contractUrl;
    }

    public String getContractUrl() 
    {
        return contractUrl;
    }

    public void setContractName(String contractName) 
    {
        this.contractName = contractName;
    }

    public String getContractName() 
    {
        return contractName;
    }

    public void setTerminateDate(Date terminateDate) 
    {
        this.terminateDate = terminateDate;
    }

    public Date getTerminateDate() 
    {
        return terminateDate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("retreatId", getRetreatId())
            .append("contractUrl", getContractUrl())
            .append("contractName", getContractName())
            .append("terminateDate", getTerminateDate())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
