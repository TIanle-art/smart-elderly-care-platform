package com.lcyl.code.mapper;

import java.util.List;
import com.lcyl.code.domain.LcCheckinApproveRecord;

/**
 * 审批记录Mapper接口
 * 
 * @author ruoyi
 * @date 2026-03-23
 */
public interface LcCheckinApproveRecordMapper 
{
    /**
     * 查询审批记录
     * 
     * @param id 审批记录主键
     * @return 审批记录
     */
    public LcCheckinApproveRecord selectLcCheckinApproveRecordById(Long id);

    /**
     * 查询审批记录列表
     * 
     * @param lcCheckinApproveRecord 审批记录
     * @return 审批记录集合
     */
    public List<LcCheckinApproveRecord> selectLcCheckinApproveRecordList(LcCheckinApproveRecord lcCheckinApproveRecord);

    /**
     * 新增审批记录
     * 
     * @param lcCheckinApproveRecord 审批记录
     * @return 结果
     */
    public int insertLcCheckinApproveRecord(LcCheckinApproveRecord lcCheckinApproveRecord);

    /**
     * 修改审批记录
     * 
     * @param lcCheckinApproveRecord 审批记录
     * @return 结果
     */
    public int updateLcCheckinApproveRecord(LcCheckinApproveRecord lcCheckinApproveRecord);

    /**
     * 删除审批记录
     * 
     * @param id 审批记录主键
     * @return 结果
     */
    public int deleteLcCheckinApproveRecordById(Long id);

    /**
     * 批量删除审批记录
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteLcCheckinApproveRecordByIds(Long[] ids);
}
