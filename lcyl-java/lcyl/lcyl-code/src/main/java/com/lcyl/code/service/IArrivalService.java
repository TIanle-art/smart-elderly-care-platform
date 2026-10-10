package com.lcyl.code.service;

import java.util.List;
import com.lcyl.code.domain.Arrival;
import com.lcyl.code.domain.vo.ElderListVo;

/**
 * 来访登记Service接口
 * 
 * @author ruoyi
 * @date 2026-03-23
 */
public interface IArrivalService 
{
    /**
     * 查询来访登记
     * 
     * @param id 来访登记主键
     * @return 来访登记
     */
    public Arrival selectArrivalById(Long id);

    /**
     * 查询来访登记列表
     * 
     * @param arrival 来访登记
     * @return 来访登记集合
     */
    public List<Arrival> selectArrivalList(Arrival arrival);

    /**
     * 新增来访登记
     * 
     * @param arrival 来访登记
     * @return 结果
     */
    public int insertArrival(Arrival arrival);

    /**
     * 修改来访登记
     * 
     * @param arrival 来访登记
     * @return 结果
     */
    public int updateArrival(Arrival arrival);

    /**
     * 批量删除来访登记
     * 
     * @param ids 需要删除的来访登记主键集合
     * @return 结果
     */
    public int deleteArrivalByIds(Long[] ids);

    /**
     * 删除来访登记信息
     * 
     * @param id 来访登记主键
     * @return 结果
     */
    public int deleteArrivalById(Long id);

    List<Arrival> selectArrivalByType(Long id);

    List<ElderListVo> selectOlderList();
}
