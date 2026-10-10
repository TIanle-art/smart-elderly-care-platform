import type { PageDomain, BaseEntity } from "../common";

/** 护理等级配置分页查询参数 */
export interface NursingLevelQueryParams extends PageDomain {
  /** 护理等级名称（唯一） */
  levelName?: string;
  /** 状态：1-启用，0-禁用 */
  status?: number;
}

/** 护理等级配置信息 */
export interface NursingLevel extends BaseEntity {
  /** 主键ID */
  id?: number;
  /** 护理等级名称（唯一） */
  levelName?: string;
  /** 关联护理计划ID */
  planId?: number;
  /** 每月护理费用 */
  monthlyFee?: string;
  /** 等级说明 */
  levelDesc?: string;
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
}
