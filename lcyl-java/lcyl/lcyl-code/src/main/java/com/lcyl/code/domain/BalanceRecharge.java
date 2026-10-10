package com.lcyl.code.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.lcyl.common.annotation.Excel;
import com.lcyl.common.core.domain.BaseEntity;

/**
 * 预缴款充值记录对象 lc_balance_recharge
 * 
 * @author ruoyi
 * @date 2026-03-27
 */
public class BalanceRecharge extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 充值编号 */
    @Excel(name = "充值编号")
    private String rechargeNo;

    /** 老人ID */
    private Long elderId;

    /** 老人姓名 */
    @Excel(name = "老人姓名")
    private String elderName;

    /** 床位号 */
    @Excel(name = "床位号")
    private String bedNo;

    /** 充值方式(微信/支付宝/网银/现金/其他) */
    @Excel(name = "充值方式(微信/支付宝/网银/现金/其他)")
    private String rechargeMethod;

    /** 充值金额 */
    @Excel(name = "充值金额")
    private BigDecimal rechargeAmount;

    /** 充值凭证 */
    private String voucherUrl;

    /** 充值备注 */
    private String rechargeRemark;

    /** 状态(0成功 1作废) */
    @Excel(name = "状态(0成功 1作废)")
    private String status;

    /** 删除标志(0存在 2删除) */
    private String delFlag;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setRechargeNo(String rechargeNo) 
    {
        this.rechargeNo = rechargeNo;
    }

    public String getRechargeNo() 
    {
        return rechargeNo;
    }

    public void setElderId(Long elderId) 
    {
        this.elderId = elderId;
    }

    public Long getElderId() 
    {
        return elderId;
    }

    public void setElderName(String elderName) 
    {
        this.elderName = elderName;
    }

    public String getElderName() 
    {
        return elderName;
    }

    public void setBedNo(String bedNo) 
    {
        this.bedNo = bedNo;
    }

    public String getBedNo() 
    {
        return bedNo;
    }

    public void setRechargeMethod(String rechargeMethod) 
    {
        this.rechargeMethod = rechargeMethod;
    }

    public String getRechargeMethod() 
    {
        return rechargeMethod;
    }

    public void setRechargeAmount(BigDecimal rechargeAmount) 
    {
        this.rechargeAmount = rechargeAmount;
    }

    public BigDecimal getRechargeAmount() 
    {
        return rechargeAmount;
    }

    public void setVoucherUrl(String voucherUrl) 
    {
        this.voucherUrl = voucherUrl;
    }

    public String getVoucherUrl() 
    {
        return voucherUrl;
    }

    public void setRechargeRemark(String rechargeRemark) 
    {
        this.rechargeRemark = rechargeRemark;
    }

    public String getRechargeRemark() 
    {
        return rechargeRemark;
    }

    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
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
            .append("rechargeNo", getRechargeNo())
            .append("elderId", getElderId())
            .append("elderName", getElderName())
            .append("bedNo", getBedNo())
            .append("rechargeMethod", getRechargeMethod())
            .append("rechargeAmount", getRechargeAmount())
            .append("voucherUrl", getVoucherUrl())
            .append("rechargeRemark", getRechargeRemark())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("delFlag", getDelFlag())
            .append("remark", getRemark())
            .toString();
    }
}
