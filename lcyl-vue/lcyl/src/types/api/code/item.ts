import type { PageDomain, BaseEntity } from "../common";

/** 账单明细配置分页查询参数 */
export interface ItemQueryParams extends PageDomain {
  /** 账单ID */
  billId?: number;
}

/** 账单明细配置信息 */
export interface BillItem extends BaseEntity {
  /** 主键ID */
  id?: number;
  /** 账单ID */
  billId?: number;
  /** 账单编号 */
  billNo?: string;
  /** 明细类型(1添加项 2扣减项) */
  itemType?: string;
  /** 费用项目 */
  feeName?: string;
  /** 服务内容 */
  serviceContent?: string;
  /** 金额 */
  amount?: string;
  /** 排序 */
  sort?: number;
  /** 备注 */
  remark?: string;
}