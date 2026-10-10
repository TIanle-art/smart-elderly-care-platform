package com.lcyl.code.mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @ClassName IndexMapper
 * @Description TODO
 * @Author GuiGui
 * @Date 2026-03-31 15:05
 * @Version 1.0
 */
public interface IndexMapper {
    Integer countLeave();

    Integer countElder();

    Integer countBed();

    Integer countBedUse();

    Integer countNoLive();

    Integer countPerson();

    Integer countMan();

    Integer countServiceFee();
    Integer countMonthFee();

    List<Map<String,Object>> selectCheckInGroupByDay(@Param("start") Date start, @Param("end") Date end);

    List<Map<String,Object>> selectRetreatGroupByDay(@Param("start")Date start,@Param("end") Date end);

    List<Map<String, Object>> selectNursingLevelStat();
    List<Map<String, Object>> selectAgeGenderStat();
    List<Map<String, Object>> selectServiceAbilityStat();

    List<Map<String,Object>> selectIncomeByDateRange(
            @Param("start") Date start,
            @Param("end") Date end
    );

    List<Map<String,Object>> selectRefundByDateRange(
            @Param("start") Date start,
            @Param("end") Date end
    );

    List<Map<String, Object>> countServiceOrderByExecuteTime(
            @Param("startDate") String startDate,
            @Param("endDate") String endDate
    );
}
