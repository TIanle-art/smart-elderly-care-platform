package com.lcyl.system.service.impl;

import com.lcyl.common.utils.DateUtils;
import com.lcyl.system.domain.Room;
import com.lcyl.system.domain.dto.RoomDTO;
import com.lcyl.system.mapper.LcRoomMapper;
import com.lcyl.system.mapper.LcRoomTypeMapper;
import com.lcyl.system.service.ILcRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 房间表Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-03-24
 */
@Service
public class LcRoomServiceImpl implements ILcRoomService 
{
    @Autowired
    private LcRoomMapper lcRoomMapper;

    @Autowired
    private LcRoomTypeMapper lcRoomTypeMapper;
    /**
     * 查询房间表
     * 
     * @param id 房间表主键
     * @return 房间表
     */
    @Override
    public Room selectLcRoomById(Long id)
    {
        return lcRoomMapper.selectLcRoomById(id);
    }

    /**
     * 查询房间表列表
     * 
     * @param room 房间表
     * @return 房间表
     */
    @Override
    public List<Room> selectLcRoomList(Room room)
    {
        return lcRoomMapper.selectLcRoomList( room );
    }

    /**
     * 新增房间表
     * 
     * @param room 房间表
     * @return 结果
     */
    @Override
    public int insertLcRoom(RoomDTO room)
    {
        room.setCreateTime(DateUtils.getNowDate());
        return lcRoomMapper.insertLcRoom( room );
    }

    /**
     * 修改房间表
     * 
     * @param room 房间表
     * @return 结果
     */
    @Override
    public int updateLcRoom(RoomDTO room)
    {
        room.setUpdateTime(DateUtils.getNowDate());
        return lcRoomMapper.updateLcRoom( room );
    }

    /**
     * 批量删除房间表
     * 
     * @param ids 需要删除的房间表主键
     * @return 结果
     */
    @Override
    public int deleteLcRoomByIds(Long[] ids)
    {
        return lcRoomMapper.deleteLcRoomByIds(ids);
    }

    /**
     * 删除房间表信息
     * 
     * @param id 房间表主键
     * @return 结果
     */
    @Override
    public int deleteLcRoomById(Long id)
    {
        return lcRoomMapper.deleteLcRoomById(id);
    }

}
