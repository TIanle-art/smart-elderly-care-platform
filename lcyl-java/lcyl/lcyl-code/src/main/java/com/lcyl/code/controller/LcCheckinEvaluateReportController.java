package com.lcyl.code.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.lcyl.common.annotation.Log;
import com.lcyl.common.core.controller.BaseController;
import com.lcyl.common.core.domain.AjaxResult;
import com.lcyl.common.enums.BusinessType;
import com.lcyl.code.domain.LcCheckinEvaluateReport;
import com.lcyl.code.service.ILcCheckinEvaluateReportService;
import com.lcyl.common.utils.poi.ExcelUtil;
import com.lcyl.common.core.page.TableDataInfo;

/**
 * 评估报告Controller
 * 
 * @author ruoyi
 * @date 2026-03-23
 */
@RestController
@RequestMapping("/lcyl-code/report")
public class LcCheckinEvaluateReportController extends BaseController
{
    @Autowired
    private ILcCheckinEvaluateReportService lcCheckinEvaluateReportService;

    /**
     * 查询评估报告列表
     */
    @PreAuthorize("@ss.hasPermi('lcyl-code:report:list')")
    @GetMapping("/list")
    public TableDataInfo list(LcCheckinEvaluateReport lcCheckinEvaluateReport)
    {
        startPage();
        List<LcCheckinEvaluateReport> list = lcCheckinEvaluateReportService.selectLcCheckinEvaluateReportList(lcCheckinEvaluateReport);
        return getDataTable(list);
    }

    /**
     * 导出评估报告列表
     */
    @PreAuthorize("@ss.hasPermi('lcyl-code:report:export')")
    @Log(title = "评估报告", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, LcCheckinEvaluateReport lcCheckinEvaluateReport)
    {
        List<LcCheckinEvaluateReport> list = lcCheckinEvaluateReportService.selectLcCheckinEvaluateReportList(lcCheckinEvaluateReport);
        ExcelUtil<LcCheckinEvaluateReport> util = new ExcelUtil<LcCheckinEvaluateReport>(LcCheckinEvaluateReport.class);
        util.exportExcel(response, list, "评估报告数据");
    }

    /**
     * 获取评估报告详细信息
     */
    @PreAuthorize("@ss.hasPermi('lcyl-code:report:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(lcCheckinEvaluateReportService.selectLcCheckinEvaluateReportById(id));
    }

    /**
     * 新增评估报告
     */
    @PreAuthorize("@ss.hasPermi('lcyl-code:report:add')")
    @Log(title = "评估报告", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody LcCheckinEvaluateReport lcCheckinEvaluateReport)
    {
        return toAjax(lcCheckinEvaluateReportService.insertLcCheckinEvaluateReport(lcCheckinEvaluateReport));
    }

    /**
     * 修改评估报告
     */
    @PreAuthorize("@ss.hasPermi('lcyl-code:report:edit')")
    @Log(title = "评估报告", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody LcCheckinEvaluateReport lcCheckinEvaluateReport)
    {
        return toAjax(lcCheckinEvaluateReportService.updateLcCheckinEvaluateReport(lcCheckinEvaluateReport));
    }

    /**
     * 删除评估报告
     */
    @PreAuthorize("@ss.hasPermi('lcyl-code:report:remove')")
    @Log(title = "评估报告", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(lcCheckinEvaluateReportService.deleteLcCheckinEvaluateReportByIds(ids));
    }
}
