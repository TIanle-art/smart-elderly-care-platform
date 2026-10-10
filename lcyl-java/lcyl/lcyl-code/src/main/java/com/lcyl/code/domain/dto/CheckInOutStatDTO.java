package com.lcyl.code.domain.dto;

import lombok.Data;

import java.util.List;

/**
 * @ClassName CheckInOutStatDTO
 * @Description TODO
 * @Author hfy
 * @Date 2026-04-01 9:03
 * @Version 1.0
 */
@Data
public class CheckInOutStatDTO {
    /**
     * X轴数据：日期（如10-09、10-10）
     */
    private List<String> xData;

    /**
     * 入住人数系列（蓝色）
     */
    private List<Integer> checkInData;

    /**
     * 退住人数系列（绿色）
     */
    private List<Integer> checkOutData;
}
