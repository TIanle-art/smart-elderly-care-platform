package com.lcyl.code.service;

import java.util.List;
import com.lcyl.code.domain.Bill;
import com.lcyl.code.domain.dto.BillCancelDTO;
import com.lcyl.code.domain.dto.BillGenerateDTO;
import com.lcyl.code.domain.dto.BillPayDTO;
import com.lcyl.code.domain.dto.MonthBillDto;

/**
 * 账单列表Service接口
 * 
 * @author ruoyi
 * @date 2026-03-25
 */
public interface IBillService 
{
    /**
     * 查询账单列表
     * 
     * @param id 账单列表主键
     * @return 账单列表
     */
    public Bill selectBillById(Long id);

    /**
     * 查询账单列表列表
     * 
     * @param bill 账单列表
     * @return 账单列表集合
     */
    public List<Bill> selectBillList(Bill bill);

    /**
     * 新增账单列表
     * 
     * @param bill 账单列表
     * @return 结果
     */
    public int insertBill(Bill bill);

    /**
     * 修改账单列表
     * 
     * @param bill 账单列表
     * @return 结果
     */
    public int updateBill(Bill bill);

    /**
     * 批量删除账单列表
     * 
     * @param ids 需要删除的账单列表主键集合
     * @return 结果
     */
    public int deleteBillByIds(Long[] ids);

    /**
     * 删除账单列表信息
     * 
     * @param id 账单列表主键
     * @return 结果
     */
    public int deleteBillById(Long id);

    /**
     * 生成月度账单
     *
     * @param dto 生成参数
     * @return 结果
     */
    public int generateMonthlyBill(BillGenerateDTO dto);
    /**
     * 取消账单
     *
     * @param dto 取消参数
     * @return 结果
     */
    public int cancelBill(BillCancelDTO dto);
    /**
     * 支付账单
     *
     * @param dto 支付参数
     * @return 结果
     */
    public int payBill(BillPayDTO dto);


    List<Bill> selectPendingBillsByElderId(Long elderId);

}
