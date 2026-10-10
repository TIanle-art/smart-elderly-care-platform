import type { PageDomain, BaseEntity } from "../common";

/** 护理计划配置分页查询参数 */
export interface PlanQueryParams extends PageDomain {
  /** 护理计划名称（唯一） */
  planName?: string;
  /** 状态：1-启用，0-禁用 */
  status?: number;
}

/** 护理计划配置信息 */
export interface NursingPlan extends BaseEntity {
  /** 主键ID */
  id?: number;
  /** 护理计划名称（唯一） */
  planName?: string;
  /** 状态：1-启用，0-禁用 */
  status?: number;
  /** 创建人ID（关联nurse.id） */
  creatorId?: number;
  /** 创建人姓名（冗余） */
  creatorName?: string;
  /** 创建时间 */
  createTime?: string;
  /** 更新时间 */
  updateTime?: string;
  /** 护理计划-项目关联信息 */
  nursingPlanItemList?: NursingPlanItem[];
  isBound?: number;
}

/** 护理计划-项目关联配置信息 */
export interface NursingPlanItem extends BaseEntity {
  /** 主键ID */
  id?: number;
  /** 护理计划ID */
  planId?: number;
  /** 护理项目ID */
  itemId?: number;
  /** 期望服务时长（分钟） */
  expectedDuration?: number;
  /** 执行周期：每日/每周/每月 */
  executeCycle?: string;
  /** 执行次数（1-7次） */
  executeTimes?: number;
}
