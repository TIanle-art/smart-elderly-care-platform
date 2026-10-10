package com.lcyl.code.service;

import java.util.List;
import com.lcyl.code.domain.LcCheckinAbilityEvaluate;

/**
 * 能力评估Service接口
 * 
 * @author ruoyi
 * @date 2026-03-23
 */
public interface ILcCheckinAbilityEvaluateService 
{
    /**
     * 查询能力评估
     * 
     * @param id 能力评估主键
     * @return 能力评估
     */
    public LcCheckinAbilityEvaluate selectLcCheckinAbilityEvaluateById(Long id);

    /**
     * 查询能力评估列表
     * 
     * @param lcCheckinAbilityEvaluate 能力评估
     * @return 能力评估集合
     */
    public List<LcCheckinAbilityEvaluate> selectLcCheckinAbilityEvaluateList(LcCheckinAbilityEvaluate lcCheckinAbilityEvaluate);

    /**
     * 新增能力评估
     * 
     * @param lcCheckinAbilityEvaluate 能力评估
     * @return 结果
     */
    public int insertLcCheckinAbilityEvaluate(LcCheckinAbilityEvaluate lcCheckinAbilityEvaluate);

    /**
     * 修改能力评估
     * 
     * @param lcCheckinAbilityEvaluate 能力评估
     * @return 结果
     */
    public int updateLcCheckinAbilityEvaluate(LcCheckinAbilityEvaluate lcCheckinAbilityEvaluate);

    /**
     * 批量删除能力评估
     * 
     * @param ids 需要删除的能力评估主键集合
     * @return 结果
     */
    public int deleteLcCheckinAbilityEvaluateByIds(Long[] ids);

    /**
     * 删除能力评估信息
     * 
     * @param id 能力评估主键
     * @return 结果
     */
    public int deleteLcCheckinAbilityEvaluateById(Long id);
}
