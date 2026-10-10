package com.lcyl.code.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.lcyl.code.domain.dto.BillCancelDTO;
import com.lcyl.code.domain.dto.BillGenerateDTO;
import com.lcyl.code.domain.dto.BillPayDTO;
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
import com.lcyl.code.domain.Bill;
import com.lcyl.code.service.IBillService;
import com.lcyl.common.utils.poi.ExcelUtil;
import com.lcyl.common.core.page.TableDataInfo;

/**
 * 账单列表Controller
 * 
 * @author ruoyi
 * @date 2026-03-25
 */
@RestController
@RequestMapping("/code/bill")
public class BillController extends BaseController
{
    @Autowired
    private IBillService billService;

    /**
     * 查询账单列表列表
     */
    @PreAuthorize("@ss.hasPermi('code:bill:list')")
    @GetMapping("/list")
    public TableDataInfo list(Bill bill)
    {
        startPage();
        List<Bill> list = billService.selectBillList(bill);
        return getDataTable(list);
    }

    /**
     * 导出账单列表列表
     */
    @PreAuthorize("@ss.hasPermi('code:bill:export')")
    @Log(title = "账单列表", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Bill bill)
    {
        List<Bill> list = billService.selectBillList(bill);
        ExcelUtil<Bill> util = new ExcelUtil<Bill>(Bill.class);
        util.exportExcel(response, list, "账单列表数据");
    }

    /**
     * 获取账单列表详细信息
     */
    @PreAuthorize("@ss.hasPermi('code:bill:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(billService.selectBillById(id));
    }

    /**
     * 新增账单列表
     */
    @PreAuthorize("@ss.hasPermi('code:bill:add')")
    @Log(title = "账单列表", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Bill bill)
    {
        return toAjax(billService.insertBill(bill));
    }

    /**
     * 修改账单列表
     */
    @PreAuthorize("@ss.hasPermi('code:bill:edit')")
    @Log(title = "账单列表", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Bill bill)
    {
        return toAjax(billService.updateBill(bill));
    }

    /**
     * 删除账单列表
     */
    @PreAuthorize("@ss.hasPermi('code:bill:remove')")
    @Log(title = "账单列表", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(billService.deleteBillByIds(ids));
    }
    /**
     * 生成月度账单
     */
    @PreAuthorize("@ss.hasPermi('code:bill:add')")
    @Log(title = "账单列表", businessType = BusinessType.INSERT)
    @PostMapping("/generateMonthlyBill")
    public AjaxResult generateMonthlyBill(@RequestBody BillGenerateDTO dto)
    {
        return toAjax(billService.generateMonthlyBill(dto));
    }
    /**
     * 取消账单
     */
    @PreAuthorize("@ss.hasPermi('code:bill:edit')")
    @Log(title = "账单列表", businessType = BusinessType.UPDATE)
    @PostMapping("/cancel")
    public AjaxResult cancel(@RequestBody BillCancelDTO dto)
    {
        return toAjax(billService.cancelBill(dto));
    }
    /**
     * 支付账单
     */
    @PreAuthorize("@ss.hasPermi('code:bill:edit')")
    @Log(title = "账单列表", businessType = BusinessType.UPDATE)
    @PostMapping("/pay")
    public AjaxResult pay(@RequestBody BillPayDTO dto)
    {
        return toAjax(billService.payBill(dto));
    }

    //查询老人待支付账单
    @GetMapping("/pending/{elderId}")
    public AjaxResult getPendingBills(@PathVariable Long elderId) {
        List<Bill> bills = billService.selectPendingBillsByElderId(elderId);
        return success(bills);
    }

}
