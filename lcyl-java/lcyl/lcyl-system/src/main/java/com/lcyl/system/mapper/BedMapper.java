package com.lcyl.system.mapper;

import com.lcyl.system.domain.Bed;
import com.lcyl.system.domain.dto.FlatBedDTO;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 床位表Mapper接口
 * 
 * @author tyf
 * @date 2026-03-23
 */
public interface BedMapper 
{
    /**
     * 查询床位表
     *
    * @param floorId 楼层表主键
     * @return 床位表
     */
    List<FlatBedDTO> selectFloorRoomBedByFloorId( @Param("floorId") Long floorId,
                                                  @Param("elderId") Long elderId);
    public Bed selectBedById(Long id);
    Bed selectBedNumber(String bedNumber);
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
    int countBedByRoomId(Long roomId);

    /**
     * 修改床位表
     * 
     * @param bed 床位表
     * @return 结果
     */
    public int updateBed1(Bed bed);

    /**
     * 删除床位表
     * 
     * @param id 床位表主键
     * @return 结果
     */
    public int deleteBedById(Long id);

    /**
     * 批量删除床位表
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBedByIds(Long[] ids);
}
