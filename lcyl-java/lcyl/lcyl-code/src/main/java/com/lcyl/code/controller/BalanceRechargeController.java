package com.lcyl.code.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import com.lcyl.code.domain.dto.BalanceRechargeDTO;
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
import com.lcyl.code.domain.BalanceRecharge;
import com.lcyl.code.service.IRechargeService;
import com.lcyl.common.utils.poi.ExcelUtil;
import com.lcyl.common.core.page.TableDataInfo;
import com.lcyl.code.domain.vo.RechargeElderOptionVO;

/**
 * 预缴款充值记录Controller
 * 
 * @author ruoyi
 * @date 2026-03-27
 */
@RestController
@RequestMapping("/code/recharge")
public class BalanceRechargeController extends BaseController
{
    @Autowired
    private IRechargeService balanceRechargeService;

    /**
     * 查询预缴款充值记录列表
     */
    @PreAuthorize("@ss.hasPermi('code:recharge:list')")
    @GetMapping("/list")
    public TableDataInfo list(BalanceRecharge balanceRecharge)
    {
        startPage();
        List<BalanceRecharge> list = balanceRechargeService.selectBalanceRechargeList(balanceRecharge);
        return getDataTable(list);
    }

    /**
     * 导出预缴款充值记录列表
     */
    @PreAuthorize("@ss.hasPermi('code:recharge:export')")
    @Log(title = "预缴款充值记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BalanceRecharge balanceRecharge)
    {
        List<BalanceRecharge> list = balanceRechargeService.selectBalanceRechargeList(balanceRecharge);
        ExcelUtil<BalanceRecharge> util = new ExcelUtil<BalanceRecharge>(BalanceRecharge.class);
        util.exportExcel(response, list, "预缴款充值记录数据");
    }

    /**
     * 获取预缴款充值记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('code:recharge:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(balanceRechargeService.selectBalanceRechargeById(id));
    }

    /**
     * 查询充值可选老人列表
     */
    @PreAuthorize("@ss.hasPermi('code:recharge:list')")
    @GetMapping("/elderOptions")
    public AjaxResult elderOptions()
    {
        List<RechargeElderOptionVO> list = balanceRechargeService.selectRechargeElderOptions();
        return success(list);
    }

    /**
     * 新增预缴款充值记录
     */
    @PreAuthorize("@ss.hasPermi('code:recharge:add')")
    @Log(title = "预缴款充值记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BalanceRecharge balanceRecharge)
    {
        return toAjax(balanceRechargeService.insertBalanceRecharge(balanceRecharge));
    }

    /**
     * 修改预缴款充值记录
     */
    @PreAuthorize("@ss.hasPermi('code:recharge:edit')")
    @Log(title = "预缴款充值记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BalanceRecharge balanceRecharge)
    {
        return toAjax(balanceRechargeService.updateBalanceRecharge(balanceRecharge));
    }

    /**
     * 删除预缴款充值记录
     */
    @PreAuthorize("@ss.hasPermi('code:recharge:remove')")
    @Log(title = "预缴款充值记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(balanceRechargeService.deleteBalanceRechargeByIds(ids));
    }

    /**
     * 预缴款充值
     */
    @PreAuthorize("@ss.hasPermi('code:recharge:add')")
    @Log(title = "预缴款充值记录", businessType = BusinessType.INSERT)
    @PostMapping("/rechargeBalance")
    public AjaxResult rechargeBalance(@RequestBody BalanceRechargeDTO dto)
    {
        return toAjax(balanceRechargeService.rechargeBalance(dto));
    }
}
