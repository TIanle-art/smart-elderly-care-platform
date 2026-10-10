package com.lcyl.code.service.impl;

import java.util.List;

import com.lcyl.code.domain.vo.ElderListVo;
import com.lcyl.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.lcyl.code.mapper.ArrivalMapper;
import com.lcyl.code.domain.Arrival;
import com.lcyl.code.service.IArrivalService;

/**
 * 来访登记Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-03-23
 */
@Service
public class ArrivalServiceImpl implements IArrivalService 
{
    @Autowired
    private ArrivalMapper arrivalMapper;

    /**
     * 查询来访登记
     * 
     * @param id 来访登记主键
     * @return 来访登记
     */
    @Override
    public Arrival selectArrivalById(Long id)
    {
        return arrivalMapper.selectArrivalById(id);
    }

    /**
     * 查询来访登记列表
     * 
     * @param arrival 来访登记
     * @return 来访登记
     */
    @Override
    public List<Arrival> selectArrivalList(Arrival arrival)
    {
        return arrivalMapper.selectArrivalList(arrival);
    }

    /**
     * 新增来访登记
     * 
     * @param arrival 来访登记
     * @return 结果
     */
    @Override
    public int insertArrival(Arrival arrival)
    {
        arrival.setCreateTime(DateUtils.getNowDate());
        return arrivalMapper.insertArrival(arrival);
    }

    /**
     * 修改来访登记
     * 
     * @param arrival 来访登记
     * @return 结果
     */
    @Override
    public int updateArrival(Arrival arrival)
    {
        return arrivalMapper.updateArrival(arrival);
    }

    /**
     * 批量删除来访登记
     * 
     * @param ids 需要删除的来访登记主键
     * @return 结果
     */
    @Override
    public int deleteArrivalByIds(Long[] ids)
    {
        return arrivalMapper.deleteArrivalByIds(ids);
    }

    /**
     * 删除来访登记信息
     * 
     * @param id 来访登记主键
     * @return 结果
     */
    @Override
    public int deleteArrivalById(Long id)
    {
        return arrivalMapper.deleteArrivalById(id);
    }

    @Override
    public List<Arrival> selectArrivalByType(Long id) {
        return arrivalMapper.selectArrivalByType(id);
    }

    @Override
    public List<ElderListVo> selectOlderList() {
        return arrivalMapper.selectOlderList();
    }
}
