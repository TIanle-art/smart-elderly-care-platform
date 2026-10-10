package com.lcyl.code.service.impl;

import java.util.Date;
import java.util.List;
import com.lcyl.code.domain.CheckInConfigg;
//import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.lcyl.code.service.IBillService;
import com.lcyl.common.core.domain.model.LoginUser;
import com.lcyl.common.utils.DateUtils;
import com.lcyl.common.utils.SecurityUtils;
import com.lcyl.code.domain.Balance;
import com.lcyl.code.domain.CheckIn;
import com.lcyl.code.domain.dto.CheckInFlowDTO;
import com.lcyl.code.vo.ConfigVo;
import com.lcyl.code.mapper.BalanceMapper;
import com.lcyl.code.mapper.BeddMapper;
import com.lcyl.code.mapper.CheckInMapper;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.lcyl.code.mapper.CheckInConfiggMapper;
import com.lcyl.code.mapper.ElderrMapper;
import com.lcyl.code.service.ICheckInConfigService;
import org.springframework.util.Assert;

import java.math.BigDecimal;

//import com.lcyl.code.service.INursingTaskService;

/**
 * 入住配置表Service业务层处理
 *
 * @author ruoyi
 * @date 2026-02-01
 */
@Service
public class CheckInConfigServiceImpl implements ICheckInConfigService
{
    @Autowired
    private CheckInConfiggMapper checkInConfigMapper;
    @Autowired
    private ElderrMapper elderMapper;

    @Autowired
    private BeddMapper bedMapper;
//    @Autowired
//    private INursingTaskService nursingTaskService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private CheckInMapper checkInMapper;
    @Autowired
    private BalanceMapper balanceMapper;
//    @Autowired
//    private BassService billService;
    @Autowired
    private IBillService billService;





    /**
     * 查询入住配置表
     *
     * @param id 入住配置表主键
     * @return 入住配置表
     */
    @Override
    public CheckInConfigg selectCheckInConfigById(Long id)
    {
        return checkInConfigMapper.selectCheckInConfigById(id);
    }

    /**
     * 查询入住配置表列表
     *
     * @param checkInConfig 入住配置表
     * @return 入住配置表
     */
    @Override
    public List<CheckInConfigg> selectCheckInConfigList(CheckInConfigg checkInConfig)
    {
        return checkInConfigMapper.selectCheckInConfigList(checkInConfig);
    }

    /**
     * 新增入住配置表
     *
     * @param checkInConfig 入住配置表
     * @return 结果
     */


    @Override
    public int insertCheckInConfig(CheckInConfigg checkInConfig)
    {
        checkInConfig.setCreateTime(DateUtils.getNowDate());
        return checkInConfigMapper.insertCheckInConfig(checkInConfig);
    }

    /**
     * 修改入住配置表
     *
     * @param checkInConfig 入住配置表
     * @return 结果
     */
    @Override
    public int updateCheckInConfig(CheckInConfigg checkInConfig)
    {
        checkInConfig.setUpdateTime(DateUtils.getNowDate());
        return checkInConfigMapper.updateCheckInConfig(checkInConfig);
    }

    /**
     * 批量删除入住配置表
     *
     * @param ids 需要删除的入住配置表主键
     * @return 结果
     */
    @Override
    public int deleteCheckInConfigByIds(Long[] ids)
    {
        return checkInConfigMapper.deleteCheckInConfigByIds(ids);
    }

    /**
     * 删除入住配置表信息
     *
     * @param id 入住配置表主键
     * @return 结果
     */
    @Override
    public int deleteCheckInConfigById(Long id)
    {
        return checkInConfigMapper.deleteCheckInConfigById(id);
    }
    /**
     * 提交入住配置：保存到入住配置表，并将老人关联到所选床位（占用床位）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CheckInFlowDTO submitConfig(CheckInConfigg checkInConfig)
    {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        checkInConfig.setCreateBy(String.valueOf(loginUser.getUserId()));
        CheckIn checkIn = checkInMapper.selectCheckInById(checkInConfig.getCheckInId());
        checkIn.setCheckInTime(checkInConfig.getCheckInStartTime());
        if (checkIn == null) {
            throw new RuntimeException("入住办理不存在");
        }

        // 检查当前状态是否为入住配置阶段
        if (checkIn.getFlowStatus() != 3L) {
            throw new RuntimeException("当前入住办理不在配置阶段，无法进行配置操作");
        }
        String businessKey = "checkin:" + checkIn.getId();
        // 查询当前评估任务
        Task task = taskService.createTaskQuery()
                .processInstanceBusinessKey(businessKey)
                .taskDefinitionKey("config")
                .singleResult();

        if (task == null) {
            throw new RuntimeException("当前配置任务不存在或已处理");
        }

        // 完成配置任务
        taskService.complete(task.getId());

        //更新床位信息为占用状态
        bedMapper.updateBedStatus(checkInConfig.getBedId(), 1);
        Integer elderId = checkInConfig.getElderId().intValue();
        Double depositAmount = checkInConfig.getDepositAmount().doubleValue();

        //保存入住配置
        checkInConfig.setCreateTime(DateUtils.getNowDate());
        int rows = checkInConfigMapper.insertCheckInConfig(checkInConfig);
        if (rows > 0 && checkInConfig.getElderId() != null && checkInConfig.getBedId() != null) {
            elderMapper.updateBedId(checkInConfig.getElderId(), checkInConfig.getBedId());
            elderMapper.updateBedNo(checkInConfig.getElderId(), checkInConfig.getBedNo());
        }

        //保存到bill
//        billService.firstMonthBillForElder(elderId,depositAmount);

        //保存到余额
        Balance balance = new Balance();
        balance.setElderId(checkInConfig.getElderId());
        balance.setElderName(checkIn.getElderName());
        balance.setBedNo(checkInConfig.getBedNo());
        balance.setStatus(0L);
        balance.setCreateTime(DateUtils.getNowDate());
        balance.setUpdateTime(DateUtils.getNowDate());
        balance.setUpdateBy(String.valueOf(loginUser.getUserId()));
        balance.setPrepaidBalance(BigDecimal.ZERO);//预存款
        balance.setDepositAmount(BigDecimal.valueOf(depositAmount));//押金
        balance.setCreateBy(String.valueOf(loginUser.getUserId()));
        balanceMapper.insertBalance(balance);

//        if (rows > 0 && checkInConfig.getElderId() != null) {
//            Balance existing = balanceMapper.selectBalanceByUserId(checkInConfig.getElderId());
//
//            Balance balance = new Balance();
//            balance.setElderId(checkInConfig.getElderId());
//            balance.setElderName(checkIn.getElderName());
//            balance.setBedNo(checkInConfig.getBedNo());
//            balance.setStatus(2);
//            balance.setUpdateTime(DateUtils.getNowDate());
//            balance.setUpdateBy(loginUser.getUserId());
//
//            if (existing == null) {
//                balance.setPrepaidBalance(BigDecimal.ZERO);
//                balance.setDepositAmount(BigDecimal.ZERO);
//                balance.setArrearsAmount(BigDecimal.ZERO);
//                balance.setCreateTime(DateUtils.getNowDate());
//                balance.setCreateBy(loginUser.getUserId());
//                balanceMapper.insertBalance(balance);
//            } else {
//                balanceMapper.updateBalanceByElderId(balance);
//            }
//        }

        // 查询下一个任务(qia签约办理-处理)
        Task nextTask = taskService.createTaskQuery()
                .processInstanceBusinessKey(businessKey)
                .singleResult();

        if (nextTask != null) {
            checkIn.setCurrentTaskId(nextTask.getTaskDefinitionKey());
            checkIn.setCurrentTaskName(nextTask.getName());
            checkIn.setFlowStatus(4L); // 4:入住合同确认阶段
        } else {
            checkIn.setCurrentTaskName("流程已结束");
            checkIn.setStatus(2L);
        }

        checkInMapper.updateCheckIn(checkIn);



//        // 生成护理计划模板（nursing_task.status=4, task_type=2）
//        try {
//            nursingTaskService.generatePlanTemplatesForElder(checkInConfig.getElderId(), checkInConfig.getNursingLevelId());
//        } catch (Exception e) {
//            // 不中断主流程，仅记录
//
//        }
        CheckInFlowDTO dto = new CheckInFlowDTO();
        dto.setCheckInId(checkIn.getId());
        dto.setFlowStatus(checkIn.getFlowStatus());
        dto.setCurrentTaskKey(checkIn.getCurrentTaskId());
        dto.setCurrentTaskName(checkIn.getCurrentTaskName());
        return dto;


//
//        //更新床位信息为占用状态
//        bedMapper.updateBedStatus(checkInConfig.getBedId(), 1);
//
//        //保存入住配置
//        checkInConfig.setCreateTime(DateUtils.getNowDate());
//        int rows = checkInConfigMapper.insertCheckInConfig(checkInConfig);
//        if (rows > 0 && checkInConfig.getElderId() != null && checkInConfig.getBedId() != null) {
//            elderMapper.updateBedId(checkInConfig.getElderId(), checkInConfig.getBedId());
//        }
//        return new CheckInFlowDTO();
    }
    //根据老人id查询入住配置
    @Override
    public ConfigVo selectCheckInConfigByElderId(Integer elderId, String billMonth, Date startTime, Date endTime) {
        // 1. 入参非空校验，避免无效查询
        Assert.notNull(elderId, "老人ID不能为空");
        Assert.notNull(billMonth, "账单月份不能为空");
        Assert.notNull(startTime, "开始时间不能为空");
        Assert.notNull(endTime, "结束时间不能为空");

        // 2. 调用Mapper查询原始数据（已包含：护理费/床位费等原始金额 + totalDays/monthTotalDays天数）
        ConfigVo configVo = checkInConfigMapper.selectCheckInConfigByElderId(startTime, endTime, elderId, billMonth);
        Assert.notNull(configVo, "未查询到该老人的入住配置数据");

        // 3. 所有参与计算的字段做NULL兜底（Double置0.0，Integer天数置0/1）
        Double nursingCost = configVo.getNursingCost() == null ? 0.0 : configVo.getNursingCost();
        Double bedCost = configVo.getBedCost() == null ? 0.0 : configVo.getBedCost();
        Double otherCost = configVo.getOtherCost() == null ? 0.0 : configVo.getOtherCost();
        Double medicalPayment = configVo.getMedicalPayment() == null ? 0.0 : configVo.getMedicalPayment();
        Double governmentSubsidy = configVo.getGovernmentSubsidy() == null ? 0.0 : configVo.getGovernmentSubsidy();
        // 天数字段兜底：共计天数置0，月份总天数置1（除零保护，避免除数为0）
        int totalDays = configVo.getTotalDays() == null ? 0 : configVo.getTotalDays();
        int monthTotalDays = configVo.getMonthTotalDays() == null ? 1 : configVo.getMonthTotalDays();

        // 4. 严格按7条规则分步计算（纯Double原生计算，无工具类）
        // 规则1+3：第一个小计firstSum = 护理+床位+其他（添加项总和）
        Double firstSum = nursingCost + bedCost + otherCost;
        // 规则2+3：第二个小计secondSum = 医保支付+政府补贴（扣减项总和）
        Double secondSum = medicalPayment + governmentSubsidy;
        // 规则4：每月应付monthlyPayment = 添加项总和 - 扣减项总和
        Double monthlyPayment = firstSum - secondSum;
        // 规则5：本期应付currentCost = 共计天数/月份总天数 * 每月应付（*1.0保证浮点除法）
        Double currentCost = (totalDays * 1.0 / monthTotalDays) * monthlyPayment;

        // 5. 财务业务兜底：所有金额不能为负（预缴款足够/扣减超支时，金额置0，符合业务逻辑）
        firstSum = Math.max(firstSum, 0.0);
        secondSum = Math.max(secondSum, 0.0);
        monthlyPayment = Math.max(monthlyPayment, 0.0);
        currentCost = Math.max(currentCost, 0.0);

        // 6. 将计算后的所有属性赋值到ConfigVo，供前端展示
        configVo.setFirstSum(firstSum);
        configVo.setSecondSum(secondSum);
        configVo.setMonthlyPayment(monthlyPayment);
        configVo.setCurrentCost(currentCost);

        // 返回赋值完成的Vo
        return configVo;
    }
}
