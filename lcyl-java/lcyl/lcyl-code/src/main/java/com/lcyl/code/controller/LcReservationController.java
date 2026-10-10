package com.lcyl.code.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.lcyl.code.dto.ArrayDto;
import com.lcyl.common.core.domain.R;
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
import com.lcyl.code.domain.LcReservation;
import com.lcyl.code.service.ILcReservationService;
import com.lcyl.common.utils.poi.ExcelUtil;
import com.lcyl.common.core.page.TableDataInfo;

/**
 * 预约来访Controller
 * 
 * @author ruoyi
 * @date 2026-03-20
 */
@RestController
@RequestMapping("/code/reservation")
public class LcReservationController extends BaseController
{
    @Autowired
    private ILcReservationService lcReservationService;

    /**
     * 查询预约来访列表
     */
    @PreAuthorize("@ss.hasPermi('code:reservation:list')")
    @GetMapping("/list")
    public TableDataInfo list(LcReservation lcReservation)
    {
        startPage();
        List<LcReservation> list = lcReservationService.selectLcReservationList(lcReservation);
        return getDataTable(list);
    }

    //@PreAuthorize("@ss.hasPermi('code:reservation:typeOptions')")
    @GetMapping("/typeOptions/{id}")
    public TableDataInfo typeList(@PathVariable("id") Long id){
        startPage();
        List<LcReservation> list = lcReservationService.selectLcReservationByType(id);
        return getDataTable(list);
    }


    /**
     * 导出预约来访列表
     */
    @PreAuthorize("@ss.hasPermi('code:reservation:export')")
    @Log(title = "预约来访", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, LcReservation lcReservation)
    {
        List<LcReservation> list = lcReservationService.selectLcReservationList(lcReservation);
        ExcelUtil<LcReservation> util = new ExcelUtil<LcReservation>(LcReservation.class);
        util.exportExcel(response, list, "预约来访数据");
    }

    /**
     * 获取预约来访详细信息
     */
    @PreAuthorize("@ss.hasPermi('code:reservation:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(lcReservationService.selectLcReservationById(id));
    }

    /**
     * 新增预约来访
     */
    @PreAuthorize("@ss.hasPermi('code:reservation:add')")
    @Log(title = "预约来访", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody LcReservation lcReservation)
    {
        return toAjax(lcReservationService.insertLcReservation(lcReservation));
    }

    //到访时间
    @PutMapping("/updateArrivalTime")
    public AjaxResult updateArrayTime(@RequestBody ArrayDto arrayDto){
        lcReservationService.updateArrayTime(arrayDto);
        return success();
    }

    /**
     * 修改预约来访
     */
    @PreAuthorize("@ss.hasPermi('code:reservation:edit')")
    @Log(title = "预约来访", businessType = BusinessType.UPDATE)
    public AjaxResult edit(@RequestBody LcReservation lcReservation)
    {
        return toAjax(lcReservationService.updateLcReservation(lcReservation));
    }

    /**
     * 删除预约来访
     */
    @PreAuthorize("@ss.hasPermi('code:reservation:remove')")
    @Log(title = "预约来访", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(lcReservationService.deleteLcReservationByIds(ids));
    }
}
