package com.lcyl.code.mapper;

import java.util.List;

import com.lcyl.code.domain.LcNursingLevel;
import org.apache.ibatis.annotations.Param;

/**
 * 护理等级Mapper接口
 * 
 * @author ruoyi
 * @date 2026-03-24
 */
public interface LcNursingLevelMapper
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
     * 删除护理等级
     * 
     * @param id 护理等级主键
     * @return 结果
     */
    public int deleteNursingLevelById(Long id);

    /**
     * 批量删除护理等级
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteNursingLevelByIds(Long[] ids);
    Integer countLevelName(@Param("levelName") String levelName,@Param("excludeId") Long excludeId);

    public int countByNursingLevelId(Long NursingLevelId);
}
