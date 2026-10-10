package com.lcyl.code.controller;

import com.lcyl.code.domain.CheckIn;
import com.lcyl.code.domain.CheckInConfigg;
import com.lcyl.code.mapper.AssignNurseMapper;
import com.lcyl.code.mapper.CheckInConfiggMapper;
import com.lcyl.code.mapper.CheckInMapper;
import com.lcyl.common.annotation.Log;
import com.lcyl.common.core.controller.BaseController;
import com.lcyl.common.core.domain.AjaxResult;
import com.lcyl.common.core.page.TableDataInfo;
import com.lcyl.common.enums.BusinessType;
import com.lcyl.common.exception.ServiceException;
import com.lcyl.common.utils.poi.ExcelUtil;
import com.lcyl.system.domain.CheckInConfig;
import com.lcyl.system.domain.Contract;
import com.lcyl.system.domain.Elder;
import com.lcyl.system.mapper.ContractMapper;
import com.lcyl.system.service.IElderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 老人信息Controller
 * 
 * @author tyf
 * @date 2026-03-24
 */
@RestController
@RequestMapping("/system/elder")
public class ElderController extends BaseController
{
    @Autowired
    private IElderService elderService;



    /**
     * 查询老人信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:elder:list')")
    @GetMapping("/list")
    public TableDataInfo list(Elder elder)
    {
        startPage();
        List<Elder> list = elderService.selectElderList(elder);
        return getDataTable(list);
    }

    /**
     * 导出老人信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:elder:export')")
    @Log(title = "老人信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Elder elder)
    {
        List<Elder> list = elderService.selectElderList(elder);
        ExcelUtil<Elder> util = new ExcelUtil<Elder>(Elder.class);
        util.exportExcel(response, list, "老人信息数据");
    }

    /**
     * 获取老人信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:elder:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(elderService.selectElderById(id));
    }

    /**
     * 新增老人信息
     */
    @PreAuthorize("@ss.hasPermi('system:elder:add')")
    @Log(title = "老人信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Elder elder)
    {
        return toAjax(elderService.insertElder(elder));
    }

    /**
     * 修改老人信息
     */
    @PreAuthorize("@ss.hasPermi('system:elder:edit')")
    @Log(title = "老人信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Elder elder)
    {
        return toAjax(elderService.updateElder(elder));
    }

    /**
     * 删除老人信息
     */
    @PreAuthorize("@ss.hasPermi('system:elder:remove')")
    @Log(title = "老人信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(elderService.deleteElderByIds(ids));
    }

    @GetMapping("/getElderList")
    public AjaxResult getElderList() {
        List<Elder> elderList = elderService.selectAllElder();
        return success(elderList);
    }

//    @GetMapping("getElderDetail/{id}")
//    public AjaxResult getElderDetail(@PathVariable("id") Long id){
//
//    }


    @Autowired
    private CheckInConfiggMapper checkInConfigMapper;
    @Autowired
    private ContractMapper contractMapper;
    @Autowired
    private AssignNurseMapper assignNurseMapper;
    @Autowired
    private CheckInMapper checkInMapper;

    @GetMapping("/fullInfo/{id}")
    public AjaxResult getElderFullInfo(@PathVariable Long id) {
    // 1. 获取老人基本信息
    Elder elder = elderService.selectElderById(id);
    if (elder == null) {
        return error("老人不存在");
    }

    // 入住配置
        CheckInConfigg checkInConfig = checkInConfigMapper.selectCheckInConfigById(id);
        if (checkInConfig == null) {
            throw new ServiceException("未找到老人的入住配置信息，请先办理入住");
        }

        // 合同信息
        Contract contract = contractMapper.selectContractById(id);
        // 入住信息（顾问）
        CheckIn checkIn = checkInMapper.selectCheckInById(id);
        // 护理员列表
        List<String> assignNurse = assignNurseMapper.selectNurseByElderId(id);
        String string = checkInMapper.getlevelName(id);

        Map<String, Object> result = new HashMap<>();

    result.put("bedNo",  checkInConfig.getBedNo());
    result.put("contract", contract.getContractNo());
    result.put("counselor", checkIn.getCounselor());
    result.put("nursingLevel", string);
    result.put("nurseNames", assignNurse);
    return success(result);
    }
}
