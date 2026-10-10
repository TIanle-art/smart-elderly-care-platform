import type { PageDomain, BaseEntity } from "../common";


/** 退款管理分页查询参数 */
export interface RefundQueryParams extends PageDomain {
  /** 退款编号 */
  refundNo?: string;
  /** 订单编号 */
  orderNo?: string;
  /** 退款状态 */
  refundStatus?: string;
  /** 申请人姓名 */
  applicantName?: string;
  /** 申请时间 */
  applyTime?: string;
  /** 扩展查询参数 */
  params?: any;
  /** 订单状态 */
  orderStatus?: string;
}


/** 退款管理配置信息 */
export interface ServiceOrderRefund extends BaseEntity {
  /** 主键ID */
  id?: number;
  /** 退款编号 */
  refundNo?: string;
  /** 订单ID */
  orderId?: number;
  /** 订单编号(冗余) */
  orderNo?: string;
  /** 退款金额 */
  refundAmount?: string;
  /** 退款状态(0处理中 1成功 2失败) */
  refundStatus?: string;
  /** 申请人ID */
  applicantId?: number;
  /** 申请人姓名 */
  applicantName?: string;
  /** 申请人类型(1家属端 2后台用户) */
  applicantType?: string;
  /** 申请时间 */
  applyTime?: string;
  /** 退款时间 */
  refundTime?: string;
  /** 退款原因 */
  refundReason?: string;
  /** 退款渠道 */
  refundChannel?: string;
  /** 退款方式(微信/支付宝/现金等) */
  refundMethod?: string;
  /** 失败状态码 */
  failCode?: string;
  /** 失败原因 */
  failReason?: string;
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
