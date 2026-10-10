package com.lcyl.code.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.lcyl.common.annotation.Excel;
import com.lcyl.common.core.domain.BaseEntity;

/**
 * 订单详情对象 lc_service_order
 * 
 * @author ruoyi
 * @date 2026-03-21
 */
public class ServiceOrder extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 订单编号 */
    @Excel(name = "订单编号")
    private String orderNo;

    /** 老人ID */
    private Long elderId;

    /** 老人姓名(冗余) */
    @Excel(name = "老人姓名(冗余)")
    private String elderName;

    /** 床位ID */
    private Long bedId;

    /** 床位号(冗余) */
    @Excel(name = "床位号(冗余)")
    private String bedNo;

    /** 护理项目ID */
    private Long projectId;

    /** 护理项目名称(冗余) */
    @Excel(name = "护理项目名称(冗余)")
    private String projectName;

    /** 订单金额 */
    @Excel(name = "订单金额")
    private BigDecimal orderAmount;

    /** 期望服务时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "期望服务时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date expectedServiceTime;

    /** 下单来源(1家属端 2后台) */
    private String orderSource;

    /** 下单人ID */
    private Long applicantId;

    /** 下单人姓名(冗余) */
    @Excel(name = "下单人姓名(冗余)")
    private String applicantName;

    /** 下单人手机号 */
    private String applicantPhone;

    /** 订单状态(0待支付 1待执行 2已执行 3已完成 4已退款 5已关闭) */
    @Excel(name = "订单状态(0待支付 1待执行 2已执行 3已完成 4已退款 5已关闭)")
    private String orderStatus;

    /** 交易状态(0待支付 1已支付 2退款处理中 3退款成功 4退款失败 5已关闭) */
    @Excel(name = "交易状态(0待支付 1已支付 2退款处理中 3退款成功 4退款失败 5已关闭)")
    private String tradeStatus;

    /** 支付时间 */
    private Date payTime;

    /** 执行时间 */
    private Date executeTime;

    /** 完成时间 */
    private Date finishTime;

    /** 取消时间 */
    private Date cancelTime;

    /** 取消原因 */
    private String cancelReason;

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

    public void setOrderNo(String orderNo) 
    {
        this.orderNo = orderNo;
    }

    public String getOrderNo() 
    {
        return orderNo;
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

    public void setBedId(Long bedId) 
    {
        this.bedId = bedId;
    }

    public Long getBedId() 
    {
        return bedId;
    }

    public void setBedNo(String bedNo) 
    {
        this.bedNo = bedNo;
    }

    public String getBedNo() 
    {
        return bedNo;
    }

    public void setProjectId(Long projectId) 
    {
        this.projectId = projectId;
    }

    public Long getProjectId() 
    {
        return projectId;
    }

    public void setProjectName(String projectName) 
    {
        this.projectName = projectName;
    }

    public String getProjectName() 
    {
        return projectName;
    }

    public void setOrderAmount(BigDecimal orderAmount) 
    {
        this.orderAmount = orderAmount;
    }

    public BigDecimal getOrderAmount() 
    {
        return orderAmount;
    }

    public void setExpectedServiceTime(Date expectedServiceTime) 
    {
        this.expectedServiceTime = expectedServiceTime;
    }

    public Date getExpectedServiceTime() 
    {
        return expectedServiceTime;
    }

    public void setOrderSource(String orderSource) 
    {
        this.orderSource = orderSource;
    }

    public String getOrderSource() 
    {
        return orderSource;
    }

    public void setApplicantId(Long applicantId) 
    {
        this.applicantId = applicantId;
    }

    public Long getApplicantId() 
    {
        return applicantId;
    }

    public void setApplicantName(String applicantName) 
    {
        this.applicantName = applicantName;
    }

    public String getApplicantName() 
    {
        return applicantName;
    }

    public void setApplicantPhone(String applicantPhone) 
    {
        this.applicantPhone = applicantPhone;
    }

    public String getApplicantPhone() 
    {
        return applicantPhone;
    }

    public void setOrderStatus(String orderStatus) 
    {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() 
    {
        return orderStatus;
    }

    public void setTradeStatus(String tradeStatus) 
    {
        this.tradeStatus = tradeStatus;
    }

    public String getTradeStatus() 
    {
        return tradeStatus;
    }

    public void setPayTime(Date payTime) 
    {
        this.payTime = payTime;
    }

    public Date getPayTime() 
    {
        return payTime;
    }

    public void setExecuteTime(Date executeTime) 
    {
        this.executeTime = executeTime;
    }

    public Date getExecuteTime() 
    {
        return executeTime;
    }

    public void setFinishTime(Date finishTime) 
    {
        this.finishTime = finishTime;
    }

    public Date getFinishTime() 
    {
        return finishTime;
    }

    public void setCancelTime(Date cancelTime) 
    {
        this.cancelTime = cancelTime;
    }

    public Date getCancelTime() 
    {
        return cancelTime;
    }

    public void setCancelReason(String cancelReason) 
    {
        this.cancelReason = cancelReason;
    }

    public String getCancelReason() 
    {
        return cancelReason;
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
            .append("orderNo", getOrderNo())
            .append("elderId", getElderId())
            .append("elderName", getElderName())
            .append("bedId", getBedId())
            .append("bedNo", getBedNo())
            .append("projectId", getProjectId())
            .append("projectName", getProjectName())
            .append("orderAmount", getOrderAmount())
            .append("expectedServiceTime", getExpectedServiceTime())
            .append("orderSource", getOrderSource())
            .append("applicantId", getApplicantId())
            .append("applicantName", getApplicantName())
            .append("applicantPhone", getApplicantPhone())
            .append("remark", getRemark())
            .append("orderStatus", getOrderStatus())
            .append("tradeStatus", getTradeStatus())
            .append("payTime", getPayTime())
            .append("executeTime", getExecuteTime())
            .append("finishTime", getFinishTime())
            .append("cancelTime", getCancelTime())
            .append("cancelReason", getCancelReason())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
