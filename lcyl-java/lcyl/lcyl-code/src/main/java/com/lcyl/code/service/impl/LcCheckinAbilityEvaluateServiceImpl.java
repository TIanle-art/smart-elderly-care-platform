package com.lcyl.code.service.impl;

import java.util.List;
import com.lcyl.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.lcyl.code.mapper.LcCheckinAbilityEvaluateMapper;
import com.lcyl.code.domain.LcCheckinAbilityEvaluate;
import com.lcyl.code.service.ILcCheckinAbilityEvaluateService;

/**
 * 能力评估Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-03-23
 */
@Service
public class LcCheckinAbilityEvaluateServiceImpl implements ILcCheckinAbilityEvaluateService 
{
    @Autowired
    private LcCheckinAbilityEvaluateMapper lcCheckinAbilityEvaluateMapper;

    /**
     * 查询能力评估
     * 
     * @param id 能力评估主键
     * @return 能力评估
     */
    @Override
    public LcCheckinAbilityEvaluate selectLcCheckinAbilityEvaluateById(Long id)
    {
        return lcCheckinAbilityEvaluateMapper.selectLcCheckinAbilityEvaluateById(id);
    }

    /**
     * 查询能力评估列表
     * 
     * @param lcCheckinAbilityEvaluate 能力评估
     * @return 能力评估
     */
    @Override
    public List<LcCheckinAbilityEvaluate> selectLcCheckinAbilityEvaluateList(LcCheckinAbilityEvaluate lcCheckinAbilityEvaluate)
    {
        return lcCheckinAbilityEvaluateMapper.selectLcCheckinAbilityEvaluateList(lcCheckinAbilityEvaluate);
    }

    /**
     * 新增能力评估
     * 
     * @param lcCheckinAbilityEvaluate 能力评估
     * @return 结果
     */
    @Override
    public int insertLcCheckinAbilityEvaluate(LcCheckinAbilityEvaluate lcCheckinAbilityEvaluate)
    {
        lcCheckinAbilityEvaluate.setCreateTime(DateUtils.getNowDate());
        return lcCheckinAbilityEvaluateMapper.insertLcCheckinAbilityEvaluate(lcCheckinAbilityEvaluate);
    }

    /**
     * 修改能力评估
     * 
     * @param lcCheckinAbilityEvaluate 能力评估
     * @return 结果
     */
    @Override
    public int updateLcCheckinAbilityEvaluate(LcCheckinAbilityEvaluate lcCheckinAbilityEvaluate)
    {
        return lcCheckinAbilityEvaluateMapper.updateLcCheckinAbilityEvaluate(lcCheckinAbilityEvaluate);
    }

    /**
     * 批量删除能力评估
     * 
     * @param ids 需要删除的能力评估主键
     * @return 结果
     */
    @Override
    public int deleteLcCheckinAbilityEvaluateByIds(Long[] ids)
    {
        return lcCheckinAbilityEvaluateMapper.deleteLcCheckinAbilityEvaluateByIds(ids);
    }

    /**
     * 删除能力评估信息
     * 
     * @param id 能力评估主键
     * @return 结果
     */
    @Override
    public int deleteLcCheckinAbilityEvaluateById(Long id)
    {
        return lcCheckinAbilityEvaluateMapper.deleteLcCheckinAbilityEvaluateById(id);
    }
}
