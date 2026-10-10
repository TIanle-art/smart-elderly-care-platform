package com.lcyl.code.service;

import java.util.List;
import com.lcyl.code.domain.LcCheckinHealthEvaluate;

/**
 * 健康评估Service接口
 * 
 * @author ruoyi
 * @date 2026-03-23
 */
public interface ILcCheckinHealthEvaluateService 
{
    /**
     * 查询健康评估
     * 
     * @param id 健康评估主键
     * @return 健康评估
     */
    public LcCheckinHealthEvaluate selectLcCheckinHealthEvaluateById(Long id);

    /**
     * 查询健康评估列表
     * 
     * @param lcCheckinHealthEvaluate 健康评估
     * @return 健康评估集合
     */
    public List<LcCheckinHealthEvaluate> selectLcCheckinHealthEvaluateList(LcCheckinHealthEvaluate lcCheckinHealthEvaluate);

    /**
     * 新增健康评估
     * 
     * @param lcCheckinHealthEvaluate 健康评估
     * @return 结果
     */
    public int insertLcCheckinHealthEvaluate(LcCheckinHealthEvaluate lcCheckinHealthEvaluate);

    /**
     * 修改健康评估
     * 
     * @param lcCheckinHealthEvaluate 健康评估
     * @return 结果
     */
    public int updateLcCheckinHealthEvaluate(LcCheckinHealthEvaluate lcCheckinHealthEvaluate);

    /**
     * 批量删除健康评估
     * 
     * @param ids 需要删除的健康评估主键集合
     * @return 结果
     */
    public int deleteLcCheckinHealthEvaluateByIds(Long[] ids);

    /**
     * 删除健康评估信息
     * 
     * @param id 健康评估主键
     * @return 结果
     */
    public int deleteLcCheckinHealthEvaluateById(Long id);
}
