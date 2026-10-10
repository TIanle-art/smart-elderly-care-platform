package com.lcyl.code.service.impl;

import com.lcyl.code.domain.vo.MyApplyVo;
import com.lcyl.code.service.IMyApplyService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MyApplyServiceImpl implements IMyApplyService {

    @Autowired
    private TaskService taskService;

    @Override
    public List<MyApplyVo> getMyTodoList(Long userId, String userName, String nickName) {
        List<MyApplyVo> result = new ArrayList<>();

        // ==============================
        // 1. 退住待办：查询个人任务 + 候选组任务
        // ==============================
        List<Task> retreatTasks = new ArrayList<>();

        // 1.1 查询直接分配给当前用户的任务
        List<Task> assigneeTasks = taskService.createTaskQuery()
                .taskAssignee(userName)
                .active()
                .processDefinitionKey("RetreatProcess")
                .list();
        retreatTasks.addAll(assigneeTasks);

        // 1.2 查询当前用户所在的候选组任务
        List<Task> candidateTasks = taskService.createTaskQuery()
                .taskCandidateUser(userName)
                .active()
                .processDefinitionKey("RetreatProcess")
                .list();
        retreatTasks.addAll(candidateTasks);

        // 1.3 去重
        retreatTasks = retreatTasks.stream()
                .distinct()
                .collect(Collectors.toList());

        // ==============================
        // 2. 入住待办：按 nickName 查询
        // ==============================
        List<Task> checkInTasks = taskService.createTaskQuery()
                .taskAssignee(nickName)
                .active()
                .processDefinitionKey("checkin")
                .list();

        // ==============================
        // 3. 请假待办：按 userName 查询
        // ==============================
        List<Task> leaveTasks = taskService.createTaskQuery()
                .taskAssignee(userName)
                .active()
                .processDefinitionKey("Process_1")
                .list();

        // 合并所有任务
        List<Task> allTasks = new ArrayList<>();
        allTasks.addAll(retreatTasks);
        allTasks.addAll(checkInTasks);
        allTasks.addAll(leaveTasks);

        // 封装成 VO
        for (Task task : allTasks) {
            MyApplyVo vo = new MyApplyVo();
            vo.setTaskId(task.getId());
            vo.setTaskName(task.getName());
            vo.setApplyTime(task.getCreateTime());
            vo.setFlowStatus("待处理");
            vo.setBusinessId(task.getBusinessKey());

            if (checkInTasks.contains(task)) {
                vo.setCategory("入住");
            } else if (retreatTasks.contains(task)) {
                vo.setCategory("退住");
            } else if (leaveTasks.contains(task)) {
                vo.setCategory("请假");
            }
            result.add(vo);
        }

        // 时间倒序
        result.sort((a, b) -> b.getApplyTime().compareTo(a.getApplyTime()));
        return result;
    }
}