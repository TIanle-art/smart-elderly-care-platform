package com.lcyl.code.config;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class ActivitiDeployer {

    @Autowired
    private RepositoryService repositoryService;

    private static final String PROCESS_KEY = "RetreatProcess";
    private static final String DEPLOYMENT_NAME = "退住流程";

    @PostConstruct
    public void deploy() {
        // 检查流程是否已部署
        DeploymentQuery query = repositoryService.createDeploymentQuery()
                .deploymentName(DEPLOYMENT_NAME);
        long count = query.count();
        if (count == 0) {
            Deployment deployment = repositoryService.createDeployment()
                    .addClasspathResource("bpmn/retreat.bpmn")
                    .name(DEPLOYMENT_NAME)
                    .deploy();
            System.out.println("流程部署成功，部署ID：" + deployment.getId());
        } else {
            System.out.println("流程已存在，无需重复部署");
        }
    }
}