import type { PageDomain, BaseEntity } from "../common";

/** 账单列表配置分页查询参数 */
export interface BillQueryParams extends PageDomain {
  /** 账单编号 */
  billNo?: string;
  /** 老人姓名 */
  elderName?: string;
  /** 老人身份证号 */
  elderIdCard?: string;
  /** 交易状态(0待支付 1已支付 2已关闭) */
  tradeStatus?: string;
}

/** 账单列表配置信息 */
export interface Bill extends BaseEntity {
  /** 主键ID */
  id?: number;
  /** 账单编号 */
  billNo?: string;
  /** 账单类型(1月度账单 2费用账单) */
  billType?: string;
  /** 账单月份yyyy-MM */
  billMonth?: string;
  /** 老人ID */
  elderId?: number;
  /** 老人姓名 */
  elderName?: string;
  /** 老人身份证号 */
  elderIdCard?: string;
  /** 床位ID */
  bedId?: number;
  /** 床位号 */
  bedNo?: string;
  /** 账单金额 */
  billAmount?: string;
  /** 应付金额 */
  payableAmount?: string;
  /** 交易状态(0待支付 1已支付 2已关闭) */
  tradeStatus?: string;
  /** 支付截止时间 */
  payDeadline?: string;
  /** 账单周期开始日期 */
  startDate?: string;
  /** 账单周期结束日期 */
  endDate?: string;
  /** 共计天数 */
  daysCount?: number;
  /** 创建人ID */
  creatorId?: number;
  /** 创建人姓名 */
  creatorName?: string;
  /** 取消原因 */
  cancelReason?: string;
  /** 取消时间 */
  cancelTime?: string;
  /** 备注 */
  remark?: string;
  /** 删除标志(0存在 2删除) */
  delFlag?: string;
  /** 创建者 */
  createBy?: string;
  /** 创建时间 */
  createTime?: string;
  /** 更新者 */
  updateBy?: string;
  /** 更新时间 */
  updateTime?: string;
}


// 服务订单 - 实体对象（单独定义）
export interface ServiceOrder extends BaseEntity {
  id?: number;
  orderNo?: string;
  elderId?: number;
  elderName?: string;
  bedNo?: string;
  projectId?: number;
  projectName?: string;
  orderAmount?: string;
  expectedServiceTime?: string;
  orderStatus?: string;
  tradeStatus?: string;
}


