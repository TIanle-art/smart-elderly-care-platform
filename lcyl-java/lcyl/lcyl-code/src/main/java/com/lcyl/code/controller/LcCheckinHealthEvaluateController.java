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
import com.lcyl.code.domain.LcCheckinHealthEvaluate;
import com.lcyl.code.service.ILcCheckinHealthEvaluateService;
import com.lcyl.common.utils.poi.ExcelUtil;
import com.lcyl.common.core.page.TableDataInfo;

/**
 * 健康评估Controller
 * 
 * @author ruoyi
 * @date 2026-03-23
 */
@RestController
@RequestMapping("/lcyl-code/healthEvaluate")
public class LcCheckinHealthEvaluateController extends BaseController
{
    @Autowired
    private ILcCheckinHealthEvaluateService lcCheckinHealthEvaluateService;

    /**
     * 查询健康评估列表
     */
    @PreAuthorize("@ss.hasPermi('lcyl-code:healthEvaluate:list')")
    @GetMapping("/list")
    public TableDataInfo list(LcCheckinHealthEvaluate lcCheckinHealthEvaluate)
    {
        startPage();
        List<LcCheckinHealthEvaluate> list = lcCheckinHealthEvaluateService.selectLcCheckinHealthEvaluateList(lcCheckinHealthEvaluate);
        return getDataTable(list);
    }

    /**
     * 导出健康评估列表
     */
    @PreAuthorize("@ss.hasPermi('lcyl-code:healthEvaluate:export')")
    @Log(title = "健康评估", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, LcCheckinHealthEvaluate lcCheckinHealthEvaluate)
    {
        List<LcCheckinHealthEvaluate> list = lcCheckinHealthEvaluateService.selectLcCheckinHealthEvaluateList(lcCheckinHealthEvaluate);
        ExcelUtil<LcCheckinHealthEvaluate> util = new ExcelUtil<LcCheckinHealthEvaluate>(LcCheckinHealthEvaluate.class);
        util.exportExcel(response, list, "健康评估数据");
    }

    /**
     * 获取健康评估详细信息
     */
    @PreAuthorize("@ss.hasPermi('lcyl-code:healthEvaluate:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(lcCheckinHealthEvaluateService.selectLcCheckinHealthEvaluateById(id));
    }

    /**
     * 新增健康评估
     */
    @PreAuthorize("@ss.hasPermi('lcyl-code:healthEvaluate:add')")
    @Log(title = "健康评估", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody LcCheckinHealthEvaluate lcCheckinHealthEvaluate)
    {
        return toAjax(lcCheckinHealthEvaluateService.insertLcCheckinHealthEvaluate(lcCheckinHealthEvaluate));
    }

    /**
     * 修改健康评估
     */
    @PreAuthorize("@ss.hasPermi('lcyl-code:healthEvaluate:edit')")
    @Log(title = "健康评估", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody LcCheckinHealthEvaluate lcCheckinHealthEvaluate)
    {
        return toAjax(lcCheckinHealthEvaluateService.updateLcCheckinHealthEvaluate(lcCheckinHealthEvaluate));
    }

    /**
     * 删除健康评估
     */
    @PreAuthorize("@ss.hasPermi('lcyl-code:healthEvaluate:remove')")
    @Log(title = "健康评估", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(lcCheckinHealthEvaluateService.deleteLcCheckinHealthEvaluateByIds(ids));
    }
}
