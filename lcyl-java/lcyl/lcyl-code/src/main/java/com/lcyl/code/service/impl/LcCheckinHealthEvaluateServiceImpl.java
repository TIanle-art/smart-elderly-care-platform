package com.lcyl.code.service.impl;

import java.util.List;
import com.lcyl.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.lcyl.code.mapper.LcCheckinHealthEvaluateMapper;
import com.lcyl.code.domain.LcCheckinHealthEvaluate;
import com.lcyl.code.service.ILcCheckinHealthEvaluateService;

/**
 * 健康评估Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-03-23
 */
@Service
public class LcCheckinHealthEvaluateServiceImpl implements ILcCheckinHealthEvaluateService 
{
    @Autowired
    private LcCheckinHealthEvaluateMapper lcCheckinHealthEvaluateMapper;

    /**
     * 查询健康评估
     * 
     * @param id 健康评估主键
     * @return 健康评估
     */
    @Override
    public LcCheckinHealthEvaluate selectLcCheckinHealthEvaluateById(Long id)
    {
        return lcCheckinHealthEvaluateMapper.selectLcCheckinHealthEvaluateById(id);
    }

    /**
     * 查询健康评估列表
     * 
     * @param lcCheckinHealthEvaluate 健康评估
     * @return 健康评估
     */
    @Override
    public List<LcCheckinHealthEvaluate> selectLcCheckinHealthEvaluateList(LcCheckinHealthEvaluate lcCheckinHealthEvaluate)
    {
        return lcCheckinHealthEvaluateMapper.selectLcCheckinHealthEvaluateList(lcCheckinHealthEvaluate);
    }

    /**
     * 新增健康评估
     * 
     * @param lcCheckinHealthEvaluate 健康评估
     * @return 结果
     */
    @Override
    public int insertLcCheckinHealthEvaluate(LcCheckinHealthEvaluate lcCheckinHealthEvaluate)
    {
        lcCheckinHealthEvaluate.setCreateTime(DateUtils.getNowDate());
        return lcCheckinHealthEvaluateMapper.insertLcCheckinHealthEvaluate(lcCheckinHealthEvaluate);
    }

    /**
     * 修改健康评估
     * 
     * @param lcCheckinHealthEvaluate 健康评估
     * @return 结果
     */
    @Override
    public int updateLcCheckinHealthEvaluate(LcCheckinHealthEvaluate lcCheckinHealthEvaluate)
    {
        return lcCheckinHealthEvaluateMapper.updateLcCheckinHealthEvaluate(lcCheckinHealthEvaluate);
    }

    /**
     * 批量删除健康评估
     * 
     * @param ids 需要删除的健康评估主键
     * @return 结果
     */
    @Override
    public int deleteLcCheckinHealthEvaluateByIds(Long[] ids)
    {
        return lcCheckinHealthEvaluateMapper.deleteLcCheckinHealthEvaluateByIds(ids);
    }

    /**
     * 删除健康评估信息
     * 
     * @param id 健康评估主键
     * @return 结果
     */
    @Override
    public int deleteLcCheckinHealthEvaluateById(Long id)
    {
        return lcCheckinHealthEvaluateMapper.deleteLcCheckinHealthEvaluateById(id);
    }
}
