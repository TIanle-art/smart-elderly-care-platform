package com.lcyl.code.controller;

import com.lcyl.code.domain.dto.NursingTaskQuery;
import com.lcyl.code.domain.vo.ExecuteRecordVO;
import com.lcyl.code.domain.vo.NursingTaskDetailVO;
import com.lcyl.code.domain.vo.NursingTaskVO;
import com.lcyl.code.domain.vo.RescheduleVO;
import com.lcyl.code.service.NursingTaskService;
import com.lcyl.common.core.controller.BaseController;
import com.lcyl.common.core.domain.AjaxResult;
import com.lcyl.common.core.page.TableDataInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @ClassName NursingTaskController
 * @Description TODO
 * @Author hfy
 * @Date 2026-03-26 16:16
 * @Version 1.0
 */
@RestController
@RequestMapping("/nursing/task")
public class NursingTaskController extends BaseController {

    @Resource
    private NursingTaskService nursingTaskService;

    /**
     * 护理任务列表（全部从订单表查询）
     */
    @GetMapping("/list")
    public TableDataInfo list(NursingTaskQuery query) {
        startPage();
        List<NursingTaskVO> list = nursingTaskService.selectTaskList(query);
        return getDataTable(list);
    }

    /**
     * 任务详情（查看按钮）
     */
    @GetMapping("/detail/{id}")
    public AjaxResult detail(@PathVariable("id") Long id) {
        NursingTaskDetailVO detail = nursingTaskService.selectTaskDetail(id);
        return AjaxResult.success(detail);
    }
    /**
     * 取消护理任务
     */
    @PutMapping("/cancel/{id}")
    public AjaxResult cancel(@PathVariable Long id, @RequestBody Map<String, String> params) {
        String cancelReason = params.get("cancelReason");
        int rows = nursingTaskService.cancelTask(id, cancelReason);
        return toAjax(rows);
    }

    // 执行任务
    @PutMapping("/execute/{id}")
    public AjaxResult execute(@PathVariable Long id, @RequestBody ExecuteRecordVO vo) {
        return toAjax(nursingTaskService.executeTask(id, vo));
    }

    // 改期任务
    @PutMapping("/reschedule/{id}")
    public AjaxResult reschedule(@PathVariable Long id, @RequestBody RescheduleVO vo) {
        return toAjax(nursingTaskService.rescheduleTask(id, vo));
    }
    // 完成任务
    @PutMapping("/completed/{id}")
    public AjaxResult completed(@PathVariable Long id) {
        return toAjax(nursingTaskService.completedTask(id));
    }
}
