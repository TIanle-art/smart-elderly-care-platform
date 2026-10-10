package com.lcyl.code.service.impl;

import java.util.List;

import com.lcyl.code.domain.LcNursingLevel;
import com.lcyl.code.domain.Nurse;
import com.lcyl.code.utils.NurseUtils;
import com.lcyl.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.lcyl.code.mapper.LcNursingLevelMapper;
import com.lcyl.code.service.INursingLevelService;

/**
 * 护理等级Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-03-24
 */
@Service
public class NursingLevelServiceImpl implements INursingLevelService 
{
    @Autowired
    private LcNursingLevelMapper lcNursingLevelMapper;

    /**
     * 查询护理等级
     * 
     * @param id 护理等级主键
     * @return 护理等级
     */
    @Override
    public LcNursingLevel selectNursingLevelById(Long id)
    {
        return lcNursingLevelMapper.selectNursingLevelById(id);
    }

    /**
     * 查询护理等级列表
     * 
     * @param lcNursingLevel 护理等级
     * @return 护理等级
     */
    @Override
    public List<LcNursingLevel> selectNursingLevelList(LcNursingLevel lcNursingLevel)
    {
        return lcNursingLevelMapper.selectNursingLevelList(lcNursingLevel);
    }

    /**
     * 新增护理等级
     * 
     * @param lcNursingLevel 护理等级
     * @return 结果
     */
    @Override
    public int insertNursingLevel(LcNursingLevel lcNursingLevel)
    {
        // 1. 获取当前登录的护理员
        Nurse currentNurse = NurseUtils.getCurrentNurse();

        // 2. 自动填充创建人ID和姓名
        lcNursingLevel.setCreatorId(currentNurse.getId());
        lcNursingLevel.setCreatorName(currentNurse.getName());
        lcNursingLevel.setCreateTime(DateUtils.getNowDate());
        return lcNursingLevelMapper.insertNursingLevel(lcNursingLevel);
    }

    /**
     * 修改护理等级
     * 
     * @param lcNursingLevel 护理等级
     * @return 结果
     */
    @Override
    public int updateNursingLevel(LcNursingLevel lcNursingLevel)
    {
        lcNursingLevel.setUpdateTime(DateUtils.getNowDate());
        return lcNursingLevelMapper.updateNursingLevel(lcNursingLevel);
    }

    /**
     * 批量删除护理等级
     * 
     * @param ids 需要删除的护理等级主键
     * @return 结果
     */
    @Override
    public int deleteNursingLevelByIds(Long[] ids)
    {
        return lcNursingLevelMapper.deleteNursingLevelByIds(ids);
    }

    /**
     * 删除护理等级信息
     * 
     * @param id 护理等级主键
     * @return 结果
     */
    @Override
    public int deleteNursingLevelById(Long id)
    {
        return lcNursingLevelMapper.deleteNursingLevelById(id);
    }

    @Override
    public boolean checkLevelNameUnique(String levelName,Long excludeId) {
        return lcNursingLevelMapper.countLevelName(levelName,excludeId)==0;
    }

    @Override
    public int countByNursingLevelId(Long nursingLevelId) {
        return lcNursingLevelMapper.countByNursingLevelId(nursingLevelId);
    }
}
