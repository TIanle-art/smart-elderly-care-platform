import type { PageDomain, BaseEntity } from "../common";

/** 余额配置分页查询参数 */
export interface BalanceQueryParams extends PageDomain {
  /** 欠费金额（元） */
  arrearsAmount?: string;
  /** 老人ID */
  elderId?: number;
  arrearsOnly?: string;
  /** 老人姓名 */
  elderName?: string;
  /** 床位号 */
  bedNo?: string;
}

/** 余额配置信息 */
export interface Balance extends BaseEntity {
  /** 主键 */
  id?: number;
  /** 预交款余额 */
  prepaidBalance?: string;
  /** 押金金额 */
  depositAmount?: string;
  /** 欠费金额（元） */
  arrearsAmount?: string;
  /** 支付截止时间 */
  paymentDeadline?: string;
  /** 状态（0：正常，1：退住  2：入住未缴押金） */
  status?: number;
  /** 老人ID */
  elderId?: number;
  /** 老人姓名 */
  elderName?: string;
  /** 床位号 */
  bedNo?: string;
  /** 创建时间 */
  createTime?: string;
  /** 更新时间 */
  updateTime?: string;
  /** 创建人id */
  createBy?: number;
  /** 更新人id */
  updateBy?: number;
  /** 备注 */
  remark?: string;
  /** 删除标志(0存在 2删除) */
  delFlag?: string;
}
