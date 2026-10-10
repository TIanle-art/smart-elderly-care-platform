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
import com.lcyl.code.domain.BillItem;
import com.lcyl.code.service.IBillItemService;
import com.lcyl.common.utils.poi.ExcelUtil;
import com.lcyl.common.core.page.TableDataInfo;

/**
 * 账单明细Controller
 * 
 * @author ruoyi
 * @date 2026-03-26
 */
@RestController
@RequestMapping("/code/item")
public class BillItemController extends BaseController
{
    @Autowired
    private IBillItemService billItemService;

    /**
     * 查询账单明细列表
     */
    @PreAuthorize("@ss.hasPermi('code:item:list')")
    @GetMapping("/list")
    public TableDataInfo list(BillItem billItem)
    {
        startPage();
        List<BillItem> list = billItemService.selectBillItemList(billItem);
        return getDataTable(list);
    }

    /**
     * 导出账单明细列表
     */
    @PreAuthorize("@ss.hasPermi('code:item:export')")
    @Log(title = "账单明细", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BillItem billItem)
    {
        List<BillItem> list = billItemService.selectBillItemList(billItem);
        ExcelUtil<BillItem> util = new ExcelUtil<BillItem>(BillItem.class);
        util.exportExcel(response, list, "账单明细数据");
    }

    /**
     * 获取账单明细详细信息
     */
    @PreAuthorize("@ss.hasPermi('code:item:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(billItemService.selectBillItemById(id));
    }

    /**
     * 新增账单明细
     */
    @PreAuthorize("@ss.hasPermi('code:item:add')")
    @Log(title = "账单明细", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BillItem billItem)
    {
        return toAjax(billItemService.insertBillItem(billItem));
    }

    /**
     * 修改账单明细
     */
    @PreAuthorize("@ss.hasPermi('code:item:edit')")
    @Log(title = "账单明细", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BillItem billItem)
    {
        return toAjax(billItemService.updateBillItem(billItem));
    }

    /**
     * 删除账单明细
     */
    @PreAuthorize("@ss.hasPermi('code:item:remove')")
    @Log(title = "账单明细", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(billItemService.deleteBillItemByIds(ids));
    }

    /**
     * 根据老人ID查询护理项目列表
     */
    @GetMapping("/nursingItems/{elderId}")
    public AjaxResult getNursingItemsByElderId(@PathVariable Long elderId) {
        List<BillItem> items = billItemService.selectNursingItemsByElderId(elderId);
        return success(items);
    }
}
