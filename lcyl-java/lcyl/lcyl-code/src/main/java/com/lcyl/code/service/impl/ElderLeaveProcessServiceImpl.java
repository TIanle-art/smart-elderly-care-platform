package com.lcyl.code.service.impl;

import com.lcyl.code.service.IElderLeaveProcessService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 部署请假流程
 */
@Service
public class ElderLeaveProcessServiceImpl implements IElderLeaveProcessService {

    @Autowired
    private RepositoryService repositoryService;

    @Override
    public String deployProcess() {
        Deployment deployment = repositoryService.createDeployment()
                .name("请假流程")
                .addClasspathResource("processes/qingjia.bpmn")
                .deploy();

        ProcessDefinition latest = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey("Process_1")
                .latestVersion()
                .singleResult();

        String definitionInfo = latest == null
                ? "未查询到流程定义"
                : ("流程定义ID：" + latest.getId() + "，版本：" + latest.getVersion() + "，资源：" + latest.getResourceName());

        return "部署成功，部署ID：" + deployment.getId() + "，部署名称：" + deployment.getName() + "；" + definitionInfo;
    }
}
