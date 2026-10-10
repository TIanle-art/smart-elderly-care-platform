package com.lcyl.system.mapper;

import com.lcyl.system.domain.Floor;

import java.util.List;

/**
 * 房位房型Mapper接口
 * 
 * @author ruoyi
 * @date 2026-03-21
 */
public interface FloorMapper 
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
     * 删除房位房型
     * 
     * @param id 房位房型主键
     * @return 结果
     */
    public int deleteFloorById(Long id);

    /**
     * 批量删除房位房型
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFloorByIds(Long[] ids);
}
