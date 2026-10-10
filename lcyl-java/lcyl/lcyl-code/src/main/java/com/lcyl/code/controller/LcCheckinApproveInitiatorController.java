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
import com.lcyl.code.domain.LcCheckinApproveInitiator;
import com.lcyl.code.service.ILcCheckinApproveInitiatorService;
import com.lcyl.common.utils.poi.ExcelUtil;
import com.lcyl.common.core.page.TableDataInfo;

/**
 * 审批发起人Controller
 * 
 * @author ruoyi
 * @date 2026-03-23
 */
@RestController
@RequestMapping("/lcyl-code/initiator")
public class LcCheckinApproveInitiatorController extends BaseController
{
    @Autowired
    private ILcCheckinApproveInitiatorService lcCheckinApproveInitiatorService;

    /**
     * 查询审批发起人列表
     */
    @PreAuthorize("@ss.hasPermi('lcyl-code:initiator:list')")
    @GetMapping("/list")
    public TableDataInfo list(LcCheckinApproveInitiator lcCheckinApproveInitiator)
    {
        startPage();
        List<LcCheckinApproveInitiator> list = lcCheckinApproveInitiatorService.selectLcCheckinApproveInitiatorList(lcCheckinApproveInitiator);
        return getDataTable(list);
    }

    /**
     * 导出审批发起人列表
     */
    @PreAuthorize("@ss.hasPermi('lcyl-code:initiator:export')")
    @Log(title = "审批发起人", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, LcCheckinApproveInitiator lcCheckinApproveInitiator)
    {
        List<LcCheckinApproveInitiator> list = lcCheckinApproveInitiatorService.selectLcCheckinApproveInitiatorList(lcCheckinApproveInitiator);
        ExcelUtil<LcCheckinApproveInitiator> util = new ExcelUtil<LcCheckinApproveInitiator>(LcCheckinApproveInitiator.class);
        util.exportExcel(response, list, "审批发起人数据");
    }

    /**
     * 获取审批发起人详细信息
     */
    @PreAuthorize("@ss.hasPermi('lcyl-code:initiator:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(lcCheckinApproveInitiatorService.selectLcCheckinApproveInitiatorById(id));
    }

    /**
     * 新增审批发起人
     */
    @PreAuthorize("@ss.hasPermi('lcyl-code:initiator:add')")
    @Log(title = "审批发起人", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody LcCheckinApproveInitiator lcCheckinApproveInitiator)
    {
        return toAjax(lcCheckinApproveInitiatorService.insertLcCheckinApproveInitiator(lcCheckinApproveInitiator));
    }

    /**
     * 修改审批发起人
     */
    @PreAuthorize("@ss.hasPermi('lcyl-code:initiator:edit')")
    @Log(title = "审批发起人", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody LcCheckinApproveInitiator lcCheckinApproveInitiator)
    {
        return toAjax(lcCheckinApproveInitiatorService.updateLcCheckinApproveInitiator(lcCheckinApproveInitiator));
    }

    /**
     * 删除审批发起人
     */
    @PreAuthorize("@ss.hasPermi('lcyl-code:initiator:remove')")
    @Log(title = "审批发起人", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(lcCheckinApproveInitiatorService.deleteLcCheckinApproveInitiatorByIds(ids));
    }
}
