import request from '@/utils/request'
import type { AjaxResult, TableDataInfo, PaymentQueryParams, BillPayment } from '@/types'

// 查询账单支付记录列表
export function listPayment(query: PaymentQueryParams): Promise<TableDataInfo<BillPayment[]>> {
  return request({
    url: '/code/payment/list',
    method: 'get',
    params: query
  })
}

// 查询账单支付记录详细
export function getPayment(id: number): Promise<AjaxResult<BillPayment>> {
  return request({
    url: '/code/payment/' + id,
    method: 'get'
  })
}

// 新增账单支付记录
export function addPayment(data: BillPayment): Promise<AjaxResult> {
  return request({
    url: '/code/payment',
    method: 'post',
    data: data
  })
}

// 修改账单支付记录
export function updatePayment(data: BillPayment): Promise<AjaxResult> {
  return request({
    url: '/code/payment',
    method: 'put',
    data: data
  })
}

// 删除账单支付记录
export function delPayment(id: number | number[]): Promise<AjaxResult> {
  return request({
    url: '/code/payment/' + id,
    method: 'delete'
  })
}

/** 兼容账单详情页中的支付记录查询命名 */
export function listBillPayment(query: PaymentQueryParams): Promise<TableDataInfo<BillPayment[]>> {
  return listPayment(query)
}



