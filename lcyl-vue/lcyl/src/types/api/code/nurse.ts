import type { PageDomain, BaseEntity } from "../common";

/** 护理员信息配置分页查询参数 */
export interface NurseQueryParams extends PageDomain {
  /** 护理员姓名 */
  name?: string;
  /** 联系电话 */
  phone?: string;
}

/** 护理员信息配置信息 */
export interface Nurse extends BaseEntity {
  /** 主键ID */
  id?: number;
  /** 护理员姓名 */
  name?: string;
  /** 联系电话 */
  phone?: string;
  /** 创建时间 */
  createTime?: string;
}
