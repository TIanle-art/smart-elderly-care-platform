package com.lcyl.system.mapper;

import com.lcyl.system.domain.Device;

import java.util.List;

/**
 * 设备管理Mapper接口
 * 
 * @author ruoyi
 * @date 2026-03-27
 */
public interface DeviceMapper 
{
    /**
     * 查询设备管理
     * 
     * @param id 设备管理主键
     * @return 设备管理
     */
    public Device selectDeviceById(Long id);

    /**
     * 查询设备管理列表
     * 
     * @param device 设备管理
     * @return 设备管理集合
     */
    public List<Device> selectDeviceList(Device device);

    /**
     * 新增设备管理
     * 
     * @param device 设备管理
     * @return 结果
     */
    public int insertDevice(Device device);

    /**
     * 修改设备管理
     * 
     * @param device 设备管理
     * @return 结果
     */
    public int updateDevice(Device device);

    /**
     * 删除设备管理
     * 
     * @param id 设备管理主键
     * @return 结果
     */
    public int deleteDeviceById(Long id);

    /**
     * 批量删除设备管理
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteDeviceByIds(Long[] ids);
}
