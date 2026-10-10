package com.lcyl.code.domain.dto;

import lombok.Data;

@Data
public class CheckInFlowDTO {
    private Long checkInId;
    private Long flowStatus;
    private String currentTaskKey;
    private String currentTaskName;
    private String taskId;
    private String assignee;//处理人
}
