package com.lcyl.code.controller;

import com.lcyl.common.annotation.Log;
import com.lcyl.common.core.controller.BaseController;
import com.lcyl.common.core.domain.AjaxResult;
import com.lcyl.common.core.page.TableDataInfo;
import com.lcyl.common.enums.BusinessType;
import com.lcyl.common.utils.poi.ExcelUtil;
import com.lcyl.system.domain.Floor;
import com.lcyl.system.service.IFloorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 房位房型Controller
 * 
 * @author ruoyi
 * @date 2026-03-21
 */
@RestController
@RequestMapping("/system/floor")
public class FloorController extends BaseController
{
    @Autowired
    private IFloorService floorService;

    /**
     * 查询房位房型列表
     */
    @PreAuthorize("@ss.hasPermi('system:floor:list')")
    @GetMapping("/list")
    public TableDataInfo list(Floor floor)
    {
        startPage();
        List<Floor> list = floorService.selectFloorList(floor);
        return getDataTable(list);
    }

    /**
     * 导出房位房型列表
     */
    @PreAuthorize("@ss.hasPermi('system:floor:export')")
    @Log(title = "房位房型", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Floor floor)
    {
        List<Floor> list = floorService.selectFloorList(floor);
        ExcelUtil<Floor> util = new ExcelUtil<Floor>(Floor.class);
        util.exportExcel(response, list, "房位房型数据");
    }

    /**
     * 获取房位房型详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:floor:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(floorService.selectFloorById(id));
    }

    /**
     * 新增房位房型
     */
    @PreAuthorize("@ss.hasPermi('system:floor:add')")
    @Log(title = "房位房型", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Floor floor)
    {
        return toAjax(floorService.insertFloor(floor));
    }

    /**
     * 修改房位房型
     */
    @PreAuthorize("@ss.hasPermi('system:floor:edit')")
    @Log(title = "房位房型", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Floor floor)
    {
        return toAjax(floorService.updateFloor(floor));
    }

    /**
     * 删除房位房型
     */
    @PreAuthorize("@ss.hasPermi('system:floor:remove')")
    @Log(title = "房位房型", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(floorService.deleteFloorByIds(ids));
    }
}
