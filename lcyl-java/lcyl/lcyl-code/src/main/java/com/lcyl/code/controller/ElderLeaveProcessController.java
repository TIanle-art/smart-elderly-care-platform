package com.lcyl.code.controller;

import com.lcyl.code.service.IElderLeaveProcessService;
import com.lcyl.common.core.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 部署请假流程
 */
@RestController
@RequestMapping("/elderLeave/process")
public class ElderLeaveProcessController {

    @Autowired
    private IElderLeaveProcessService elderLeaveProcessService;

    @GetMapping("/deploy")
    public AjaxResult deployProcess() {
        String result = elderLeaveProcessService.deployProcess();
        return AjaxResult.success(result);
    }

}
