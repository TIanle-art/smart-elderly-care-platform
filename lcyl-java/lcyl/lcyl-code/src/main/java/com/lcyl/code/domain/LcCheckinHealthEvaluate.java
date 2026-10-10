package com.lcyl.code.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.lcyl.common.annotation.Excel;
import com.lcyl.common.core.domain.BaseEntity;

/**
 * 健康评估对象 lc_checkin_health_evaluate
 * 
 * @author ruoyi
 * @date 2026-03-23
 */
public class LcCheckinHealthEvaluate extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 关联申请ID */
    @Excel(name = "关联申请ID")
    private Long applyId;

    /** 身高（cm） */
    @Excel(name = "身高", readConverterExp = "c=m")
    private BigDecimal height;

    /** 体重（kg） */
    @Excel(name = "体重", readConverterExp = "k=g")
    private BigDecimal weight;

    /** 血压（高压/低压） */
    @Excel(name = "血压", readConverterExp = "高=压/低压")
    private String bloodPressure;

    /** 血糖（mmol/L） */
    @Excel(name = "血糖", readConverterExp = "m=mol/L")
    private BigDecimal bloodSugar;

    /** 慢性病（高血压/糖尿病/无） */
    @Excel(name = "慢性病", readConverterExp = "高=血压/糖尿病/无")
    private String chronicDisease;

    /** 过敏史 */
    @Excel(name = "过敏史")
    private String allergy;

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

    public void setHeight(BigDecimal height) 
    {
        this.height = height;
    }

    public BigDecimal getHeight() 
    {
        return height;
    }

    public void setWeight(BigDecimal weight) 
    {
        this.weight = weight;
    }

    public BigDecimal getWeight() 
    {
        return weight;
    }

    public void setBloodPressure(String bloodPressure) 
    {
        this.bloodPressure = bloodPressure;
    }

    public String getBloodPressure() 
    {
        return bloodPressure;
    }

    public void setBloodSugar(BigDecimal bloodSugar) 
    {
        this.bloodSugar = bloodSugar;
    }

    public BigDecimal getBloodSugar() 
    {
        return bloodSugar;
    }

    public void setChronicDisease(String chronicDisease) 
    {
        this.chronicDisease = chronicDisease;
    }

    public String getChronicDisease() 
    {
        return chronicDisease;
    }

    public void setAllergy(String allergy) 
    {
        this.allergy = allergy;
    }

    public String getAllergy() 
    {
        return allergy;
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
            .append("height", getHeight())
            .append("weight", getWeight())
            .append("bloodPressure", getBloodPressure())
            .append("bloodSugar", getBloodSugar())
            .append("chronicDisease", getChronicDisease())
            .append("allergy", getAllergy())
            .append("evaluateDoctor", getEvaluateDoctor())
            .append("evaluateTime", getEvaluateTime())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .toString();
    }
}
