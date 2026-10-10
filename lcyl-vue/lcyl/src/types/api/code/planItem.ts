import type { PageDomain, BaseEntity } from "../common";

/** 护理计划-项目关联配置分页查询参数 */
export interface ItemQueryParams extends PageDomain {
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
