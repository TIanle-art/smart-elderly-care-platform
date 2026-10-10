package com.lcyl.code.service;

import com.lcyl.code.domain.dto.NursingTaskQuery;
import com.lcyl.code.domain.vo.ExecuteRecordVO;
import com.lcyl.code.domain.vo.NursingTaskDetailVO;
import com.lcyl.code.domain.vo.NursingTaskVO;
import com.lcyl.code.domain.vo.RescheduleVO;

import java.util.Date;
import java.util.List;

/**
 * @ClassName NursingTaskService
 * @Description TODO
 * @Author hfy
 * @Date 2026-03-26 16:14
 * @Version 1.0
 */
public interface NursingTaskService {
    List<NursingTaskVO> selectTaskList(NursingTaskQuery query);
    NursingTaskDetailVO selectTaskDetail(Long id);
    int cancelTask(Long id, String cancelReason);
    int executeTask(Long id, ExecuteRecordVO vo);
    int rescheduleTask(Long id, RescheduleVO vo);
    int completedTask(Long id);
}
