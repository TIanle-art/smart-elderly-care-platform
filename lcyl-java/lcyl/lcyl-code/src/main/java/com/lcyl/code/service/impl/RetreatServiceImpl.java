package com.lcyl.code.service.impl;

import com.alibaba.fastjson2.JSON;
import com.lcyl.code.constant.RetreatConstants;
import com.lcyl.code.domain.*;
import com.lcyl.code.domain.dto.*;
import com.lcyl.code.mapper.*;
import com.lcyl.code.service.IRetreatService;
import com.lcyl.code.service.RetreatFinishService;
import com.lcyl.common.core.domain.entity.SysRole;
import com.lcyl.common.core.domain.model.LoginUser;
import com.lcyl.common.exception.ServiceException;
import com.lcyl.common.utils.DateUtils;
import com.lcyl.common.utils.SecurityUtils;
import com.lcyl.common.utils.StringUtils;
import com.lcyl.system.domain.CheckInConfig;
import com.lcyl.system.domain.Contract;
import com.lcyl.system.domain.Elder;
import com.lcyl.system.domain.NursingLevel;
import com.lcyl.system.mapper.ContractMapper;
import com.lcyl.system.mapper.ElderMapper;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.apache.poi.ss.formula.functions.Now;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 退住申请服务实现类
 * 负责：退住申请CRUD、Activiti工作流启动、任务审批、任务待办、流程历史、业务数据快照
 *
 * @author lcyl
 * @date 2026-03-31
 */
@Slf4j
@Service
public class RetreatServiceImpl implements IRetreatService {

    @Autowired
    private RetreatMapper retreatMapper;
    @Autowired
    private LcRetreatBillMapper billMapper;
    @Autowired
    private LcRetreatContractMapper retreatContractMapper;
    @Autowired
    private LcRetreatHistoryMapper historyMapper;


    @Autowired
    private ElderMapper elderMapper;
    @Autowired
    private CheckInConfiggMapper checkInConfigMapper;
    @Autowired
    private ContractMapper contractMapper;
    @Autowired
    private AssignNurseMapper assignNurseMapper;
    @Autowired
    private CheckInMapper checkInMapper;


    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private RepositoryService repositoryService;


    @Autowired
    private RetreatFinishService retreatFinishService;


    @Override
    public List<Retreat> selectRetreatList(Retreat retreat) {
        return retreatMapper.selectRetreatList(retreat);
    }

    @Override
    public Retreat selectRetreatById(Long id) {
        Retreat retreat = retreatMapper.selectRetreatById(id);
        String applicatName = retreatMapper.selectApplicatName(retreat.getApplicatId());
        retreat.setApplicatName(applicatName);
        return retreat;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertRetreat(Retreat retreat) {
        retreat.setCreateTime(DateUtils.getNowDate());
        retreat.setCreateBy(SecurityUtils.getUsername());

        // 自动生成退住编号
        if (StringUtils.isBlank(retreat.getRetreatCode())) {
            retreat.setRetreatCode("RT" + System.currentTimeMillis());
        }
        // 默认状态：待提交
        if (retreat.getStatus() == null) {
            retreat.setStatus(RetreatConstants.STATUS_PENDING);
        }
        // 默认流程节点：未开始
        if (retreat.getFlowStatus() == null) {
            retreat.setFlowStatus(RetreatConstants.RETREAT_STEP_NO_INIT);
        }
        return retreatMapper.insertRetreat(retreat);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateRetreat(Retreat retreat) {
        retreat.setUpdateTime(DateUtils.getNowDate());
        retreat.setUpdateBy(SecurityUtils.getUsername());
        return retreatMapper.updateRetreat(retreat);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteRetreatByIds(Long[] ids) {
        for (Long id : ids) {
            Retreat retreat = retreatMapper.selectRetreatById(id);
            if (retreat != null) {
                // 待提交 / 已审批 不可删除
                if (RetreatConstants.STATUS_PENDING.equals(retreat.getStatus())
                        || RetreatConstants.STATUS_APPROVED.equals(retreat.getStatus())) {
                    throw new ServiceException("申请单【" + retreat.getRetreatCode() + "】状态不允许删除");
                }
                // 流程运行中不可删除
                if (retreat.getProcessInstanceId() != null) {
                    long count = runtimeService.createProcessInstanceQuery()
                            .processInstanceId(retreat.getProcessInstanceId())
                            .count();
                    if (count > 0) {
                        throw new ServiceException("申请单【" + retreat.getRetreatCode() + "】流程仍在运行，不能删除");
                    }
                }
            }
        }
        return retreatMapper.deleteRetreatByIds(ids);
    }

    // 工作流核心方法
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Retreat startApplication(RetreatStartDTO dto) {
        log.info("==================== 发起退住申请 ====================");

        // 构建退住申请主表对象
        Retreat retreat = new Retreat();
        retreat.setElderId(dto.getElderId());
        retreat.setReason(dto.getReason());
        retreat.setCheckOutTime(dto.getCheckOutTime());

        // ====================== 校验并加载关联数据 ======================
        // 老人信息
        Elder elder = elderMapper.selectElderById(dto.getElderId());
        if (elder == null) {
            throw new ServiceException("老人不存在");
        }

        // 入住配置
        CheckInConfigg checkInConfig = checkInConfigMapper.selectCheckInConfigById(dto.getElderId());
        if (checkInConfig == null) {
            throw new ServiceException("未找到老人的入住配置信息，请先办理入住");
        }

        // 合同信息
        Contract contract = contractMapper.selectContractById(dto.getElderId());
        // 入住信息（顾问）
        CheckIn checkIn = checkInMapper.selectCheckInByElderId(dto.getElderId());
        // 护理员列表
        List<String> assignNurse = assignNurseMapper.selectNurseByElderId(dto.getElderId());

        // ====================== 填充业务快照数据 ======================
        retreat.setName(elder.getName());
        retreat.setIdCardNo(elder.getIdCardNo());
        retreat.setPhone(elder.getPhone());

        String string = checkInMapper.getlevelName(dto.getElderId());
        retreat.setNursingLevelName(string);

        // 床位号
        retreat.setBedNo(checkInConfig.getBedNo());

        // 合同信息非空判断
        if (contract != null) {
            retreat.setContractName(contract.getName());
            retreat.setContractUrl(contract.getPdfUrl());
            retreat.setContractNo(contract.getContractNo());
        }
        // 入住顾问非空判断
        if (checkIn != null) {
            retreat.setCounselor(checkIn.getCounselor());
        }
        // 护理员姓名
        retreat.setNursingName(String.join(",", assignNurse));

        String nickName = retreatMapper.selectNickNameByusername(SecurityUtils.getUsername());



        // ====================== 基础信息赋值 ======================
        retreat.setFlowStatus(RetreatConstants.RETREAT_STEP_NO_INIT);
        retreat.setStatus(RetreatConstants.STATUS_PENDING);
        retreat.setApplicatId(SecurityUtils.getUserId());
        retreat.setApplicat(nickName);
        retreat.setCreateTime(DateUtils.getNowDate());
        retreat.setCreateBy(SecurityUtils.getUsername());
        retreat.setRetreatCode("RT" + System.currentTimeMillis());

        retreatMapper.insertRetreat(retreat);

        // ====================== 获取流程定义 ======================
        ProcessDefinition procDef = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey(RetreatConstants.RETREAT_PROCESS_KEY)
                .latestVersion()
                .singleResult();
        if (procDef == null) {
            throw new ServiceException("流程定义【" + RetreatConstants.RETREAT_PROCESS_KEY + "】未部署");
        }
//        log.info("使用流程定义ID: {}, 版本: {}", procDef.getId(), procDef.getVersion());

        // 打印BPMN节点信息
        BpmnModel bpmnModel = repositoryService.getBpmnModel(procDef.getId());
        if (bpmnModel != null) {
            Process process = bpmnModel.getMainProcess();
            log.info("=== 流程元素 ===");
            for (FlowElement element : process.getFlowElements()) {
//                log.info("ID: {}, 类型: {}", element.getId(), element.getClass().getSimpleName());
                if (element instanceof UserTask) {
                    UserTask userTask = (UserTask) element;
//                    log.info("  名称: {}, 候选组: {}", userTask.getName(), userTask.getCandidateGroups());
                }
            }
        }

        // ====================== 启动流程实例 ======================
        Map<String, Object> variables = new HashMap<>();
        variables.put(RetreatConstants.VAR_RETREAT_ID, retreat.getId());

        ProcessInstance processInstance;
        try {
            processInstance = runtimeService.startProcessInstanceById(
                    procDef.getId(),
                    String.valueOf(retreat.getId()),
                    variables
            );
//            log.info("流程实例ID: {}", processInstance.getId());
        } catch (Exception e) {
            log.error("启动流程失败", e);
            throw new ServiceException("启动流程失败: " + e.getMessage());
        }

        // 更新流程实例ID到退住表
        retreat.setProcessInstanceId(processInstance.getId());
        retreatMapper.updateRetreat(retreat);

        // ====================== 获取流程启动后的第一个任务 ======================
        List<Task> tasks = taskService.createTaskQuery()
                .processInstanceId(processInstance.getId())
                .list();
//        log.info("启动后任务数量: {}", tasks.size());

        // 无任务则抛出异常
        if (tasks.isEmpty()) {
            List<Execution> executions = runtimeService.createExecutionQuery()
                    .processInstanceId(processInstance.getId())
                    .list();
//            log.info("执行实例数量: {}", executions.size());
            for (Execution exec : executions) {
//                log.info("执行实例ID: {}, 活动ID: {}", exec.getId(), exec.getActivityId());
            }
            throw new ServiceException("流程启动后未生成任何任务，请检查BPMN定义");
        }

        // 获取第一个任务 node0
        Task firstTask = tasks.get(0);
        log.info("第一个任务定义Key: {}, 名称: {}", firstTask.getTaskDefinitionKey(), firstTask.getName());

        // ====================== 自动完成起始节点 node0 ======================
        if (firstTask.getTaskDefinitionKey().equals("node0")) {
            // 完成任务并传递变量
            Map<String, Object> vars = new HashMap<>();
            vars.put(RetreatConstants.VAR_ACTION, RetreatConstants.ACTION_PASS);
            taskService.complete(firstTask.getId(), vars);

            // 更新业务状态
            retreat.setFlowStatus(RetreatConstants.RETREAT_STEP_NO_ONE);
            retreatMapper.updateRetreat(retreat);
            log.info("已完成第一个任务 node0");

            // 设置下一个任务的候选组
            Task nextTask = taskService.createTaskQuery()
                    .processInstanceId(processInstance.getId())
                    .singleResult();
            if (nextTask != null && "node1".equals(nextTask.getTaskDefinitionKey())) {
                assignTaskAssignee(nextTask, "node1");
                // log.info("已为任务 node1 分配处理人/候选组");

                log.info("已为任务添加候选组: nurse_leader");
            }
        } else {
            throw new ServiceException("第一个任务不是 node0，实际是: " + firstTask.getTaskDefinitionKey());
        }

        // 获取 node1 任务（流程启动后的第一个待办任务）
        Task currentTask = taskService.createTaskQuery()
                .processInstanceId(processInstance.getId())
                .singleResult();

        if (currentTask != null) {
            // 将 taskId 设置到 retreat 对象中
            retreat.setTaskId(currentTask.getId());
        }


        // 记录流程历史
        addHistory(retreat.getId(), null, RetreatConstants.RETREAT_STEP_NO_INIT,
                null, "发起申请", null);

        return retreat;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String completeTask(RetreatTaskCompletedDTO dto) {
        // 1. 查询任务是否存在
        Task task = taskService.createTaskQuery()
                .taskId(dto.getTaskId())
                .singleResult();
        if (task == null) {
            throw new ServiceException("任务不存在");
        }

        // 2. 权限校验：支持【固定办理人】+【候选组】两种模式
        LoginUser loginUser = SecurityUtils.getLoginUser();
        String currentUsername = loginUser.getUsername();
        List<String> userRoles = loginUser.getUser().getRoles().stream()
                .map(SysRole::getRoleKey)
                .collect(Collectors.toList());

        List<IdentityLink> links = taskService.getIdentityLinksForTask(task.getId());
        boolean authorized = false;

        // 先判断：任务是否分配了固定办理人
        if (StringUtils.isNotEmpty(task.getAssignee())) {
            if (task.getAssignee().equals(currentUsername)) {
                authorized = true;
            }
        }

        // 固定办理人不匹配，再判断候选组
        if (!authorized) {
            for (IdentityLink link : links) {
                if ("candidate".equals(link.getType()) && link.getGroupId() != null) {
                    if (userRoles.contains(link.getGroupId())) {
                        authorized = true;
                        break;
                    }
                }
            }
        }

        if (!authorized) {
            throw new ServiceException("您没有权限操作此任务");
        }

        // 3. 获取退住申请
        Long retreatId = (Long) runtimeService.getVariable(task.getExecutionId(), RetreatConstants.VAR_RETREAT_ID);
        Retreat retreat = retreatMapper.selectRetreatById(retreatId);
        if (retreat == null) {
            throw new ServiceException("申请单不存在");
        }

        // 4. 获取当前任务节点
        String taskDefKey = task.getTaskDefinitionKey();
        Integer currentNode = mapTaskKeyToNode(taskDefKey);



        // 5. 根据不同节点执行不同业务逻辑

        if (currentNode == RetreatConstants.RETREAT_STEP_NO_TWO) {
            // 法务节点：上传解除合同
            RetreatLegalDTO uploadDto = convertBusinessData(dto.getBusinessData(), RetreatLegalDTO.class);
            if (uploadDto == null) {
                throw new ServiceException("请上传解除合同");
            }
            LcRetreatContract contract = new LcRetreatContract();
            contract.setRetreatId(retreat.getId());
            contract.setContractNo(uploadDto.getContractNo());
            contract.setContractUrl(uploadDto.getContractUrl());
            contract.setContractName(uploadDto.getContractName());
            contract.setTerminateDate(DateUtils.parseDate(uploadDto.getTerminateDate()));
            contract.setCreateBy(SecurityUtils.getUserId());
            contract.setCreateTime(DateUtils.getNowDate());
            retreatContractMapper.insert(contract);

        } else if (currentNode == RetreatConstants.RETREAT_STEP_NO_THREE) {
            // 结算节点：录入费用结算单
            RetreatSettleBillDTO settleDto = convertBusinessData(dto.getBusinessData(), RetreatSettleBillDTO.class);
            if (settleDto == null) {
                throw new ServiceException("请填写结算金额");
            }
            LcRetreatBill bill = new LcRetreatBill();
            bill.setRetreatId(retreat.getId());
            bill.setElderId(retreat.getElderId());
            bill.setBillJson("{\"amount\":" + settleDto.getAmount() + "}");
            bill.setCreateBy(SecurityUtils.getUserId());
            bill.setCreateTime(DateUtils.getNowDate());
            billMapper.insert(bill);

        } else if (currentNode == RetreatConstants.RETREAT_STEP_NO_SIX) {
            // 最终退款节点：更新退款金额 + 保存退款信息
            RetreatFinalDTO clearDto = convertBusinessData(dto.getBusinessData(), RetreatFinalDTO.class);
            if (clearDto == null) {
                throw new ServiceException("请填写最终清算金额");
            }
            LcRetreatBill latestBill = billMapper.selectLatestByRetreatId(retreat.getId());
            if (latestBill != null) {
                // 金额
                latestBill.setRefundAmount(clearDto.getFinalAmount());
                latestBill.setTradingChannel(clearDto.getRefundMethod());    // 退款方式
                latestBill.setRefundVoucherUrl(clearDto.getRefundVoucher());// 退款凭证URL
                latestBill.setRemark(clearDto.getRefundRemark());           // 退款备注

                latestBill.setIsRefund(1);
                latestBill.setUpdateBy(SecurityUtils.getUserId());
                latestBill.setUpdateTime(DateUtils.getNowDate());
                billMapper.updateById(latestBill);
            }
        } else if (currentNode == RetreatConstants.RETREAT_STEP_NO_ONE) {
            Retreat infoDto = convertBusinessData(dto.getBusinessData(), Retreat.class);
            if (infoDto == null) {
                throw new ServiceException("请重新输入");
            }
            retreat.setUpdateBy(infoDto.getUpdateBy());
            retreatMapper.updateRetreat(retreat);
        }
        //}

        // 6. 判断：驳回 = 直接终止流程，回到初始节点
        if (RetreatConstants.ACTION_REJECT.equals(dto.getAction())) {
            // 1. 终止 Activiti 流程实例
            runtimeService.deleteProcessInstance(
                    task.getProcessInstanceId(),
                    "审核驳回，流程终止，需重新发起申请"
            );

            // 2. 更新退住状态：驳回 + 回到初始节点
            retreat.setStatus(RetreatConstants.STATUS_REJECTED); // 驳回状态 2
            retreat.setFlowStatus(RetreatConstants.RETREAT_STEP_NO_INIT); // 回到 0
            retreatMapper.updateRetreat(retreat);

            // 3. 记录驳回历史
            addHistory(
                    retreat.getId(),
                    currentNode,
                    RetreatConstants.RETREAT_STEP_NO_INIT,
                    dto.getAction(),
                    dto.getOpinion(),
                    null
            );

            // 4. 直接返回，不继续流转
            return "";
        }

            // 只有通过才继续走流程
        Map<String, Object> variables = new HashMap<>();
        variables.put(RetreatConstants.VAR_ACTION, dto.getAction());
        variables.put(RetreatConstants.VAR_OPINION, dto.getOpinion());
        taskService.complete(dto.getTaskId(), variables);

        // 7. 获取下一任务，并设置对应角色候选组
        List<Task> nextTasks = taskService.createTaskQuery()
                .processInstanceId(task.getProcessInstanceId())
                .active()
                .list();
        String nextTaskId = "";

        if (!nextTasks.isEmpty()) {
            Task nextTask = nextTasks.get(0);
            nextTaskId= nextTask.getId();
            String nextDefKey = nextTask.getTaskDefinitionKey();

            assignTaskAssignee(nextTask, nextDefKey);
        }

        // 8. 判断流程是否结束，更新主表状态
        ProcessInstance pi = runtimeService.createProcessInstanceQuery()
                .processInstanceId(task.getProcessInstanceId())
                .singleResult();

        if (pi == null || pi.isEnded()) {
            // 流程已结束
            retreat.setStatus(RetreatConstants.STATUS_COMPLETED);
            retreat.setFlowStatus(7);

            // 自动执行退住数据整理
            retreatFinishService.finishRetreatData(retreatId);
        } else {
            // 流程未结束，更新当前节点
            List<Task> activeTasks = taskService.createTaskQuery()
                    .processInstanceId(task.getProcessInstanceId())
                    .active()
                    .list();
            if (!activeTasks.isEmpty()) {
                Task activeTask = activeTasks.get(0);
                Integer nextNode = mapTaskKeyToNode(activeTask.getTaskDefinitionKey());
                retreat.setFlowStatus(nextNode);
            }
        }
        retreatMapper.updateRetreat(retreat);

        // 9. 记录审批历史
        addHistory(retreat.getId(), currentNode,
                getNextNodeByAction(currentNode, dto.getAction()),
                dto.getAction(), dto.getOpinion(), null);
        return nextTaskId;
    }

    /**
     * 获取当前用户的待办任务列表
     * 根据用户角色自动过滤，不同角色看到不同的任务
     */
    @Override
    public List<Retreat> getTodoList() {
        // 1. 获取当前登录用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (loginUser == null || loginUser.getUser() == null) {
            throw new ServiceException("用户未登录");
        }

        // 2. 获取用户所有角色标识（role_key）
        List<String> roleKeys = loginUser.getUser().getRoles().stream()
                .map(SysRole::getRoleKey)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (roleKeys.isEmpty()) {
            log.info("当前用户无角色，待办列表为空");
            return new ArrayList<>();
        }

        log.info("当前用户角色: {}", roleKeys);

        // 3. 根据角色查询待办任务（Activiti 自动过滤）
        List<Task> tasks = taskService.createTaskQuery()
                .taskCandidateGroupIn(roleKeys)  // 关键：根据候选组查询
                .active()
                .list();

        if (tasks.isEmpty()) {
            log.info("当前用户没有待办任务");
            return new ArrayList<>();
        }

        log.info("查询到 {} 个待办任务", tasks.size());

        // 4. 提取退住ID并填充任务信息
        List<Retreat> retreatList = new ArrayList<>();
        for (Task task : tasks) {
            // 从流程变量中获取退住申请ID
            Long retreatId = (Long) runtimeService.getVariable(task.getExecutionId(), RetreatConstants.VAR_RETREAT_ID);
            if (retreatId == null) {
                log.warn("任务 {} 未关联退住ID", task.getId());
                continue;
            }

            Retreat retreat = retreatMapper.selectRetreatById(retreatId);
            if (retreat != null) {
                // 填充任务信息，供前端使用
                retreat.setTaskId(task.getId());
                retreat.setTaskDefKey(task.getTaskDefinitionKey());
                retreat.setTaskName(task.getName());
                retreatList.add(retreat);
            }
        }

        return retreatList;
    }

    /**
     * 获取退住申请详情（含合同、流程历史）
     * @param retreatId 退住ID
     * @return 退住详情
     */
    @Override
    public Retreat getDetail(Long retreatId) {
        Retreat retreat = retreatMapper.selectRetreatById(retreatId);
        if (retreat == null) {
            return null;
        }

        // 加载合同附件
        List<LcRetreatContract> contracts = retreatContractMapper.selectByRetreatId(retreatId);
        retreat.setContracts(contracts);

        // 加载流程历史节点
        String processInstanceId = retreat.getProcessInstanceId();
        if (StringUtils.isNotBlank(processInstanceId)) {
            List<HistoricActivityInstance> activities = historyService.createHistoricActivityInstanceQuery()
                    .processInstanceId(processInstanceId)
                    .orderByHistoricActivityInstanceStartTime().asc()
                    .list();
            retreat.setHistoryActivities(activities);
        }
        return retreat;
    }

    // ========================== 工具/辅助方法 ==========================

    /**
     * 任务节点Key → 流程节点常量
     * @param taskDefKey 任务key
     * @return 节点编号
     */
    private Integer mapTaskKeyToNode(String taskDefKey) {
        switch (taskDefKey) {
            case "node0": return RetreatConstants.RETREAT_STEP_NO_INIT;
            case "node1": return RetreatConstants.RETREAT_STEP_NO_ONE;
            case "node2": return RetreatConstants.RETREAT_STEP_NO_TWO;
            case "node3": return RetreatConstants.RETREAT_STEP_NO_THREE;
            case "node4": return RetreatConstants.RETREAT_STEP_NO_FOUR;
            case "node5": return RetreatConstants.RETREAT_STEP_NO_FIVE;
            case "node6": return RetreatConstants.RETREAT_STEP_NO_SIX;
            default: throw new ServiceException("未知的任务节点: " + taskDefKey);
        }
    }

    /**
     * 根据当前节点+审批动作，获取下一节点
     * @param currentNode 当前节点
     * @param action 动作：通过/驳回
     * @return 下一节点
     */
    private Integer getNextNodeByAction(Integer currentNode, Integer action) {
        if (action == RetreatConstants.ACTION_PASS) {
            return RetreatConstants.NEXT_NODE_MAP.get(currentNode);
        } else {
            // 驳回 → 回到初始节点
            return RetreatConstants.RETREAT_STEP_NO_INIT;
        }
    }

    /**
     * 统一添加流程历史记录
     * @param retreatId 退住ID
     * @param fromNode 来源节点
     * @param toNode 目标节点
     * @param action 动作
     * @param opinion 意见
     * @param attachment 附件
     */
    private void addHistory(Long retreatId, Integer fromNode, Integer toNode,
                            Integer action, String opinion, String attachment) {
        LcRetreatHistory history = new LcRetreatHistory();
        history.setRetreatId(retreatId);
        history.setFromNode(fromNode);
        history.setToNode(toNode);
        history.setAction(action);
        history.setOperatorId(SecurityUtils.getUserId());
        history.setOperatorName(SecurityUtils.getUsername());
        history.setOpinion(opinion);
        history.setAttachment(attachment);
        history.setCreateTime(DateUtils.getNowDate());
        historyMapper.insert(history);
    }

    /**
     * 通用类型转换：前端JSON → DTO
     * @param businessData 源数据
     * @param targetClass 目标类型
     * @return 转换后对象
     */
    private <T> T convertBusinessData(Object businessData, Class<T> targetClass) {
        if (businessData == null) {
            return null;
        }
        if (targetClass.isInstance(businessData)) {
            return targetClass.cast(businessData);
        }
        return JSON.parseObject(JSON.toJSONString(businessData), targetClass);
    }

    /**
     * 给任务设置候选组（先删再加，避免重复）
     * @param task 任务
     * @param groupId 角色组
     */
    private void setTaskCandidateGroup(Task task, String groupId) {
        taskService.deleteCandidateGroup(task.getId(), groupId);
        taskService.addCandidateGroup(task.getId(), groupId);
        log.info("任务{} 设置候选组: {}", task.getId(), groupId);
    }

    private String getFixedAssignee(String taskNodeKey) {
        // 配置固定的处理人（可以根据实际需求从数据库读取）
     Map<String, String> fixedAssignees = new HashMap<>();
     fixedAssignees.put("node1", "nurse_leader1");      // 护理组长
     fixedAssignees.put("node2", "legal_staff1");       // 王法务
     fixedAssignees.put("node3", "settleman_staff1");   // 赵结算
     fixedAssignees.put("node4", "settleman_leader1");  // 孙组长
     fixedAssignees.put("node5", "vice_dean1");         // 周副院长
     fixedAssignees.put("node6", "settleman_staff1");  // 孙组长

     return fixedAssignees.get(taskNodeKey);
    }

/**
 * 根据节点获取对应的角色Key
 */
 private String getRoleKeyByNode(String taskNodeKey) {
     switch (taskNodeKey) {
         case "node1":
             return "nurse_leader";
         case "node2":
             return "legal_staff";
         case "node3":
            return "settleman_staff";
         case "node4":
             return "settleman_leader";
         case "node5":
            return "vice_dean";
         case "node6":
            return "settleman_staff";
         default:
            return null;
     }
    }

/**
 * 为任务分配处理人
 * 优先使用固定组员，如果没有则设置候选组
 *
 * @param task 任务对象
 * @param taskNodeKey 任务节点Key
 */
 private void assignTaskAssignee(Task task, String taskNodeKey) {
        // 策略1：固定组员分配
     String assignee = getFixedAssignee(taskNodeKey);

        // 策略2：如果固定组员不存在，则设置候选组
     if (assignee != null) {
         taskService.setAssignee(task.getId(), assignee);
         log.info("任务 {} 设置固定处理人: {}", task.getId(), assignee);
     } else {
        String roleKey = getRoleKeyByNode(taskNodeKey);
        if (roleKey != null) {
             setTaskCandidateGroup(task, roleKey);
             log.info("任务 {} 设置候选组: {}", task.getId(), roleKey);
        } else {
             log.warn("任务 {} 无法分配处理人", task.getId());
         }
    }
    }
}