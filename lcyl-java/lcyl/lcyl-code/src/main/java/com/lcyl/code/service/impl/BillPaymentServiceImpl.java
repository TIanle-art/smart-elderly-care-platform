package com.lcyl.code.service.impl;

import java.util.List;
import com.lcyl.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.lcyl.code.mapper.BillPaymentMapper;
import com.lcyl.code.domain.BillPayment;
import com.lcyl.code.service.IBillPaymentService;

/**
 * 账单支付记录Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-03-26
 */
@Service
public class BillPaymentServiceImpl implements IBillPaymentService 
{
    @Autowired
    private BillPaymentMapper billPaymentMapper;

    /**
     * 查询账单支付记录
     * 
     * @param id 账单支付记录主键
     * @return 账单支付记录
     */
    @Override
    public BillPayment selectBillPaymentById(Long id)
    {
        return billPaymentMapper.selectBillPaymentById(id);
    }

    /**
     * 查询账单支付记录列表
     * 
     * @param billPayment 账单支付记录
     * @return 账单支付记录
     */
    @Override
    public List<BillPayment> selectBillPaymentList(BillPayment billPayment)
    {
        return billPaymentMapper.selectBillPaymentList(billPayment);
    }

    /**
     * 新增账单支付记录
     * 
     * @param billPayment 账单支付记录
     * @return 结果
     */
    @Override
    public int insertBillPayment(BillPayment billPayment)
    {
        billPayment.setCreateTime(DateUtils.getNowDate());
        return billPaymentMapper.insertBillPayment(billPayment);
    }

    /**
     * 修改账单支付记录
     * 
     * @param billPayment 账单支付记录
     * @return 结果
     */
    @Override
    public int updateBillPayment(BillPayment billPayment)
    {
        return billPaymentMapper.updateBillPayment(billPayment);
    }

    /**
     * 批量删除账单支付记录
     * 
     * @param ids 需要删除的账单支付记录主键
     * @return 结果
     */
    @Override
    public int deleteBillPaymentByIds(Long[] ids)
    {
        return billPaymentMapper.deleteBillPaymentByIds(ids);
    }

    /**
     * 删除账单支付记录信息
     * 
     * @param id 账单支付记录主键
     * @return 结果
     */
    @Override
    public int deleteBillPaymentById(Long id)
    {
        return billPaymentMapper.deleteBillPaymentById(id);
    }
}
