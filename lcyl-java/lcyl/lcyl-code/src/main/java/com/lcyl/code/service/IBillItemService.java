package com.lcyl.code.service;

import java.util.List;
import com.lcyl.code.domain.BillItem;

/**
 * 账单明细Service接口
 * 
 * @author ruoyi
 * @date 2026-03-26
 */
public interface IBillItemService 
{
    /**
     * 查询账单明细
     * 
     * @param id 账单明细主键
     * @return 账单明细
     */
    public BillItem selectBillItemById(Long id);

    /**
     * 查询账单明细列表
     * 
     * @param billItem 账单明细
     * @return 账单明细集合
     */
    public List<BillItem> selectBillItemList(BillItem billItem);

    /**
     * 新增账单明细
     * 
     * @param billItem 账单明细
     * @return 结果
     */
    public int insertBillItem(BillItem billItem);

    /**
     * 修改账单明细
     * 
     * @param billItem 账单明细
     * @return 结果
     */
    public int updateBillItem(BillItem billItem);

    /**
     * 批量删除账单明细
     * 
     * @param ids 需要删除的账单明细主键集合
     * @return 结果
     */
    public int deleteBillItemByIds(Long[] ids);

    /**
     * 删除账单明细信息
     * 
     * @param id 账单明细主键
     * @return 结果
     */
    public int deleteBillItemById(Long id);

    List<BillItem> selectNursingItemsByElderId(Long elderId);
}
