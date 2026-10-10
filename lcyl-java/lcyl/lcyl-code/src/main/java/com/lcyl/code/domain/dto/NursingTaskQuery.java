package com.lcyl.code.domain.dto;

import lombok.Data;

/**
 * @ClassName NursingTaskQuery
 * @Description TODO
 * @Author hfy
 * @Date 2026-03-26 16:09
 * @Version 1.0
 */
@Data
public class NursingTaskQuery {
    private String elderName;
    private String itemType;
    private String executeStatus;
    private String beginTime;
    private String endTime;
    private String nurseName;
    private Integer pageNum = 1;
    private Integer pageSize = 10;
}
