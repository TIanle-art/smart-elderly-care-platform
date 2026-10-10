package com.lcyl.system.service;

import com.lcyl.system.domain.Elder;
import com.lcyl.system.domain.dto.AddInfo;
import com.lcyl.system.domain.vo.BedVO;

import java.util.List;

/**
 * 老人信息Service接口
 * 
 * @author tyf
 * @date 2026-03-24
 */
public interface IElderService 
{
    /**
     * 查询老人信息
     * 
     * @param id 老人信息主键
     * @return 老人信息
     */
    public Elder selectElderById(Long id);

    /**
     * 查询老人信息列表
     * 
     * @param elder 老人信息
     * @return 老人信息集合
     */
    public List<Elder> selectElderList(Elder elder);

    /**
     * 新增老人信息
     * 
     * @param elder 老人信息
     * @return 结果
     */
    public int insertElder(Elder elder);

    /**
     * 修改老人信息
     * 
     * @param elder 老人信息
     * @return 结果
     */
    public int updateElder(Elder elder);

    /**
     * 批量删除老人信息
     * 
     * @param ids 需要删除的老人信息主键集合
     * @return 结果
     */
    public int deleteElderByIds(Long[] ids);

    /**
     * 删除老人信息信息
     * 
     * @param id 老人信息主键
     * @return 结果
     */
    public int deleteElderById(Long id);

    List<Elder> selectElderByUser(Long memberId);

    List<Elder> selectAllElder();

    String insertInfo(AddInfo addInfo);

    List<BedVO> getElderBedList();

    Integer deleteElderInfoById(Long id);
}
