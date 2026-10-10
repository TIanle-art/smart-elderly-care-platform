package com.lcyl.code.service;

import com.lcyl.code.domain.Retreat;
import com.lcyl.code.domain.dto.*;

import java.util.List;

/**
 * 退住Service接口
 * 
 * @author ruoyi
 * @date 2026-03-24
 */
public interface IRetreatService 
{
    /**
     * 发起退住申请（护理员）
     * @param dto 申请参数
     */
    Retreat startApplication(RetreatStartDTO dto);

    /**
     * 完成任务（通用）
     * 根据任务ID和操作类型，执行相应的业务逻辑并调用 Activiti 完成任务
     * @param dto 任务完成参数，包含 taskId、action、opinion 及业务数据
     */
    String completeTask(RetreatTaskCompletedDTO dto);

    /**
     * 获取当前用户的待办任务列表（返回业务数据）
     * @return 退住申请列表（仅包含状态为“审核中”且流程未结束的记录）
     */
    List<Retreat> getTodoList();

    /**
     * 获取退住申请详情，包含关联的解除合同列表和 Activiti 历史活动
     * @param retreatId 申请ID
     * @return 退住申请实体
     */
    Retreat getDetail(Long retreatId);

    /**
     * 分页查询退住列表
     */
    List<Retreat> selectRetreatList(Retreat retreat);

    /**
     * 根据ID查询退住信息
     */
    Retreat selectRetreatById(Long id);

    /**
     * 新增退住申请（后台直接添加，不启动流程）
     */
    int insertRetreat(Retreat retreat);

    /**
     * 修改退住申请
     */
    int updateRetreat(Retreat retreat);

    /**
     * 批量删除退住申请
     */
    int deleteRetreatByIds(Long[] ids);

}
