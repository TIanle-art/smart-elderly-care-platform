package com.lcyl.system.service;

import com.lcyl.system.domain.Bed;
import com.lcyl.system.domain.dto.FlatBedDTO;
import com.lcyl.system.domain.dto.FloorRoomBedDTO;

import java.util.List;

public interface BedService {
    List<FloorRoomBedDTO> getFloorRoomBedByFloorId(Long floorId);
    FlatBedDTO getFloorRoomBedByFloorId1(Long elderId);
    /**
     * 查询床位表
     *
     * @param id 床位表主键
     * @return 床位表
     */
    public Bed selectBedById(Long id);

    /**
     * 查询床位表列表
     *
     * @param bed 床位表
     * @return 床位表集合
     */
    public List<Bed> selectBedList(Bed bed);

    /**
     * 新增床位表
     *
     * @param bed 床位表
     * @return 结果
     */
    public int insertBed(Bed bed);

    /**
     * 修改床位表
     *
     * @param bed 床位表
     * @return 结果
     */
    public int updateBed(Bed bed, String name);

    /**
     * 批量删除床位表
     *
     * @param ids 需要删除的床位表主键集合
     * @return 结果
     */
    public int deleteBedByIds(Long[] ids);

    /**
     * 删除床位表信息
     *
     * @param id 床位表主键
     * @return 结果
     */
    public int deleteBedById(Long id);
}