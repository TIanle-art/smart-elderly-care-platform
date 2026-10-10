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
import com.lcyl.code.domain.LcCheckinContract;
import com.lcyl.code.service.ILcCheckinContractService;
import com.lcyl.common.utils.poi.ExcelUtil;
import com.lcyl.common.core.page.TableDataInfo;

/**
 * 签约办理Controller
 * 
 * @author ruoyi
 * @date 2026-03-23
 */
@RestController
@RequestMapping("/lcyl-code/contract")
public class LcCheckinContractController extends BaseController
{
    @Autowired
    private ILcCheckinContractService lcCheckinContractService;

    /**
     * 查询签约办理列表
     */
    @PreAuthorize("@ss.hasPermi('lcyl-code:contract:list')")
    @GetMapping("/list")
    public TableDataInfo list(LcCheckinContract lcCheckinContract)
    {
        startPage();
        List<LcCheckinContract> list = lcCheckinContractService.selectLcCheckinContractList(lcCheckinContract);
        return getDataTable(list);
    }

    /**
     * 导出签约办理列表
     */
    @PreAuthorize("@ss.hasPermi('lcyl-code:contract:export')")
    @Log(title = "签约办理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, LcCheckinContract lcCheckinContract)
    {
        List<LcCheckinContract> list = lcCheckinContractService.selectLcCheckinContractList(lcCheckinContract);
        ExcelUtil<LcCheckinContract> util = new ExcelUtil<LcCheckinContract>(LcCheckinContract.class);
        util.exportExcel(response, list, "签约办理数据");
    }

    /**
     * 获取签约办理详细信息
     */
    @PreAuthorize("@ss.hasPermi('lcyl-code:contract:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(lcCheckinContractService.selectLcCheckinContractById(id));
    }

    /**
     * 新增签约办理
     */
    @PreAuthorize("@ss.hasPermi('lcyl-code:contract:add')")
    @Log(title = "签约办理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody LcCheckinContract lcCheckinContract)
    {
        return toAjax(lcCheckinContractService.insertLcCheckinContract(lcCheckinContract));
    }

    /**
     * 修改签约办理
     */
    @PreAuthorize("@ss.hasPermi('lcyl-code:contract:edit')")
    @Log(title = "签约办理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody LcCheckinContract lcCheckinContract)
    {
        return toAjax(lcCheckinContractService.updateLcCheckinContract(lcCheckinContract));
    }

    /**
     * 删除签约办理
     */
    @PreAuthorize("@ss.hasPermi('lcyl-code:contract:remove')")
    @Log(title = "签约办理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(lcCheckinContractService.deleteLcCheckinContractByIds(ids));
    }
}
