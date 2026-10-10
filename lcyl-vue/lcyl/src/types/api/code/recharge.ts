import type { PageDomain, BaseEntity } from "../common";

/** 预缴款充值记录配置分页查询参数 */
export interface RechargeQueryParams extends PageDomain {
  /** 充值编号 */
  rechargeNo?: string;
  /** 老人ID */
  elderId?: number;
  /** 老人姓名 */
  elderName?: string;
  /** 床位号 */
  bedNo?: string;
  /** 充值方式(微信/支付宝/网银/现金/其他) */
  rechargeMethod?: string;
  /** 状态(0成功 1作废) */
  status?: string;
  /** 创建时间 */
  createTime?: string;
}

/** 预缴款充值记录配置信息 */
export interface BalanceRecharge extends BaseEntity {
  /** 主键ID */
  id?: number;
  /** 充值编号 */
  rechargeNo?: string;
  /** 老人ID */
  elderId?: number;
  /** 老人姓名 */
  elderName?: string;
  /** 床位号 */
  bedNo?: string;
  /** 充值方式(微信/支付宝/网银/现金/其他) */
  rechargeMethod?: string;
  /** 充值金额 */
  rechargeAmount?: string;
  /** 充值凭证 */
  voucherUrl?: string;
  /** 充值备注 */
  rechargeRemark?: string;
  /** 状态(0成功 1作废) */
  status?: string;
  /** 创建者 */
  createBy?: string;
  /** 创建时间 */
  createTime?: string;
  /** 更新者 */
  updateBy?: string;
  /** 更新时间 */
  updateTime?: string;
  /** 删除标志(0存在 2删除) */
  delFlag?: string;
  /** 备注 */
  remark?: string;
}
