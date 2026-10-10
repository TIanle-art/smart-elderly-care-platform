package com.lcyl.code.controller;

import com.lcyl.code.domain.ElderLeave;
import com.lcyl.code.dto.ElderLeaveApproveDto;
import com.lcyl.code.dto.ElderLeaveDto;
import com.lcyl.code.dto.ElderLeaveResubmitDto;
import com.lcyl.code.dto.ElderLeaveSubmitDto;
import com.lcyl.code.service.IElderLeaveService;
import com.lcyl.code.vo.ElderLeaveVo;
import com.lcyl.common.annotation.Log;
import com.lcyl.common.core.controller.BaseController;
import com.lcyl.common.core.domain.AjaxResult;
import com.lcyl.common.core.page.TableDataInfo;
import com.lcyl.common.enums.BusinessType;
import com.lcyl.common.utils.poi.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 老人请假申请Controller
 * 
 * @author cch
 * @date 2026-03-20
 */
@RestController
@RequestMapping("/code/leave")
public class ElderLeaveController extends BaseController
{
    @Autowired
    private IElderLeaveService elderLeaveService;

    /**
     * 查询老人请假申请列表
     */
    @PreAuthorize("@ss.hasPermi('code:leave:list')")
    @GetMapping("/list")
    public TableDataInfo list(ElderLeaveDto elderLeaveDto)
    {
        startPage();
        List<ElderLeaveVo> list = elderLeaveService.selectElderLeaveList(elderLeaveDto);
        return getDataTable(list);
    }

    /**
     * 导出老人请假申请列表
     */
    @PreAuthorize("@ss.hasPermi('code:leave:export')")
    @Log(title = "老人请假申请", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ElderLeaveDto elderLeaveDto)
    {
        List<ElderLeaveVo> list = elderLeaveService.selectElderLeaveList(elderLeaveDto);
        ExcelUtil<ElderLeaveVo> util = new ExcelUtil<ElderLeaveVo>(ElderLeaveVo.class);
        util.exportExcel(response, list, "老人请假申请数据");
    }

    /**
     * 获取老人请假申请详细信息
     */
    @PreAuthorize("@ss.hasPermi('code:leave:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(elderLeaveService.selectElderLeaveById(id));
    }

    /**
     * 查询请假审批记录
     */
    @PreAuthorize("@ss.hasPermi('code:leave:query')")
    @GetMapping("/records/{leaveId}")
    public AjaxResult getApproveRecords(@PathVariable("leaveId") Long leaveId) {
        return AjaxResult.success(elderLeaveService.selectApproveRecordList(leaveId));
    }

    /**
     * 新增老人请假申请
     */
    @PreAuthorize("@ss.hasPermi('code:leave:add')")
    @Log(title = "老人请假申请", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ElderLeave elderLeave)
    {
        return toAjax(elderLeaveService.insertElderLeave(elderLeave));
    }

    /**
     * 修改老人请假申请
     */
    @PreAuthorize("@ss.hasPermi('code:leave:edit')")
    @Log(title = "老人请假申请", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ElderLeave elderLeave)
    {
        return toAjax(elderLeaveService.updateElderLeave(elderLeave));
    }

    /**
     * 删除老人请假申请
     */
    @PreAuthorize("@ss.hasPermi('code:leave:remove')")
    @Log(title = "老人请假申请", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(elderLeaveService.deleteElderLeaveByIds(ids));
    }

    /*---------------------------------------------------------------------------------------------------------------*/

    /**
     * 提交老人请假信息（发起申请时手动填入的数据）
     * @param dto
     * @return
     */
    @PreAuthorize("@ss.hasPermi('code:leave:submit')")
    @PostMapping("/submit")
    public AjaxResult submit(@RequestBody @Validated ElderLeaveSubmitDto dto) {
        return toAjax(elderLeaveService.submitElderLeave(dto));
    }


    /**
     * 根据老人id查询老人基本信息
     * @param elderId
     * @return
     */
    @GetMapping("/formInfo/{elderId}")
    public AjaxResult getLeaveFormInfo(@PathVariable Long elderId) {
        return AjaxResult.success(elderLeaveService.getLeaveFormInfoByElderId(elderId));
    }


    /**
     * 查询当前登录人的请假审批待办列表
     * @return
     */
    @PreAuthorize("@ss.hasPermi('code:leave:approve')")
    @GetMapping("/todoList")
    public AjaxResult selectMyTodoList() {
        return AjaxResult.success(elderLeaveService.selectMyTodoList());
    }

    /**
     * 审批
     * @param dto
     * @return
     */
    @Log(title = "老人请假审批", businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasPermi('code:leave:approve')")
    @PostMapping("/approve")
    public AjaxResult approve(@RequestBody @Validated ElderLeaveApproveDto dto) {
        return toAjax(elderLeaveService.approveLeave(dto));
    }

    /**
     * 驳回后再次提交
     */
    @Log(title = "老人请假重新提交", businessType = BusinessType.UPDATE)
    @PreAuthorize("@ss.hasPermi('code:leave:resubmit')")
    @PostMapping("/resubmit")
    public AjaxResult resubmit(@RequestBody @Validated ElderLeaveResubmitDto dto) {
        return toAjax(elderLeaveService.resubmitLeave(dto));
    }

    /**
     * 查询老人的id和姓名
     *
     * @return
     */
    @GetMapping("/options")
    public AjaxResult getElderOptions() {
        return AjaxResult.success(elderLeaveService.selectElderOptions());
    }
}
