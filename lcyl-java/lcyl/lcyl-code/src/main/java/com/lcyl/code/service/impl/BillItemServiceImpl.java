package com.lcyl.code.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.lcyl.code.mapper.BillItemMapper;
import com.lcyl.code.domain.BillItem;
import com.lcyl.code.service.IBillItemService;

/**
 * 账单明细Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-03-26
 */
@Service
public class BillItemServiceImpl implements IBillItemService 
{
    @Autowired
    private BillItemMapper billItemMapper;

    /**
     * 查询账单明细
     * 
     * @param id 账单明细主键
     * @return 账单明细
     */
    @Override
    public BillItem selectBillItemById(Long id)
    {
        return billItemMapper.selectBillItemById(id);
    }

    /**
     * 查询账单明细列表
     * 
     * @param billItem 账单明细
     * @return 账单明细
     */
    @Override
    public List<BillItem> selectBillItemList(BillItem billItem)
    {
        return billItemMapper.selectBillItemList(billItem);
    }

    /**
     * 新增账单明细
     * 
     * @param billItem 账单明细
     * @return 结果
     */
    @Override
    public int insertBillItem(BillItem billItem)
    {
        return billItemMapper.insertBillItem(billItem);
    }

    /**
     * 修改账单明细
     * 
     * @param billItem 账单明细
     * @return 结果
     */
    @Override
    public int updateBillItem(BillItem billItem)
    {
        return billItemMapper.updateBillItem(billItem);
    }

    /**
     * 批量删除账单明细
     * 
     * @param ids 需要删除的账单明细主键
     * @return 结果
     */
    @Override
    public int deleteBillItemByIds(Long[] ids)
    {
        return billItemMapper.deleteBillItemByIds(ids);
    }

    /**
     * 删除账单明细信息
     * 
     * @param id 账单明细主键
     * @return 结果
     */
    @Override
    public int deleteBillItemById(Long id)
    {
        return billItemMapper.deleteBillItemById(id);
    }

    @Override
    public List<BillItem> selectNursingItemsByElderId(Long elderId) {
        return billItemMapper.selectNursingItemsByElderId(elderId);
    }
}
