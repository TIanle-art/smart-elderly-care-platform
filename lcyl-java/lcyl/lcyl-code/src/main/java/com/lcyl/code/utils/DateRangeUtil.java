package com.lcyl.code.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateRangeUtil {

    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 获取今日/本周/本月时间范围
     */
    public static Date[] getDateRange(String timeType) {
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        Date endOfDay = cal.getTime();

        if ("today".equals(timeType)) {
            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            return new Date[]{endOfDay, cal.getTime()};
        }

        if ("week".equals(timeType)) {
            cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            Date start = cal.getTime();
            cal.setTime(now);
            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            return new Date[]{start, cal.getTime()};
        }

        if ("month".equals(timeType)) {
            cal.set(Calendar.DAY_OF_MONTH, 1);
            Date start = cal.getTime();
            cal.setTime(now);
            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            return new Date[]{start, cal.getTime()};
        }

        // 默认本周
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return new Date[]{cal.getTime(), endOfDay};
    }

    /**
     * 字符串转日期范围，并把结束时间设为 23:59:59
     */
    public static Date[] parseDateRange(String startDate, String endDate) throws ParseException {
        Date start = SDF.parse(startDate);
        Date end = SDF.parse(endDate);

        Calendar cal = Calendar.getInstance();
        cal.setTime(end);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        end = cal.getTime();

        return new Date[]{start, end};
    }
}