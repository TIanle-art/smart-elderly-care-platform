package com.lcyl.code.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lcyl.common.annotation.Excel;
import com.lcyl.common.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.activiti.engine.history.HistoricActivityInstance;

import java.util.Date;
import java.util.List;

/**
 * 退住对象 lc_retreat
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Retreat extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 退住编号 */
    @Excel(name = "退住编号")
    private String retreatCode;

    /** 标题 */
    @Excel(name = "标题")
    private String title;

    /** 老人id */
    @Excel(name = "老人id")
    private Long elderId;

    /** 姓名 */
    @Excel(name = "姓名")
    private String name;

    /** 身份证号 */
    @Excel(name = "身份证号")
    private String idCardNo;

    /** 联系方式 */
    @Excel(name = "联系方式")
    private String phone;

    /** 入住开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "入住开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date checkInStartTime;

    /** 入住结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "入住结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date checkInEndTime;

    /** 护理等级 */
    @Excel(name = "护理等级")
    private String nursingLevelName;

    /** 入住床位 */
    @Excel(name = "入住床位")
    private String bedNo;

    /** 签约合同 */
    @Excel(name = "签约合同")
    private String contractName;

    /** 合同URL */
    @Excel(name = "合同URL")
    private String contractUrl;

    /** 合同编号 */
    @Excel(name = "合同编号")
    private String contractNo;

    /** 养老顾问 */
    @Excel(name = "养老顾问")
    private String counselor;

    /** 护理员名称 */
    @Excel(name = "护理员名称")
    private String nursingName;

    /** 退住时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "退住时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date checkOutTime;

    /** 退住原因 */
    @Excel(name = "退住原因")
    private String reason;

    /** 申请人 */
    @Excel(name = "申请人")
    private String applicat;

    /** 申请人id */
    @Excel(name = "申请人id")
    private Long applicatId;

    private String applicatName;

    /** 申请人部门编号 */
    @Excel(name = "申请人部门编号")
    private String deptNo;

    private String taskId;

    /** 流程状态（0:申请退住，1:申请审批，2:解除合同，3:调整账单，4:账单审批，5:退住审批，6:费用算清） */
    @Excel(name = "流程状态", readConverterExp = "0=:申请退住，1:申请审批，2:解除合同，3:调整账单，4:账单审批，5:退住审批，6:费用算清")
    private Integer flowStatus;   // 改为 Integer 类型

    /** 状态（1：申请中，2:已完成,3:已关闭） */
    @Excel(name = "状态", readConverterExp = "1=：申请中，2:已完成,3:已关闭")
    private Integer status;       // 改为 Integer 类型

    // 非数据库字段：当前任务节点Key
    private String taskDefKey;

    // 非数据库字段：当前任务节点名称
    private String taskName;


    // 流程实例ID（非BaseEntity字段）
    private String processInstanceId;

    // 扩展字段：合同列表（非数据库字段）
    private List<LcRetreatContract> contracts;
    // 非数据库字段：流程历史活动（用于时间轴）
    private List<HistoricActivityInstance> historyActivities;

}