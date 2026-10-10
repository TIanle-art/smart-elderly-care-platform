package com.lcyl.code.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.lcyl.common.annotation.Excel;
import com.lcyl.common.core.domain.BaseEntity;

/**
 * 来访登记对象 lc_reservation
 * 
 * @author ruoyi
 * @date 2026-03-23
 */
public class arrivalReservation extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Integer id;

    /** 访问类别 */
    @Excel(name = "访问类别")
    private String type;

    /** 预约人名 */
    @Excel(name = "预约人名")
    private String name;

    /** 预约人电话 */
    @Excel(name = "预约人电话")
    private String phone;

    /** 老人姓名 */
    @Excel(name = "老人姓名")
    private String olderName;

    /** 预约时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "预约时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date appointmentTime;

    /** 创建人 */
    @Excel(name = "创建人")
    private String createPeople;

    /** 状态 */
    @Excel(name = "状态")
    private Integer status;

    /** 到访时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "到访时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date visitTime;

    public void setId(Integer id) 
    {
        this.id = id;
    }

    public Integer getId() 
    {
        return id;
    }

    public void setType(String type) 
    {
        this.type = type;
    }

    public String getType() 
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

    public void setOlderName(String olderName) 
    {
        this.olderName = olderName;
    }

    public String getOlderName() 
    {
        return olderName;
    }

    public void setAppointmentTime(Date appointmentTime) 
    {
        this.appointmentTime = appointmentTime;
    }

    public Date getAppointmentTime() 
    {
        return appointmentTime;
    }

    public void setCreatePeople(String createPeople) 
    {
        this.createPeople = createPeople;
    }

    public String getCreatePeople() 
    {
        return createPeople;
    }

    public void setStatus(Integer status) 
    {
        this.status = status;
    }

    public Integer getStatus() 
    {
        return status;
    }

    public void setVisitTime(Date visitTime) 
    {
        this.visitTime = visitTime;
    }

    public Date getVisitTime() 
    {
        return visitTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("type", getType())
            .append("name", getName())
            .append("phone", getPhone())
            .append("olderName", getOlderName())
            .append("appointmentTime", getAppointmentTime())
            .append("createPeople", getCreatePeople())
            .append("createTime", getCreateTime())
            .append("status", getStatus())
            .append("visitTime", getVisitTime())
            .toString();
    }
}
