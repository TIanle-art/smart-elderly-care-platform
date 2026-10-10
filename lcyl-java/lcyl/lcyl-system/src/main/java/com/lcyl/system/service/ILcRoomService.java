package com.lcyl.system.service;

import com.lcyl.system.domain.Room;
import com.lcyl.system.domain.dto.RoomDTO;

import java.util.List;

/**
 * 房间表Service接口
 * 
 * @author ruoyi
 * @date 2026-03-24
 */
public interface ILcRoomService 
{
    /**
     * 查询房间表
     * 
     * @param id 房间表主键
     * @return 房间表
     */
    public Room selectLcRoomById(Long id);

    /**
     * 查询房间表列表
     * 
     * @param room 房间表
     * @return 房间表集合
     */
    public List<Room> selectLcRoomList(Room room);

    /**
     * 新增房间表
     * 
     * @param room 房间表
     * @return 结果
     */
    public int insertLcRoom(RoomDTO room);

    /**
     * 修改房间表
     * 
     * @param room 房间表
     * @return 结果
     */
    public int updateLcRoom(RoomDTO room);

    /**
     * 批量删除房间表
     * 
     * @param ids 需要删除的房间表主键集合
     * @return 结果
     */
    public int deleteLcRoomByIds(Long[] ids);

    /**
     * 删除房间表信息
     * 
     * @param id 房间表主键
     * @return 结果
     */
    public int deleteLcRoomById(Long id);

}
