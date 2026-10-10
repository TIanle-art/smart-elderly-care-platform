package com.lcyl.code.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.lcyl.common.annotation.Excel;
import com.lcyl.common.core.domain.BaseEntity;

/**
 * 能力评估对象 lc_checkin_ability_evaluate
 * 
 * @author ruoyi
 * @date 2026-03-23
 */
public class LcCheckinAbilityEvaluate extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 关联申请ID */
    @Excel(name = "关联申请ID")
    private Long applyId;

    /** 自理能力评分（0-100） */
    @Excel(name = "自理能力评分", readConverterExp = "0=-100")
    private Long selfCareScore;

    /** 精神状态（正常/轻度异常/重度异常） */
    @Excel(name = "精神状态", readConverterExp = "正=常/轻度异常/重度异常")
    private String mentalStatus;

    /** 行动能力（正常/需辅助/无法行动） */
    @Excel(name = "行动能力", readConverterExp = "正=常/需辅助/无法行动")
    private String mobility;

    /** 护理等级（自理/半自理/失能） */
    @Excel(name = "护理等级", readConverterExp = "自=理/半自理/失能")
    private String careLevel;

    /** 评估医生 */
    @Excel(name = "评估医生")
    private String evaluateDoctor;

    /** 评估时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "评估时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date evaluateTime;

    /** 删除标志 */
    private String delFlag;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setApplyId(Long applyId) 
    {
        this.applyId = applyId;
    }

    public Long getApplyId() 
    {
        return applyId;
    }

    public void setSelfCareScore(Long selfCareScore) 
    {
        this.selfCareScore = selfCareScore;
    }

    public Long getSelfCareScore() 
    {
        return selfCareScore;
    }

    public void setMentalStatus(String mentalStatus) 
    {
        this.mentalStatus = mentalStatus;
    }

    public String getMentalStatus() 
    {
        return mentalStatus;
    }

    public void setMobility(String mobility) 
    {
        this.mobility = mobility;
    }

    public String getMobility() 
    {
        return mobility;
    }

    public void setCareLevel(String careLevel) 
    {
        this.careLevel = careLevel;
    }

    public String getCareLevel() 
    {
        return careLevel;
    }

    public void setEvaluateDoctor(String evaluateDoctor) 
    {
        this.evaluateDoctor = evaluateDoctor;
    }

    public String getEvaluateDoctor() 
    {
        return evaluateDoctor;
    }

    public void setEvaluateTime(Date evaluateTime) 
    {
        this.evaluateTime = evaluateTime;
    }

    public Date getEvaluateTime() 
    {
        return evaluateTime;
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
            .append("applyId", getApplyId())
            .append("selfCareScore", getSelfCareScore())
            .append("mentalStatus", getMentalStatus())
            .append("mobility", getMobility())
            .append("careLevel", getCareLevel())
            .append("evaluateDoctor", getEvaluateDoctor())
            .append("evaluateTime", getEvaluateTime())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .toString();
    }
}
