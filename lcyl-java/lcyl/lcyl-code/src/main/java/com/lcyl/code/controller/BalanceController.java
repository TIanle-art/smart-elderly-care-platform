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
import com.lcyl.code.domain.Balance;
import com.lcyl.code.service.IBalanceService;
import com.lcyl.common.utils.poi.ExcelUtil;
import com.lcyl.common.core.page.TableDataInfo;

/**
 * 余额Controller
 * 
 * @author ruoyi
 * @date 2026-03-27
 */
@RestController
@RequestMapping("/code/balance")
public class BalanceController extends BaseController
{
    @Autowired
    private IBalanceService balanceService;

    /**
     * 查询余额列表
     */
    @PreAuthorize("@ss.hasPermi('code:balance:list')")
    @GetMapping("/list")
    public TableDataInfo list(Balance balance)
    {
        startPage();
        List<Balance> list = balanceService.selectBalanceList(balance);
        return getDataTable(list);
    }

    /**
     * 导出余额列表
     */
    @PreAuthorize("@ss.hasPermi('code:balance:export')")
    @Log(title = "余额", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Balance balance)
    {
        List<Balance> list = balanceService.selectBalanceList(balance);
        ExcelUtil<Balance> util = new ExcelUtil<Balance>(Balance.class);
        util.exportExcel(response, list, "余额数据");
    }

    /**
     * 获取余额详细信息
     */
    @PreAuthorize("@ss.hasPermi('code:balance:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(balanceService.selectBalanceById(id));
    }

    /**
     * 新增余额
     */
    @PreAuthorize("@ss.hasPermi('code:balance:add')")
    @Log(title = "余额", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Balance balance)
    {
        return toAjax(balanceService.insertBalance(balance));
    }

    /**
     * 修改余额
     */
    @PreAuthorize("@ss.hasPermi('code:balance:edit')")
    @Log(title = "余额", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Balance balance)
    {
        return toAjax(balanceService.updateBalance(balance));
    }

    /**
     * 删除余额
     */
    @PreAuthorize("@ss.hasPermi('code:balance:remove')")
    @Log(title = "余额", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(balanceService.deleteBalanceByIds(ids));
    }

    //根据老人id查询余额
    @GetMapping("/elder/{elderId}")
    public AjaxResult getBalanceByElderId(@PathVariable Long elderId) {
        Balance balance = balanceService.selectBalanceByElderId(elderId);
        return success(balance);
    }
}
