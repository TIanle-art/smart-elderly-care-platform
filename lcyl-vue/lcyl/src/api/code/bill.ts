import request from '@/utils/request'
import type { AjaxResult, TableDataInfo, BillQueryParams, Bill } from '@/types'

export interface BillElderOption {
  id: number
  name: string
}

// 查询账单列表列表
export function listBill(query: BillQueryParams): Promise<TableDataInfo<Bill[]>> {
  return request({
    url: '/code/bill/list',
    method: 'get',
    params: query
  })
}
// 查询账单列表详细
export function getBill(id: number): Promise<AjaxResult<Bill>> {
  return request({
    url: '/code/bill/' + id,
    method: 'get'
  })
}

// 新增账单列表
export function addBill(data: Bill): Promise<AjaxResult> {
  return request({
    url: '/code/bill',
    method: 'post',
    data: data
  })
}

// 修改账单列表
export function updateBill(data: Bill): Promise<AjaxResult> {
  return request({
    url: '/code/bill',
    method: 'put',
    data: data
  })
}

// 删除账单列表
export function delBill(id: number | number[]): Promise<AjaxResult> {
  return request({
    url: '/code/bill/' + id,
    method: 'delete'
  })
}
// 生成月度账单
export function generateMonthlyBill(data: { elderId: number; billMonth: string }) {
  return request({
    url: '/code/bill/generateMonthlyBill',
    method: 'post',
    data
  })
}
// 取消账单
export function cancelBill(data: { billId: number; cancelReason: string }) {
  return request({
    url: '/code/bill/cancel',
    method: 'post',
    data
  })
}
// 支付账单
export function payBill(data: { billId: number; payMethod: string; voucherUrl: string; payRemark: string }) {
  return request({
    url: '/code/bill/pay',
    method: 'post',
    data
  })
}
// 查询老人列表
export function listBillElderOptions() {
  return request({
    url: '/system/elder/list',
    method: 'get',
    params: {
      pageNum: 1,
      pageSize: 1000
    }
  })
}
// 查询老人待支付账单（欠费账单）
export function getPendingBills(elderId: number): Promise<AjaxResult<Bill[]>> {
  return request({
    url: '/code/bill/pending/' + elderId,
    method: 'get'
  })
}

// 查询退住应退服务订单
export function listServiceOrder(elderId: number): Promise<AjaxResult<any[]>> {
  return request({
    url: '/code/orders/refundList/' + elderId,
    method: 'get'
  })
}