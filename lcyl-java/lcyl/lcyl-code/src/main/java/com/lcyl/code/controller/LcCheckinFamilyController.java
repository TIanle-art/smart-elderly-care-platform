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
import com.lcyl.code.domain.LcCheckinFamily;
import com.lcyl.code.service.ILcCheckinFamilyService;
import com.lcyl.common.utils.poi.ExcelUtil;
import com.lcyl.common.core.page.TableDataInfo;

/**
 * 家属信息Controller
 * 
 * @author ruoyi
 * @date 2026-03-23
 */
@RestController
@RequestMapping("/lcyl-code/family")
public class LcCheckinFamilyController extends BaseController
{
    @Autowired
    private ILcCheckinFamilyService lcCheckinFamilyService;

    /**
     * 查询家属信息列表
     */
    @PreAuthorize("@ss.hasPermi('lcyl-code:family:list')")
    @GetMapping("/list")
    public TableDataInfo list(LcCheckinFamily lcCheckinFamily)
    {
        startPage();
        List<LcCheckinFamily> list = lcCheckinFamilyService.selectLcCheckinFamilyList(lcCheckinFamily);
        return getDataTable(list);
    }

    /**
     * 导出家属信息列表
     */
    @PreAuthorize("@ss.hasPermi('lcyl-code:family:export')")
    @Log(title = "家属信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, LcCheckinFamily lcCheckinFamily)
    {
        List<LcCheckinFamily> list = lcCheckinFamilyService.selectLcCheckinFamilyList(lcCheckinFamily);
        ExcelUtil<LcCheckinFamily> util = new ExcelUtil<LcCheckinFamily>(LcCheckinFamily.class);
        util.exportExcel(response, list, "家属信息数据");
    }

    /**
     * 获取家属信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('lcyl-code:family:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(lcCheckinFamilyService.selectLcCheckinFamilyById(id));
    }

    /**
     * 新增家属信息
     */
    @PreAuthorize("@ss.hasPermi('lcyl-code:family:add')")
    @Log(title = "家属信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody LcCheckinFamily lcCheckinFamily)
    {
        return toAjax(lcCheckinFamilyService.insertLcCheckinFamily(lcCheckinFamily));
    }

    /**
     * 修改家属信息
     */
    @PreAuthorize("@ss.hasPermi('lcyl-code:family:edit')")
    @Log(title = "家属信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody LcCheckinFamily lcCheckinFamily)
    {
        return toAjax(lcCheckinFamilyService.updateLcCheckinFamily(lcCheckinFamily));
    }

    /**
     * 删除家属信息
     */
    @PreAuthorize("@ss.hasPermi('lcyl-code:family:remove')")
    @Log(title = "家属信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(lcCheckinFamilyService.deleteLcCheckinFamilyByIds(ids));
    }
}
