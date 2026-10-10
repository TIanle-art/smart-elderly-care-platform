package com.lcyl.code.domain.dto;

public class WxCreateOrderDTO
{
    private Long elderId;
    private Long projectId;
    private String expectedServiceTime;
    private String remark;

    public Long getElderId()
    {
        return elderId;
    }

    public void setElderId(Long elderId)
    {
        this.elderId = elderId;
    }

    public Long getProjectId()
    {
        return projectId;
    }

    public void setProjectId(Long projectId)
    {
        this.projectId = projectId;
    }

    public String getExpectedServiceTime()
    {
        return expectedServiceTime;
    }

    public void setExpectedServiceTime(String expectedServiceTime)
    {
        this.expectedServiceTime = expectedServiceTime;
    }

    public String getRemark()
    {
        return remark;
    }

    public void setRemark(String remark)
    {
        this.remark = remark;
    }
}
