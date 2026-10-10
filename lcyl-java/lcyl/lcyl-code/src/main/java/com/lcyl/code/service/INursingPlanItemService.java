package com.lcyl.code.service;

import java.util.List;
import com.lcyl.code.domain.NursingPlanItem;

/**
 * 护理计划-项目关联Service接口
 * 
 * @author ruoyi
 * @date 2026-03-23
 */
public interface INursingPlanItemService 
{
    /**
     * 查询护理计划-项目关联
     * 
     * @param id 护理计划-项目关联主键
     * @return 护理计划-项目关联
     */
    public NursingPlanItem selectNursingPlanItemById(Long id);

    /**
     * 查询护理计划-项目关联列表
     * 
     * @param nursingPlanItem 护理计划-项目关联
     * @return 护理计划-项目关联集合
     */
    public List<NursingPlanItem> selectNursingPlanItemList(NursingPlanItem nursingPlanItem);

    /**
     * 新增护理计划-项目关联
     * 
     * @param nursingPlanItem 护理计划-项目关联
     * @return 结果
     */
    public int insertNursingPlanItem(NursingPlanItem nursingPlanItem);

    /**
     * 修改护理计划-项目关联
     * 
     * @param nursingPlanItem 护理计划-项目关联
     * @return 结果
     */
    public int updateNursingPlanItem(NursingPlanItem nursingPlanItem);

    /**
     * 批量删除护理计划-项目关联
     * 
     * @param ids 需要删除的护理计划-项目关联主键集合
     * @return 结果
     */
    public int deleteNursingPlanItemByIds(Long[] ids);

    /**
     * 删除护理计划-项目关联信息
     * 
     * @param id 护理计划-项目关联主键
     * @return 结果
     */
    public int deleteNursingPlanItemById(Long id);

    int countByItemId(Long itemId);
}
