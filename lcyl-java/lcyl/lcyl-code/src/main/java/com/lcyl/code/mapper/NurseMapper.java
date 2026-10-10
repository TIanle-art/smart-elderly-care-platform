package com.lcyl.code.mapper;

import java.util.List;
import com.lcyl.code.domain.Nurse;

/**
 * 护理员信息Mapper接口
 * 
 * @author ruoyi
 * @date 2026-03-23
 */
public interface NurseMapper 
{
    /**
     * 查询护理员信息
     * 
     * @param id 护理员信息主键
     * @return 护理员信息
     */
    public Nurse selectNurseById(Long id);

    /**
     * 查询护理员信息列表
     * 
     * @param nurse 护理员信息
     * @return 护理员信息集合
     */
    public List<Nurse> selectNurseList(Nurse nurse);

    /**
     * 新增护理员信息
     * 
     * @param nurse 护理员信息
     * @return 结果
     */
    public int insertNurse(Nurse nurse);

    /**
     * 修改护理员信息
     * 
     * @param nurse 护理员信息
     * @return 结果
     */
    public int updateNurse(Nurse nurse);

    /**
     * 删除护理员信息
     * 
     * @param id 护理员信息主键
     * @return 结果
     */
    public int deleteNurseById(Long id);

    /**
     * 批量删除护理员信息
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteNurseByIds(Long[] ids);

    Nurse selectNurseByUserId(Long userId);
}
