package com.lcyl.code.domain.dto;

import lombok.Data;

/**
 * @ClassName StatQueryDTO
 * @Description TODO
 * @Author hfy
 * @Date 2026-04-01 9:01
 * @Version 1.0
 */
@Data
public class StatQueryDTO {
    /**
     * 时间类型：today-今日、week-本周、month-本月
     */
    private String timeType;

    /**
     * 自定义开始时间（当 timeType 为自定义时使用）
     */
    private String startDate;

    /**
     * 自定义结束时间
     */
    private String endDate;
}
