import request from '@/utils/request'
import type { AjaxResult, TableDataInfo, RechargeQueryParams, BalanceRecharge } from '@/types'

export interface RechargeElderOption {
  elderId: number
  elderName: string
  bedNo?: string
}

// 查询预缴款充值记录列表
export function listRecharge(query: RechargeQueryParams): Promise<TableDataInfo<BalanceRecharge[]>> {
  return request({
    url: '/code/recharge/list',
    method: 'get',
    params: query
  })
}

// 查询预缴款充值记录详细
export function getRecharge(id: number): Promise<AjaxResult<BalanceRecharge>> {
  return request({
    url: '/code/recharge/' + id,
    method: 'get'
  })
}

// 新增预缴款充值记录
export function addRecharge(data: BalanceRecharge): Promise<AjaxResult> {
  return request({
    url: '/code/recharge',
    method: 'post',
    data: data
  })
}

// 修改预缴款充值记录
export function updateRecharge(data: BalanceRecharge): Promise<AjaxResult> {
  return request({
    url: '/code/recharge',
    method: 'put',
    data: data
  })
}

// 删除预缴款充值记录
export function delRecharge(id: number | number[]): Promise<AjaxResult> {
  return request({
    url: '/code/recharge/' + id,
    method: 'delete'
  })
}

// 预缴款充值并同步更新余额
export function rechargeBalance(data: BalanceRecharge): Promise<AjaxResult> {
  return request({
    url: '/code/recharge/rechargeBalance',
    method: 'post',
    data
  })
}

// 查询充值可选老人列表
export function listRechargeElderOptions(): Promise<AjaxResult<RechargeElderOption[]>> {
  return request({
    url: '/code/recharge/elderOptions',
    method: 'get'
  })
}


