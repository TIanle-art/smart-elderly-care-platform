package com.lcyl.code.service;

import java.util.List;
import java.util.Map;

import com.lcyl.code.domain.CheckIn;
import com.lcyl.code.domain.dto.CheckInDetailDTO;
import com.lcyl.code.domain.dto.CheckInFlowDTO;

/**
 * 入住办理Service接口
 *
 * @author ruoyi
 * @date
 */
public interface ICheckInService
{
    /**
     * 查询入住办理
     *
     * @param id 入住办理主键
     * @return 入住办理
     */
    public CheckIn selectCheckInById(Long id);

    /**
     * 查询入住办理列表
     *
     * @param checkIn 入住办理
     * @return 入住办理集合
     */
    public List<CheckIn> selectCheckInList(CheckIn checkIn);

    /**
     * 新增入住办理
     *
     * @param checkIn 入住办理
     * @return 结果
     */
    public int insertCheckIn(CheckIn checkIn);

    /**
     * 修改入住办理
     *
     * @param checkIn 入住办理
     * @return 结果
     */
    public int updateCheckIn(CheckIn checkIn);

    /**
     * 批量删除入住办理
     *
     * @param ids 需要删除的入住办理主键集合
     * @return 结果
     */
    public int deleteCheckInByIds(Long[] ids);

    /**
     * 删除入住办理信息
     *
     * @param id 入住办理主键
     * @return 结果
     */
    public int deleteCheckInById(Long id);

    //发起申请
    public CheckInFlowDTO applyCheckIn(String otherInfo, String reviewInfo);

    //入住评估
    public CheckInFlowDTO evaluateCheckIn(Long checkInId,String evaluation);
    //审批
    public CheckInFlowDTO approveCheckIn(Long checkInId, Map<String,Object> vars);

    //重新申请（处理被驳回的申请）
    public CheckInFlowDTO reapplyCheckIn(Long checkInId, String otherInfo, String reviewInfo);


    //响应待办任务（已认领的任务）
    public List<CheckIn> responseCheckIn();

    //获取候选任务列表（需要认领的任务）
    public List<CheckIn> getCandidateTasks();

    //认领任务
    public void claimTask(String taskId);

    //获取已完成任务列表
    public List<CheckIn> getCompletedTasks();

    Map<String, Object> diagnoseTaskVisibility();

    public CheckInFlowDTO getCheckInInfo(Long checkInId);

    public CheckInDetailDTO getCheckInDetail(Long checkInId);

    //    public String uploadFile(MultipartFile file) throws Exception;
    //查看入住申请详情
    public CheckIn viewCheckInDetail(Long checkInId);

    //完成签约（保存合同并结束流程）
    public CheckInFlowDTO completeContract(Long checkInId, Map<String, Object> signInfo);



}
