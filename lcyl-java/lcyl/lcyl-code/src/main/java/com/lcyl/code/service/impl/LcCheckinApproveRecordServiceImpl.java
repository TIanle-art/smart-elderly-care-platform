package com.lcyl.code.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.lcyl.code.mapper.LcCheckinApproveRecordMapper;
import com.lcyl.code.domain.LcCheckinApproveRecord;
import com.lcyl.code.service.ILcCheckinApproveRecordService;

/**
 * 审批记录Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-03-23
 */
@Service
public class LcCheckinApproveRecordServiceImpl implements ILcCheckinApproveRecordService 
{
    @Autowired
    private LcCheckinApproveRecordMapper lcCheckinApproveRecordMapper;

    /**
     * 查询审批记录
     * 
     * @param id 审批记录主键
     * @return 审批记录
     */
    @Override
    public LcCheckinApproveRecord selectLcCheckinApproveRecordById(Long id)
    {
        return lcCheckinApproveRecordMapper.selectLcCheckinApproveRecordById(id);
    }

    /**
     * 查询审批记录列表
     * 
     * @param lcCheckinApproveRecord 审批记录
     * @return 审批记录
     */
    @Override
    public List<LcCheckinApproveRecord> selectLcCheckinApproveRecordList(LcCheckinApproveRecord lcCheckinApproveRecord)
    {
        return lcCheckinApproveRecordMapper.selectLcCheckinApproveRecordList(lcCheckinApproveRecord);
    }

    /**
     * 新增审批记录
     * 
     * @param lcCheckinApproveRecord 审批记录
     * @return 结果
     */
    @Override
    public int insertLcCheckinApproveRecord(LcCheckinApproveRecord lcCheckinApproveRecord)
    {
        return lcCheckinApproveRecordMapper.insertLcCheckinApproveRecord(lcCheckinApproveRecord);
    }

    /**
     * 修改审批记录
     * 
     * @param lcCheckinApproveRecord 审批记录
     * @return 结果
     */
    @Override
    public int updateLcCheckinApproveRecord(LcCheckinApproveRecord lcCheckinApproveRecord)
    {
        return lcCheckinApproveRecordMapper.updateLcCheckinApproveRecord(lcCheckinApproveRecord);
    }

    /**
     * 批量删除审批记录
     * 
     * @param ids 需要删除的审批记录主键
     * @return 结果
     */
    @Override
    public int deleteLcCheckinApproveRecordByIds(Long[] ids)
    {
        return lcCheckinApproveRecordMapper.deleteLcCheckinApproveRecordByIds(ids);
    }

    /**
     * 删除审批记录信息
     * 
     * @param id 审批记录主键
     * @return 结果
     */
    @Override
    public int deleteLcCheckinApproveRecordById(Long id)
    {
        return lcCheckinApproveRecordMapper.deleteLcCheckinApproveRecordById(id);
    }
}
