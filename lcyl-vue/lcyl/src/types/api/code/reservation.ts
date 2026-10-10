import type { PageDomain, BaseEntity } from "../common";

/** 预约来访配置分页查询参数 */
export interface ReservationQueryParams extends PageDomain {
  /** 访问类别 */
  type?: string;
  /** 预约人名 */
  name?: string;
  /** 预约人电话 */
  phone?: string;
  /** 老人姓名 */
  olderName?: string;
  /** 预约时间 */
  appointmentTime?: string;
  /** 创建人 */
  createPeople?: string;
  /** 状态 */
  status?: number;
}

/** 预约来访配置信息 */
export interface LcReservation extends BaseEntity {
  /** id */
  id?: number;
  /** 访问类别 */
  type?: string;
  /** 预约人名 */
  name?: string;
  /** 预约人电话 */
  phone?: string;
  /** 老人姓名 */
  olderName?: string;
  /** 预约时间 */
  appointmentTime?: string;
  /** 创建人 */
  createPeople?: string;
  /** 创建时间 */
  createTime?: string;
  /** 状态 */
  status?: number;
}
