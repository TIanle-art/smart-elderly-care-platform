package com.lcyl.code.mapper;

import java.util.List;
import com.lcyl.code.domain.NursingPlan;
import com.lcyl.code.domain.NursingPlanItem;
import org.apache.ibatis.annotations.Param;

/**
 * 护理计划Mapper接口
 * 
 * @author ruoyi
 * @date 2026-03-23
 */
public interface NursingPlanMapper 
{
    /**
     * 查询护理计划
     * 
     * @param id 护理计划主键
     * @return 护理计划
     */
    public NursingPlan selectNursingPlanById(Long id);

    /**
     * 查询护理计划列表
     * 
     * @param nursingPlan 护理计划
     * @return 护理计划集合
     */
    public List<NursingPlan> selectNursingPlanList(NursingPlan nursingPlan);

    /**
     * 新增护理计划
     * 
     * @param nursingPlan 护理计划
     * @return 结果
     */
    public int insertNursingPlan(NursingPlan nursingPlan);

    /**
     * 修改护理计划
     * 
     * @param nursingPlan 护理计划
     * @return 结果
     */
    public int updateNursingPlan(NursingPlan nursingPlan);

    /**
     * 删除护理计划
     * 
     * @param id 护理计划主键
     * @return 结果
     */
    public int deleteNursingPlanById(Long id);

    /**
     * 批量删除护理计划
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteNursingPlanByIds(Long[] ids);

    /**
     * 批量删除护理计划-项目关联
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteNursingPlanItemByPlanIds(Long[] ids);
    
    /**
     * 批量新增护理计划-项目关联
     * 
     * @param nursingPlanItemList 护理计划-项目关联列表
     * @return 结果
     */
    public int batchNursingPlanItem(List<NursingPlanItem> nursingPlanItemList);
    

    /**
     * 通过护理计划主键删除护理计划-项目关联信息
     * 
     * @param id 护理计划ID
     * @return 结果
     */
    public int deleteNursingPlanItemByPlanId(Long id);
    Integer countPlanName(@Param("planName") String planName);
}
