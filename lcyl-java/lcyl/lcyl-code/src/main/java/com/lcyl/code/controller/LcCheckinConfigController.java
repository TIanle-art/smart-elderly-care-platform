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
import com.lcyl.code.domain.LcCheckinConfig;
import com.lcyl.code.service.ILcCheckinConfigService;
import com.lcyl.common.utils.poi.ExcelUtil;
import com.lcyl.common.core.page.TableDataInfo;

/**
 * 入住配置Controller
 * 
 * @author ruoyi
 * @date 2026-03-23
 */
@RestController
@RequestMapping("/lcyl-code/config")
public class LcCheckinConfigController extends BaseController
{
    @Autowired
    private ILcCheckinConfigService lcCheckinConfigService;

    /**
     * 查询入住配置列表
     */
    @PreAuthorize("@ss.hasPermi('lcyl-code:config:list')")
    @GetMapping("/list")
    public TableDataInfo list(LcCheckinConfig lcCheckinConfig)
    {
        startPage();
        List<LcCheckinConfig> list = lcCheckinConfigService.selectLcCheckinConfigList(lcCheckinConfig);
        return getDataTable(list);
    }

    /**
     * 导出入住配置列表
     */
    @PreAuthorize("@ss.hasPermi('lcyl-code:config:export')")
    @Log(title = "入住配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, LcCheckinConfig lcCheckinConfig)
    {
        List<LcCheckinConfig> list = lcCheckinConfigService.selectLcCheckinConfigList(lcCheckinConfig);
        ExcelUtil<LcCheckinConfig> util = new ExcelUtil<LcCheckinConfig>(LcCheckinConfig.class);
        util.exportExcel(response, list, "入住配置数据");
    }

    /**
     * 获取入住配置详细信息
     */
    @PreAuthorize("@ss.hasPermi('lcyl-code:config:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(lcCheckinConfigService.selectLcCheckinConfigById(id));
    }

    /**
     * 新增入住配置
     */
    @PreAuthorize("@ss.hasPermi('lcyl-code:config:add')")
    @Log(title = "入住配置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody LcCheckinConfig lcCheckinConfig)
    {
        return toAjax(lcCheckinConfigService.insertLcCheckinConfig(lcCheckinConfig));
    }

    /**
     * 修改入住配置
     */
    @PreAuthorize("@ss.hasPermi('lcyl-code:config:edit')")
    @Log(title = "入住配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody LcCheckinConfig lcCheckinConfig)
    {
        return toAjax(lcCheckinConfigService.updateLcCheckinConfig(lcCheckinConfig));
    }

    /**
     * 删除入住配置
     */
    @PreAuthorize("@ss.hasPermi('lcyl-code:config:remove')")
    @Log(title = "入住配置", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(lcCheckinConfigService.deleteLcCheckinConfigByIds(ids));
    }
}
