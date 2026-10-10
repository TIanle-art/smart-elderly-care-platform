package com.lcyl.code.service.impl;

import com.lcyl.code.domain.ElderLeave;
import com.lcyl.code.domain.ElderLeaveApproveRecord;
import com.lcyl.code.dto.ElderLeaveApproveDto;
import com.lcyl.code.dto.ElderLeaveDto;
import com.lcyl.code.dto.ElderLeaveResubmitDto;
import com.lcyl.code.dto.ElderLeaveSubmitDto;
import com.lcyl.code.mapper.ElderLeaveApproveRecordMapper;
import com.lcyl.code.mapper.ElderLeaveMapper;
import com.lcyl.code.service.IElderLeaveService;
import com.lcyl.code.vo.ElderLeaveFormVo;
import com.lcyl.code.vo.ElderLeaveTodoVo;
import com.lcyl.code.vo.ElderLeaveVo;
import com.lcyl.code.vo.ElderOptionVo;
import com.lcyl.common.exception.ServiceException;
import com.lcyl.common.utils.DateUtils;
import com.lcyl.common.utils.SecurityUtils;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * 老人请假申请Service业务层处理
 *
 * @author cch
 * @date 2026-03-20
 */
@Service
public class ElderLeaveServiceImpl implements IElderLeaveService {
    private static final String PROCESS_DEFINITION_KEY = "Process_1"; //BPMN 中 process 的 id
    private static final String TASK_KEY_APPLY = "Activity_0esuoda"; //提交申请节点的唯一id
    private static final String TASK_KEY_SUPERVISOR = "Activity_13rgojy"; //主管审批节点的唯一id
    private static final String TASK_KEY_MINISTER = "Activity_023fe55"; //部长审批节点的唯一id

    @Autowired
    private ElderLeaveMapper elderLeaveMapper;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ElderLeaveApproveRecordMapper elderLeaveApproveRecordMapper;

    /**
     * 查询老人请假申请
     *
     * @param id 老人请假申请主键
     * @return 老人请假申请
     */
    @Override
    public ElderLeave selectElderLeaveById(Long id) {
        return elderLeaveMapper.selectElderLeaveById(id);
    }

    /**
     * 查询老人请假申请列表
     *
     * @param elderLeaveDto 老人请假申请
     * @return 老人请假申请
     */
    @Override
    public List<ElderLeaveVo> selectElderLeaveList(ElderLeaveDto elderLeaveDto) {
        return elderLeaveMapper.selectElderLeaveList(elderLeaveDto);
    }

    /**
     * 新增老人请假申请
     *
     * @param elderLeave 老人请假申请
     * @return 结果
     */
    @Override
    public int insertElderLeave(ElderLeave elderLeave) {
        elderLeave.setCreateTime(DateUtils.getNowDate());
        return elderLeaveMapper.insertElderLeave(elderLeave);
    }

    /**
     * 修改老人请假申请
     *
     * @param elderLeave 老人请假申请
     * @return 结果
     */
    @Override
    public int updateElderLeave(ElderLeave elderLeave) {
        elderLeave.setUpdateTime(DateUtils.getNowDate());
        return elderLeaveMapper.updateElderLeave(elderLeave);
    }

    /**
     * 批量删除老人请假申请
     *
     * @param ids 需要删除的老人请假申请主键
     * @return 结果
     */
    @Override
    public int deleteElderLeaveByIds(Long[] ids) {
        return elderLeaveMapper.deleteElderLeaveByIds(ids);
    }

    /**
     * 删除老人请假申请信息
     *
     * @param id 老人请假申请主键
     * @return 结果
     */
    @Override
    public int deleteElderLeaveById(Long id) {
        return elderLeaveMapper.deleteElderLeaveById(id);
    }

    /*------------------------------------------------------------------------------------------------*/
    // 生成单据编号
    private String generateOrderNo() {
        //生成规则：LEAVE+当前时间戳（毫秒级）
        return "LEAVE" + System.currentTimeMillis();
    }

    // 计算请假天数
    private Long calculateLeaveDays(Date leaveStartTime, Date plannedReturnTime) {
        long diff = plannedReturnTime.getTime() - leaveStartTime.getTime();
        if (diff < 0) {
            throw new RuntimeException("预计返回时间不能早于请假开始时间");
        }

        long oneDayMillis = 24 * 60 * 60 * 1000L;
        long days = diff / oneDayMillis;

        // 有余数就按 1 天算起
        if (diff % oneDayMillis != 0) {
            days++;
        }
        // 至少算 1 天
        if (days == 0) {
            days = 1;
        }
        return days;
    }

    // 从流程变量中获取某个“审批人/处理人”，如果取不到就用默认值
    private String resolveFlowAssignee(
            String executionId,  // 流程实例ID（或执行ID），用来定位是哪一个流程实例
            String variableName, // 变量名
            String fallback) // 兜底值（默认值）
    {
        // 如果 executionId 为空，直接返回默认值
        if (!StringUtils.hasText(executionId)) {
            return fallback;
        }
        try {
            // 从流程中取变量
            Object value = runtimeService.getVariable(executionId, variableName);
            // 判断变量是否有效
            if (value instanceof String && StringUtils.hasText((String) value)) {
                return (String) value;
            }
        } catch (Exception e) {
            // ignore
        }
        return fallback;
    }

    // 根据业务规则计算出应该由谁审批，并把任务分配给他（分配审批人）
    private void assignTaskAssignee(Task task, ElderLeave elderLeave) {
        // 空判断或者已分配直接返回
        if (task == null || StringUtils.hasText(task.getAssignee())) {
            return;
        }

        // 计算这个任务应该分配给谁
        String assignee = resolveTaskAssignee(task, elderLeave);

        // 分配任务
        if (StringUtils.hasText(assignee)) {
            taskService.setAssignee(task.getId(), assignee);
        }
    }

    // 根据当前任务节点（流程位置）和请假单数据，解析出该任务的办理人（assignee）
    private String resolveTaskAssignee(Task task, ElderLeave elderLeave) {
        // 没有任务或业务数据，无法决策，直接返回 null
        if (task == null || elderLeave == null) {
            return null;
        }
        String assignee = null;
        // 按“节点”分支决策
        if (TASK_KEY_APPLY.equals(task.getTaskDefinitionKey()) || "护理员发起申请".equals(task.getName())) {
            assignee = elderLeave.getApplyUserName();
        } else if (TASK_KEY_SUPERVISOR.equals(task.getTaskDefinitionKey()) || "护理组主管审批".equals(task.getName())) {
            assignee = resolveFlowAssignee(task.getProcessInstanceId(), "zhuguan", "zhuguan");
        } else if (TASK_KEY_MINISTER.equals(task.getTaskDefinitionKey()) || "护理部部长审批".equals(task.getName())) {
            assignee = resolveFlowAssignee(task.getProcessInstanceId(), "buzhang", "buzhang");
        }
        return assignee;
    }

    // 当前这张请假单，是否需要走“部长审批”这一层
    private boolean isMinisterApprovalRequired(ElderLeave elderLeave) {
        if (elderLeave == null) {
            return false;
        }
        // 请假天数
        Long leaveDays = elderLeave.getLeaveDays();
        // 护理等级
        String nursingLevel = elderLeave.getNursingLevel();
        return (leaveDays != null && leaveDays >= 3L) || "特级护理等级".equals(nursingLevel);
    }

    //判断是不是部长审批
    private boolean isMinisterTask(Task task) {
        if (task == null) {
            return false;
        }
        if (TASK_KEY_MINISTER.equals(task.getTaskDefinitionKey()) || "护理部部长审批".equals(task.getName())) {
            return true;
        }
        String name = task.getName();
        return StringUtils.hasText(name) && name.contains("部长");
    }

    //判断是不是主管审批
    private boolean isSupervisorTask(Task task) {
        if (task == null) {
            return false;
        }
        if (TASK_KEY_SUPERVISOR.equals(task.getTaskDefinitionKey()) || "护理组主管审批".equals(task.getName())) {
            return true;
        }
        String name = task.getName();
        return StringUtils.hasText(name) && name.contains("主管") && !name.contains("部长");
    }





    /**
     * 查询所有的老人和id
     * @return List<ElderOptionVo>
     */
    @Override
    public List<ElderOptionVo> selectElderOptions() {
        return elderLeaveMapper.selectElderOptions();
    }


    /**
     * 根据老人id查询老人请假表单基本信息（根据老人id关联老人表、床位表、登记表查询）
     * @param elderId
     * @return ElderLeaveFormVo
     */
    @Override
    public ElderLeaveFormVo getLeaveFormInfoByElderId(Long elderId) {
        ElderLeaveFormVo formVo = elderLeaveMapper.selectLeaveFormInfoByElderId(elderId);
        if (formVo == null) {
            throw new ServiceException("未查询到老人信息");
        }
        return formVo;
    }

    /**
     * 提交老人请假信息
     * 主要负责查询老人基本信息并插入请假主表，然后启动流程实例，最后回写流程信息，更新主表
     * @param dto
     * @return int
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int submitElderLeave(ElderLeaveSubmitDto dto) {
        ElderLeaveFormVo formVo = getLeaveFormInfoByElderId(dto.getElderId());

        // 校验时间
        if (dto.getLeaveStartTime() == null || dto.getPlannedReturnTime() == null) {
            throw new RuntimeException("请假开始时间和预计返回时间不能为空");
        }
        if (dto.getPlannedReturnTime().before(dto.getLeaveStartTime())) {
            throw new RuntimeException("预计返回时间不能早于请假开始时间");
        }

        // 计算请假天数（调用上面封装的计算天数的方法）
        Long leaveDays = calculateLeaveDays(dto.getLeaveStartTime(), dto.getPlannedReturnTime());

        // 4. 组装请假主表实体
        ElderLeave elderLeave = new ElderLeave();
        //按照规则生成单据编号
        elderLeave.setOrderNo(generateOrderNo());

        elderLeave.setElderId(formVo.getElderId());
        elderLeave.setElderName(formVo.getElderName());
        elderLeave.setElderIdCard(formVo.getElderIdCard());
        elderLeave.setPhone(formVo.getPhone());
        elderLeave.setNursingLevel(formVo.getNursingLevel());
        elderLeave.setBedNo(formVo.getBedNo());
        elderLeave.setNurseNames(formVo.getNurseNames());

        elderLeave.setCompanionType(dto.getCompanionType());
        elderLeave.setCompanionName(dto.getCompanionName());
        elderLeave.setCompanionPhone(dto.getCompanionPhone());
        elderLeave.setLeaveStartTime(dto.getLeaveStartTime());
        elderLeave.setPlannedReturnTime(dto.getPlannedReturnTime());
        elderLeave.setLeaveDays(leaveDays);
        elderLeave.setLeaveReason(dto.getLeaveReason());

        elderLeave.setStatus("approving");
        elderLeave.setIsReturned(0);
        elderLeave.setApplyTime(new Date());
        elderLeave.setDelFlag("0");

        //获取登录的用户的相关信息
        elderLeave.setApplyUserId(SecurityUtils.getUserId());
        elderLeave.setApplyUserName(SecurityUtils.getUsername());
        elderLeave.setCreateBy(SecurityUtils.getUsername());
        elderLeave.setCreateTime(new Date());

        // 插入主表
        int rows = elderLeaveMapper.insertElderLeave(elderLeave);
        if (rows <= 0) {
            throw new RuntimeException("提交请假失败");
        }

        // 构造流程变量
        Map<String, Object> variables = new HashMap<>();
        variables.put("leaveDays", elderLeave.getLeaveDays());
        variables.put("nursingLevel", elderLeave.getNursingLevel());

        // 当前申请人（护理员）
        variables.put("huliyuan", SecurityUtils.getUsername());

        // 写死了
        variables.put("zhuguan", "zhuguan");
        variables.put("buzhang", "buzhang");

        // 绑定业务主键
        variables.put("businessId", elderLeave.getId());
        variables.put("orderNo", elderLeave.getOrderNo());

        // 启动流程实例
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(
                PROCESS_DEFINITION_KEY, //流程定义的key
                String.valueOf(elderLeave.getId()), //业务key，关联请假主表
                variables);
        if (processInstance == null) {
            throw new RuntimeException("启动请假流程失败");
        }

        // 查询任务
        Task task = taskService.createTaskQuery()
                .processInstanceId(processInstance.getProcessInstanceId()) //根据实例流程id来查询任务
                .singleResult();

        // 完成任务
        if (task != null) {
            Map<String, Object> completeVars = new HashMap<>();
            completeVars.put("leaveDays", elderLeave.getLeaveDays());
            completeVars.put("nursingLevel", elderLeave.getNursingLevel());
            taskService.complete(task.getId(), completeVars);
        }

        // 再查询当前最新任务（应该进入主管审批）
        Task currentTask = taskService.createTaskQuery()
                .processInstanceId(processInstance.getProcessInstanceId())
                .singleResult();

        //为当前任务分配护理人
        assignTaskAssignee(currentTask, elderLeave);

        // 回写流程实例ID、当前任务信息
        elderLeave.setProcessInstanceId(processInstance.getProcessInstanceId());

        // 如果当前查询到的任务不为空（流程正在运行、有审批节点）
        if (currentTask != null) {
            // 保存：当前任务的唯一标识
            elderLeave.setCurrentTaskKey(currentTask.getTaskDefinitionKey());
            // 保存：当前任务的名称
            elderLeave.setCurrentTaskName(currentTask.getName());
        }
        // 如果任务为空（流程已结束 / 异常 / 已完成）
        else {
            // 清空任务key
            elderLeave.setCurrentTaskKey(null);
            // 清空任务名称
            elderLeave.setCurrentTaskName(null);
        }

        elderLeave.setUpdateBy(SecurityUtils.getUsername());
        elderLeave.setUpdateTime(new Date());

        // 更新主表
        elderLeaveMapper.updateElderLeave(elderLeave);

        return 1;
    }

    /**
     * 按实例流程ID查请假单
     * 作用：查询“当前登录用户”的所有待办审批任务（请假审批列表）
     * @return List<ElderLeaveTodoVo>
     */
    @Override
    public List<ElderLeaveTodoVo> selectMyTodoList() {
        String username = SecurityUtils.getUsername();

        // 查询当前登录人的待办任务

        // 查询已经分配给个人的任务
        List<Task> taskList = taskService.createTaskQuery()
                .taskAssignee(username)
                .orderByTaskCreateTime()
                .desc()
                .list();

        // 查询分配给角色的任务
       /* List<Task> unassignedTasks = taskService.createTaskQuery()
                .taskCandidateOrAssigned(username)
                .active()
                .orderByTaskCreateTime()
                .desc()
                .list();

        // 将两种方式查询到的任务合并并去重
        Set<String> taskIds = new HashSet<>();
        for (Task task : taskList) {
            taskIds.add(task.getId());
        }

        // 判断有没有候选任务
        if (unassignedTasks != null && !unassignedTasks.isEmpty()) {
            for (Task task : unassignedTasks) {
                //先去重，避免重复加入
                if (taskIds.contains(task.getId())) {
                    continue; // 已存在则跳过
                }

                // 优先通过流程实例ID查业务单据
                ElderLeave elderLeave = null;
                try {
                    elderLeave = elderLeaveMapper.selectByProcessInstanceId(task.getProcessInstanceId());
                } catch (Exception e) {
                    // ignore
                }

                // 如果查不到，尝试通过 businessKey (leaveId) 查
                if (elderLeave == null && StringUtils.hasText(task.getBusinessKey())) {
                    try {
                        Long leaveId = Long.valueOf(task.getBusinessKey());
                        elderLeave = elderLeaveMapper.selectElderLeaveById(leaveId);
                    } catch (Exception e) {
                        // ignore
                    }
                }

                if (elderLeave == null) {
                    continue;
                }

                // 如果任务没有办理人，尝试自动分配
                if (!StringUtils.hasText(task.getAssignee())) {
                    String expectedAssignee = null;
                    try {
                        expectedAssignee = resolveTaskAssignee(task, elderLeave);
                    } catch (Exception e) {
                        // ignore
                    }
                    // 判断当前用户是否有资格接这个任务
                    if (!username.equals(expectedAssignee) && !username.equals(elderLeave.getApplyUserName())) {
                        continue;
                    }
                    // 把任务正式分配给当前用户
                    taskService.setAssignee(task.getId(), username);
                }

                // 重新查一次任务对象，再加入待办列表
                try {
                    Task assignedTask = taskService.createTaskQuery()
                            .taskId(task.getId())
                            .singleResult();
                    if (assignedTask != null) {
                        taskList.add(assignedTask);
                        taskIds.add(assignedTask.getId());
                    }
                } catch (Exception e) {
                    // ignore
                }
            }
        }

        // 最后再次查询真正的未分配任务（防止候选组等配置导致上面的查询漏掉）
        List<Task> pureUnassignedTasks = taskService.createTaskQuery()
                .taskUnassigned()
                .active()
                .orderByTaskCreateTime()
                .desc()
                .list();

        if (pureUnassignedTasks != null && !pureUnassignedTasks.isEmpty()) {
            for (Task task : pureUnassignedTasks) {
                if (taskIds.contains(task.getId())) {
                    continue;
                }

                ElderLeave elderLeave = null;
                try {
                    elderLeave = elderLeaveMapper.selectByProcessInstanceId(task.getProcessInstanceId());
                } catch (Exception e) {
                    // System.err.println("pureUnassignedTasks: 查询单据报错: " + e.getMessage());
                }
                if (elderLeave == null && StringUtils.hasText(task.getBusinessKey())) {
                    try {
                        Long leaveId = Long.valueOf(task.getBusinessKey());
                        elderLeave = elderLeaveMapper.selectElderLeaveById(leaveId);
                    } catch (Exception e) {
                        // ignore
                    }
                }

                if (elderLeave == null) continue;

                // 为了防止在解析流程变量时报错，增加安全防护
                String expectedAssignee = null;
                try {
                    expectedAssignee = resolveTaskAssignee(task, elderLeave);
                } catch (Exception e) {
                    // ignore
                }

                if (username.equals(expectedAssignee)) {
                    taskService.setAssignee(task.getId(), username);
                    try {
                        Task assignedTask = taskService.createTaskQuery().taskId(task.getId()).singleResult();
                        if (assignedTask != null) {
                            taskList.add(assignedTask);
                            taskIds.add(assignedTask.getId());
                        }
                    } catch (Exception e) {
                        // ignore
                    }
                }
            }
        }*/

        List<ElderLeaveTodoVo> result = new ArrayList<>();

        if (taskList == null || taskList.isEmpty()) {
            return result;
        }

        // 遍历任务，查询对应业务数据
        for (Task task : taskList) {
            ElderLeave elderLeave = null;

            // 避免触发数据权限拦截器报错，用一种更安全的查询方式或捕获异常
            try {
                // 优先通过流程实例ID查业务单据
                elderLeave = elderLeaveMapper.selectByProcessInstanceId(task.getProcessInstanceId());
            } catch (Exception e) {
                // 如果出现数据权限拦截器异常，我们退而求其次
            }

            // 如果查不到，尝试通过 businessKey (leaveId) 查
            if (elderLeave == null && StringUtils.hasText(task.getBusinessKey())) {
                try {
                    Long leaveId = Long.valueOf(task.getBusinessKey());
                    elderLeave = elderLeaveMapper.selectElderLeaveById(leaveId);
                } catch (Exception e) {
                }
            }

            // 如果都查不到，可能该任务不是请假流程的任务
            if (elderLeave == null) {
                continue;
            }

            ElderLeaveTodoVo vo = new ElderLeaveTodoVo();
            vo.setTaskId(task.getId());
            vo.setProcessInstanceId(task.getProcessInstanceId());
            vo.setTaskDefinitionKey(task.getTaskDefinitionKey());
            vo.setTaskName(task.getName());

            vo.setLeaveId(elderLeave.getId());
            vo.setOrderNo(elderLeave.getOrderNo());
            vo.setElderName(elderLeave.getElderName());
            vo.setLeaveStartTime(elderLeave.getLeaveStartTime());
            vo.setPlannedReturnTime(elderLeave.getPlannedReturnTime());
            vo.setLeaveDays(elderLeave.getLeaveDays());
            vo.setLeaveReason(elderLeave.getLeaveReason());
            vo.setApplyTime(elderLeave.getApplyTime());
            vo.setStatus(elderLeave.getStatus());

            result.add(vo);
        }
        return result;
    }



    /**
     * 审批
     * @param dto
     * @return int
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int approveLeave(ElderLeaveApproveDto dto) {

        // 校验审批结果
        String approveResult = dto.getApproveResult();

        if (!"approved".equals(approveResult)&& !"rejected".equals(approveResult)&& !"back".equals(approveResult)) {
            throw new RuntimeException("审批结果不合法，只能是 approved / rejected / back");
        }

        // 查询当前任务
        Task task = taskService.createTaskQuery()
                .taskId(dto.getTaskId())
                .singleResult();

        if (task == null) {
            throw new RuntimeException("待办任务不存在或已处理");
        }

        // 查询请假单
        ElderLeave elderLeave = elderLeaveMapper.selectElderLeaveById(dto.getLeaveId());
        if (elderLeave == null) {
            throw new RuntimeException("请假单不存在");
        }

        // 校验任务和业务数据是否匹配
        if (elderLeave.getProcessInstanceId() == null
                || !elderLeave.getProcessInstanceId().equals(task.getProcessInstanceId())) {
            throw new RuntimeException("任务与请假单流程实例不匹配");
        }

        // 写审批记录
        ElderLeaveApproveRecord record = new ElderLeaveApproveRecord();
        record.setLeaveId(elderLeave.getId());
        record.setProcessInstanceId(task.getProcessInstanceId());
        record.setTaskId(task.getId());
        record.setNodeKey(task.getTaskDefinitionKey());
        record.setNodeName(task.getName());
        record.setApproveUserId(SecurityUtils.getUserId());
        record.setApproveUserName(SecurityUtils.getUsername());

        String roleName = "审批人";
        if ("护理组主管审批".equals(task.getName())) {
            roleName = "护理组主管";
        } else if ("护理部部长审批".equals(task.getName())) {
            roleName = "护理部部长";
        }
        record.setApproveRoleName(roleName);

        record.setApproveResult(approveResult);
        record.setApproveOpinion(dto.getApproveOpinion());
        record.setSortNo(1L);
        record.setApproveTime(new Date());
        record.setCreateTime(new Date());

        elderLeaveApproveRecordMapper.insertElderLeaveApproveRecord(record);

        boolean needMinisterAfterSupervisorApprove =
                isSupervisorTask(task)
                        && "approved".equals(approveResult)
                        && isMinisterApprovalRequired(elderLeave);

        // 设置流程变量
        Map<String, Object> variables = new HashMap<>();
        variables.put("approveResult", approveResult);
        if (isSupervisorTask(task)) {
            variables.put("supervisorResult", approveResult);
        } else if (isMinisterTask(task)) {
            variables.put("ministerResult", approveResult);
        }
        variables.put("leaveDays", elderLeave.getLeaveDays());
        variables.put("nursingLevel", elderLeave.getNursingLevel());

        variables.put("zhuguan", resolveFlowAssignee(task.getProcessInstanceId(), "zhuguan", "zhuguan"));
        variables.put("buzhang", resolveFlowAssignee(task.getProcessInstanceId(), "buzhang", "buzhang"));

//        runtimeService.setVariables(task.getExecutionId(), variables);
        runtimeService.setVariables(task.getProcessInstanceId(), variables);
        taskService.setVariables(task.getId(), variables);

        // 完成当前任务
        taskService.complete(task.getId(), variables);

        // 查询流程当前最新任务
        List<Task> currentTasks = taskService.createTaskQuery()
                .processInstanceId(task.getProcessInstanceId())
                .active()
                .list();
        Task currentTask = null;
        if (currentTasks != null && !currentTasks.isEmpty()) {
            for (Task current : currentTasks) {
                assignTaskAssignee(current, elderLeave);
                if (currentTask == null || isMinisterTask(current)) {
                    currentTask = current;
                }
            }
        }

        if (needMinisterAfterSupervisorApprove && (currentTasks == null || currentTasks.stream().noneMatch(this::isMinisterTask))) {
            List<Task> refreshedTasks = taskService.createTaskQuery()
                    .processInstanceId(task.getProcessInstanceId())
                    .active()
                    .list();
            if (refreshedTasks != null && !refreshedTasks.isEmpty()) {
//                currentTasks = refreshedTasks;
                currentTask = null;
                for (Task refreshed : refreshedTasks) {
                    assignTaskAssignee(refreshed, elderLeave);
                    if (currentTask == null || isMinisterTask(refreshed)) {
                        currentTask = refreshed;
                    }
                }
            }
        }

        // 回写请假主表状态和任务信息
        if (currentTask != null) {
            // 流程未结束，记录当前任务信息
            elderLeave.setCurrentTaskKey(currentTask.getTaskDefinitionKey());
            elderLeave.setCurrentTaskName(currentTask.getName());
            // 确保流程实例ID同步
            elderLeave.setProcessInstanceId(currentTask.getProcessInstanceId());
        } else {
            // 流程可能处于网关或已结束，清空当前任务信息
            elderLeave.setCurrentTaskKey(null);
            elderLeave.setCurrentTaskName(null);
        }

        // 根据审批结果更新业务状态（不论当前任务是否查询到）
        if ("back".equals(approveResult)) {
            elderLeave.setStatus("back");
        } else if ("approved".equals(approveResult)) {
            if (currentTask == null) {
                if (needMinisterAfterSupervisorApprove) {
                    elderLeave.setStatus("approving");
                    elderLeave.setCurrentTaskKey(TASK_KEY_MINISTER);
                    elderLeave.setCurrentTaskName("护理部部长审批");
                    elderLeave.setProcessInstanceId(task.getProcessInstanceId());
                } else {
                    elderLeave.setStatus("approved");
                }
            } else {
                elderLeave.setStatus("approving");
            }
        } else if ("rejected".equals(approveResult)) {
            elderLeave.setStatus("rejected");
        }

        elderLeave.setUpdateBy(SecurityUtils.getUsername());
        elderLeave.setUpdateTime(new Date());

        return elderLeaveMapper.updateProcessInfo(elderLeave);
    }


    /**
     * 驳回后再次提交
     * @param dto
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int resubmitLeave(ElderLeaveResubmitDto dto) {

        // 查询请假单
        ElderLeave elderLeave = elderLeaveMapper.selectElderLeaveById(dto.getLeaveId());

        if (elderLeave == null) {
            throw new RuntimeException("请假单不存在");
        }

        // 校验当前状态 （back 驳回）
        if (!"back".equals(elderLeave.getStatus())) {
            throw new RuntimeException("当前请假单不是驳回状态，不能重新提交");
        }

        // 查询当前任务
        Task task;
        if (StringUtils.hasText(dto.getTaskId())) {  //等价于 str != null && !str.trim().isEmpty()
            task = taskService.createTaskQuery()
                    .taskId(dto.getTaskId())
                    .singleResult();
        } else {
            // 自己去查
            List<Task> taskList = taskService.createTaskQuery()
                    .processInstanceId(elderLeave.getProcessInstanceId()) // 根据这个请假单的流程ID
                    .active() // 只查未完成的任务
                    .orderByTaskCreateTime() //根据创建时间排序
                    .desc() //降序
                    .list();

            task = taskList.stream()
                    .filter(item -> TASK_KEY_APPLY.equals(item.getTaskDefinitionKey()) || "护理员发起申请".equals(item.getName()))
                    .findFirst()
                    .orElse(taskList.stream().findFirst().orElse(null));
        }

        if (task == null) {
            throw new RuntimeException("未找到可重新提交任务，可能已被处理或流程已结束");
        }

        // 校验任务归属
        if (!elderLeave.getProcessInstanceId().equals(task.getProcessInstanceId())) {
            throw new RuntimeException("任务与请假单流程实例不匹配");
        }

        // 校验时间
        if (dto.getLeaveStartTime() == null || dto.getPlannedReturnTime() == null) {
            throw new RuntimeException("请假开始时间和预计返回时间不能为空");
        }
        if (dto.getPlannedReturnTime().before(dto.getLeaveStartTime())) {
            throw new RuntimeException("预计返回时间不能早于请假开始时间");
        }

        // 重新计算请假天数
        Long leaveDays = calculateLeaveDays(dto.getLeaveStartTime(), dto.getPlannedReturnTime());

        // 更新业务数据：先落库业务字段，再推进流程，避免流程推进后业务数据仍是旧值
        elderLeave.setCompanionType(dto.getCompanionType());
        elderLeave.setCompanionName(dto.getCompanionName());
        elderLeave.setCompanionPhone(dto.getCompanionPhone());
        elderLeave.setLeaveStartTime(dto.getLeaveStartTime());
        elderLeave.setPlannedReturnTime(dto.getPlannedReturnTime());
        elderLeave.setLeaveDays(leaveDays);
        elderLeave.setLeaveReason(dto.getLeaveReason());

        elderLeave.setStatus("approving");
        elderLeave.setUpdateBy(SecurityUtils.getUsername());
        elderLeave.setUpdateTime(new Date());

        elderLeaveMapper.updateElderLeave(elderLeave);

        // 重新提交时，设置变量到流程实例，确保变量可见性
        Map<String, Object> variables = new HashMap<>();
        variables.put("leaveDays", elderLeave.getLeaveDays());
        variables.put("nursingLevel", elderLeave.getNursingLevel());

        // 写死
        variables.put("zhuguan", "zhuguan");
        variables.put("buzhang", "buzhang");

        runtimeService.setVariables(
                task.getProcessInstanceId(), // 流程实例id
                variables); // 要存的封装的信息，也就是需要传给审批节点的参数

        // 完成护理员发起申请任务
        taskService.complete(task.getId(), variables);

        // 查询下一个任务（应回到主管审批）
        List<Task> currentTasks = taskService.createTaskQuery()
                .processInstanceId(task.getProcessInstanceId())
                .active()
                .list();

        // 如果查到了 更新最新的信息到数据库
        if (currentTasks != null && !currentTasks.isEmpty()) {
            for (Task currentTask : currentTasks) {
                assignTaskAssignee(currentTask, elderLeave);
                // 回写最新任务信息，确保页面“当前节点”与流程引擎一致
                elderLeave.setCurrentTaskKey(currentTask.getTaskDefinitionKey());
                elderLeave.setCurrentTaskName(currentTask.getName());
                // 确保流程实例ID同步（防止某些情况下ID变化）
                elderLeave.setProcessInstanceId(currentTask.getProcessInstanceId());
            }
        } else {
            // 如果暂时查不到任务，不清空原节点信息；状态已在前面设置为 approving
        }

        elderLeave.setUpdateBy(SecurityUtils.getUsername());
        elderLeave.setUpdateTime(new Date());

        // 更新数据库
        return elderLeaveMapper.updateProcessInfo(elderLeave);
    }


    /**
     * 根据请假单 ID，查询这条请假单的所有审批记录，并且按时间顺序排好后返回
     * @param leaveId 请假单id
     * @return
     */
    @Override
    public List<ElderLeaveApproveRecord> selectApproveRecordList(Long leaveId) {
        // 构造查询条件
        ElderLeaveApproveRecord query = new ElderLeaveApproveRecord();
        query.setLeaveId(leaveId);

        List<ElderLeaveApproveRecord> list = elderLeaveApproveRecordMapper.selectElderLeaveApproveRecordList(query);

        if (list == null || list.isEmpty()) {
            return Collections.emptyList();
        }

        // 排序
        list.sort(Comparator
                .comparing(ElderLeaveApproveRecord::getApproveTime, Comparator.nullsLast(Date::compareTo)) // 根据审批时间
                .thenComparing(ElderLeaveApproveRecord::getCreateTime, Comparator.nullsLast(Date::compareTo)) //按照创建时间
                .thenComparing(ElderLeaveApproveRecord::getId, Comparator.nullsLast(Long::compareTo))); //根据记录ID
        return list;
    }

}