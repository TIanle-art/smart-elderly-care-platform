package com.lcyl.code.service;

import java.util.List;

import com.lcyl.code.domain.LcNursingLevel;

/**
 * 护理等级Service接口
 * 
 * @author ruoyi
 * @date 2026-03-24
 */
public interface INursingLevelService 
{
    /**
     * 查询护理等级
     * 
     * @param id 护理等级主键
     * @return 护理等级
     */
    public LcNursingLevel selectNursingLevelById(Long id);

    /**
     * 查询护理等级列表
     * 
     * @param lcNursingLevel 护理等级
     * @return 护理等级集合
     */
    public List<LcNursingLevel> selectNursingLevelList(LcNursingLevel lcNursingLevel);

    /**
     * 新增护理等级
     * 
     * @param lcNursingLevel 护理等级
     * @return 结果
     */
    public int insertNursingLevel(LcNursingLevel lcNursingLevel);

    /**
     * 修改护理等级
     * 
     * @param lcNursingLevel 护理等级
     * @return 结果
     */
    public int updateNursingLevel(LcNursingLevel lcNursingLevel);

    /**
     * 批量删除护理等级
     * 
     * @param ids 需要删除的护理等级主键集合
     * @return 结果
     */
    public int deleteNursingLevelByIds(Long[] ids);

    /**
     * 删除护理等级信息
     * 
     * @param id 护理等级主键
     * @return 结果
     */
    public int deleteNursingLevelById(Long id);
    boolean checkLevelNameUnique(String levelName,Long excludeId);

    int countByNursingLevelId(Long nursingLevelId);
}
