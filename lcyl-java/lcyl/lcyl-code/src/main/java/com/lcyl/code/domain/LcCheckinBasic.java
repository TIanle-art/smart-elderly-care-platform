package com.lcyl.code.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lcyl.common.annotation.Excel;
import com.lcyl.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 老人基础信息对象 lc_checkin_basic
 * 
 * @author ruoyi
 * @date 2026-03-23
 */
public class LcCheckinBasic extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 关联申请ID */
    @Excel(name = "关联申请ID")
    private Long applyId;

    /** 老人姓名 */
    @Excel(name = "老人姓名")
    private String elderlyName;

    /** 身份证号 */
    @Excel(name = "身份证号")
    private String idCard;

    /** 联系方式 */
    @Excel(name = "联系方式")
    private String phone;

    /** 家庭住址 */
    @Excel(name = "家庭住址")
    private String address;

    /** 性别（0男 1女） */
    @Excel(name = "性别", readConverterExp = "0=男,1=女")
    private String gender;

    /** 出生日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "出生日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date birthday;

    /** 年龄 */
    @Excel(name = "年龄")
    private Long age;

    /** 民族 */
    @Excel(name = "民族")
    private String nation;

    /** 政治面貌 */
    @Excel(name = "政治面貌")
    private String politicalStatus;

    /** 宗教信仰 */
    @Excel(name = "宗教信仰")
    private String religion;

    /** 婚姻状况 */
    @Excel(name = "婚姻状况")
    private String maritalStatus;

    /** 文化程度 */
    @Excel(name = "文化程度")
    private String education;

    /** 经济来源 */
    @Excel(name = "经济来源")
    private String economicSource;

    /** 特长爱好 */
    @Excel(name = "特长爱好")
    private String hobby;

    /** 医疗保障 */
    @Excel(name = "医疗保障")
    private String medicalSecurity;

    /** 医保卡号 */
    @Excel(name = "医保卡号")
    private String medicalCardNo;

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

    public void setElderlyName(String elderlyName) 
    {
        this.elderlyName = elderlyName;
    }

    public String getElderlyName() 
    {
        return elderlyName;
    }

    public void setIdCard(String idCard) 
    {
        this.idCard = idCard;
    }

    public String getIdCard() 
    {
        return idCard;
    }

    public void setPhone(String phone) 
    {
        this.phone = phone;
    }

    public String getPhone() 
    {
        return phone;
    }

    public void setAddress(String address) 
    {
        this.address = address;
    }

    public String getAddress() 
    {
        return address;
    }

    public void setGender(String gender) 
    {
        this.gender = gender;
    }

    public String getGender() 
    {
        return gender;
    }

    public void setBirthday(Date birthday) 
    {
        this.birthday = birthday;
    }

    public Date getBirthday() 
    {
        return birthday;
    }

    public void setAge(Long age) 
    {
        this.age = age;
    }

    public Long getAge() 
    {
        return age;
    }

    public void setNation(String nation) 
    {
        this.nation = nation;
    }

    public String getNation() 
    {
        return nation;
    }

    public void setPoliticalStatus(String politicalStatus) 
    {
        this.politicalStatus = politicalStatus;
    }

    public String getPoliticalStatus() 
    {
        return politicalStatus;
    }

    public void setReligion(String religion) 
    {
        this.religion = religion;
    }

    public String getReligion() 
    {
        return religion;
    }

    public void setMaritalStatus(String maritalStatus) 
    {
        this.maritalStatus = maritalStatus;
    }

    public String getMaritalStatus() 
    {
        return maritalStatus;
    }

    public void setEducation(String education) 
    {
        this.education = education;
    }

    public String getEducation() 
    {
        return education;
    }

    public void setEconomicSource(String economicSource) 
    {
        this.economicSource = economicSource;
    }

    public String getEconomicSource() 
    {
        return economicSource;
    }

    public void setHobby(String hobby) 
    {
        this.hobby = hobby;
    }

    public String getHobby() 
    {
        return hobby;
    }

    public void setMedicalSecurity(String medicalSecurity) 
    {
        this.medicalSecurity = medicalSecurity;
    }

    public String getMedicalSecurity() 
    {
        return medicalSecurity;
    }

    public void setMedicalCardNo(String medicalCardNo) 
    {
        this.medicalCardNo = medicalCardNo;
    }

    public String getMedicalCardNo() 
    {
        return medicalCardNo;
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
            .append("elderlyName", getElderlyName())
            .append("idCard", getIdCard())
            .append("phone", getPhone())
            .append("address", getAddress())
            .append("gender", getGender())
            .append("birthday", getBirthday())
            .append("age", getAge())
            .append("nation", getNation())
            .append("politicalStatus", getPoliticalStatus())
            .append("religion", getReligion())
            .append("maritalStatus", getMaritalStatus())
            .append("education", getEducation())
            .append("economicSource", getEconomicSource())
            .append("hobby", getHobby())
            .append("medicalSecurity", getMedicalSecurity())
            .append("medicalCardNo", getMedicalCardNo())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
