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
import com.lcyl.code.domain.LcCheckinAttachment;
import com.lcyl.code.service.ILcCheckinAttachmentService;
import com.lcyl.common.utils.poi.ExcelUtil;
import com.lcyl.common.core.page.TableDataInfo;

/**
 * 资料上传Controller
 * 
 * @author ruoyi
 * @date 2026-03-23
 */
@RestController
@RequestMapping("/lcyl-code/attachment")
public class LcCheckinAttachmentController extends BaseController
{
    @Autowired
    private ILcCheckinAttachmentService lcCheckinAttachmentService;

    /**
     * 查询资料上传列表
     */
    @PreAuthorize("@ss.hasPermi('lcyl-code:attachment:list')")
    @GetMapping("/list")
    public TableDataInfo list(LcCheckinAttachment lcCheckinAttachment)
    {
        startPage();
        List<LcCheckinAttachment> list = lcCheckinAttachmentService.selectLcCheckinAttachmentList(lcCheckinAttachment);
        return getDataTable(list);
    }

    /**
     * 导出资料上传列表
     */
    @PreAuthorize("@ss.hasPermi('lcyl-code:attachment:export')")
    @Log(title = "资料上传", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, LcCheckinAttachment lcCheckinAttachment)
    {
        List<LcCheckinAttachment> list = lcCheckinAttachmentService.selectLcCheckinAttachmentList(lcCheckinAttachment);
        ExcelUtil<LcCheckinAttachment> util = new ExcelUtil<LcCheckinAttachment>(LcCheckinAttachment.class);
        util.exportExcel(response, list, "资料上传数据");
    }

    /**
     * 获取资料上传详细信息
     */
    @PreAuthorize("@ss.hasPermi('lcyl-code:attachment:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(lcCheckinAttachmentService.selectLcCheckinAttachmentById(id));
    }

    /**
     * 新增资料上传
     */
    @PreAuthorize("@ss.hasPermi('lcyl-code:attachment:add')")
    @Log(title = "资料上传", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody LcCheckinAttachment lcCheckinAttachment)
    {
        return toAjax(lcCheckinAttachmentService.insertLcCheckinAttachment(lcCheckinAttachment));
    }

    /**
     * 修改资料上传
     */
    @PreAuthorize("@ss.hasPermi('lcyl-code:attachment:edit')")
    @Log(title = "资料上传", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody LcCheckinAttachment lcCheckinAttachment)
    {
        return toAjax(lcCheckinAttachmentService.updateLcCheckinAttachment(lcCheckinAttachment));
    }

    /**
     * 删除资料上传
     */
    @PreAuthorize("@ss.hasPermi('lcyl-code:attachment:remove')")
    @Log(title = "资料上传", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(lcCheckinAttachmentService.deleteLcCheckinAttachmentByIds(ids));
    }
}
