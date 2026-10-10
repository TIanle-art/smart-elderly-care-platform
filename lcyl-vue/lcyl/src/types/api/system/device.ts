import type { PageDomain, BaseEntity } from "../common";

/** 设备管理配置分页查询参数 */
export interface DeviceQueryParams extends PageDomain {
  /** 所属产品名称 */
  productName?: string;
}

/** 设备管理配置信息 */
export interface Device extends BaseEntity {
  /** 设备ID */
  id?: number;
  /** 设备名称 */
  deviceName?: string;
  /** 备注名称 */
  remarkName?: string;
  /** 所属产品ID */
  productId?: number;
  /** 所属产品名称 */
  productName?: string;
  /** 接入位置（房间/床位） */
  accessLocation?: string;
  /** 节点类型 */
  nodeType?: string;
  /** 创建人 */
  creator?: string;
  /** 创建时间 */
  createTime?: string;
  /** 更新时间 */
  updateTime?: string;
  /** 备注 */
  remark?: string;
}
