package com.lcyl.code.domain.vo;

/**
 * 生成账单时使用的老人基础信息
 *
 * @author ruoyi
 * @date 2026-03-26
 */
public class BillGenerateElderVO
{
    private Long elderId;
    private String elderName;
    private String elderIdCard;
    private String bedNo;

    public Long getElderId()
    {
        return elderId;
    }

    public void setElderId(Long elderId)
    {
        this.elderId = elderId;
    }

    public String getElderName()
    {
        return elderName;
    }

    public void setElderName(String elderName)
    {
        this.elderName = elderName;
    }

    public String getElderIdCard()
    {
        return elderIdCard;
    }

    public void setElderIdCard(String elderIdCard)
    {
        this.elderIdCard = elderIdCard;
    }

    public String getBedNo()
    {
        return bedNo;
    }

    public void setBedNo(String bedNo)
    {
        this.bedNo = bedNo;
    }
}
