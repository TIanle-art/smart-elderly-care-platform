package com.lcyl.code.service.impl;

import java.util.List;
import com.lcyl.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.lcyl.code.mapper.NurseMapper;
import com.lcyl.code.domain.Nurse;
import com.lcyl.code.service.INurseService;

/**
 * 护理员信息Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-03-23
 */
@Service
public class NurseServiceImpl implements INurseService 
{
    @Autowired
    private NurseMapper nurseMapper;

    /**
     * 查询护理员信息
     * 
     * @param id 护理员信息主键
     * @return 护理员信息
     */
    @Override
    public Nurse selectNurseById(Long id)
    {
        return nurseMapper.selectNurseById(id);
    }

    /**
     * 查询护理员信息列表
     * 
     * @param nurse 护理员信息
     * @return 护理员信息
     */
    @Override
    public List<Nurse> selectNurseList(Nurse nurse)
    {
        return nurseMapper.selectNurseList(nurse);
    }

    /**
     * 新增护理员信息
     * 
     * @param nurse 护理员信息
     * @return 结果
     */
    @Override
    public int insertNurse(Nurse nurse)
    {
        nurse.setCreateTime(DateUtils.getNowDate());
        return nurseMapper.insertNurse(nurse);
    }

    /**
     * 修改护理员信息
     * 
     * @param nurse 护理员信息
     * @return 结果
     */
    @Override
    public int updateNurse(Nurse nurse)
    {
        return nurseMapper.updateNurse(nurse);
    }

    /**
     * 批量删除护理员信息
     * 
     * @param ids 需要删除的护理员信息主键
     * @return 结果
     */
    @Override
    public int deleteNurseByIds(Long[] ids)
    {
        return nurseMapper.deleteNurseByIds(ids);
    }

    /**
     * 删除护理员信息信息
     * 
     * @param id 护理员信息主键
     * @return 结果
     */
    @Override
    public int deleteNurseById(Long id)
    {
        return nurseMapper.deleteNurseById(id);
    }

    @Override
    public Nurse selectNurseByUserId(Long userId) {
        return nurseMapper.selectNurseByUserId(userId);
    }

}
