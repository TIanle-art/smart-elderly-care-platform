/**
 * 入住管理相关类型定义
 */

// 首页列表查询参数
export interface CheckinQueryParams {
    pageNum: number;
    pageSize: number;
    applyNo?: string;       // 单据编号（精准）
    elderlyName?: string;   // 老人姓名（模糊）
    idCard?: string;        // 身份证号（精准）
    beginTime?: string;     // 入住日期开始
    endTime?: string;       // 入住日期结束
}

// 首页列表项/详情数据类型
export interface CheckinHomeVO {
    applyId: number;                // 申请ID
    applyNo: string;                // 单据编号
    elderlyName: string;            // 老人姓名
    idCard: string;                 // 身份证号
    bedNo?: string;                 // 入住床位
    checkinTime?: string;           // 入住日期
    createBy: string;               // 创建人
    createTime: string;             // 创建时间
    phone?: string;                 // 联系电话
    gender?: '0' | '1';             // 性别 0-男 1-女
    age?: number;                   // 年龄
    careFee?: number;               // 护理费
    roomFee?: number;               // 床位费
    totalFee?: number;              // 总费用
    contractNo?: string;            // 合同编号
    signTime?: string;              // 签约时间
    payStatus?: '0' | '1';          // 缴费状态 0-未缴 1-已缴
}

// 分页返回结果类型（适配后端TableDataInfo）
export interface PageResult<T> {
    rows: T[];
    total: number;
    code?: number;
    msg?: string;
}

// 通用响应类型
export interface ApiResponse<T = any> {
    code: number;
    msg: string;
    data: T;
}
