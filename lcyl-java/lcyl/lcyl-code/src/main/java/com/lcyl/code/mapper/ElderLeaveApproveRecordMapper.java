package com.lcyl.code.mapper;

import com.lcyl.code.domain.ElderLeaveApproveRecord;

import java.util.List;

/**
 * 老人请假审批记录Mapper接口
 * 
 * @author cch
 * @date 2026-03-24
 */
public interface ElderLeaveApproveRecordMapper 
{
    /**
     * 查询老人请假审批记录
     * 
     * @param id 老人请假审批记录主键
     * @return 老人请假审批记录
     */
    public ElderLeaveApproveRecord selectElderLeaveApproveRecordById(Long id);

    /**
     * 查询老人请假审批记录列表
     * 
     * @param elderLeaveApproveRecord 老人请假审批记录
     * @return 老人请假审批记录集合
     */
    public List<ElderLeaveApproveRecord> selectElderLeaveApproveRecordList(ElderLeaveApproveRecord elderLeaveApproveRecord);

    /**
     * 新增老人请假审批记录
     * 
     * @param elderLeaveApproveRecord 老人请假审批记录
     * @return 结果
     */
    public int insertElderLeaveApproveRecord(ElderLeaveApproveRecord elderLeaveApproveRecord);

    /**
     * 修改老人请假审批记录
     * 
     * @param elderLeaveApproveRecord 老人请假审批记录
     * @return 结果
     */
    public int updateElderLeaveApproveRecord(ElderLeaveApproveRecord elderLeaveApproveRecord);

    /**
     * 删除老人请假审批记录
     * 
     * @param id 老人请假审批记录主键
     * @return 结果
     */
    public int deleteElderLeaveApproveRecordById(Long id);

    /**
     * 批量删除老人请假审批记录
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteElderLeaveApproveRecordByIds(Long[] ids);
}
