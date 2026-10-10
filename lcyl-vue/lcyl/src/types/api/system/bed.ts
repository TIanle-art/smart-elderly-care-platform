import type { PageDomain, BaseEntity } from "../common";

/** 床位表配置分页查询参数 */
export interface BedQueryParams extends PageDomain {
  /** 床位号 */
  bedNumber?: string;
  /** 床位状态 0 = 空闲
1 = 已入住
2 = 请假 / 待入住 */
  bedStatus?: number;
  /** 所属房间ID */
  roomId?: number;
  /** 护理员姓名 */
  lname?: string;
  /** 老人姓名 */
  name?: string;
  /** 老人ID */
  elderId?: number;
}

/** 床位表配置信息 */
export interface Bed extends BaseEntity {
  /** 床位ID */
  id?: number;
  /** 床位号 */
  bedNumber?: string;
  /** 床位状态 0 = 空闲
1 = 已入住
2 = 请假 / 待入住 */
  bedStatus?: number;
  /** 所属房间ID */
  roomId?: number;
  /** 护理员姓名 */
  lname?: string;
  /** 老人姓名 */
  name?: string;
  /** 老人ID */
  elderId?: number;
}
