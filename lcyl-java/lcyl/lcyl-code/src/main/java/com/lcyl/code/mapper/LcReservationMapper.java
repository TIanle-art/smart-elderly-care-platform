package com.lcyl.code.mapper;

import com.lcyl.code.domain.LcReservation;
import com.lcyl.code.domain.vo.MyVisitVo;
import com.lcyl.code.dto.ArrayDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 预约来访Mapper接口
 * 
 * @author ruoyi
 * @date 2026-03-20
 */
public interface LcReservationMapper 
{
    /**
     * 查询预约来访
     * 
     * @param id 预约来访主键
     * @return 预约来访
     */
    public LcReservation selectLcReservationById(Long id);

    /**
     * 查询预约来访列表
     * 
     * @param lcReservation 预约来访
     * @return 预约来访集合
     */
    public List<LcReservation> selectLcReservationList(LcReservation lcReservation);

    /**
     * 新增预约来访
     * 
     * @param lcReservation 预约来访
     * @return 结果
     */
    public int insertLcReservation(LcReservation lcReservation);

    /**
     * 修改预约来访
     * 
     * @param lcReservation 预约来访
     * @return 结果
     */
    public int updateLcReservation(LcReservation lcReservation);

    /**
     * 删除预约来访
     * 
     * @param id 预约来访主键
     * @return 结果
     */
    public int deleteLcReservationById(Long id);

    /**
     * 批量删除预约来访
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteLcReservationByIds(Long[] ids);

    List<LcReservation> selectLcReservationType(Long id);

    void updateStatus();

    Integer updateVisitTime(ArrayDto arrayDto);

    void updateStatusByTime(Long id);

    List<MyVisitVo> selectLcReservationstatus(@Param("status") Integer status, @Param("memberId") Long memberId);

    Integer updateLcReservationById(Long id);
}
