package com.lcyl.code.controller;

import com.lcyl.code.domain.NursingItem;
import com.lcyl.code.service.INursingItemService;
import com.lcyl.common.annotation.Log;
import com.lcyl.common.core.controller.BaseController;
import com.lcyl.common.core.domain.AjaxResult;
import com.lcyl.common.core.page.TableDataInfo;
import com.lcyl.common.enums.BusinessType;
import com.lcyl.common.utils.poi.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 护理项目Controller
 * 
 * @author ruoyi
 * @date 2026-03-23
 */
@RestController
@RequestMapping("/nursingItem/item")
public class NursingItemController extends BaseController
{
    @Autowired
    private INursingItemService nursingItemService;

    /**
     * 查询护理项目列表
     */
   @PreAuthorize("@ss.hasPermi('nursingItem:item:list')")
    @GetMapping("/list")
    public TableDataInfo list(NursingItem nursingItem)
    {
        startPage();
        List<NursingItem> list = nursingItemService.selectNursingItemList(nursingItem);
        return getDataTable(list);
    }

    /**
     * 导出护理项目列表
     */
    @PreAuthorize("@ss.hasPermi('nursingItem:item:export')")
    @Log(title = "护理项目", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, NursingItem nursingItem)
    {
        List<NursingItem> list = nursingItemService.selectNursingItemList(nursingItem);
        ExcelUtil<NursingItem> util = new ExcelUtil<NursingItem>(NursingItem.class);
        util.exportExcel(response, list, "护理项目数据");
    }

    /**
     * 获取护理项目详细信息
     */
   @PreAuthorize("@ss.hasPermi('nursingItem:item:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(nursingItemService.selectNursingItemById(id));
    }

    /**
     * 新增护理项目
     */
    @PreAuthorize("@ss.hasPermi('nursingItem:item:add')")
    @Log(title = "护理项目", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody NursingItem nursingItem)
    {
        return toAjax(nursingItemService.insertNursingItem(nursingItem));
    }

    /**
     * 修改护理项目
     */
    @PreAuthorize("@ss.hasPermi('nursingItem:item:edit')")
    @Log(title = "护理项目", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody NursingItem nursingItem)
    {
        return toAjax(nursingItemService.updateNursingItem(nursingItem));
    }

    /**
     * 删除护理项目
     */
    @PreAuthorize("@ss.hasPermi('nursingItem:item:remove')")
    @Log(title = "护理项目", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(nursingItemService.deleteNursingItemByIds(ids));
    }
    @GetMapping("/checkItemNameUnique")
    public AjaxResult checkItemNameUnique(@RequestParam String itemName) {
        boolean unique = nursingItemService.checkItemNameUnique(itemName);
        return AjaxResult.success(unique);
    }
}
