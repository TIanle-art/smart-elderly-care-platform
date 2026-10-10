package com.lcyl.code.service;

import com.lcyl.common.core.domain.AjaxResult;

import java.util.List;
import java.util.Map;

/**
 * @ClassName IndexService
 * @Description TODO
 * @Author GuiGui
 * @Date 2026-03-31 15:04
 * @Version 1.0
 */
public interface IndexService {
    Map<String, Object> count();

    AjaxResult getCheckInOutStat(Map<String, String> params);

    List<Map<String, Object>> getNursingLevelStat();

    Map<String, Object> getAgeGenderStat();

    List<Map<String, Object>> getServiceAbilityStat();

    AjaxResult getIncomeStat(Map<String, String> params);

    AjaxResult getServiceStat(Map<String, String> params);
}
