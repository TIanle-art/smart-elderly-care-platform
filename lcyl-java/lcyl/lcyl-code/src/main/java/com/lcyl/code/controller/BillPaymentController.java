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
import com.lcyl.code.domain.BillPayment;
import com.lcyl.code.service.IBillPaymentService;
import com.lcyl.common.utils.poi.ExcelUtil;
import com.lcyl.common.core.page.TableDataInfo;

/**
 * 账单支付记录Controller
 * 
 * @author ruoyi
 * @date 2026-03-26
 */
@RestController
@RequestMapping("/code/payment")
public class BillPaymentController extends BaseController
{
    @Autowired
    private IBillPaymentService billPaymentService;

    /**
     * 查询账单支付记录列表
     */
    @PreAuthorize("@ss.hasPermi('code:payment:list')")
    @GetMapping("/list")
    public TableDataInfo list(BillPayment billPayment)
    {
        startPage();
        List<BillPayment> list = billPaymentService.selectBillPaymentList(billPayment);
        return getDataTable(list);
    }

    /**
     * 导出账单支付记录列表
     */
    @PreAuthorize("@ss.hasPermi('code:payment:export')")
    @Log(title = "账单支付记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BillPayment billPayment)
    {
        List<BillPayment> list = billPaymentService.selectBillPaymentList(billPayment);
        ExcelUtil<BillPayment> util = new ExcelUtil<BillPayment>(BillPayment.class);
        util.exportExcel(response, list, "账单支付记录数据");
    }

    /**
     * 获取账单支付记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('code:payment:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(billPaymentService.selectBillPaymentById(id));
    }

    /**
     * 新增账单支付记录
     */
    @PreAuthorize("@ss.hasPermi('code:payment:add')")
    @Log(title = "账单支付记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BillPayment billPayment)
    {
        return toAjax(billPaymentService.insertBillPayment(billPayment));
    }

    /**
     * 修改账单支付记录
     */
    @PreAuthorize("@ss.hasPermi('code:payment:edit')")
    @Log(title = "账单支付记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BillPayment billPayment)
    {
        return toAjax(billPaymentService.updateBillPayment(billPayment));
    }

    /**
     * 删除账单支付记录
     */
    @PreAuthorize("@ss.hasPermi('code:payment:remove')")
    @Log(title = "账单支付记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(billPaymentService.deleteBillPaymentByIds(ids));
    }
}
