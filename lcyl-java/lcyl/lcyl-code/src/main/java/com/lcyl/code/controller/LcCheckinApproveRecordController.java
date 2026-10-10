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
import com.lcyl.code.domain.LcCheckinApproveRecord;
import com.lcyl.code.service.ILcCheckinApproveRecordService;
import com.lcyl.common.utils.poi.ExcelUtil;
import com.lcyl.common.core.page.TableDataInfo;

/**
 * 审批记录Controller
 * 
 * @author ruoyi
 * @date 2026-03-23
 */
@RestController
@RequestMapping("/lcyl-code/record")
public class LcCheckinApproveRecordController extends BaseController
{
    @Autowired
    private ILcCheckinApproveRecordService lcCheckinApproveRecordService;

    /**
     * 查询审批记录列表
     */
    @PreAuthorize("@ss.hasPermi('lcyl-code:record:list')")
    @GetMapping("/list")
    public TableDataInfo list(LcCheckinApproveRecord lcCheckinApproveRecord)
    {
        startPage();
        List<LcCheckinApproveRecord> list = lcCheckinApproveRecordService.selectLcCheckinApproveRecordList(lcCheckinApproveRecord);
        return getDataTable(list);
    }

    /**
     * 导出审批记录列表
     */
    @PreAuthorize("@ss.hasPermi('lcyl-code:record:export')")
    @Log(title = "审批记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, LcCheckinApproveRecord lcCheckinApproveRecord)
    {
        List<LcCheckinApproveRecord> list = lcCheckinApproveRecordService.selectLcCheckinApproveRecordList(lcCheckinApproveRecord);
        ExcelUtil<LcCheckinApproveRecord> util = new ExcelUtil<LcCheckinApproveRecord>(LcCheckinApproveRecord.class);
        util.exportExcel(response, list, "审批记录数据");
    }

    /**
     * 获取审批记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('lcyl-code:record:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(lcCheckinApproveRecordService.selectLcCheckinApproveRecordById(id));
    }

    /**
     * 新增审批记录
     */
    @PreAuthorize("@ss.hasPermi('lcyl-code:record:add')")
    @Log(title = "审批记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody LcCheckinApproveRecord lcCheckinApproveRecord)
    {
        return toAjax(lcCheckinApproveRecordService.insertLcCheckinApproveRecord(lcCheckinApproveRecord));
    }

    /**
     * 修改审批记录
     */
    @PreAuthorize("@ss.hasPermi('lcyl-code:record:edit')")
    @Log(title = "审批记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody LcCheckinApproveRecord lcCheckinApproveRecord)
    {
        return toAjax(lcCheckinApproveRecordService.updateLcCheckinApproveRecord(lcCheckinApproveRecord));
    }

    /**
     * 删除审批记录
     */
    @PreAuthorize("@ss.hasPermi('lcyl-code:record:remove')")
    @Log(title = "审批记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(lcCheckinApproveRecordService.deleteLcCheckinApproveRecordByIds(ids));
    }
}
