import type { PageDomain, BaseEntity } from "../common";

/** 订单详情配置分页查询参数 */
export interface OrdersQueryParams extends PageDomain {
  orderNo?: string;
  elderName?: string;
  applicantName?: string;
  createTime?: string;
  orderStatus?: string;
  params?: any;
}

export interface ServiceOrderExecution {
  id?: number;
  orderId?: number;
  executeStatus?: string;
  cancelReason?: string;
  cancelTime?: string;
  executeTime?: string;
  executeImage?: string;
  executeRecord?: string;
  executorId?: number;
  executorName?: string;
  createTime?: string;
  updateTime?: string;
}

/** 退款申请参数 */
export interface OrderRefundApplyForm {
  orderId?: number;
  refundReason?: string;
}

/** 订单详情配置信息 */
export interface ServiceOrder extends BaseEntity {
  /** 主键ID */
  id?: number;
  /** 订单编号 */
  orderNo?: string;
  /** 老人ID */
  elderId?: number;
  /** 老人姓名(冗余) */
  elderName?: string;
  /** 床位ID */
  bedId?: number;
  /** 床位号(冗余) */
  bedNo?: string;
  /** 护理项目ID */
  projectId?: number;
  /** 护理项目名称(冗余) */
  projectName?: string;
  /** 订单金额 */
  orderAmount?: string;
  /** 期望服务时间 */
  expectedServiceTime?: string;
  /** 下单来源(1家属端 2后台) */
  orderSource?: string;
  /** 下单人ID */
  applicantId?: number;
  /** 下单人姓名(冗余) */
  applicantName?: string;
  /** 下单人手机号 */
  applicantPhone?: string;
  /** 备注 */
  remark?: string;
  /** 订单状态(0待支付 1待执行 2已执行 3已完成 4已退款 5已关闭) */
  orderStatus?: string;
  /** 交易状态(0待支付 1已支付 2退款处理中 3退款成功 4退款失败 5已关闭) */
  tradeStatus?: string;
  /** 支付时间 */
  payTime?: string;
  /** 执行时间 */
  executeTime?: string;
  /** 完成时间 */
  finishTime?: string;
  /** 取消时间 */
  cancelTime?: string;
  /** 取消原因 */
  cancelReason?: string;
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
