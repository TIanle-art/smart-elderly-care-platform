package com.lcyl.system.service;

import com.lcyl.system.domain.LcRoomType;

import java.util.List;

/**
 * 房型设置Service接口
 * 
 * @author ruoyi
 * @date 2026-03-23
 */
public interface ILcRoomTypeService 
{
    /**
     * 查询房型设置
     * 
     * @param id 房型设置主键
     * @return 房型设置
     */
    public LcRoomType selectLcRoomTypeById(Long id);

    /**
     * 查询房型设置列表
     * 
     * @param lcRoomType 房型设置
     * @return 房型设置集合
     */
    public List<LcRoomType> selectLcRoomTypeList(LcRoomType lcRoomType);

    /**
     * 新增房型设置
     * 
     * @param lcRoomType 房型设置
     * @return 结果
     */
    public int insertLcRoomType(LcRoomType lcRoomType);

    /**
     * 修改房型设置
     * 
     * @param lcRoomType 房型设置
     * @return 结果
     */
    public int updateLcRoomType(LcRoomType lcRoomType);

    /**
     * 批量删除房型设置
     * 
     * @param ids 需要删除的房型设置主键集合
     * @return 结果
     */
    public int deleteLcRoomTypeByIds(Long[] ids);

    /**
     * 删除房型设置信息
     * 
     * @param id 房型设置主键
     * @return 结果
     */
    public int deleteLcRoomTypeById(Long id);
}
