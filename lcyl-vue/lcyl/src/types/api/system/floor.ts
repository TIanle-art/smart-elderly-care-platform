import type { PageDomain, BaseEntity } from "../common";

/** 房位房型配置分页查询参数 */
export interface FloorQueryParams extends PageDomain {
}

/** 房位房型配置信息 */
export interface Floor extends BaseEntity {
  /** 主键ID */
  id?: number;
  /** 楼层名称 */
  name?: string;
  /** 楼层编号 */
  code?: string;
  /** 创建人 */
  createBy?: string;
  /** 更新人 */
  updateBy?: string;
  /** 备注 */
  remark?: string;
  /** 创建时间 */
  createTime?: string;
  /** 更新时间 */
  updateTime?: string;
}
