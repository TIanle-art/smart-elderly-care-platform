import type { PageDomain, BaseEntity } from "../common";

/** 房间表配置分页查询参数 */
export interface RoomQueryParams extends PageDomain {
  /** 房间编号 */
  code?: string;
  /** 排序号 */
  sort?: number;
  /** 所属楼层ID */
  floorId?: number;
  /** 房间价格 */
  price?: string;
  /** 关联房型ID */
  roomTypeId?: number;
}

/** 房间表配置信息 */
export interface Room extends BaseEntity {
  /** 房间ID */
  id?: number;
  /** 房间编号 */
  code?: string;
  /** 排序号 */
  sort?: number;
  /** 所属楼层ID */
  floorId?: number;
  /** 房间价格 */
  price?: string;
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
  /** 关联房型ID */
  roomTypeId?: number;
}
