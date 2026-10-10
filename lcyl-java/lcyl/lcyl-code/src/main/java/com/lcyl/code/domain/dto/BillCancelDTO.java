package com.lcyl.code.domain.dto;

/**
 * 取消账单参数对象
 *
 * @author ruoyi
 * @date 2026-03-26
 */
public class BillCancelDTO
{
    /** 账单ID */
    private Long billId;

    /** 取消原因 */
    private String cancelReason;

    public Long getBillId()
    {
        return billId;
    }

    public void setBillId(Long billId)
    {
        this.billId = billId;
    }

    public String getCancelReason()
    {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason)
    {
        this.cancelReason = cancelReason;
    }
}
