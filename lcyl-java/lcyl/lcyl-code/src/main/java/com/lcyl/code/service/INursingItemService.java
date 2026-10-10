package com.lcyl.code.service;

import java.util.List;
import com.lcyl.code.domain.NursingItem;

/**
 * 护理项目Service接口
 * 
 * @author ruoyi
 * @date 2026-03-23
 */
public interface INursingItemService 
{
    /**
     * 查询护理项目
     * 
     * @param id 护理项目主键
     * @return 护理项目
     */
    public NursingItem selectNursingItemById(Long id);

    /**
     * 查询护理项目列表
     * 
     * @param nursingItem 护理项目
     * @return 护理项目集合
     */
    public List<NursingItem> selectNursingItemList(NursingItem nursingItem);

    /**
     * 新增护理项目
     * 
     * @param nursingItem 护理项目
     * @return 结果
     */
    public int insertNursingItem(NursingItem nursingItem);

    /**
     * 修改护理项目
     * 
     * @param nursingItem 护理项目
     * @return 结果
     */
    public int updateNursingItem(NursingItem nursingItem);

    /**
     * 批量删除护理项目
     * 
     * @param ids 需要删除的护理项目主键集合
     * @return 结果
     */
    public int deleteNursingItemByIds(Long[] ids);

    /**
     * 删除护理项目信息
     * 
     * @param id 护理项目主键
     * @return 结果
     */
    public int deleteNursingItemById(Long id);
    boolean checkItemNameUnique(String itemName);
}
