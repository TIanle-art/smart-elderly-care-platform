package com.lcyl.quartz.task;

import com.lcyl.code.mapper.ElderLeaveMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component("leave")
public class LeaveTimeoutTask {

    @Autowired
    private ElderLeaveMapper elderLeaveMapper;

    /**
     * 每天凌晨  执行
     * 把已批准、未销假、已过期的请假 → 自动标记为 超时未归
     */
    public void autoUpdateTimeoutLeave() {
        elderLeaveMapper.updateLeaveToTimeout();
    }
}