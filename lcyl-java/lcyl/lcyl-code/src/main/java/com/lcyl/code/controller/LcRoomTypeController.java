package com.lcyl.code.controller;

import com.lcyl.common.annotation.Log;
import com.lcyl.common.core.controller.BaseController;
import com.lcyl.common.core.domain.AjaxResult;
import com.lcyl.common.core.page.TableDataInfo;
import com.lcyl.common.enums.BusinessType;
import com.lcyl.common.utils.poi.ExcelUtil;
import com.lcyl.system.domain.LcRoomType;
import com.lcyl.system.service.ILcRoomTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 房型设置Controller
 *
 * @author ruoyi
 * @date 2026-03-23
 */
@RestController
@RequestMapping("/system/type")
public class LcRoomTypeController extends BaseController
{
    @Autowired
    private ILcRoomTypeService lcRoomTypeService;

    /**
     * 查询房型设置列表
     */
   @PreAuthorize("@ss.hasPermi('system:type:list')")
    @GetMapping("/list")
    public TableDataInfo list(LcRoomType lcRoomType)
    {
        startPage();
        List<LcRoomType> list = lcRoomTypeService.selectLcRoomTypeList(lcRoomType);
        return getDataTable(list);
    }

    /**
     * 导出房型设置列表
     */
    @PreAuthorize("@ss.hasPermi('system:type:export')")
    @Log(title = "房型设置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, LcRoomType lcRoomType)
    {
        List<LcRoomType> list = lcRoomTypeService.selectLcRoomTypeList(lcRoomType);
        ExcelUtil<LcRoomType> util = new ExcelUtil<LcRoomType>(LcRoomType.class);
        util.exportExcel(response, list, "房型设置数据");
    }

    /**
     * 获取房型设置详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:type:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(lcRoomTypeService.selectLcRoomTypeById(id));
    }

    /**
     * 新增房型设置
     */
    @PreAuthorize("@ss.hasPermi('system:type:add')")
    @Log(title = "房型设置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody LcRoomType lcRoomType)
    {
        return toAjax(lcRoomTypeService.insertLcRoomType(lcRoomType));
    }

    /**
     * 修改房型设置
     */
    @PreAuthorize("@ss.hasPermi('system:type:edit')")
    @Log(title = "房型设置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody LcRoomType lcRoomType)
    {
        return toAjax(lcRoomTypeService.updateLcRoomType(lcRoomType));
    }

    /**
     * 删除房型设置
     */
    @PreAuthorize("@ss.hasPermi('system:type:remove')")
    @Log(title = "房型设置", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(lcRoomTypeService.deleteLcRoomTypeByIds(ids));
    }
}
