package com.lcyl.code.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.lcyl.common.annotation.Excel;
import com.lcyl.common.core.domain.BaseEntity;

/**
 * 来访登记对象 lc_arrival
 * 
 * @author ruoyi
 * @date 2026-03-23
 */
public class Arrival extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 序号 */
    private Long id;

    /** 来访类别 */
    @Excel(name = "来访类别")
    private Long type;

    /** 来访人姓名 */
    @Excel(name = "来访人姓名")
    private String name;

    /** 来访人手机号 */
    @Excel(name = "来访人手机号")
    private String phone;

    /** 老人姓名 */
    @Excel(name = "老人姓名")
    private String elderName;

    /** 来访时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "来访时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date visitTime;

    /** 创建人 */
    @Excel(name = "创建人")
    private String createPerson;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setType(Long type) 
    {
        this.type = type;
    }

    public Long getType() 
    {
        return type;
    }

    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }

    public void setPhone(String phone) 
    {
        this.phone = phone;
    }

    public String getPhone() 
    {
        return phone;
    }

    public void setElderName(String elderName) 
    {
        this.elderName = elderName;
    }

    public String getElderName() 
    {
        return elderName;
    }

    public void setVisitTime(Date visitTime) 
    {
        this.visitTime = visitTime;
    }

    public Date getVisitTime() 
    {
        return visitTime;
    }

    public void setCreatePerson(String createPerson) 
    {
        this.createPerson = createPerson;
    }

    public String getCreatePerson() 
    {
        return createPerson;
    }


    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("type", getType())
            .append("name", getName())
            .append("phone", getPhone())
            .append("elderName", getElderName())
            .append("visitTime", getVisitTime())
            .append("createPerson", getCreatePerson())
            .append("createTime", getCreateTime())
            .toString();
    }
}
