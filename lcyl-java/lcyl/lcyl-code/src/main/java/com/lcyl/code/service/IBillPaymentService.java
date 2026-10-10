package com.lcyl.code.service;

import java.util.List;
import com.lcyl.code.domain.BillPayment;

/**
 * 账单支付记录Service接口
 * 
 * @author ruoyi
 * @date 2026-03-26
 */
public interface IBillPaymentService 
{
    /**
     * 查询账单支付记录
     * 
     * @param id 账单支付记录主键
     * @return 账单支付记录
     */
    public BillPayment selectBillPaymentById(Long id);

    /**
     * 查询账单支付记录列表
     * 
     * @param billPayment 账单支付记录
     * @return 账单支付记录集合
     */
    public List<BillPayment> selectBillPaymentList(BillPayment billPayment);

    /**
     * 新增账单支付记录
     * 
     * @param billPayment 账单支付记录
     * @return 结果
     */
    public int insertBillPayment(BillPayment billPayment);

    /**
     * 修改账单支付记录
     * 
     * @param billPayment 账单支付记录
     * @return 结果
     */
    public int updateBillPayment(BillPayment billPayment);

    /**
     * 批量删除账单支付记录
     * 
     * @param ids 需要删除的账单支付记录主键集合
     * @return 结果
     */
    public int deleteBillPaymentByIds(Long[] ids);

    /**
     * 删除账单支付记录信息
     * 
     * @param id 账单支付记录主键
     * @return 结果
     */
    public int deleteBillPaymentById(Long id);
}
