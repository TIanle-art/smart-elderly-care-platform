package com.lcyl.code.controller;

import com.lcyl.code.service.IndexService;
import com.lcyl.common.core.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @ClassName IndexController
 * @Description TODO
 * @Author GuiGui
 * @Date 2026-03-31 15:01
 * @Version 1.0
 */
@RestController
@RequestMapping("/code/index")
public class IndexController {

    @Autowired
    private IndexService indexService;

    @GetMapping("/count")
    public AjaxResult count(){
        Map<String, Object> countMap =indexService.count();
        return AjaxResult.success(countMap);
    }

    @PostMapping("/checkInOutStat")
    public AjaxResult checkInOutStat(@RequestBody Map<String, String> params) {
        return indexService.getCheckInOutStat(params);
    }

    @GetMapping("/nursingLevelStat")
    public AjaxResult nursingLevelStat() {
        return AjaxResult.success(indexService.getNursingLevelStat());
    }

    @GetMapping("/ageGenderStat")
    public AjaxResult ageGenderStat() {
        return AjaxResult.success(indexService.getAgeGenderStat());
    }

    @GetMapping("/serviceAbilityStat")
    public AjaxResult serviceAbilityStat() {
        return AjaxResult.success(indexService.getServiceAbilityStat());
    }

    @GetMapping("/incomeStat")
    public AjaxResult incomeStat(@RequestParam Map<String, String> params) {
        return indexService.getIncomeStat(params);
    }

    @GetMapping("/getServiceStat")
    public AjaxResult getServiceStat(@RequestParam Map<String, String> params) {
        return indexService.getServiceStat(params);
    }

}
