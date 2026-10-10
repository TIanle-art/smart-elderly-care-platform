package com.lcyl.code.mapper;

import java.util.List;
import com.lcyl.code.domain.NursingItem;
import org.apache.ibatis.annotations.Param;

/**
 * 护理项目Mapper接口
 * 
 * @author ruoyi
 * @date 2026-03-23
 */
public interface NursingItemMapper 
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
     * 删除护理项目
     * 
     * @param id 护理项目主键
     * @return 结果
     */
    public int deleteNursingItemById(Long id);

    /**
     * 批量删除护理项目
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteNursingItemByIds(Long[] ids);
    Integer countItemName(@Param("itemName") String itemName);
}
