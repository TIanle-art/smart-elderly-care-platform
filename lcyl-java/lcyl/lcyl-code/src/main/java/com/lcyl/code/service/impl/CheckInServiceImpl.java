package com.lcyl.code.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.lcyl.code.domain.CheckIn;
import com.lcyl.code.domain.Elderr;
import com.lcyl.code.domain.dto.CheckInDetailDTO;
import com.lcyl.code.domain.dto.CheckInFlowDTO;
import com.lcyl.code.mapper.CheckInMapper;
import com.lcyl.code.mapper.ContracttMapper;
import com.lcyl.code.mapper.ElderCheckInMapper;
import com.lcyl.code.service.ICheckInService;
import com.lcyl.common.core.domain.model.LoginUser;
import com.lcyl.common.utils.DateUtils;
import com.lcyl.common.utils.SecurityUtils;
import com.lcyl.common.utils.StringUtils;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
//import org.apache.http.annotation.Contract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.lcyl.code.domain.Contractt;

import java.util.*;

import static org.apache.commons.collections4.CollectionUtils.containsAny;

@Service
public class CheckInServiceImpl implements ICheckInService {
    //    @Autowired
//    private RetreatServiceImpl retreatService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private CheckInMapper checkInMapper;
    @Autowired
    private ElderCheckInMapper elderCheckInMapper;
    @Autowired
    private ContracttMapper contractMapper;


    @Override
    public CheckIn selectCheckInById(Long id) {
        return checkInMapper.selectCheckInById(id);
    }

    /**
     * 查询入住办理列表
     *
     * @param checkIn 入住办理
     * @return 入住办理
     */
    @Override
    public List<CheckIn> selectCheckInList(CheckIn checkIn) {
        return checkInMapper.selectCheckInList(checkIn);
    }

    /**
     * 新增入住办理
     *
     * @param checkIn 入住办理
     * @return 结果
     */
    @Override
    public int insertCheckIn(CheckIn checkIn) {
        checkIn.setCreateTime(DateUtils.getNowDate());
        return checkInMapper.insertCheckIn(checkIn);
    }

    @Override
    public int updateCheckIn(CheckIn checkIn) {
        checkIn.setUpdateTime(DateUtils.getNowDate());
        return checkInMapper.updateCheckIn(checkIn);
    }

    /**
     * 批量删除入住办理
     *
     * @param ids 需要删除的入住办理主键
     * @return 结果
     */
    @Override
    public int deleteCheckInByIds(Long[] ids) {
        return checkInMapper.deleteCheckInByIds(ids);
    }

    /**
     * 删除入住办理信息
     *
     * @param id 入住办理主键
     * @return 结果
     */
    @Override
    public int deleteCheckInById(Long id) {
        return checkInMapper.deleteCheckInById(id);
    }

    //    发起入住申请
    @Transactional
    @Override
    public CheckInFlowDTO applyCheckIn(String otherInfo, String reviewInfo) {

        LoginUser loginUser = SecurityUtils.getLoginUser();
        CheckIn checkIn = new CheckIn();
        checkIn.setOtherApplyInfo(otherInfo);
        checkIn.setReviewInfo(reviewInfo);
        checkIn.setDeptNo(loginUser.getUser().getDeptId());

        String dateStr = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmmSSS"));
        String checkInCode = "RZ" + dateStr;

        checkIn.setCreateTime(DateUtils.getNowDate());
        checkIn.setUpdateTime(DateUtils.getNowDate());
        checkIn.setFlowStatus(0L); // 发起申请
        checkIn.setApplicatId(loginUser.getUser().getUserId());
        checkIn.setCounselor(loginUser.getUser().getNickName());
        checkIn.setStatus(0L); // 待审批
        checkIn.setCheckInCode(checkInCode);
        checkIn.setApplicat(loginUser.getUser().getNickName());

        // 只解析 JSON（不做业务校验）

        String elderName = null;
        String idCard = null;

        try {
            JSONObject otherInfoJson = JSON.parseObject(otherInfo);
            JSONObject basicForm = otherInfoJson.getJSONObject("basicForm");
            if (basicForm != null) {
                elderName = basicForm.getString("name");
                idCard = basicForm.getString("idCard");
            }
        } catch (Exception e) {
            throw new RuntimeException("申请信息格式错误");
        }
//        业务字段赋值
        if (StringUtils.isNotBlank(elderName)) {
            checkIn.setElderName(elderName);
            checkIn.setTitle(elderName + "的入住申请");
        } else {
            checkIn.setTitle("入住申请");
        }

        if (StringUtils.isBlank(idCard)) {
            throw new RuntimeException("身份证号不能为空");
        }
//        身份证唯一性检验
        CheckIn existCheckIn = checkInMapper.selectActiveByIdCard(idCard);
        if (existCheckIn != null) {
            throw new RuntimeException("该身份证老人已存在未完成的入住申请，请勿重复提交");
        }
        // 保存业务数据

        checkInMapper.insertCheckIn(checkIn);

        //  流程部署判断

        Deployment deployment = repositoryService
                .createDeploymentQuery()
                .deploymentName("checkin")
                .singleResult();

        if (deployment == null) {
            repositoryService.createDeployment()
                    .addClasspathResource("activiti/checkin.bpmn20.xml")
                    .name("checkin")
                    .deploy();
        }

        // 启动流程

        String businessKey = "checkin:" + checkIn.getId();

        ProcessInstance existInstance = runtimeService
                .createProcessInstanceQuery()
                .processDefinitionKey("checkin")
                .processInstanceBusinessKey(businessKey)
                .singleResult();

        if (existInstance != null) {
            throw new RuntimeException("该入住申请流程已启动，请勿重复发起");
        }

        String dean = checkInMapper.selectDean("副院长");
        String nurseBoss = checkInMapper.selectName("护理部");
        Set<String> legalCandidates = getLegalCandidateIdentities();
        String assignee4Candidates = legalCandidates.isEmpty() ? "" : String.join(",", legalCandidates);
        Map<String, Object> variables = new HashMap<>();
        variables.put("processTitle", checkIn.getTitle());
        variables.put("assignee0", loginUser.getUser().getNickName());
        variables.put("assignee1", nurseBoss);
        variables.put("assignee2", dean);
        variables.put("assignee3", loginUser.getUser().getNickName());
        variables.put("assignee4", assignee4Candidates);

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(
                "checkin",
                businessKey,
                variables
        );

        //完成第一个任务

        Task task = taskService.createTaskQuery()
                .processInstanceBusinessKey(businessKey)
                .singleResult();

        if (task != null) {
            taskService.complete(task.getId());
        }
        Task nextTask = taskService.createTaskQuery()
                .processInstanceBusinessKey(businessKey)
                .singleResult();

        if (nextTask != null) {
            checkIn.setCurrentTaskId(nextTask.getTaskDefinitionKey());
            checkIn.setCurrentTaskName(nextTask.getName());
            checkIn.setFlowStatus(1L); // 1:入住评估
        } else {
            checkIn.setCurrentTaskName("流程已结束");
            checkIn.setStatus(2L);
        }
        checkIn.setProcessId(processInstance.getId());
        checkInMapper.updateCheckIn(checkIn);

        CheckInFlowDTO dto = new CheckInFlowDTO();
        dto.setCheckInId(checkIn.getId());
        dto.setFlowStatus(checkIn.getFlowStatus());
        dto.setCurrentTaskKey(checkIn.getCurrentTaskId());
        dto.setCurrentTaskName(checkIn.getCurrentTaskName());
        return dto;

    }

    //    入住评估
    @Override
    @Transactional
    public CheckInFlowDTO evaluateCheckIn(Long checkInId, String evaluation) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        CheckIn checkIn = checkInMapper.selectCheckInById(checkInId);
        if (checkIn == null) {
            throw new RuntimeException("入住办理不存在");
        }

        // 检查当前状态是否为入住评估阶段
        if (checkIn.getFlowStatus() != 1L) {
            throw new RuntimeException("当前入住办理不在评估阶段，无法进行评估操作");
        }
        String businessKey = "checkin:" + checkIn.getId();
        // 查询当前评估任务
        Task task = taskService.createTaskQuery()
                .processInstanceBusinessKey(businessKey)
                .taskDefinitionKey("evaluate")
                .singleResult();

        if (task == null) {
            throw new RuntimeException("当前评估任务不存在或已处理");
        }

        // 保存评估信息
        checkIn.setEvaluation(evaluation);
        checkIn.setEvaluator(loginUser.getUser().getNickName());
        // 完成评估任务
        taskService.complete(task.getId());

        // 查询下一个任务
        Task nextTask = taskService.createTaskQuery()
                .processInstanceBusinessKey(businessKey)
                .singleResult();

        if (nextTask != null) {
            checkIn.setCurrentTaskId(nextTask.getTaskDefinitionKey());
            checkIn.setCurrentTaskName(nextTask.getName());
            checkIn.setFlowStatus(2L); // 2:入住审批
        } else {
            checkIn.setCurrentTaskName("流程已结束");
            checkIn.setStatus(2L);
        }

        checkIn.setProcessId(task.getProcessInstanceId());
        checkInMapper.updateCheckIn(checkIn);

        CheckInFlowDTO dto = new CheckInFlowDTO();
        dto.setCheckInId(checkIn.getId());
        dto.setFlowStatus(checkIn.getFlowStatus());
        dto.setCurrentTaskKey(checkIn.getCurrentTaskId());
        dto.setCurrentTaskName(checkIn.getCurrentTaskName());
        return dto;
    }

    //    入住审批
    @Override
    @Transactional
    public CheckInFlowDTO approveCheckIn(Long checkInId, Map<String, Object> vars) {
        LoginUser loginUser = SecurityUtils.getLoginUser();

        CheckIn checkIn = checkInMapper.selectCheckInById(checkInId);
        if (checkIn == null) {
            throw new RuntimeException("入住办理不存在");
        }

        // 只能在 入住审批 阶段操作
        if (!checkIn.getFlowStatus().equals(2L)) {
            throw new RuntimeException("当前不是入住审批阶段");
        }

        String approveResult = (String) vars.get("approveResult");
        if (!"同意".equals(approveResult)
                && !"拒绝".equals(approveResult)
                && !"驳回".equals(approveResult)) {
            throw new RuntimeException("审批结果只能是：同意 / 拒绝 / 驳回");
        }
        String businessKey = "checkin:" + checkIn.getId();
        Task task = taskService.createTaskQuery()
                .processInstanceBusinessKey(businessKey)
                .taskDefinitionKey("approve")
                .singleResult();

        if (task == null) {
            throw new RuntimeException("当前审批任务不存在或已处理");
        }
        Map<String, Object> processVars = new HashMap<>();

        // 审批结果 对影成ops的值
        if ("同意".equals(approveResult)) {
            processVars.put("ops", 1);
            checkIn.setStatus(2L);     // 已通过

            // 完成审批任务
            taskService.complete(task.getId(), processVars);

            // 审批通过时根据申请单创建老人并关联（后端执行，不依赖前端权限）
            addElderOnApproval(checkIn);
        } else if ("驳回".equals(approveResult)) {
            processVars.put("ops", 3);
            checkIn.setStatus(4L);     // 已驳回
            // 完成审批任务
            taskService.complete(task.getId(), processVars);
        } else {
            processVars.put("ops", 2);
            checkIn.setFlowStatus(5L);
            checkIn.setCurrentTaskId("reject");
            checkIn.setCurrentTaskName("申请已拒绝");

            taskService.complete(task.getId(), processVars);

        }
        checkIn.setApprover(loginUser.getUser().getNickName());


        checkIn.setReason((String) vars.get("approveRemark"));
        // 查询下一任务
        Task nextTask = taskService.createTaskQuery()
                .processInstanceBusinessKey(businessKey)
                .singleResult();

        if (nextTask == null) {
            //流程结束（拒绝）
            checkIn.setFlowStatus(5L); // 入住完成（流程结束）
            checkIn.setCurrentTaskId("reject");
            checkIn.setCurrentTaskName("申请已拒绝");
            checkIn.setStatus(3L); // 已拒绝
        } else {
            checkIn.setCurrentTaskId(nextTask.getTaskDefinitionKey());
            checkIn.setCurrentTaskName(nextTask.getName());

            //  根据节点名称gaiflowStatus
            if ("入住选配-处理".equals(nextTask.getName())) {
                checkIn.setFlowStatus(3L);
            } else if ("发起入住申请".equals(nextTask.getName())) {
                checkIn.setFlowStatus(0L);
            }
        }
        checkInMapper.updateCheckIn(checkIn);


        CheckInFlowDTO dto = new CheckInFlowDTO();
        dto.setCheckInId(checkInId);
        dto.setFlowStatus(checkIn.getFlowStatus());
        dto.setCurrentTaskKey(checkIn.getCurrentTaskId());
        dto.setCurrentTaskName(checkIn.getCurrentTaskName());
        dto.setTaskId(nextTask == null ? null : nextTask.getId());

        return dto;
    }

    //审批通过时根据otherApplyInfo创建老人并关联到checkin
    private void addElderOnApproval(CheckIn checkIn) {
        String otherApplyInfo = checkIn.getOtherApplyInfo();
        String reviewInfo = checkIn.getReviewInfo();
        if (StringUtils.isBlank(otherApplyInfo)) {
            return;
        }
        try {
            JSONObject json = JSON.parseObject(otherApplyInfo);
            JSONObject basicForm = json.getJSONObject("basicForm");
            JSONObject json2 = JSON.parseObject(reviewInfo);
            JSONObject reviewForm = json.getJSONObject("reviewForm");
            if (basicForm == null) {
                return;
            }
            String name = basicForm.getString("name");
            String idCardNo = basicForm.getString("idCard");
            if (StringUtils.isBlank(idCardNo)) {
                return;
            }
            Elderr existing = elderCheckInMapper.selectByIdCardNo(idCardNo);
            if (existing != null) {
                checkIn.setElderId(existing.getId());
                return;
            }
            Elderr elder = new Elderr();
            elder.setName(StringUtils.isNotBlank(name) ? name : "未知");
            elder.setIdCardNo(idCardNo);
            elder.setAge(basicForm.getString("age"));
            elder.setSex(basicForm.getString("sex"));
            if (elder.getSex() == null || elder.getSex().isEmpty()) {
                elder.setSex(basicForm.getString("gender"));
            }
            elder.setPhone(basicForm.getString("phone"));

            // 从reviewInfo中解析imagePaths中的photo字段
            if (json2 != null) {
                JSONObject imagePaths = json2.getJSONObject("imagePaths");
                if (imagePaths != null) {
                    String photo = imagePaths.getString("photo");
                    if (StringUtils.isNotBlank(photo)) {
                        elder.setImage(photo);
                    }
                }
            }
            elder.setStatus(4);
            elder.setRemark("通过入住审批添加");
            elder.setCreateTime(DateUtils.getNowDate());
            elder.setUpdateTime(DateUtils.getNowDate());
            try {
                LoginUser loginUser = SecurityUtils.getLoginUser();
                if (loginUser != null && loginUser.getUserId() != null) {
                    elder.setCreateBy(loginUser.getUserId());
                    elder.setUpdateBy(loginUser.getUserId());
                }
            } catch (Exception ignored) {
            }
            elderCheckInMapper.insertElder(elder);
            checkIn.setElderId(elder.getId());
        } catch (Exception e) {
            throw new RuntimeException("审批通过但添加老人信息失败：" + e.getMessage());
        }
    }
    //响应待办任务列表（已认领的任务）
    @Override
    public List<CheckIn> responseCheckIn() {
        Set<String> identities =  getCurrentUserIdentities();
        if (identities.isEmpty()) return new ArrayList<>();

        Map<String, Task> taskById = new LinkedHashMap<>();
        for (String identity : identities) {
            try {
                List<Task> tasks = taskService.createTaskQuery()
                        .processDefinitionKey("checkin")
                        .taskAssignee(identity)
                        .list();
                if (tasks == null) continue;
                for (Task t : tasks) {
                    if (t != null && t.getId() != null) taskById.putIfAbsent(t.getId(), t);
                }
            } catch (Exception ignored) {
            }
        }

        List<CheckIn> checkIns = new ArrayList<>();
        for (Task task : taskById.values()) {
            addCheckInFromTask(checkIns, task);
        }
        return checkIns;
    }

    //获取候选任务列表（需要认领的任务）：签约办理等节点使用 candidateUsers，可能存为逗号分隔的单字符串，需兼容
    @Override
    public List<CheckIn> getCandidateTasks() {
        Set<String> identities = getCurrentUserIdentities();
        if (identities.isEmpty()) return new ArrayList<>();

        List<CheckIn> checkIns = new ArrayList<>();
        Set<String> legalCandidates = getLegalCandidateIdentities();
        if (!legalCandidates.isEmpty()) {
            try {
                ensureLegalCandidatesOnUnassignedSignTasks(legalCandidates);
            } catch (Exception ignored) {
            }
        }

        List<Task> allUnassigned = null;// 所有未认领的任务
        try {
            allUnassigned = taskService.createTaskQuery()
                    .processDefinitionKey("checkin")
                    .taskUnassigned()// 只查未被分配给具体人的任务
                    .list();
        } catch (Exception ignored) {
        }
        if (allUnassigned == null || allUnassigned.isEmpty()) return checkIns;
        Set<String> addedProcessIds = new HashSet<>();
        for (Task task : allUnassigned) {
            if (task == null || task.getProcessInstanceId() == null) continue;
            if (addedProcessIds.contains(task.getProcessInstanceId())) continue;
            List<IdentityLink> links = null;// 任务关联的用户
            try {
                links = taskService.getIdentityLinksForTask(task.getId());
            } catch (Exception ignored) {
            }
            if (links == null || links.isEmpty()) continue;
            boolean match = false;
            for (IdentityLink link : links) {
                if (link == null) continue;
                if (!"candidate".equals(link.getType())) continue;
                String userId = link.getUserId();
                if (userId == null) continue;
                if (matchesAnyIdentity(userId, identities)) {
                    match = true;
                    break;
                }
            }
            if (match) {
                addCheckInFromTask(checkIns, task);
                addedProcessIds.add(task.getProcessInstanceId());
            }
        }
        return checkIns;
    }

    /**
     * 根据任务对象添加对应的签到信息到签到列表中
     * @param checkIns 签到列表，用于存储签到信息
     * @param task 任务对象，包含流程实例等信息
     */
    private void addCheckInFromTask(List<CheckIn> checkIns, Task task) {
        // 检查任务对象是否为空，为空则直接返回
        if (task == null) return;
        // 获取任务对象的流程实例ID
        String processInstanceId = task.getProcessInstanceId();
        // 检查流程实例ID是否为空，为空则直接返回
        if (processInstanceId == null) return;
        try {
            // 根据流程实例ID解析对应的签到信息
            CheckIn checkIn = resolveCheckInByProcessInstanceId(processInstanceId);
            // 检查签到信息是否存在，不存在则直接返回
            if (checkIn == null) return;
            // 设置签到信息的任务ID
            checkIn.setTaskId(task.getId());
            // 将签到信息添加到签到列表中
            checkIns.add(checkIn);
        } catch (Exception e) {
            // 捕获并打印异常信息
            e.printStackTrace();
        }
    }

    /**
     * 根据流程实例ID解析并获取CheckIn对象
     * 该方法通过多种尝试来获取CheckIn信息，首先尝试直接通过流程ID查询，
     * 如果失败则尝试从流程实例或历史流程实例中获取业务键，再从业务键中解析CheckIn ID
     *
     * @param processInstanceId 流程实例ID，用于查找对应的CheckIn记录
     * @return CheckIn对象，如果找不到则返回null
     */
    private CheckIn resolveCheckInByProcessInstanceId(String processInstanceId) {
        if (processInstanceId == null || processInstanceId.trim().isEmpty()) return null;
        try {
            CheckIn direct = checkInMapper.selectCheckInByProcessId(processInstanceId);
            if (direct != null) return direct;
        } catch (Exception ignored) {
        }

        String businessKey = null;
        try {
            ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
            if (pi != null) businessKey = pi.getBusinessKey();
        } catch (Exception ignored) {
        }
        if (businessKey == null) {
            try {
                HistoricProcessInstance hpi = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
                if (hpi != null) businessKey = hpi.getBusinessKey();
            } catch (Exception ignored) {
            }
        }

        Long checkInId = parseCheckInIdFromBusinessKey(businessKey);
        if (checkInId == null) return null;
        try {
            return checkInMapper.selectCheckInById(checkInId);
        } catch (Exception ignored) {
        }
        return null;
    }

    /**
     * 从业务键中解析签到ID
     * @param businessKey 业务键字符串，格式应为"checkin:数字"
     * @return 解析出的Long类型签到ID，如果解析失败则返回null
     */
    private Long parseCheckInIdFromBusinessKey(String businessKey) {
        // 检查业务键是否为null
        if (businessKey == null) return null;
        // 去除字符串两端的空白字符
        String key = businessKey.trim();
        // 检查字符串是否以"checkin:"开头
        if (!key.startsWith("checkin:")) return null;
        // 获取"checkin:"之后的部分并去除两端空白
        String idPart = key.substring("checkin:".length()).trim();
        // 检查ID部分是否为空
        if (idPart.isEmpty()) return null;
        try {
            // 尝试将ID部分转换为Long类型
            return Long.parseLong(idPart);
        } catch (Exception ignored) {
            // 捕获所有异常并忽略，返回null
        }
        // 如果所有条件都不满足，返回null
        return null;
    }

    //认领任务
    @Override
    @Transactional
    public void claimTask(String taskId) {
        Set<String> identities = getCurrentUserIdentities();
        if (identities.isEmpty()) {
            throw new RuntimeException("当前用户信息异常");
        }
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task == null) throw new RuntimeException("任务不存在");
        if (task.getAssignee() != null && !task.getAssignee().isEmpty()) throw new RuntimeException("任务已被认领");

        boolean allowed = false;
        try {
            List<IdentityLink> links = taskService.getIdentityLinksForTask(taskId);//获取任务关联的用户
            for (IdentityLink link : links) {
                if (!"candidate".equals(link.getType())) continue;
                String userId = link.getUserId();
                if (userId != null && matchesAnyIdentity(userId, identities)) {
                    allowed = true;
                    break;
                }
            }
        } catch (Exception ignored) {
        }
        if (!allowed) throw new RuntimeException("无权限认领该任务");

        String assignee = identities.iterator().next();
        taskService.claim(taskId, assignee);
    }

    //获取已完成任务列表
    @Override
    public List<CheckIn> getCompletedTasks() {
        Set<String> identities = getCurrentUserIdentities();
        if (identities.isEmpty()) return new ArrayList<>();

        Map<String, HistoricTaskInstance> taskById = new LinkedHashMap<>();
        for (String identity : identities) {
            List<HistoricTaskInstance> tasks = historyService.createHistoricTaskInstanceQuery()
                    .processDefinitionKey("checkin")
                    .taskAssignee(identity)
                    .finished()
                    .orderByHistoricTaskInstanceEndTime()
                    .desc()
                    .list();
            if (tasks == null) continue;
            for (HistoricTaskInstance t : tasks) {
                if (t != null && t.getId() != null) taskById.putIfAbsent(t.getId(), t);
            }
        }
        List<HistoricTaskInstance> historicTasks = new ArrayList<>(taskById.values());
        List<CheckIn> checkIns = new ArrayList<>();
        Set<String> processInstanceIds = new HashSet<>();

        for (HistoricTaskInstance historicTask : historicTasks) {
            String processInstanceId = historicTask.getProcessInstanceId();

            // 去重，一个流程实例只显示一次
            if (!processInstanceIds.contains(processInstanceId)) {
                processInstanceIds.add(processInstanceId);
                CheckIn checkIn = checkInMapper.selectCheckInByProcessId(processInstanceId);

                if (checkIn != null) {
                    checkIns.add(checkIn);
                }
            }
        }
        return checkIns;
    }

    /**
     * 获取当前用户的身份标识集合
     * 该方法会获取当前登录用户的信息，并提取用户名和昵称作为身份标识
     * 处理过程中会去除字符串两端的空格，并确保添加到集合中的值不为空
     *
     * @return 返回包含用户身份标识的Set集合，使用LinkedHashSet保持插入顺序
     */
    private Set<String> getCurrentUserIdentities() {
        // 创建一个LinkedHashSet集合来存储用户身份标识，保持插入顺序
        Set<String> identities = new LinkedHashSet<>();
        try {
            // 获取当前登录用户信息
            LoginUser loginUser = SecurityUtils.getLoginUser();
            // 如果用户信息或用户对象为空，直接返回空集合
            if (loginUser == null || loginUser.getUser() == null) return identities;
            // 获取用户名和昵称
            String userName = loginUser.getUser().getUserName();
            String nickName = loginUser.getUser().getNickName();
            // 添加用户名到集合（非空检查）
            if (userName != null && !userName.isEmpty()) identities.add(userName);
            // 添加去除空格后的用户名到集合（非空检查）
            if (userName != null && !userName.trim().isEmpty()) identities.add(userName.trim());
            // 添加昵称到集合（非空检查）
            if (nickName != null && !nickName.isEmpty()) identities.add(nickName);
            // 添加去除空格后的昵称到集合（非空检查）
            if (nickName != null && !nickName.trim().isEmpty()) identities.add(nickName.trim());
        } catch (Exception ignored) {
            // 捕获所有异常但不做处理，直接忽略
        }
        // 返回包含用户身份标识的集合
        return identities;
    }
    private boolean matchesAnyIdentity(String identityText, Set<String> identities) {
        if (identityText == null) return false;
        String text = identityText.trim();
        if (text.isEmpty()) return false;
        if (identities.contains(text)) return true;
        if (text.contains(",")) {
            for (String part : text.split(",")) {
                if (part != null && identities.contains(part.trim())) return true;
            }
        }
        return false;
    }

    @Override
    public Map<String, Object> diagnoseTaskVisibility() {
        Map<String, Object> result = new LinkedHashMap<>();
        Map<String, Object> errors = new LinkedHashMap<>();
        result.put("errors", errors);

        Set<String> identities = getCurrentUserIdentities();
        result.put("currentUserIdentities", identities);

        Set<String> legalCandidates = new LinkedHashSet<>();
        try {
            legalCandidates = getLegalCandidateIdentities();
        } catch (Exception e) {
            errors.put("getLegalCandidateIdentities", e.getClass().getName() + ": " + e.getMessage());
        }
        result.put("legalCandidatesCount", legalCandidates.size());
        result.put("legalCandidatesSample", new ArrayList<>(legalCandidates).subList(0, Math.min(30, legalCandidates.size())));

        boolean userIsLegalCandidate = containsAny(identities, legalCandidates);
        result.put("currentUserMatchesLegalCandidates", userIsLegalCandidate);

        try {
            long totalRuntimeTasks = taskService.createTaskQuery().processDefinitionKey("checkin").count();
            long totalUnassignedRuntimeTasks = taskService.createTaskQuery().processDefinitionKey("checkin").taskUnassigned().count();
            long totalUnassignedSignTasks = taskService.createTaskQuery().processDefinitionKey("checkin").taskDefinitionKey("sign").taskUnassigned().count();
            result.put("checkinRuntimeTaskCount", totalRuntimeTasks);
            result.put("checkinUnassignedTaskCount", totalUnassignedRuntimeTasks);
            result.put("checkinUnassignedSignTaskCount", totalUnassignedSignTasks);
        } catch (Exception e) {
            errors.put("taskCounts", e.getClass().getName() + ": " + e.getMessage());
        }

        try {
            Map<String, Long> candidateUnassignedCountByIdentity = new LinkedHashMap<>();
            Map<String, Long> assignedCountByIdentity = new LinkedHashMap<>();
            for (String identity : identities) {
                long candidateUnassigned = taskService.createTaskQuery()
                        .processDefinitionKey("checkin")
                        .taskCandidateOrAssigned(identity)
                        .taskUnassigned()
                        .count();
                long assigned = taskService.createTaskQuery()
                        .processDefinitionKey("checkin")
                        .taskAssignee(identity)
                        .count();
                candidateUnassignedCountByIdentity.put(identity, candidateUnassigned);
                assignedCountByIdentity.put(identity, assigned);
            }
            result.put("candidateUnassignedCountByIdentity", candidateUnassignedCountByIdentity);
            result.put("assignedCountByIdentity", assignedCountByIdentity);
        } catch (Exception e) {
            errors.put("identityCounts", e.getClass().getName() + ": " + e.getMessage());
        }

        boolean injected = false;
        try {
            if (userIsLegalCandidate) {
                ensureLegalCandidatesOnUnassignedSignTasks(legalCandidates);
                injected = true;
            }
        } catch (Exception e) {
            errors.put("injectLegalCandidates", e.getClass().getName() + ": " + e.getMessage());
        }
        result.put("legalCandidatesInjectedToUnassignedSignTasks", injected);
        try {
            List<Task> signTasks = taskService.createTaskQuery()
                    .processDefinitionKey("checkin")
                    .taskDefinitionKey("sign")
                    .taskUnassigned()
                    .listPage(0, 20);
            List<Map<String, Object>> signTaskSamples = new ArrayList<>();
            if (signTasks != null) {
                for (Task t : signTasks) {
                    if (t == null) continue;
                    Map<String, Object> one = new LinkedHashMap<>();
                    one.put("taskId", t.getId());
                    one.put("taskName", t.getName());
                    one.put("taskDefinitionKey", t.getTaskDefinitionKey());
                    one.put("assignee", t.getAssignee());
                    one.put("processInstanceId", t.getProcessInstanceId());
                    List<String> candidateUsers = new ArrayList<>();
                    try {
                        List<IdentityLink> links = taskService.getIdentityLinksForTask(t.getId());
                        if (links != null) {
                            for (IdentityLink link : links) {
                                if (link == null) continue;
                                if (!"candidate".equals(link.getType())) continue;
                                if (link.getUserId() != null && !link.getUserId().trim().isEmpty()) {
                                    candidateUsers.add(link.getUserId());
                                }
                            }
                        }
                    } catch (Exception e) {
                        errors.put("identityLinksForTask:" + t.getId(), e.getClass().getName() + ": " + e.getMessage());
                    }
                    one.put("candidateUsers", candidateUsers);
                    signTaskSamples.add(one);
                }
            }
            result.put("unassignedSignTaskSamples", signTaskSamples);
        } catch (Exception e) {
            errors.put("signTaskSamples", e.getClass().getName() + ": " + e.getMessage());
        }
        return result;
    }

    /**
     * 获取合法的候选人身份集合
     * 该方法通过多种方式查询法务部门的相关人员，包括精确匹配和模糊匹配
     * 返回一个去除空值和空字符串后的候选人身份集合
     *
     * @return 包含合法候选人身份的Set集合，保持插入顺序
     */
    private Set<String> getLegalCandidateIdentities() {
        // 使用LinkedHashSet保持插入顺序
        Set<String> candidates = new LinkedHashSet<>();
        // 尝试精确匹配法务部的昵称
        try {
            safeAddAll(candidates, checkInMapper.selectNickNamesByDeptName("法务部"));
        } catch (Exception ignored) {
            // 忽略异常，继续执行
        }
        // 尝试精确匹配法务部的用户名
        try {
            safeAddAll(candidates, checkInMapper.selectUserNamesByDeptName("法务部"));
        } catch (Exception ignored) {
            // 忽略异常，继续执行
        }

        // 如果精确匹配结果为空，尝试模糊匹配
        if (candidates.isEmpty()) {
            // 尝试模糊匹配法务相关的昵称
            try {
                safeAddAll(candidates, checkInMapper.selectNickNamesByDeptNameLike("法务"));
            } catch (Exception ignored) {
                // 忽略异常，继续执行
            }
            // 尝试模糊匹配法务相关的用户名
            try {
                safeAddAll(candidates, checkInMapper.selectUserNamesByDeptNameLike("法务"));
            } catch (Exception ignored) {
                // 忽略异常，继续执行
            }
        }
        // 尝试模糊匹配法务相关的角色昵称
        try {
            safeAddAll(candidates, checkInMapper.selectNickNamesByRoleNameLike("法务"));
        } catch (Exception ignored) {
            // 忽略异常，继续执行
        }
        // 尝试模糊匹配法务相关的角色用户名
        try {
            safeAddAll(candidates, checkInMapper.selectUserNamesByRoleNameLike("法务"));
        } catch (Exception ignored) {
            // 忽略异常，继续执行
        }

        // 移除集合中的空值和空字符串
        candidates.removeIf(s -> s == null || s.trim().isEmpty());
        return candidates;
    }

    /**
     * 安全地将List中的非空字符串元素添加到Set中
     * 方法会处理null值和空字符串，确保只添加有效的字符串
     * @param target 目标Set集合，不能为null
     * @param values 源List集合，不能为null
     */
    private void safeAddAll(Set<String> target, List<String> values) {
        // 如果任一参数为null，直接返回
        if (target == null || values == null) return;
        // 遍历List中的每个元素
        for (String v : values) {
            // 如果元素为null，跳过当前循环
            if (v == null) continue;
            // 去除字符串两端的空白字符
            String trimmed = v.trim();
            // 如果处理后的字符串不为空，则添加到Set中
            if (!trimmed.isEmpty()) target.add(trimmed);
        }
    }
    /**
     * 检查候选用户集合中是否有任何用户与给定的身份链接列表匹配
     * @param candidates 候选用户ID集合，用于匹配
     * @param links 身份链接列表，包含用户与任务的身份关系
     * @return 如果存在匹配的候选用户则返回true，否则返回false
     */
    private boolean anyCandidateMatches(Set<String> candidates, List<IdentityLink> links) {
        // 检查输入参数是否为空或空集合，如果是则直接返回false
        if (candidates == null || candidates.isEmpty() || links == null || links.isEmpty()) return false;
        // 遍历身份链接列表
        for (IdentityLink link : links) {
            // 如果当前链接为空，则跳过本次循环
            if (link == null) continue;
            // 如果当前链接的类型不是"candidate"，则跳过本次循环
            if (!"candidate".equals(link.getType())) continue;
            // 获取当前链接的用户ID
            String userId = link.getUserId();
            // 如果用户ID为空，则跳过本次循环
            if (userId == null) continue;
            // 检查当前用户ID是否匹配候选集合中的任何一个ID，如果匹配则返回true
            if (matchesAnyIdentity(userId, candidates)) return true;
        }
        // 遍历完所有链接后没有找到匹配的候选用户，返回false
        return false;
    }
    /**
     * 检查集合a中是否包含集合b中的任何一个元素
     * @param a 第一个字符串集合
     * @param b 第二个字符串集合
     * @return 如果a或b为null或空集合，返回false；如果a中包含b中的任何一个元素，返回true；否则返回false
     */
    private boolean containsAny(Set<String> a, Set<String> b) {
        // 检查任一集合为null或空集合的情况
        if (a == null || a.isEmpty() || b == null || b.isEmpty()) return false;
        // 遍历集合a中的每个元素
        for (String v : a) {
            // 检查元素不为null且存在于集合b中
            if (v != null && b.contains(v)) return true;
        }
        return false;
    }
    /**
     * 确保未分配的签核任务具有合法的候选人
     * @param legalCandidates 合法的候选人集合
     */
    private void ensureLegalCandidatesOnUnassignedSignTasks(Set<String> legalCandidates) {
        // 如果合法候选人为空，直接返回
        if (legalCandidates.isEmpty()) return;

        // 查询所有未分配的签核任务
        List<Task> signTasks = taskService.createTaskQuery()
                .processDefinitionKey("checkin")  // 设置流程定义键为"checkin"
                .taskDefinitionKey("sign")       // 设置任务定义键为"sign"
                .taskUnassigned()                // 只查询未分配的任务
                .list();
        // 如果没有找到任务，直接返回
        if (signTasks == null || signTasks.isEmpty()) return;

        // 遍历所有签核任务
        for (Task task : signTasks) {
            // 如果任务或任务ID为空，跳过当前任务
            if (task == null || task.getId() == null) continue;
            // 获取任务的所有身份链接
            List<IdentityLink> links = taskService.getIdentityLinksForTask(task.getId());
            boolean hasAnyCandidate = false;
            // 检查任务是否有任何候选人
            if (links != null) {
                for (IdentityLink link : links) {
                    // 如果链接有效且类型为"candidate"，并且用户ID不为空，则标记为有候选人
                    if (link != null && "candidate".equals(link.getType()) && link.getUserId() != null && !link.getUserId().trim().isEmpty()) {
                        hasAnyCandidate = true;
                        break;
                    }
                }
            }

            // 检查是否已有合法的候选人
            boolean legalAlreadyIncluded = anyCandidateMatches(legalCandidates, links);
            // 如果任务没有候选人或没有包含合法的候选人，则添加合法候选人
            if (!hasAnyCandidate || !legalAlreadyIncluded) {
                // 收集现有的候选人
                Set<String> existingCandidates = new HashSet<>();
                if (links != null) {
                    for (IdentityLink link : links) {
                        if (link == null) continue;
                        if (!"candidate".equals(link.getType())) continue;
                        String userId = link.getUserId();
                        if (userId == null) continue;
                        // 处理可能包含多个用户ID的情况（用逗号分隔）
                        if (userId.contains(",")) {
                            for (String part : userId.split(",")) {
                                if (part != null && !part.trim().isEmpty()) existingCandidates.add(part.trim());
                            }
                        } else if (!userId.trim().isEmpty()) {
                            existingCandidates.add(userId.trim());
                        }
                    }
                }
                // 添加合法的候选人到任务中
                for (String candidate : legalCandidates) {
                    if (candidate == null || candidate.trim().isEmpty()) continue;
                    if (existingCandidates.contains(candidate.trim())) continue;
                    taskService.addCandidateUser(task.getId(), candidate.trim());
                }
            }
        }
    }
    @Override
    @Transactional
    public CheckInFlowDTO getCheckInInfo(Long checkInId) {//“操作之后，流程现在到哪了？”

        CheckIn checkIn = checkInMapper.selectCheckInById(checkInId);
        if (checkIn == null) {
            throw new RuntimeException("入住办理不存在");
        }

        // 查当前流程任务
        Task task = taskService.createTaskQuery()
                .processInstanceBusinessKey("checkin:" + checkInId)
                .singleResult();

        CheckInFlowDTO dto = new CheckInFlowDTO();
        dto.setCheckInId(checkIn.getId());
        dto.setFlowStatus(checkIn.getFlowStatus());

        if (task != null) {
            dto.setCurrentTaskKey(task.getTaskDefinitionKey());
            dto.setCurrentTaskName(task.getName());
            dto.setTaskId(task.getId());
            dto.setAssignee(task.getAssignee());
        } else {
            dto.setCurrentTaskKey(null);
            dto.setCurrentTaskName("流程已结束");
        }

        return dto;
    }

    @Override
    @Transactional
    public CheckInDetailDTO getCheckInDetail(Long checkInId) {//“我打开这个入住申请页面，需要哪些数据？”

        CheckIn checkIn = checkInMapper.selectCheckInById(checkInId);
        if (checkIn == null) {
            throw new RuntimeException("入住申请不存在");
        }

        CheckInDetailDTO dto = new CheckInDetailDTO();

        // 基础业务数据
        dto.setCheckInId(checkIn.getId());
        dto.setTitle(checkIn.getTitle());
        dto.setElderName(checkIn.getElderName());
        dto.setOtherApplyInfo(checkIn.getOtherApplyInfo());
        dto.setReviewInfo(checkIn.getReviewInfo());
        dto.setFlowStatus(checkIn.getFlowStatus());
        dto.setStatus(checkIn.getStatus());

        //  当前流程节点
        dto.setCurrentTaskKey(checkIn.getCurrentTaskId());
        dto.setCurrentTaskName(checkIn.getCurrentTaskName());
        dto.setProcessId(checkIn.getProcessId());

        // 当前登录人权限判断
        LoginUser loginUser = SecurityUtils.getLoginUser();

        boolean canHandle = false;
        String taskId = null;

        if (StringUtils.isNotBlank(checkIn.getProcessId())) {
            Task task = taskService.createTaskQuery()
                    .processInstanceId(checkIn.getProcessId())
                    .taskCandidateOrAssigned(loginUser.getUsername())
                    .singleResult();

            if (task != null) {
                canHandle = true;
                taskId = task.getId();
            }
        }

        dto.setCanHandle(canHandle);//是否可以处理
        dto.setTaskId(taskId);

        return dto;
    }

    //重新申请（处理被驳回的申请）
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CheckInFlowDTO reapplyCheckIn(Long checkInId, String otherInfo, String reviewInfo) {
        // 1. 获取原入住申请信息
        CheckIn checkIn = checkInMapper.selectCheckInById(checkInId);
        if (checkIn == null) {
            throw new RuntimeException("入住申请不存在");
        }

        // 2. 检查当前状态是否为已驳回（状态4）
        if (!checkIn.getStatus().equals(4L)) {
            throw new RuntimeException("只有被驳回的申请才能重新提交");
        }

        String businessKey = "checkin:" + checkIn.getId();
        Task task = taskService.createTaskQuery()
                .processInstanceBusinessKey(businessKey)
                .taskDefinitionKey("apply")
                .singleResult();
        if (task == null) {
            throw new RuntimeException("当前重新申请任务不存在或已处理");
        }

        // 3. 更新申请信息
        checkIn.setOtherApplyInfo(otherInfo);
        checkIn.setReviewInfo(reviewInfo);
        checkIn.setStatus(0L); // 重置为待审批

        // 4. 完成 apply 任务，让流程继续往下走（evaluate）
        taskService.complete(task.getId());

        Task nextTask = taskService.createTaskQuery()
                .processInstanceBusinessKey(businessKey)
                .singleResult();

        if (nextTask != null) {
            checkIn.setCurrentTaskId(nextTask.getTaskDefinitionKey());
            checkIn.setCurrentTaskName(nextTask.getName());
            if ("入住评估-处理".equals(nextTask.getName())) {
                checkIn.setFlowStatus(1L);
            } else if ("副院长审批-处理".equals(nextTask.getName())) {
                checkIn.setFlowStatus(2L);
            } else if ("入住选配-处理".equals(nextTask.getName())) {
                checkIn.setFlowStatus(3L);
            } else if ("签约办理-处理".equals(nextTask.getName())) {
                checkIn.setFlowStatus(4L);
            } else if ("发起入住申请".equals(nextTask.getName())) {
                checkIn.setFlowStatus(0L);
            }
        } else {
            checkIn.setFlowStatus(5L);
            checkIn.setCurrentTaskId("finish");
            checkIn.setCurrentTaskName("流程已结束");
        }

        // 5. 更新数据库
        checkInMapper.updateCheckIn(checkIn);

        // 6. 返回流程信息
        CheckInFlowDTO dto = new CheckInFlowDTO();
        dto.setCheckInId(checkInId);
        dto.setFlowStatus(checkIn.getFlowStatus());
        dto.setCurrentTaskKey(checkIn.getCurrentTaskId());
        dto.setCurrentTaskName(checkIn.getCurrentTaskName());
        dto.setTaskId(nextTask == null ? null : nextTask.getId());

        return dto;
    }

    //查看入住申请详情
    @Override
    @Transactional
    public CheckIn viewCheckInDetail(Long checkInId) {
        return checkInMapper.selectCheckInById(checkInId);
    }


    //完成签约(结束流程)
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CheckInFlowDTO completeContract(Long checkInId, Map<String, Object> signInfo) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        CheckIn checkIn = checkInMapper.selectCheckInById(checkInId);
        if (checkIn == null) {
            throw new RuntimeException("入住办理不存在");
        }

        // 检查当前状态是否为签约办理阶段
        if (checkIn.getFlowStatus() != 4L) {
            throw new RuntimeException("当前入住办理不在签约办理阶段，无法进行操作");
        }
        String businessKey = "checkin:" + checkIn.getId();
        // 查询当前评估任务
        Task task = taskService.createTaskQuery()
                .processInstanceBusinessKey(businessKey)
                .taskDefinitionKey("sign")
                .singleResult();

        if (task == null) {
            throw new RuntimeException("当前签约任务不存在或已处理");
        }

        if (signInfo == null) {
            signInfo = new HashMap<>();
        }

        Date signDate = DateUtils.parseDate(signInfo.get("signDate"));
        if (signDate == null) {
            throw new RuntimeException("签约日期不能为空");
        }

        String contractPeriodStr = String.valueOf(signInfo.getOrDefault("contractPeriod", "")).trim();
        if (StringUtils.isBlank(contractPeriodStr)) {
            throw new RuntimeException("合同期限不能为空");
        }
        int contractPeriod;
        try {
            contractPeriod = Integer.parseInt(contractPeriodStr);
        } catch (Exception e) {
            throw new RuntimeException("合同期限不合法");
        }

        String contractNo = String.valueOf(signInfo.getOrDefault("contractNo", "")).trim();
        if (StringUtils.isBlank(contractNo)) {
            contractNo = checkIn.getCheckInCode();
        }
        String signer = String.valueOf(signInfo.getOrDefault("signer", "")).trim();
        String signerPhone = String.valueOf(signInfo.getOrDefault("signerPhone", "")).trim();
        String pdfUrl = String.valueOf(signInfo.getOrDefault("pdfUrl", "")).trim();
        if (StringUtils.isBlank(pdfUrl)) {
            pdfUrl = String.valueOf(signInfo.getOrDefault("contractFileUrl", "")).trim();
        }

        Date startTime = signDate;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startTime);
        calendar.add(Calendar.YEAR, contractPeriod == 99 ? 99 : contractPeriod);
        Date endTime = calendar.getTime();

        if (checkIn.getElderId() == null) {
            throw new RuntimeException("老人信息缺失，无法生成合同");
        }

        Contractt contract = new Contractt();
        contract.setName((StringUtils.isNotBlank(checkIn.getElderName()) ? checkIn.getElderName() : "老人") + "入住合同");
        contract.setContractNo(contractNo);
        if (StringUtils.isNotBlank(pdfUrl)) {
            contract.setPdfUrl(pdfUrl);
        }
        if (StringUtils.isNotBlank(signerPhone)) {
            contract.setMemberPhone(signerPhone);
        }
        contract.setElderId(checkIn.getElderId());
        contract.setElderName(checkIn.getElderName());
        if (StringUtils.isNotBlank(signer)) {
            contract.setMemberName(signer);
        }
        contract.setStartTime(startTime);
        contract.setEndTime(endTime);
        contract.setStatus(0L);
        contract.setCheckInNo(checkIn.getCheckInCode());
        contract.setSignDate(signDate);
        contract.setRemark(String.valueOf(signInfo.getOrDefault("remark", "")).trim());
        if (loginUser != null && loginUser.getUser() != null) {
            contract.setCreateBy(loginUser.getUser().getUserName());
            contract.setUpdateBy(loginUser.getUser().getUserName());
        }

        contractMapper.insertContract(contract);

        // 完成签约任务
        taskService.complete(task.getId());

        Task nextTask = taskService.createTaskQuery()
                .processInstanceBusinessKey(businessKey)
                .singleResult();

        if (nextTask == null) {
            checkIn.setFlowStatus(5L);
            checkIn.setCurrentTaskId("finish");
            checkIn.setCurrentTaskName("流程已结束");
            checkIn.setStatus(2L);
        } else {
            checkIn.setCurrentTaskId(nextTask.getTaskDefinitionKey());
            checkIn.setCurrentTaskName(nextTask.getName());
        }
        checkInMapper.updateCheckIn(checkIn);

        CheckInFlowDTO dto = new CheckInFlowDTO();
        dto.setCheckInId(checkIn.getId());
        dto.setFlowStatus(checkIn.getFlowStatus());
        dto.setCurrentTaskKey(checkIn.getCurrentTaskId());
        dto.setCurrentTaskName(checkIn.getCurrentTaskName());
        dto.setTaskId(nextTask == null ? null : nextTask.getId());
        return dto;

    }

    }


