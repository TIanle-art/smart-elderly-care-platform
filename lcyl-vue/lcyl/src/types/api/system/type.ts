import type { PageDomain, BaseEntity } from "../common";

/** 房型设置配置分页查询参数 */
export interface TypeQueryParams extends PageDomain {
}

/** 房型设置配置信息 */
export interface LcRoomType extends BaseEntity {
  /** 房型ID */
  id?: number;
  /** 房型照片 */
  photo?: string;
  /** 房型名称 */
  name?: string;
  /** 床位数量 */
  bedCount?: number;
  /** 房型价格 */
  price?: string;
  /** 创建人 */
  createBy?: string;
  /** 房型介绍 */
  introduction?: string;
  /** 房型状态 */
  status?: number;
  /** 更新人 */
  updateBy?: string;
  /** 备注 */
  remark?: string;
  /** 创建时间 */
  createTime?: string;
  /** 更新时间 */
  updateTime?: string;
}

/** 房型查询参数 */
export interface RoomTypeQueryParams extends BaseEntity {
  /** 房型名称 */
  name?: string;
  /** 房型编号 */
  code?: string;
  /** 床位数量 */
  bedNum?: number;
}

/** 房型实体 */
export interface RoomType extends BaseEntity {
  /** 房型ID */
  id?: number;
  /** 房型名称 */
  name?: string;
  /** 房型编号 */
  code?: string;
  /** 床位数量 */
  bedNum?: number;
  /** 价格 */
  price?: number;
  /** 备注 */
  remark?: string;
}