import type { PageDomain, BaseEntity } from "../common";

/** 合同跟踪配置分页查询参数 */
export interface ContractQueryParams extends PageDomain {
  /** 合同编号 */
  contractNo?: string;
  /** 老人姓名 */
  elderId?: number;
  /** 合同开始时间 */
  startTime?: string;
  /** 合同结束时间 */
  endTime?: string;
  /** 合同状态 */
  status?: number;
}

/** 合同跟踪配置信息 */
export interface Contract extends BaseEntity {
  /** 主键ID */
  id?: number;
  /** 合同编号 */
  contractNo?: string;
  /** 合同名称 */
  name?: string;
  /** 老人姓名 */
  elderId?: number;
  /** 合同开始时间 */
  startTime?: string;
  /** 合同结束时间 */
  endTime?: string;
  /** 合同状态 */
  status?: number;
  /** 创建人 */
  createBy?: string;
  /** 创建时间 */
  createTime?: string;
  /** 合同PDF地址 */
  pdfUrl?: string;
  /** 入住单号 */
  checkinNo?: string;
  /** 等级描述 */
  levelDesc?: string;
}
