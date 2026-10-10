package com.lcyl.code.service.impl;

import java.util.List;
import com.lcyl.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.lcyl.code.mapper.LcCheckinFamilyMapper;
import com.lcyl.code.domain.LcCheckinFamily;
import com.lcyl.code.service.ILcCheckinFamilyService;

/**
 * 家属信息Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-03-23
 */
@Service
public class LcCheckinFamilyServiceImpl implements ILcCheckinFamilyService 
{
    @Autowired
    private LcCheckinFamilyMapper lcCheckinFamilyMapper;

    /**
     * 查询家属信息
     * 
     * @param id 家属信息主键
     * @return 家属信息
     */
    @Override
    public LcCheckinFamily selectLcCheckinFamilyById(Long id)
    {
        return lcCheckinFamilyMapper.selectLcCheckinFamilyById(id);
    }

    /**
     * 查询家属信息列表
     * 
     * @param lcCheckinFamily 家属信息
     * @return 家属信息
     */
    @Override
    public List<LcCheckinFamily> selectLcCheckinFamilyList(LcCheckinFamily lcCheckinFamily)
    {
        return lcCheckinFamilyMapper.selectLcCheckinFamilyList(lcCheckinFamily);
    }

    /**
     * 新增家属信息
     * 
     * @param lcCheckinFamily 家属信息
     * @return 结果
     */
    @Override
    public int insertLcCheckinFamily(LcCheckinFamily lcCheckinFamily)
    {
        lcCheckinFamily.setCreateTime(DateUtils.getNowDate());
        return lcCheckinFamilyMapper.insertLcCheckinFamily(lcCheckinFamily);
    }

    /**
     * 修改家属信息
     * 
     * @param lcCheckinFamily 家属信息
     * @return 结果
     */
    @Override
    public int updateLcCheckinFamily(LcCheckinFamily lcCheckinFamily)
    {
        lcCheckinFamily.setUpdateTime(DateUtils.getNowDate());
        return lcCheckinFamilyMapper.updateLcCheckinFamily(lcCheckinFamily);
    }

    /**
     * 批量删除家属信息
     * 
     * @param ids 需要删除的家属信息主键
     * @return 结果
     */
    @Override
    public int deleteLcCheckinFamilyByIds(Long[] ids)
    {
        return lcCheckinFamilyMapper.deleteLcCheckinFamilyByIds(ids);
    }

    /**
     * 删除家属信息信息
     * 
     * @param id 家属信息主键
     * @return 结果
     */
    @Override
    public int deleteLcCheckinFamilyById(Long id)
    {
        return lcCheckinFamilyMapper.deleteLcCheckinFamilyById(id);
    }
}
