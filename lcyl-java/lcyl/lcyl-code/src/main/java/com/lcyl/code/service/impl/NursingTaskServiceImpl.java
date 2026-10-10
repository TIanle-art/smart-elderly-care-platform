package com.lcyl.code.service.impl;

import com.lcyl.code.domain.Nurse;
import com.lcyl.code.domain.ServiceOrder;
import com.lcyl.code.domain.dto.NursingTaskQuery;
import com.lcyl.code.domain.vo.ExecuteRecordVO;
import com.lcyl.code.domain.vo.NursingTaskDetailVO;
import com.lcyl.code.domain.vo.NursingTaskVO;
import com.lcyl.code.domain.vo.RescheduleVO;
import com.lcyl.code.mapper.NursingTaskMapper;
import com.lcyl.code.mapper.ServiceOrderMapper;
import com.lcyl.code.service.NursingTaskService;
import com.lcyl.code.utils.NurseUtils;
import com.lcyl.common.exception.ServiceException;
import com.lcyl.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class NursingTaskServiceImpl implements NursingTaskService {

    @Autowired
    private NursingTaskMapper nursingTaskMapper;

    @Autowired
    private ServiceOrderMapper serviceOrderMapper;

    @Override
    public List<NursingTaskVO> selectTaskList(NursingTaskQuery query) {
        return nursingTaskMapper.selectTaskList(query);
    }

    @Override
    public NursingTaskDetailVO selectTaskDetail(Long id) {
        return nursingTaskMapper.selectTaskDetail(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int executeTask(Long taskId, ExecuteRecordVO vo) {
        Nurse currentNurse = NurseUtils.getCurrentNurse();
        Long executorId = currentNurse.getId();
        String executorName = currentNurse.getName();
        Date executeTime = DateUtils.getNowDate();

        int rows = nursingTaskMapper.updateExecuteStatus(
            taskId,
            vo,
            executeTime,
            executorId,
            executorName
        );

        updateOrderForExecute(taskId, executeTime);
        return rows;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int cancelTask(Long taskId, String cancelReason) {
        Nurse currentNurse = NurseUtils.getCurrentNurse();
        Long executorId = currentNurse.getId();
        String executorName = currentNurse.getName();
        Date cancelTime = DateUtils.getNowDate();

        int rows = nursingTaskMapper.updateCancelStatus(
            taskId,
            cancelReason,
            cancelTime,
            executorId,
            executorName
        );

        updateOrderForCancel(taskId, cancelReason, cancelTime);
        return rows;
    }

    @Override
    public int rescheduleTask(Long id, RescheduleVO vo) {
        return nursingTaskMapper.updateExpectedServiceTime(id, vo.getExpectedServiceTime());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int completedTask(Long taskId) {
        Nurse currentNurse = NurseUtils.getCurrentNurse();
        Long executorId = currentNurse.getId();
        String executorName = currentNurse.getName();
        Date finishTime = DateUtils.getNowDate();

        int rows = nursingTaskMapper.updateCompletedStatus(
            taskId,
            finishTime,
            executorId,
            executorName
        );

        updateOrderForComplete(taskId, finishTime);
        return rows;
    }

    private void updateOrderForExecute(Long orderId, Date executeTime) {
        ServiceOrder order = getOrderOrThrow(orderId);
        order.setOrderStatus("2");
        order.setExecuteTime(executeTime);
        order.setUpdateTime(DateUtils.getNowDate());
        serviceOrderMapper.updateServiceOrder(order);
    }

    private void updateOrderForCancel(Long orderId, String cancelReason, Date cancelTime) {
        ServiceOrder order = getOrderOrThrow(orderId);
        order.setOrderStatus("5");
        order.setCancelReason(cancelReason);
        order.setCancelTime(cancelTime);
        order.setUpdateTime(DateUtils.getNowDate());
        serviceOrderMapper.updateServiceOrder(order);
    }

    private void updateOrderForComplete(Long orderId, Date finishTime) {
        ServiceOrder order = getOrderOrThrow(orderId);
        order.setOrderStatus("3");
        if (order.getExecuteTime() == null) {
            order.setExecuteTime(finishTime);
        }
        order.setFinishTime(finishTime);
        order.setUpdateTime(DateUtils.getNowDate());
        serviceOrderMapper.updateServiceOrder(order);
    }

    private ServiceOrder getOrderOrThrow(Long orderId) {
        ServiceOrder order = serviceOrderMapper.selectServiceOrderById(orderId);
        if (order == null) {
            throw new ServiceException("关联订单不存在");
        }
        return order;
    }
}
