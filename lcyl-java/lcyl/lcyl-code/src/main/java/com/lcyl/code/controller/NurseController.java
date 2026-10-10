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
import com.lcyl.code.domain.Nurse;
import com.lcyl.code.service.INurseService;
import com.lcyl.common.utils.poi.ExcelUtil;
import com.lcyl.common.core.page.TableDataInfo;

/**
 * 护理员信息Controller
 * 
 * @author ruoyi
 * @date 2026-03-23
 */
@RestController
@RequestMapping("/nurse/nurse")
public class NurseController extends BaseController
{
    @Autowired
    private INurseService nurseService;

    /**
     * 查询护理员信息列表
     */
    @PreAuthorize("@ss.hasPermi('nurse:nurse:list')")
    @GetMapping("/list")
    public TableDataInfo list(Nurse nurse)
    {
        startPage();
        List<Nurse> list = nurseService.selectNurseList(nurse);
        return getDataTable(list);
    }

    /**
     * 导出护理员信息列表
     */
    @PreAuthorize("@ss.hasPermi('nurse:nurse:export')")
    @Log(title = "护理员信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Nurse nurse)
    {
        List<Nurse> list = nurseService.selectNurseList(nurse);
        ExcelUtil<Nurse> util = new ExcelUtil<Nurse>(Nurse.class);
        util.exportExcel(response, list, "护理员信息数据");
    }

    /**
     * 获取护理员信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('nurse:nurse:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(nurseService.selectNurseById(id));
    }

    /**
     * 新增护理员信息
     */
    @PreAuthorize("@ss.hasPermi('nurse:nurse:add')")
    @Log(title = "护理员信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Nurse nurse)
    {
        return toAjax(nurseService.insertNurse(nurse));
    }

    /**
     * 修改护理员信息
     */
    @PreAuthorize("@ss.hasPermi('nurse:nurse:edit')")
    @Log(title = "护理员信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Nurse nurse)
    {
        return toAjax(nurseService.updateNurse(nurse));
    }

    /**
     * 删除护理员信息
     */
    @PreAuthorize("@ss.hasPermi('nurse:nurse:remove')")
    @Log(title = "护理员信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(nurseService.deleteNurseByIds(ids));
    }
}
