package com.lcyl.code.mapper;

import java.util.List;
import com.lcyl.code.domain.BillItem;

/**
 * 账单明细Mapper接口
 * 
 * @author ruoyi
 * @date 2026-03-26
 */
public interface BillItemMapper 
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
     * 删除账单明细
     * 
     * @param id 账单明细主键
     * @return 结果
     */
    public int deleteBillItemById(Long id);

    /**
     * 批量删除账单明细
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBillItemByIds(Long[] ids);

    List<BillItem> selectNursingItemsByElderId(Long elderId);
}
