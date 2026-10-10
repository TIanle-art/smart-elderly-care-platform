package com.lcyl.code.service.impl;

import java.util.List;
import com.lcyl.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.lcyl.code.mapper.LcCheckinConfigMapper;
import com.lcyl.code.domain.LcCheckinConfig;
import com.lcyl.code.service.ILcCheckinConfigService;

/**
 * 入住配置Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-03-23
 */
@Service
public class LcCheckinConfigServiceImpl implements ILcCheckinConfigService 
{
    @Autowired
    private LcCheckinConfigMapper lcCheckinConfigMapper;

    /**
     * 查询入住配置
     * 
     * @param id 入住配置主键
     * @return 入住配置
     */
    @Override
    public LcCheckinConfig selectLcCheckinConfigById(Long id)
    {
        return lcCheckinConfigMapper.selectLcCheckinConfigById(id);
    }

    /**
     * 查询入住配置列表
     * 
     * @param lcCheckinConfig 入住配置
     * @return 入住配置
     */
    @Override
    public List<LcCheckinConfig> selectLcCheckinConfigList(LcCheckinConfig lcCheckinConfig)
    {
        return lcCheckinConfigMapper.selectLcCheckinConfigList(lcCheckinConfig);
    }

    /**
     * 新增入住配置
     * 
     * @param lcCheckinConfig 入住配置
     * @return 结果
     */
    @Override
    public int insertLcCheckinConfig(LcCheckinConfig lcCheckinConfig)
    {
        lcCheckinConfig.setCreateTime(DateUtils.getNowDate());
        return lcCheckinConfigMapper.insertLcCheckinConfig(lcCheckinConfig);
    }

    /**
     * 修改入住配置
     * 
     * @param lcCheckinConfig 入住配置
     * @return 结果
     */
    @Override
    public int updateLcCheckinConfig(LcCheckinConfig lcCheckinConfig)
    {
        return lcCheckinConfigMapper.updateLcCheckinConfig(lcCheckinConfig);
    }

    /**
     * 批量删除入住配置
     * 
     * @param ids 需要删除的入住配置主键
     * @return 结果
     */
    @Override
    public int deleteLcCheckinConfigByIds(Long[] ids)
    {
        return lcCheckinConfigMapper.deleteLcCheckinConfigByIds(ids);
    }

    /**
     * 删除入住配置信息
     * 
     * @param id 入住配置主键
     * @return 结果
     */
    @Override
    public int deleteLcCheckinConfigById(Long id)
    {
        return lcCheckinConfigMapper.deleteLcCheckinConfigById(id);
    }
}
