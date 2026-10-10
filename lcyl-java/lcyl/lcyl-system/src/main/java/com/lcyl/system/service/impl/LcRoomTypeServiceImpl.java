package com.lcyl.system.service.impl;

import com.lcyl.common.utils.DateUtils;
import com.lcyl.system.domain.LcRoomType;
import com.lcyl.system.mapper.LcRoomTypeMapper;
import com.lcyl.system.service.ILcRoomTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 房型设置Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-03-23
 */
@Service
public class LcRoomTypeServiceImpl implements ILcRoomTypeService 
{
    @Autowired
    private LcRoomTypeMapper lcRoomTypeMapper;

    /**
     * 查询房型设置
     * 
     * @param id 房型设置主键
     * @return 房型设置
     */
    @Override
    public LcRoomType selectLcRoomTypeById(Long id)
    {
        return lcRoomTypeMapper.selectLcRoomTypeById(id);
    }

    /**
     * 查询房型设置列表
     * 
     * @param lcRoomType 房型设置
     * @return 房型设置
     */
    @Override
    public List<LcRoomType> selectLcRoomTypeList(LcRoomType lcRoomType)
    {
        return lcRoomTypeMapper.selectLcRoomTypeList(lcRoomType);
    }

    /**
     * 新增房型设置
     * 
     * @param lcRoomType 房型设置
     * @return 结果
     */
    @Override
    public int insertLcRoomType(LcRoomType lcRoomType)
    {
        lcRoomType.setCreateTime(DateUtils.getNowDate());
        return lcRoomTypeMapper.insertLcRoomType(lcRoomType);
    }

    /**
     * 修改房型设置
     * 
     * @param lcRoomType 房型设置
     * @return 结果
     */
    @Override
    public int updateLcRoomType(LcRoomType lcRoomType)
    {
        lcRoomType.setUpdateTime(DateUtils.getNowDate());
        return lcRoomTypeMapper.updateLcRoomType(lcRoomType);
    }

    /**
     * 批量删除房型设置
     * 
     * @param ids 需要删除的房型设置主键
     * @return 结果
     */
    @Override
    public int deleteLcRoomTypeByIds(Long[] ids)
    {
        return lcRoomTypeMapper.deleteLcRoomTypeByIds(ids);
    }

    /**
     * 删除房型设置信息
     * 
     * @param id 房型设置主键
     * @return 结果
     */
    @Override
    public int deleteLcRoomTypeById(Long id)
    {
        return lcRoomTypeMapper.deleteLcRoomTypeById(id);
    }
}
