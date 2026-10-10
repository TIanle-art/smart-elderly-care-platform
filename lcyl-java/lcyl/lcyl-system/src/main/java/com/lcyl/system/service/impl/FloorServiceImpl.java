package com.lcyl.system.service.impl;

import com.lcyl.common.utils.DateUtils;
import com.lcyl.system.domain.Floor;
import com.lcyl.system.mapper.FloorMapper;
import com.lcyl.system.service.IFloorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 房位房型Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-03-21
 */
@Service
public class FloorServiceImpl implements IFloorService 
{
    @Autowired
    private FloorMapper floorMapper;

    /**
     * 查询房位房型
     * 
     * @param id 房位房型主键
     * @return 房位房型
     */
    @Override
    public Floor selectFloorById(Long id)
    {
        return floorMapper.selectFloorById(id);
    }

    /**
     * 查询房位房型列表
     * 
     * @param floor 房位房型
     * @return 房位房型
     */
    @Override
    public List<Floor> selectFloorList(Floor floor)
    {
        return floorMapper.selectFloorList(floor);
    }

    /**
     * 新增房位房型
     * 
     * @param floor 房位房型
     * @return 结果
     */
    @Override
    public int insertFloor(Floor floor)
    {
        floor.setCreateTime(DateUtils.getNowDate());
        return floorMapper.insertFloor(floor);
    }

    /**
     * 修改房位房型
     * 
     * @param floor 房位房型
     * @return 结果
     */
    @Override
    public int updateFloor(Floor floor)
    {
        floor.setUpdateTime(DateUtils.getNowDate());
        return floorMapper.updateFloor(floor);
    }

    /**
     * 批量删除房位房型
     * 
     * @param ids 需要删除的房位房型主键
     * @return 结果
     */
    @Override
    public int deleteFloorByIds(Long[] ids)
    {
        return floorMapper.deleteFloorByIds(ids);
    }

    /**
     * 删除房位房型信息
     * 
     * @param id 房位房型主键
     * @return 结果
     */
    @Override
    public int deleteFloorById(Long id)
    {
        return floorMapper.deleteFloorById(id);
    }
}
