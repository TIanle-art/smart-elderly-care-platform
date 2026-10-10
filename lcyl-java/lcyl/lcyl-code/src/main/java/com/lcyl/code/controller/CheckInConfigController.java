package com.lcyl.code.controller;

import com.lcyl.common.annotation.Log;
import com.lcyl.common.core.controller.BaseController;
import com.lcyl.common.core.domain.AjaxResult;
import com.lcyl.common.core.page.TableDataInfo;
import com.lcyl.common.enums.BusinessType;
import com.lcyl.common.utils.poi.ExcelUtil;
import com.lcyl.code.domain.CheckInConfigg;
import com.lcyl.code.domain.dto.CheckInFlowDTO;
import com.lcyl.code.service.ICheckInConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * 入住配置表Controller
 * 
 * @author ruoyi
 * @date
 */
@RestController
@RequestMapping("/system/checkin-config")
public class CheckInConfigController extends BaseController
{
    @Autowired
    private ICheckInConfigService checkInConfigService;

    /**
     * 查询入住配置表列表
     */
    @PreAuthorize("@ss.hasPermi('system:config:list')")
    @GetMapping("/list")
    public TableDataInfo list(CheckInConfigg checkInConfig)
    {
        startPage();
        List<CheckInConfigg> list = checkInConfigService.selectCheckInConfigList(checkInConfig);
        return getDataTable(list);
    }

    /**
     * 导出入住配置表列表
     */
    @PreAuthorize("@ss.hasPermi('system:config:export')")
    @Log(title = "入住配置表", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CheckInConfigg checkInConfig)
    {
        List<CheckInConfigg> list = checkInConfigService.selectCheckInConfigList(checkInConfig);
        ExcelUtil<CheckInConfigg> util = new ExcelUtil<CheckInConfigg>(CheckInConfigg.class);
        util.exportExcel(response, list, "入住配置表数据");
    }

    /**
     * 获取入住配置表详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:config:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(checkInConfigService.selectCheckInConfigById(id));
    }

    /**
     * 新增入住配置表
     */
    @PreAuthorize("@ss.hasPermi('system:config:add')")
    @Log(title = "入住配置表", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CheckInConfigg checkInConfig)
    {
        return toAjax(checkInConfigService.insertCheckInConfig(checkInConfig));
    }

    /**
     * 修改入住配置表
     */
    @PreAuthorize("@ss.hasPermi('system:config:edit')")
    @Log(title = "入住配置表", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CheckInConfigg checkInConfig)
    {
        return toAjax(checkInConfigService.updateCheckInConfig(checkInConfig));
    }

    /**
     * 删除入住配置表
     */
    @PreAuthorize("@ss.hasPermi('system:config:remove')")
    @Log(title = "入住配置表", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(checkInConfigService.deleteCheckInConfigByIds(ids));
    }

    /**
     * 提交入住配置：保存到入住配置表，并将老人关联到所选床位（占用床位）
     */
    @PostMapping("/submit")
    public AjaxResult submit(@RequestBody CheckInConfigg checkInConfig)
    {
        CheckInFlowDTO checkInFlowDTO = checkInConfigService.submitConfig(checkInConfig);
        return success(checkInFlowDTO);
    }
    //根据老人id查询入住配置
    @GetMapping("/getConfigByElderId")
    public AjaxResult getConfigByElderId(Integer elderId, String billMonth, Date startTime, Date endTime){
        return success(checkInConfigService.selectCheckInConfigByElderId(elderId, billMonth, startTime, endTime));
    }
}
