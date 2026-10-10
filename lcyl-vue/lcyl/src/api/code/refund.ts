import request from '@/utils/request'
import type { AjaxResult, TableDataInfo, RefundQueryParams, ServiceOrderRefund } from '@/types'

// 查询退款管理列表
export function listRefund(query: RefundQueryParams): Promise<TableDataInfo<ServiceOrderRefund[]>> {
  return request({
    url: '/code/refund/list',
    method: 'get',
    params: query
  })
}

// 查询退款管理详细
export function getRefund(id: number): Promise<AjaxResult<ServiceOrderRefund>> {
  return request({
    url: '/code/refund/' + id,
    method: 'get'
  })
}

// 新增退款管理
export function addRefund(data: ServiceOrderRefund): Promise<AjaxResult> {
  return request({
    url: '/code/refund',
    method: 'post',
    data: data
  })
}

// 修改退款管理
export function updateRefund(data: ServiceOrderRefund): Promise<AjaxResult> {
  return request({
    url: '/code/refund',
    method: 'put',
    data: data
  })
}

// 删除退款管理
export function delRefund(id: number | number[]): Promise<AjaxResult> {
  return request({
    url: '/code/refund/' + id,
    method: 'delete'
  })
}
export function refundSuccess(id: number) {
  return request({
    url: '/code/refund/success/' + id,
    method: 'post'
  })
}

export function refundFail(data: { refundId: number; failCode?: string; failReason: string }) {
  return request({
    url: '/code/refund/fail',
    method: 'post',
    data
  })
}



