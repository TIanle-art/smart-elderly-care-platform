package com.lcyl.code.service;

import com.lcyl.code.domain.ElderLeave;
import com.lcyl.code.domain.ElderLeaveApproveRecord;
import com.lcyl.code.dto.ElderLeaveApproveDto;
import com.lcyl.code.dto.ElderLeaveDto;
import com.lcyl.code.dto.ElderLeaveResubmitDto;
import com.lcyl.code.dto.ElderLeaveSubmitDto;
import com.lcyl.code.vo.ElderLeaveFormVo;
import com.lcyl.code.vo.ElderLeaveTodoVo;
import com.lcyl.code.vo.ElderLeaveVo;
import com.lcyl.code.vo.ElderOptionVo;

import java.util.List;

/**
 * 老人请假申请Service接口
 * 
 * @author cch
 * @date 2026-03-20
 */
public interface IElderLeaveService 
{
    /**
     * 查询老人请假申请
     * 
     * @param id 老人请假申请主键
     * @return 老人请假申请
     */
    public ElderLeave selectElderLeaveById(Long id);

    /**
     * 查询老人请假申请列表
     * 
     * @param elderLeaveDto 老人请假申请
     * @return 老人请假申请集合
     */
    public List<ElderLeaveVo> selectElderLeaveList(ElderLeaveDto elderLeaveDto);

    /**
     * 新增老人请假申请
     * 
     * @param elderLeave 老人请假申请
     * @return 结果
     */
    public int insertElderLeave(ElderLeave elderLeave);

    /**
     * 修改老人请假申请
     * 
     * @param elderLeave 老人请假申请
     * @return 结果
     */
    public int updateElderLeave(ElderLeave elderLeave);

    /**
     * 批量删除老人请假申请
     * 
     * @param ids 需要删除的老人请假申请主键集合
     * @return 结果
     */
    public int deleteElderLeaveByIds(Long[] ids);

    /**
     * 删除老人请假申请信息
     * 
     * @param id 老人请假申请主键
     * @return 结果
     */
    public int deleteElderLeaveById(Long id);

    /*----------------------------------------------------------*/

    /**
     * 提交请假信息
     * @param dto
     * @return
     */
    int submitElderLeave(ElderLeaveSubmitDto dto);

    /**
     * 根据老人id查询基本信息
     * @param elderId
     * @return
     */
    ElderLeaveFormVo getLeaveFormInfoByElderId(Long elderId);

    /**
     * 按流程实例ID查请假单
     * @return
     */
    List<ElderLeaveTodoVo> selectMyTodoList();

    /**
     * 审批
     * @param dto
     * @return
     */
    int approveLeave(ElderLeaveApproveDto dto);

    /**
     * 驳回后再次提交
     * @param dto
     * @return
     */
    int resubmitLeave(ElderLeaveResubmitDto dto);

    /**
     * 查询请假审批记录
     * @param leaveId
     * @return
     */
    List<ElderLeaveApproveRecord> selectApproveRecordList(Long leaveId);

    /**
     * 查询老人的id和姓名（下拉框展示）
     * @return
     */
    List<ElderOptionVo> selectElderOptions();
}
