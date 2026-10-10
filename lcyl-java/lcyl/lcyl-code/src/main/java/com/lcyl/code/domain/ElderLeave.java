package com.lcyl.code.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lcyl.common.annotation.Excel;
import com.lcyl.common.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.checkerframework.checker.units.qual.A;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 老人请假申请对象 lc_elder_leave
 *
 * @author cch
 * @date 2026-03-20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ElderLeave extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @Excel(name = "主键ID")
    private Long id;

    /**
     * 单据编号
     */
    @Excel(name = "单据编号")
    private String orderNo;

    /**
     * 老人ID
     */
    private Long elderId;

    /**
     * 老人姓名（冗余）
     */
    @Excel(name = "老人姓名", readConverterExp = "冗=余")
    private String elderName;

    /**
     * 老人身份证号
     */
    @Excel(name = "老人身份证号")
    private String elderIdCard;

    /**
     * 联系方式
     */
    private String phone;

    /**
     * 护理等级
     */
    private String nursingLevel;

    /**
     * 入住床位
     */
    private String bedNo;

    /**
     * 护理员姓名，多个用逗号分隔
     */
    private String nurseNames;

    /**
     * 陪同人类型：家属、护理人员、其他、无
     */
    private String companionType;

    /**
     * 陪同人姓名
     */
    private String companionName;

    /**
     * 陪同人联系方式
     */
    private String companionPhone;

    /**
     * 请假开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "请假开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date leaveStartTime;

    /**
     * 预计返回时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "预计返回时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date plannedReturnTime;

    /**
     * 请假天数
     */
    private Long leaveDays;

    /**
     * 请假原因
     */
    private String leaveReason;

    /**
     * 流程实例ID
     */
    private String processInstanceId;

    /**
     * 当前任务节点key
     */
    private String currentTaskKey;

    /**
     * 当前任务节点名称
     */
    private String currentTaskName;

    /**
     * 业务状态：draft草稿、approving审批中、approved已通过、rejected已拒绝、revoked已撤回、leaving请假中、returned已返回、timeout超时未归、cancelled已作废
     */
    private String status;

    /**
     * 实际返回时间（冗余字段，便于列表查询）
     */
    @JsonFormat(
            pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
            timezone = "GMT"
    )
    @Excel(name = "实际返回时间", readConverterExp = "冗=余字段，便于列表查询")
    private Date actualReturnTime;

    /**
     * 实际请假天数（冗余字段，便于列表查询）
     */
    private BigDecimal actualLeaveDays;

    /**
     * 是否已返回：0否，1是
     */
    @Excel(name = "是否已返回：0否，1是")
    private Integer isReturned;

    /**
     * 申请人ID
     */
    private Long applyUserId;

    /**
     * 申请人姓名
     */
    private String applyUserName;

    /**
     * 申请时间
     */
    private Date applyTime;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setElderId(Long elderId) {
        this.elderId = elderId;
    }

    public Long getElderId() {
        return elderId;
    }

    public void setElderName(String elderName) {
        this.elderName = elderName;
    }

    public String getElderName() {
        return elderName;
    }

    public void setElderIdCard(String elderIdCard) {
        this.elderIdCard = elderIdCard;
    }

    public String getElderIdCard() {
        return elderIdCard;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setNursingLevel(String nursingLevel) {
        this.nursingLevel = nursingLevel;
    }

    public String getNursingLevel() {
        return nursingLevel;
    }

    public void setBedNo(String bedNo) {
        this.bedNo = bedNo;
    }

    public String getBedNo() {
        return bedNo;
    }

    public void setNurseNames(String nurseNames) {
        this.nurseNames = nurseNames;
    }

    public String getNurseNames() {
        return nurseNames;
    }

    public void setCompanionType(String companionType) {
        this.companionType = companionType;
    }

    public String getCompanionType() {
        return companionType;
    }

    public void setCompanionName(String companionName) {
        this.companionName = companionName;
    }

    public String getCompanionName() {
        return companionName;
    }

    public void setCompanionPhone(String companionPhone) {
        this.companionPhone = companionPhone;
    }

    public String getCompanionPhone() {
        return companionPhone;
    }

    public void setLeaveStartTime(Date leaveStartTime) {
        this.leaveStartTime = leaveStartTime;
    }

    public Date getLeaveStartTime() {
        return leaveStartTime;
    }

    public void setPlannedReturnTime(Date plannedReturnTime) {
        this.plannedReturnTime = plannedReturnTime;
    }

    public Date getPlannedReturnTime() {
        return plannedReturnTime;
    }

    public void setLeaveDays(Long leaveDays) {
        this.leaveDays = leaveDays;
    }

    public Long getLeaveDays() {
        return leaveDays;
    }

    public void setLeaveReason(String leaveReason) {
        this.leaveReason = leaveReason;
    }

    public String getLeaveReason() {
        return leaveReason;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setCurrentTaskKey(String currentTaskKey) {
        this.currentTaskKey = currentTaskKey;
    }

    public String getCurrentTaskKey() {
        return currentTaskKey;
    }

    public void setCurrentTaskName(String currentTaskName) {
        this.currentTaskName = currentTaskName;
    }

    public String getCurrentTaskName() {
        return currentTaskName;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setActualReturnTime(Date actualReturnTime) {
        this.actualReturnTime = actualReturnTime;
    }

    public Date getActualReturnTime() {
        return actualReturnTime;
    }

    public void setActualLeaveDays(BigDecimal actualLeaveDays) {
        this.actualLeaveDays = actualLeaveDays;
    }

    public BigDecimal getActualLeaveDays() {
        return actualLeaveDays;
    }

    public void setIsReturned(Integer isReturned) {
        this.isReturned = isReturned;
    }

    public Integer getIsReturned() {
        return isReturned;
    }

    public void setApplyUserId(Long applyUserId) {
        this.applyUserId = applyUserId;
    }

    public Long getApplyUserId() {
        return applyUserId;
    }

    public void setApplyUserName(String applyUserName) {
        this.applyUserName = applyUserName;
    }

    public String getApplyUserName() {
        return applyUserName;
    }

    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }

    public Date getApplyTime() {
        return applyTime;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getDelFlag() {
        return delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("orderNo", getOrderNo())
                .append("elderId", getElderId())
                .append("elderName", getElderName())
                .append("elderIdCard", getElderIdCard())
                .append("phone", getPhone())
                .append("nursingLevel", getNursingLevel())
                .append("bedNo", getBedNo())
                .append("nurseNames", getNurseNames())
                .append("companionType", getCompanionType())
                .append("companionName", getCompanionName())
                .append("companionPhone", getCompanionPhone())
                .append("leaveStartTime", getLeaveStartTime())
                .append("plannedReturnTime", getPlannedReturnTime())
                .append("leaveDays", getLeaveDays())
                .append("leaveReason", getLeaveReason())
                .append("processInstanceId", getProcessInstanceId())
                .append("currentTaskKey", getCurrentTaskKey())
                .append("currentTaskName", getCurrentTaskName())
                .append("status", getStatus())
                .append("actualReturnTime", getActualReturnTime())
                .append("actualLeaveDays", getActualLeaveDays())
                .append("isReturned", getIsReturned())
                .append("applyUserId", getApplyUserId())
                .append("applyUserName", getApplyUserName())
                .append("applyTime", getApplyTime())
                .append("remark", getRemark())
                .append("delFlag", getDelFlag())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
