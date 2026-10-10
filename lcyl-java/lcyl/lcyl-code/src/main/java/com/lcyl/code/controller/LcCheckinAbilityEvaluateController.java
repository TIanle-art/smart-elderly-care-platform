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
import com.lcyl.code.domain.LcCheckinAbilityEvaluate;
import com.lcyl.code.service.ILcCheckinAbilityEvaluateService;
import com.lcyl.common.utils.poi.ExcelUtil;
import com.lcyl.common.core.page.TableDataInfo;

/**
 * 能力评估Controller
 * 
 * @author ruoyi
 * @date 2026-03-23
 */
@RestController
@RequestMapping("/lcyl-code/evaluate")
public class LcCheckinAbilityEvaluateController extends BaseController
{
    @Autowired
    private ILcCheckinAbilityEvaluateService lcCheckinAbilityEvaluateService;

    /**
     * 查询能力评估列表
     */
    @PreAuthorize("@ss.hasPermi('lcyl-code:evaluate:list')")
    @GetMapping("/list")
    public TableDataInfo list(LcCheckinAbilityEvaluate lcCheckinAbilityEvaluate)
    {
        startPage();
        List<LcCheckinAbilityEvaluate> list = lcCheckinAbilityEvaluateService.selectLcCheckinAbilityEvaluateList(lcCheckinAbilityEvaluate);
        return getDataTable(list);
    }

    /**
     * 导出能力评估列表
     */
    @PreAuthorize("@ss.hasPermi('lcyl-code:evaluate:export')")
    @Log(title = "能力评估", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, LcCheckinAbilityEvaluate lcCheckinAbilityEvaluate)
    {
        List<LcCheckinAbilityEvaluate> list = lcCheckinAbilityEvaluateService.selectLcCheckinAbilityEvaluateList(lcCheckinAbilityEvaluate);
        ExcelUtil<LcCheckinAbilityEvaluate> util = new ExcelUtil<LcCheckinAbilityEvaluate>(LcCheckinAbilityEvaluate.class);
        util.exportExcel(response, list, "能力评估数据");
    }

    /**
     * 获取能力评估详细信息
     */
    @PreAuthorize("@ss.hasPermi('lcyl-code:evaluate:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(lcCheckinAbilityEvaluateService.selectLcCheckinAbilityEvaluateById(id));
    }

    /**
     * 新增能力评估
     */
    @PreAuthorize("@ss.hasPermi('lcyl-code:evaluate:add')")
    @Log(title = "能力评估", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody LcCheckinAbilityEvaluate lcCheckinAbilityEvaluate)
    {
        return toAjax(lcCheckinAbilityEvaluateService.insertLcCheckinAbilityEvaluate(lcCheckinAbilityEvaluate));
    }

    /**
     * 修改能力评估
     */
    @PreAuthorize("@ss.hasPermi('lcyl-code:evaluate:edit')")
    @Log(title = "能力评估", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody LcCheckinAbilityEvaluate lcCheckinAbilityEvaluate)
    {
        return toAjax(lcCheckinAbilityEvaluateService.updateLcCheckinAbilityEvaluate(lcCheckinAbilityEvaluate));
    }

    /**
     * 删除能力评估
     */
    @PreAuthorize("@ss.hasPermi('lcyl-code:evaluate:remove')")
    @Log(title = "能力评估", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(lcCheckinAbilityEvaluateService.deleteLcCheckinAbilityEvaluateByIds(ids));
    }
}
