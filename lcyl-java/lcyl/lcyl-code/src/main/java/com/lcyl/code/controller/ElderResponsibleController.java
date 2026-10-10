package com.lcyl.code.controller;

import com.lcyl.code.domain.ElderResponsible;
import com.lcyl.code.domain.dto.AssignNurseDto;
import com.lcyl.code.service.IElderResponsibleService;
import com.lcyl.common.core.controller.BaseController;
import com.lcyl.common.core.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName ElderResponsibleController
 * @Description TODO
 * @Author hfy
 * @Date 2026-03-25 10:58
 * @Version 1.0
 */
@RestController
@RequestMapping("/elder/responsible")
public class ElderResponsibleController extends BaseController {
    @Autowired
    private IElderResponsibleService elderResponsibleService;
    
    @GetMapping("/myList")
    public AjaxResult myList() {
        List<ElderResponsible> list=elderResponsibleService.selectMyElderList();
        return success(list);
    }

    @PostMapping("/assign")
    public AjaxResult assign(@RequestBody AssignNurseDto dto) {
        elderResponsibleService.assignNurse(dto.getElderId(), dto.getNurseIds());
        return AjaxResult.success("设置成功");
    }

    @PostMapping("/batchAssign")
    public AjaxResult batchAssign(@RequestBody AssignNurseDto dto) {
        elderResponsibleService.batchAssignNurse(dto.getRoomId(), dto.getNurseIds());
        return AjaxResult.success("批量设置成功");
    }
}
