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
import com.lcyl.code.domain.NursingPlanItem;
import com.lcyl.code.service.INursingPlanItemService;
import com.lcyl.common.utils.poi.ExcelUtil;
import com.lcyl.common.core.page.TableDataInfo;

/**
 * 护理计划-项目关联Controller
 * 
 * @author ruoyi
 * @date 2026-03-23
 */
@RestController
@RequestMapping("/planItem/item")
public class NursingPlanItemController extends BaseController
{
    @Autowired
    private INursingPlanItemService nursingPlanItemService;

    /**
     * 查询护理计划-项目关联列表
     */
    @PreAuthorize("@ss.hasPermi('planItem:item:list')")
    @GetMapping("/list")
    public TableDataInfo list(NursingPlanItem nursingPlanItem)
    {
        startPage();
        List<NursingPlanItem> list = nursingPlanItemService.selectNursingPlanItemList(nursingPlanItem);
        return getDataTable(list);
    }

    /**
     * 导出护理计划-项目关联列表
     */
    @PreAuthorize("@ss.hasPermi('planItem:item:export')")
    @Log(title = "护理计划-项目关联", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, NursingPlanItem nursingPlanItem)
    {
        List<NursingPlanItem> list = nursingPlanItemService.selectNursingPlanItemList(nursingPlanItem);
        ExcelUtil<NursingPlanItem> util = new ExcelUtil<NursingPlanItem>(NursingPlanItem.class);
        util.exportExcel(response, list, "护理计划-项目关联数据");
    }

    /**
     * 获取护理计划-项目关联详细信息
     */
    @PreAuthorize("@ss.hasPermi('planItem:item:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(nursingPlanItemService.selectNursingPlanItemById(id));
    }

    /**
     * 新增护理计划-项目关联
     */
    @PreAuthorize("@ss.hasPermi('planItem:item:add')")
    @Log(title = "护理计划-项目关联", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody NursingPlanItem nursingPlanItem)
    {
        return toAjax(nursingPlanItemService.insertNursingPlanItem(nursingPlanItem));
    }

    /**
     * 修改护理计划-项目关联
     */
    @PreAuthorize("@ss.hasPermi('planItem:item:edit')")
    @Log(title = "护理计划-项目关联", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody NursingPlanItem nursingPlanItem)
    {
        return toAjax(nursingPlanItemService.updateNursingPlanItem(nursingPlanItem));
    }

    /**
     * 删除护理计划-项目关联
     */
    @PreAuthorize("@ss.hasPermi('planItem:item:remove')")
    @Log(title = "护理计划-项目关联", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(nursingPlanItemService.deleteNursingPlanItemByIds(ids));
    }
}
