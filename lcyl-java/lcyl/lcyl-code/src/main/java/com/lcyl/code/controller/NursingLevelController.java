package com.lcyl.code.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.lcyl.code.domain.LcNursingLevel;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.lcyl.common.annotation.Log;
import com.lcyl.common.core.controller.BaseController;
import com.lcyl.common.core.domain.AjaxResult;
import com.lcyl.common.enums.BusinessType;
import com.lcyl.code.service.INursingLevelService;
import com.lcyl.common.utils.poi.ExcelUtil;
import com.lcyl.common.core.page.TableDataInfo;

/**
 * 护理等级Controller
 * 
 * @author ruoyi
 * @date 2026-03-24
 */
@RestController
@RequestMapping("/nursingLevel/nursingLevel")
public class NursingLevelController extends BaseController
{
    @Autowired
    private INursingLevelService nursingLevelService;

    /**
     * 查询护理等级列表
     */
    @PreAuthorize("@ss.hasPermi('nursingLevel:nursingLevel:list')")
    @GetMapping("/list")
    public TableDataInfo list(LcNursingLevel lcNursingLevel)
    {
        startPage();
        List<LcNursingLevel> list = nursingLevelService.selectNursingLevelList(lcNursingLevel);
        return getDataTable(list);
    }

    /**
     * 导出护理等级列表
     */
    @PreAuthorize("@ss.hasPermi('nursingLevel:nursingLevel:export')")
    @Log(title = "护理等级", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, LcNursingLevel lcNursingLevel)
    {
        List<LcNursingLevel> list = nursingLevelService.selectNursingLevelList(lcNursingLevel);
        ExcelUtil<LcNursingLevel> util = new ExcelUtil<LcNursingLevel>(LcNursingLevel.class);
        util.exportExcel(response, list, "护理等级数据");
    }

    /**
     * 获取护理等级详细信息
     */
    @PreAuthorize("@ss.hasPermi('nursingLevel:nursingLevel:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(nursingLevelService.selectNursingLevelById(id));
    }

    /**
     * 新增护理等级
     */
    @PreAuthorize("@ss.hasPermi('nursingLevel:nursingLevel:add')")
    @Log(title = "护理等级", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody LcNursingLevel lcNursingLevel)
    {
        return toAjax(nursingLevelService.insertNursingLevel(lcNursingLevel));
    }

    /**
     * 修改护理等级
     */
    @PreAuthorize("@ss.hasPermi('nursingLevel:nursingLevel:edit')")
    @Log(title = "护理等级", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody LcNursingLevel lcNursingLevel)
    {
        return toAjax(nursingLevelService.updateNursingLevel(lcNursingLevel));
    }

    /**
     * 删除护理等级
     */
    @PreAuthorize("@ss.hasPermi('nursingLevel:nursingLevel:remove')")
    @Log(title = "护理等级", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(nursingLevelService.deleteNursingLevelByIds(ids));
    }
    @GetMapping("/checkLevelNameUnique")
    public AjaxResult checkLevelNameUnique(@RequestParam String levelName,@RequestParam(required = false) Long excludeId) {
        boolean unique = nursingLevelService.checkLevelNameUnique(levelName,excludeId);
        return AjaxResult.success(unique);
    }

    @GetMapping("/checkNursingLevelIsReferenced")
    public AjaxResult checkNursingLevelIsReferenced(Long nursingLevelId) {
        if (nursingLevelId == null || nursingLevelId <= 0) {
            return AjaxResult.error("护理项目ID不能为空！");
        }
        boolean isReferenced = nursingLevelService.countByNursingLevelId(nursingLevelId) > 0;
        return AjaxResult.success(isReferenced);
    }
}
