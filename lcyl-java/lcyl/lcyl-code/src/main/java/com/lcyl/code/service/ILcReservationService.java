package com.lcyl.code.service;

import java.util.List;
import com.lcyl.code.domain.LcReservation;
import com.lcyl.code.domain.vo.MyVisitVo;
import com.lcyl.code.dto.ArrayDto;

/**
 * 预约来访Service接口
 * 
 * @author ruoyi
 * @date 2026-03-20
 */
public interface ILcReservationService 
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
     * 批量删除预约来访
     * 
     * @param ids 需要删除的预约来访主键集合
     * @return 结果
     */
    public int deleteLcReservationByIds(Long[] ids);

    /**
     * 删除预约来访信息
     * 
     * @param id 预约来访主键
     * @return 结果
     */
    public int deleteLcReservationById(Long id);

    List<LcReservation> selectLcReservationByType(Long id);

    Integer updateArrayTime(ArrayDto arrayDto);

    List<MyVisitVo> selectvisitInfo(Integer status);

    Integer deleteReservationById(Long id);
}
