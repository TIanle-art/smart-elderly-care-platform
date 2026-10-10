package com.lcyl.code.controller;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson2.JSON;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.lcyl.common.annotation.Log;
import com.lcyl.common.core.controller.BaseController;
import com.lcyl.common.core.domain.AjaxResult;
import com.lcyl.common.enums.BusinessType;
import com.lcyl.code.domain.CheckIn;
import com.lcyl.code.service.ICheckInService;
import com.lcyl.common.utils.poi.ExcelUtil;
import com.lcyl.common.core.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 入住办理Controller
 *
 * @author ruoyi
 * @date
 */
@RestController
@RequestMapping("/system/checkin")
public class CheckInController extends BaseController {
    @Autowired
    private ICheckInService checkInService;

    /**
     * 查询入住办理列表
     */
    @PreAuthorize("@ss.hasPermi('system:checkin:list')")
    @GetMapping("/list")
    public TableDataInfo list(CheckIn checkIn) {
        startPage();
        List<CheckIn> list = checkInService.selectCheckInList(checkIn);
        return getDataTable(list);
    }

    /**
     * 导出入住办理列表
     */
    @PreAuthorize("@ss.hasPermi('system:checkin:export')")
    @Log(title = "入住办理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CheckIn checkIn) {
        List<CheckIn> list = checkInService.selectCheckInList(checkIn);
        ExcelUtil<CheckIn> util = new ExcelUtil<CheckIn>(CheckIn.class);
        util.exportExcel(response, list, "入住办理数据");
    }

    /**
     * 获取入住办理详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(checkInService.selectCheckInById(id));
    }

    /**
     * 新增入住办理
     */
    @PreAuthorize("@ss.hasPermi('system:checkin:add')")
    @Log(title = "入住办理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CheckIn checkIn) {
        return toAjax(checkInService.insertCheckIn(checkIn));
    }

    /**
     * 修改入住办理
     */
    @PreAuthorize("@ss.hasPermi('system:checkin:edit')")
    @Log(title = "入住办理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CheckIn checkIn) {
        return toAjax(checkInService.updateCheckIn(checkIn));
    }

    /**
     * 删除入住办理
     */
    @PreAuthorize("@ss.hasPermi('system:checkin:remove')")
    @Log(title = "入住办理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(checkInService.deleteCheckInByIds(ids));
    }

    /**
     * 响应待办任务列表（已认领的任务）
     */
    @GetMapping("/response")
    public AjaxResult responseCheckIn() {
        return success(checkInService.responseCheckIn());
    }

    /**
     * 获取候选任务列表（需要认领的任务）
     */
    @GetMapping("/candidate-tasks")
    public AjaxResult getCandidateTasks() {
        return success(checkInService.getCandidateTasks());
    }

    /**
     * 认领任务
     *
     * @param taskId 任务ID
     */
    @PostMapping("/claim-task/{taskId}")
    public AjaxResult claimTask(@PathVariable String taskId) {
        try {
            checkInService.claimTask(taskId);
            return success("任务认领成功");
        } catch (Exception e) {
            return error("任务认领失败：" + e.getMessage());
        }
    }

    /**
     * 获取已完成任务列表
     */
    @GetMapping("/completed-tasks")
    public AjaxResult getCompletedTasks() {
        return success(checkInService.getCompletedTasks());
    }

    @GetMapping("/task-diagnose")
    public AjaxResult diagnoseTaskVisibility() {
        return success(checkInService.diagnoseTaskVisibility());
    }

    /**
     * 发起入住申请
     */
    @PreAuthorize("@ss.hasPermi('system:checkin:add')")
    @Log(title = "入住办理", businessType = BusinessType.INSERT)
    @PostMapping("/apply")
    public AjaxResult applyCheckIn(@RequestBody Map<String, Object> requestMap) {
        String otherApplyInfo = JSON.toJSONString(requestMap.get("otherApplyInfo"));
        String reviewInfo = JSON.toJSONString(requestMap.get("reviewInfo"));
        return success(checkInService.applyCheckIn(otherApplyInfo, reviewInfo));
    }

    /**
     * 完成评估任务，进入审批阶段
     *
     * @param checkInId 入住办理ID
     */
    @PostMapping("/evaluate/{checkInId}")
    public AjaxResult evaluateCheckIn(@PathVariable Long checkInId, @RequestBody Map<String, Object> requestMap) {
        try {
            String evaluation = JSON.toJSONString(requestMap.get("evaluation"));
            return success(checkInService.evaluateCheckIn(checkInId, evaluation));
        } catch (Exception e) {
            return error("评估任务处理失败：" + e.getMessage());
        }
    }



    /**
     * 完成审批任务，进入入住办理阶段
     *
     * @param checkInId 入住办理ID
     */
    @PostMapping("/approve/{checkInId}")
    public AjaxResult approveCheckIn(@PathVariable Long checkInId, @RequestBody Map<String, Object> vars) {
        try {
            return success(checkInService.approveCheckIn(checkInId, vars));
        } catch (Exception e) {
            return error("审批任务处理失败：" + e.getMessage());
        }
    }

    /**
     * 重新申请入住（处理被驳回的申请）
     *
     * @param checkInId 入住办理ID
     * @param requestMap 申请数据
     */
    @PostMapping("/reapply/{checkInId}")
    public AjaxResult reapplyCheckIn(@PathVariable Long checkInId, @RequestBody Map<String, Object> requestMap) {
        try {
            String otherApplyInfo = JSON.toJSONString(requestMap.get("otherApplyInfo"));
            String reviewInfo = JSON.toJSONString(requestMap.get("reviewInfo"));
            return success(checkInService.reapplyCheckIn(checkInId, otherApplyInfo, reviewInfo));
        } catch (Exception e) {
            return error("重新申请处理失败：" + e.getMessage());
        }
    }

    /**
     * 查看入住申请详情
     */
    @PreAuthorize("@ss.hasPermi('system:checkin:view')")
    @GetMapping("/detail/{checkInId}")
    public AjaxResult viewCheckInDetail(@PathVariable Long checkInId) {
        return success(checkInService.viewCheckInDetail(checkInId));
    }

    //完成签约
    @PostMapping("/complete-contract/{checkInId}")
    public AjaxResult completeContract(@PathVariable Long checkInId, @RequestBody(required = false) Map<String, Object> signInfo) {
        try {
            return success(checkInService.completeContract(checkInId, signInfo));
        } catch (Exception e) {
            return error("签约办理处理失败：" + e.getMessage());
        }
    }
}
