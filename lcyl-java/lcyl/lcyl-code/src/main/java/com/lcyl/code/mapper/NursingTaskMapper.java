package com.lcyl.code.mapper;

import com.lcyl.code.domain.dto.NursingTaskQuery;
import com.lcyl.code.domain.vo.ExecuteRecordVO;
import com.lcyl.code.domain.vo.NursingTaskDetailVO;
import com.lcyl.code.domain.vo.NursingTaskVO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @ClassName NursingTaskMapper
 * @Description TODO
 * @Author hfy
 * @Date 2026-03-26 16:10
 * @Version 1.0
 */
public interface NursingTaskMapper {
    List<NursingTaskVO> selectTaskList(@Param("query") NursingTaskQuery query);
    NursingTaskDetailVO selectTaskDetail(@Param("id") Long id);
    int updateExecuteStatus(
            @Param("id") Long id,
            @Param("vo") ExecuteRecordVO vo,
            @Param("executeTime") Date executeTime,
            @Param("executorId") Long executorId,
            @Param("executorName") String executorName
    );

    int updateCancelStatus(
            @Param("id") Long id,
            @Param("cancelReason") String cancelReason,
            @Param("cancelTime") Date cancelTime,
            @Param("executorId") Long executorId,
            @Param("executorName") String executorName
    );
    int updateExpectedServiceTime(@Param("id") Long id, @Param("expectedServiceTime") String expectedServiceTime);
    int updateCompletedStatus(
            @Param("id") Long id,
            @Param("executeTime") Date executeTime,
            @Param("executorId") Long executorId,
            @Param("executorName") String executorName
    );

    int countTaskExecutionByOrderId(@Param("orderId") Long orderId);

    int insertTaskExecutionInit(@Param("orderId") Long orderId,
                                @Param("createBy") String createBy,
                                @Param("createTime") Date createTime);
}
