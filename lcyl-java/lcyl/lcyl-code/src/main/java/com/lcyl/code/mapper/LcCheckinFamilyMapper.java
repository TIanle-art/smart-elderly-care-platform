package com.lcyl.code.mapper;

import java.util.List;
import com.lcyl.code.domain.LcCheckinFamily;

/**
 * 家属信息Mapper接口
 * 
 * @author ruoyi
 * @date 2026-03-23
 */
public interface LcCheckinFamilyMapper 
{
    /**
     * 查询家属信息
     * 
     * @param id 家属信息主键
     * @return 家属信息
     */
    public LcCheckinFamily selectLcCheckinFamilyById(Long id);

    /**
     * 查询家属信息列表
     * 
     * @param lcCheckinFamily 家属信息
     * @return 家属信息集合
     */
    public List<LcCheckinFamily> selectLcCheckinFamilyList(LcCheckinFamily lcCheckinFamily);

    /**
     * 新增家属信息
     * 
     * @param lcCheckinFamily 家属信息
     * @return 结果
     */
    public int insertLcCheckinFamily(LcCheckinFamily lcCheckinFamily);

    /**
     * 修改家属信息
     * 
     * @param lcCheckinFamily 家属信息
     * @return 结果
     */
    public int updateLcCheckinFamily(LcCheckinFamily lcCheckinFamily);

    /**
     * 删除家属信息
     * 
     * @param id 家属信息主键
     * @return 结果
     */
    public int deleteLcCheckinFamilyById(Long id);

    /**
     * 批量删除家属信息
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteLcCheckinFamilyByIds(Long[] ids);
}
