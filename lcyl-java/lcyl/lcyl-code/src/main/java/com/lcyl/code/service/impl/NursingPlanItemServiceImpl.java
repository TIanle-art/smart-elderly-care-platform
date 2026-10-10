package com.lcyl.code.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.lcyl.code.mapper.NursingPlanItemMapper;
import com.lcyl.code.domain.NursingPlanItem;
import com.lcyl.code.service.INursingPlanItemService;

/**
 * 护理计划-项目关联Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-03-23
 */
@Service
public class NursingPlanItemServiceImpl implements INursingPlanItemService 
{
    @Autowired
    private NursingPlanItemMapper nursingPlanItemMapper;

    /**
     * 查询护理计划-项目关联
     * 
     * @param id 护理计划-项目关联主键
     * @return 护理计划-项目关联
     */
    @Override
    public NursingPlanItem selectNursingPlanItemById(Long id)
    {
        return nursingPlanItemMapper.selectNursingPlanItemById(id);
    }

    /**
     * 查询护理计划-项目关联列表
     * 
     * @param nursingPlanItem 护理计划-项目关联
     * @return 护理计划-项目关联
     */
    @Override
    public List<NursingPlanItem> selectNursingPlanItemList(NursingPlanItem nursingPlanItem)
    {
        return nursingPlanItemMapper.selectNursingPlanItemList(nursingPlanItem);
    }

    /**
     * 新增护理计划-项目关联
     * 
     * @param nursingPlanItem 护理计划-项目关联
     * @return 结果
     */
    @Override
    public int insertNursingPlanItem(NursingPlanItem nursingPlanItem)
    {
        return nursingPlanItemMapper.insertNursingPlanItem(nursingPlanItem);
    }

    /**
     * 修改护理计划-项目关联
     * 
     * @param nursingPlanItem 护理计划-项目关联
     * @return 结果
     */
    @Override
    public int updateNursingPlanItem(NursingPlanItem nursingPlanItem)
    {
        return nursingPlanItemMapper.updateNursingPlanItem(nursingPlanItem);
    }

    /**
     * 批量删除护理计划-项目关联
     * 
     * @param ids 需要删除的护理计划-项目关联主键
     * @return 结果
     */
    @Override
    public int deleteNursingPlanItemByIds(Long[] ids)
    {
        return nursingPlanItemMapper.deleteNursingPlanItemByIds(ids);
    }

    /**
     * 删除护理计划-项目关联信息
     * 
     * @param id 护理计划-项目关联主键
     * @return 结果
     */
    @Override
    public int deleteNursingPlanItemById(Long id)
    {
        return nursingPlanItemMapper.deleteNursingPlanItemById(id);
    }

    @Override
    public int countByItemId(Long itemId) {
        return nursingPlanItemMapper.countByItemId(itemId);
    }
}
