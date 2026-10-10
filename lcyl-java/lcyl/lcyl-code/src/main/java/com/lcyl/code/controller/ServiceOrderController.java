package com.lcyl.code.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.lcyl.code.mapper.ServiceOrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.lcyl.code.domain.ServiceOrder;
import com.lcyl.code.domain.dto.ServiceOrderCancelDTO;
import com.lcyl.code.domain.dto.ServiceOrderGenerateBillDTO;
import com.lcyl.code.domain.dto.ServiceOrderRefundApplyDTO;
import com.lcyl.code.domain.vo.ServiceOrderExecutionVO;
import com.lcyl.code.service.IServiceOrderService;
import com.lcyl.common.annotation.Log;
import com.lcyl.common.core.controller.BaseController;
import com.lcyl.common.core.domain.AjaxResult;
import com.lcyl.common.core.page.TableDataInfo;
import com.lcyl.common.enums.BusinessType;
import com.lcyl.common.utils.poi.ExcelUtil;

@RestController
@RequestMapping("/code/orders")
public class ServiceOrderController extends BaseController
{
    @Autowired
    private IServiceOrderService serviceOrderService;

    @PreAuthorize("@ss.hasPermi('code:orders:list')")
    @GetMapping("/list")
    public TableDataInfo list(ServiceOrder serviceOrder)
    {
        startPage();
        List<ServiceOrder> list = serviceOrderService.selectServiceOrderList(serviceOrder);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('code:orders:export')")
    @Log(title = "订单详情", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ServiceOrder serviceOrder)
    {
        List<ServiceOrder> list = serviceOrderService.selectServiceOrderList(serviceOrder);
        ExcelUtil<ServiceOrder> util = new ExcelUtil<ServiceOrder>(ServiceOrder.class);
        util.exportExcel(response, list, "订单详情数据");
    }

    @PreAuthorize("@ss.hasPermi('code:orders:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(serviceOrderService.selectServiceOrderById(id));
    }

    @PreAuthorize("@ss.hasPermi('code:orders:query')")
    @GetMapping("/execution/{orderId}")
    public AjaxResult getExecutionInfo(@PathVariable("orderId") Long orderId)
    {
        ServiceOrderExecutionVO execution = serviceOrderService.selectExecutionByOrderId(orderId);
        return success(execution);
    }

    @PreAuthorize("@ss.hasPermi('code:orders:add')")
    @Log(title = "订单详情", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ServiceOrder serviceOrder)
    {
        return toAjax(serviceOrderService.insertServiceOrder(serviceOrder));
    }

    @PreAuthorize("@ss.hasPermi('code:orders:edit')")
    @Log(title = "订单详情", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ServiceOrder serviceOrder)
    {
        return toAjax(serviceOrderService.updateServiceOrder(serviceOrder));
    }

    @PreAuthorize("@ss.hasPermi('code:orders:remove')")
    @Log(title = "订单详情", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(serviceOrderService.deleteServiceOrderByIds(ids));
    }

    @PreAuthorize("@ss.hasPermi('code:orders:edit')")
    @Log(title = "订单详情", businessType = BusinessType.UPDATE)
    @PostMapping("/cancel")
    public AjaxResult cancel(@RequestBody ServiceOrderCancelDTO dto)
    {
        return toAjax(serviceOrderService.cancelOrder(dto));
    }

    @PreAuthorize("@ss.hasPermi('code:orders:edit')")
    @Log(title = "订单退款", businessType = BusinessType.UPDATE)
    @PostMapping("/refund/apply")
    public AjaxResult applyRefund(@RequestBody ServiceOrderRefundApplyDTO dto)
    {
        return toAjax(serviceOrderService.applyRefund(dto));
    }

    @PreAuthorize("@ss.hasPermi('code:orders:edit')")
    @Log(title = "费用账单", businessType = BusinessType.INSERT)
    @PostMapping("/generateExpenseBill")
    public AjaxResult generateExpenseBill(@RequestBody ServiceOrderGenerateBillDTO dto)
    {
        return toAjax(serviceOrderService.generateExpenseBill(dto));
    }

    @Autowired
    private ServiceOrderMapper serviceOrderMapper;
//    @PreAuthorize("@ss.hasPermi('code:orders:list')")
    @GetMapping("/refundList/{elderId}")
    public AjaxResult refundList(@PathVariable("elderId") Long elderId) {
        // 调用独立新增的方法，完全不影响原来的list查询
        List<ServiceOrder> list = serviceOrderMapper.selectRefundServiceOrderList(elderId);
        return success(list);
    }
}
