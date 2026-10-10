package com.lcyl.code.controller;

import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.lcyl.code.vo.CheckinHomeVO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.lcyl.common.annotation.Log;
import com.lcyl.common.core.controller.BaseController;
import com.lcyl.common.core.domain.AjaxResult;
import com.lcyl.common.enums.BusinessType;
import com.lcyl.code.domain.LcCheckinApply;
import com.lcyl.code.service.ILcCheckinApplyService;
import com.lcyl.common.utils.poi.ExcelUtil;
import com.lcyl.common.core.page.TableDataInfo;

/**
 * 入住申请Controller
 * 
 * @author ruoyi
 * @date 2026-03-23
 */
@RestController
@RequestMapping("/lcyl-code/apply")
public class LcCheckinApplyController extends BaseController
{
    @Autowired
    private ILcCheckinApplyService lcCheckinApplyService;

    /**
     * 查询入住申请列表
     */
    @PreAuthorize("@ss.hasPermi('lcyl-code:apply:list')")
    @GetMapping("/list")
    public TableDataInfo list(LcCheckinApply lcCheckinApply)
    {
        startPage();
        List<LcCheckinApply> list = lcCheckinApplyService.selectLcCheckinApplyList(lcCheckinApply);
        return getDataTable(list);
    }

    /**
     * 导出入住申请列表
     */
    @PreAuthorize("@ss.hasPermi('lcyl-code:apply:export')")
    @Log(title = "入住申请", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, LcCheckinApply lcCheckinApply)
    {
        List<LcCheckinApply> list = lcCheckinApplyService.selectLcCheckinApplyList(lcCheckinApply);
        ExcelUtil<LcCheckinApply> util = new ExcelUtil<LcCheckinApply>(LcCheckinApply.class);
        util.exportExcel(response, list, "入住申请数据");
    }

    /**
     * 获取入住申请详细信息
     */
    @PreAuthorize("@ss.hasPermi('lcyl-code:apply:query')")
    @GetMapping(value = "/{applyId}")
    public AjaxResult getInfo(@PathVariable("applyId") Long applyId)
    {
        return success(lcCheckinApplyService.selectLcCheckinApplyByApplyId(applyId));
    }

    /**
     * 新增入住申请
     */
    @PreAuthorize("@ss.hasPermi('lcyl-code:apply:add')")
    @Log(title = "入住申请", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody LcCheckinApply lcCheckinApply)
    {
        return toAjax(lcCheckinApplyService.insertLcCheckinApply(lcCheckinApply));
    }

    /**
     * 修改入住申请
     */
    @PreAuthorize("@ss.hasPermi('lcyl-code:apply:edit')")
    @Log(title = "入住申请", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody LcCheckinApply lcCheckinApply)
    {
        return toAjax(lcCheckinApplyService.updateLcCheckinApply(lcCheckinApply));
    }

    /**
     * 删除入住申请
     */
    @PreAuthorize("@ss.hasPermi('lcyl-code:apply:remove')")
    @Log(title = "入住申请", businessType = BusinessType.DELETE)
	@DeleteMapping("/{applyIds}")
    public AjaxResult remove(@PathVariable Long[] applyIds)
    {
        return toAjax(lcCheckinApplyService.deleteLcCheckinApplyByApplyIds(applyIds));
    }

    @GetMapping("/home/list")
    public TableDataInfo homeList(
            LcCheckinApply lcCheckinApply,
            @RequestParam(required = false) Date beginTime,
            @RequestParam(required = false) Date endTime
    ) {
        startPage();
        List<CheckinHomeVO> list = lcCheckinApplyService.selectCheckinHomeList(lcCheckinApply, beginTime, endTime);
        return getDataTable(list);
    }

    @GetMapping("/detail/{applyId}")
    public AjaxResult getDetail(@PathVariable Long applyId) {
        CheckinHomeVO detail = lcCheckinApplyService.selectCheckinDetail(applyId);
        return detail != null ? success(detail) : error("申请不存在");
    }

    @Log(title = "入住办理", businessType = BusinessType.INSERT)
    @PostMapping("/initiate")
    public AjaxResult initiate() {
        Long applyId = lcCheckinApplyService.initiateCheckinApply();
        return success(applyId);
    }
}
