package com.lcyl.code.domain.vo;

import java.util.List;

import com.lcyl.code.domain.Bill;
import com.lcyl.code.domain.BillItem;
import com.lcyl.code.domain.BillPayment;

public class WxMyBillDetailVO
{
    private Bill billInfo;

    private List<BillItem> billItemList;

    private BillPayment paymentRecord;

    public Bill getBillInfo()
    {
        return billInfo;
    }

    public void setBillInfo(Bill billInfo)
    {
        this.billInfo = billInfo;
    }

    public List<BillItem> getBillItemList()
    {
        return billItemList;
    }

    public void setBillItemList(List<BillItem> billItemList)
    {
        this.billItemList = billItemList;
    }

    public BillPayment getPaymentRecord()
    {
        return paymentRecord;
    }

    public void setPaymentRecord(BillPayment paymentRecord)
    {
        this.paymentRecord = paymentRecord;
    }
}
