package com.lcyl.code.domain.dto;

/**
 * 生成月度账单参数对象
 *
 * @author ruoyi
 * @date 2026-03-26
 */

public class BillGenerateDTO
{
    /** 老人ID */
    private Long elderId;

    /** 账单月份 yyyy-MM */
    private String billMonth;


    public Long getElderId()
    {
        return elderId;
    }

    public void setElderId(Long elderId)
    {
        this.elderId = elderId;
    }

    public String getBillMonth()
    {
        return billMonth;
    }

    public void setBillMonth(String billMonth)
    {
        this.billMonth = billMonth;
    }
}
