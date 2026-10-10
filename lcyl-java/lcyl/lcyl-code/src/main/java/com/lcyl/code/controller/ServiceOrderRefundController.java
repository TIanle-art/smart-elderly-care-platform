package com.lcyl.code.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.lcyl.common.annotation.Log;
import com.lcyl.common.core.controller.BaseController;
import com.lcyl.common.core.domain.AjaxResult;
import com.lcyl.common.core.page.TableDataInfo;
import com.lcyl.common.enums.BusinessType;
import com.lcyl.common.utils.poi.ExcelUtil;
import com.lcyl.code.domain.ServiceOrderRefund;
import com.lcyl.code.domain.dto.ServiceOrderRefundFailDTO;
import com.lcyl.code.service.IServiceOrderRefundService;

/**
 * 退款管理Controller
 *
 * @author ruoyi
 * @date 2026-03-21
 */
@RestController
@RequestMapping("/code/refund")
public class ServiceOrderRefundController extends BaseController
{
    @Autowired
    private IServiceOrderRefundService serviceOrderRefundService;

    /**
     * 查询退款管理列表
     */
    @PreAuthorize("@ss.hasPermi('code:refund:list')")
    @GetMapping("/list")
    public TableDataInfo list(ServiceOrderRefund serviceOrderRefund)
    {
        startPage();
        List<ServiceOrderRefund> list = serviceOrderRefundService.selectServiceOrderRefundList(serviceOrderRefund);
        return getDataTable(list);
    }

    /**
     * 导出退款管理列表
     */
    @PreAuthorize("@ss.hasPermi('code:refund:export')")
    @Log(title = "退款管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ServiceOrderRefund serviceOrderRefund)
    {
        List<ServiceOrderRefund> list = serviceOrderRefundService.selectServiceOrderRefundList(serviceOrderRefund);
        ExcelUtil<ServiceOrderRefund> util = new ExcelUtil<ServiceOrderRefund>(ServiceOrderRefund.class);
        util.exportExcel(response, list, "退款管理数据");
    }

    /**
     * 获取退款管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('code:refund:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(serviceOrderRefundService.selectServiceOrderRefundById(id));
    }

    /**
     * 退款成功
     */
    @PreAuthorize("@ss.hasPermi('code:refund:edit')")
    @Log(title = "退款管理", businessType = BusinessType.UPDATE)
    @PostMapping("/success/{id}")
    public AjaxResult refundSuccess(@PathVariable("id") Long id)
    {
        return toAjax(serviceOrderRefundService.refundSuccess(id));
    }

    /**
     * 退款失败
     */
    @PreAuthorize("@ss.hasPermi('code:refund:edit')")
    @Log(title = "退款管理", businessType = BusinessType.UPDATE)
    @PostMapping("/fail")
    public AjaxResult refundFail(@RequestBody ServiceOrderRefundFailDTO dto)
    {
        return toAjax(serviceOrderRefundService.refundFail(dto));
    }
}
