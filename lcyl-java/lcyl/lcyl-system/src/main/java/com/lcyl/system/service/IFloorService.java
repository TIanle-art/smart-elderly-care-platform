package com.lcyl.system.service;

import com.lcyl.system.domain.Floor;

import java.util.List;

/**
 * 房位房型Service接口
 * 
 * @author ruoyi
 * @date 2026-03-21
 */
public interface IFloorService 
{
    /**
     * 查询房位房型
     * 
     * @param id 房位房型主键
     * @return 房位房型
     */
    public Floor selectFloorById(Long id);

    /**
     * 查询房位房型列表
     * 
     * @param floor 房位房型
     * @return 房位房型集合
     */
    public List<Floor> selectFloorList(Floor floor);

    /**
     * 新增房位房型
     * 
     * @param floor 房位房型
     * @return 结果
     */
    public int insertFloor(Floor floor);

    /**
     * 修改房位房型
     * 
     * @param floor 房位房型
     * @return 结果
     */
    public int updateFloor(Floor floor);

    /**
     * 批量删除房位房型
     * 
     * @param ids 需要删除的房位房型主键集合
     * @return 结果
     */
    public int deleteFloorByIds(Long[] ids);

    /**
     * 删除房位房型信息
     * 
     * @param id 房位房型主键
     * @return 结果
     */
    public int deleteFloorById(Long id);
}
