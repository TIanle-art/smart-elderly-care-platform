import request from '@/utils/request';
import { CheckinQueryParams, CheckinHomeVO, PageResult, ApiResponse } from '@/views/checkin/types';

/**
 * 入住管理首页列表查询
 * @param params 查询参数
 * @returns 分页结果
 */
export function getCheckinHomeList(params: Partial<CheckinQueryParams>): Promise<PageResult<CheckinHomeVO>> {
    return request({
        url: '/lcyl-code/apply/home/list',
        method: 'get',
        params
    });
}

/**
 * 发起入住办理
 * @returns 申请ID
 */
export function initiateCheckin(): Promise<ApiResponse<number>> {
    return request({
        url: '/lcyl-code/apply/initiate',
        method: 'post'
    });
}

/**
 * 查看入住办理详情
 * @param applyId 申请ID
 * @returns 详情数据
 */
export function getCheckinDetail(applyId: number): Promise<ApiResponse<CheckinHomeVO>> {
    return request({
        url: `/lcyl-code/apply/detail/${applyId}`,
        method: 'get'
    });
}

/**
 * 测试用：更新申请状态为已审批通过
 * @param applyId 申请ID
 * @returns 操作结果
 */
export function updateCheckinStatus(applyId: number): Promise<ApiResponse<number>> {
    return request({
        url: `/lcyl-code/apply/status/${applyId}`,
        method: 'put'
    });
}