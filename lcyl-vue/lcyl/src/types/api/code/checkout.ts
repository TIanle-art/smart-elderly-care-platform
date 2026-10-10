import type { PageDomain, BaseEntity } from "../common";

/**
 * 解除合同实体
 */
export interface LcRetreatContract {
  id?: number;
  retreatId?: number;
  contractUrl?: string;
  contractName?: string;
  terminateDate?: string;
  createBy?: number;
  createTime?: string;
  updateBy?: number;
  updateTime?: string;
  createName?: string;  // 提交人姓名
}

/**
 * 流程历史活动（简版）
 */
export interface HistoricActivityInstance {
  id?: string;
  activityId?: string;
  activityName?: string;
  activityType?: string;
  startTime?: string;
  endTime?: string;
  duration?: number;
  assignee?: string;
}

/**
 * 退住配置分页查询参数
 */
export interface CheckoutQueryParams extends PageDomain {
  retreatCode?: string;
  title?: string;
  elderId?: number;
  name?: string;
  idCardNo?: string;
  phone?: string;
  checkInStartTime?: string;
  checkInEndTime?: string;
  nursingLevelName?: string;
  bedNo?: string;
  contractName?: string;
  contractUrl?: string;
  contractNo?: string;
  counselor?: string;
  nursingName?: string;
  checkOutTime?: string;
  reason?: string;
  applicat?: string;
  applicatId?: number;
  deptNo?: string;
  flowStatus?: number;
  status?: number;
}

/**
 * 退住主表实体
 */
export interface Retreat extends BaseEntity {
  id?: number;
  retreatCode?: string;
  title?: string;
  elderId?: number;
  name?: string;
  idCardNo?: string;
  phone?: string;
  checkInStartTime?: string;
  checkInEndTime?: string;
  nursingLevelName?: string;
  bedNo?: string;
  contractName?: string;
  contractUrl?: string;
  contractNo?: string;
  counselor?: string;
  nursingName?: string;
  checkOutTime?: string;
  reason?: string;
  applicat?: string;
  applicatId?: number;
  deptNo?: string;
  flowStatus?: number;
  status?: number;
  /** 流程实例ID（Activiti） */
  processInstanceId?: string;

  // ========== 非数据库字段（用于前端展示） ==========
  /** 当前任务ID（非数据库字段） */
  taskId?: string;
  /** 当前任务节点Key（非数据库字段） */
  taskDefKey?: string;
  /** 当前任务节点名称（非数据库字段） */
  taskName?: string;
  /** 解除合同列表（非数据库字段） */
  contracts?: LcRetreatContract[];
  /** 流程历史活动（非数据库字段，用于时间轴） */
  historyActivities?: HistoricActivityInstance[];
}

/**
 * 发起退住申请 DTO（前端提交后端用）
 */
export interface RetreatStartDTO {
  elderId?: number | string;
  checkOutTime?: string;
  reason?: string;
  remark?: string;
}

/**
 * 完成审批任务 DTO
 */
export interface RetreatTaskCompletedDTO {
  taskId?: string;
  action?: number | string;
  opinion?: string;
  businessData?: any;
}

/**
 * 法务上传合同 DTO
 */
export interface RetreatLegalDTO {
  contractUrl?: string;
  contractName?: string;
  terminateDate?: string;
}

/**
 * 结算员结算 DTO
 */
export interface RetreatSettleBillDTO {
  amount?: number;
  remark?: string;
}

/**
 * 最终清算 DTO
 */
export interface RetreatFinalDTO {
  finalAmount?: number;
  remark?: string;
}