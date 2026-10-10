import type { PageDomain, BaseEntity } from "../common";

/** 来访登记配置分页查询参数 */
export interface ArrivalQueryParams extends PageDomain {
  /** 来访类别 */
  type?: number;
  /** 来访人姓名 */
  name?: string;
  /** 来访人手机号 */
  phone?: string;
  /** 老人姓名 */
  elderName?: string;
  /** 来访时间 */
  visitTime?: string;
  /** 创建人 */
  createPerson?: string;
}

/** 来访登记配置信息 */
export interface Arrival extends BaseEntity {
  /** 序号 */
  id?: number;
  /** 来访类别 */
  type?: number;
  /** 来访人姓名 */
  name?: string;
  /** 来访人手机号 */
  phone?: string;
  /** 老人姓名 */
  elderName?: string;
  /** 来访时间 */
  visitTime?: string;
  /** 创建人 */
  createPerson?: string;
  /** 创建时间 */
  createTime?: string;
}
