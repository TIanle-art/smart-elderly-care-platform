package com.lcyl.code.service;

import java.util.List;
import com.lcyl.code.domain.LcCheckinConfig;

/**
 * 入住配置Service接口
 * 
 * @author ruoyi
 * @date 2026-03-23
 */
public interface ILcCheckinConfigService 
{
    /**
     * 查询入住配置
     * 
     * @param id 入住配置主键
     * @return 入住配置
     */
    public LcCheckinConfig selectLcCheckinConfigById(Long id);

    /**
     * 查询入住配置列表
     * 
     * @param lcCheckinConfig 入住配置
     * @return 入住配置集合
     */
    public List<LcCheckinConfig> selectLcCheckinConfigList(LcCheckinConfig lcCheckinConfig);

    /**
     * 新增入住配置
     * 
     * @param lcCheckinConfig 入住配置
     * @return 结果
     */
    public int insertLcCheckinConfig(LcCheckinConfig lcCheckinConfig);

    /**
     * 修改入住配置
     * 
     * @param lcCheckinConfig 入住配置
     * @return 结果
     */
    public int updateLcCheckinConfig(LcCheckinConfig lcCheckinConfig);

    /**
     * 批量删除入住配置
     * 
     * @param ids 需要删除的入住配置主键集合
     * @return 结果
     */
    public int deleteLcCheckinConfigByIds(Long[] ids);

    /**
     * 删除入住配置信息
     * 
     * @param id 入住配置主键
     * @return 结果
     */
    public int deleteLcCheckinConfigById(Long id);
}
