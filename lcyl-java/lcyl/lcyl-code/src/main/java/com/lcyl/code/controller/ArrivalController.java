package com.lcyl.code.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.lcyl.code.domain.LcReservation;
import com.lcyl.code.domain.vo.ElderListVo;
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
import com.lcyl.code.domain.Arrival;
import com.lcyl.code.service.IArrivalService;
import com.lcyl.common.utils.poi.ExcelUtil;
import com.lcyl.common.core.page.TableDataInfo;

/**
 * 来访登记Controller
 * 
 * @author ruoyi
 * @date 2026-03-23
 */
@RestController
@RequestMapping("/code/arrival")
public class ArrivalController extends BaseController
{
    @Autowired
    private IArrivalService arrivalService;

    /**
     * 查询来访登记列表
     */
    @PreAuthorize("@ss.hasPermi('code:arrival:list')")
    @GetMapping("/list")
    public TableDataInfo list(Arrival arrival)
    {
        startPage();
        List<Arrival> list = arrivalService.selectArrivalList(arrival);
        return getDataTable(list);
    }

    /**
     * 导出来访登记列表
     */
    @PreAuthorize("@ss.hasPermi('code:arrival:export')")
    @Log(title = "来访登记", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Arrival arrival)
    {
        List<Arrival> list = arrivalService.selectArrivalList(arrival);
        ExcelUtil<Arrival> util = new ExcelUtil<Arrival>(Arrival.class);
        util.exportExcel(response, list, "来访登记数据");
    }

    /**
     * 获取来访登记详细信息
     */
    @PreAuthorize("@ss.hasPermi('code:arrival:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(arrivalService.selectArrivalById(id));
    }

    /**
     * 新增来访登记
     */
    @PreAuthorize("@ss.hasPermi('code:arrival:add')")
    @Log(title = "来访登记", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Arrival arrival)
    {
        return toAjax(arrivalService.insertArrival(arrival));
    }

    /**
     * 修改来访登记
     */
    @PreAuthorize("@ss.hasPermi('code:arrival:edit')")
    @Log(title = "来访登记", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Arrival arrival)
    {
        return toAjax(arrivalService.updateArrival(arrival));
    }

    /**
     * 删除来访登记
     */
    @PreAuthorize("@ss.hasPermi('code:arrival:remove')")
    @Log(title = "来访登记", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(arrivalService.deleteArrivalByIds(ids));
    }

    @GetMapping("/typeOptions/{id}")
    public TableDataInfo typeList(@PathVariable("id") Long id){
        startPage();
        List<Arrival> list = arrivalService.selectArrivalByType(id);
        return getDataTable(list);
    }

    @GetMapping("/olderList")
    public AjaxResult olderList(){
        List<ElderListVo> elderList = arrivalService.selectOlderList();
        return AjaxResult.success(elderList);
    }
}
