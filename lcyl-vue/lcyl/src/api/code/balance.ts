import request from '@/utils/request'
import type { AjaxResult, TableDataInfo, BalanceQueryParams, Balance } from '@/types'

// 查询余额列表
export function listBalance(query: BalanceQueryParams): Promise<TableDataInfo<Balance[]>> {
  return request({
    url: '/code/balance/list',
    method: 'get',
    params: query
  })
}

// 查询余额详细
export function getBalance(id: number): Promise<AjaxResult<Balance>> {
  return request({
    url: '/code/balance/' + id,
    method: 'get'
  })
}

// 新增余额
export function addBalance(data: Balance): Promise<AjaxResult> {
  return request({
    url: '/code/balance',
    method: 'post',
    data: data
  })
}

// 修改余额
export function updateBalance(data: Balance): Promise<AjaxResult> {
  return request({
    url: '/code/balance',
    method: 'put',
    data: data
  })
}

// 删除余额
export function delBalance(id: number | number[]): Promise<AjaxResult> {
  return request({
    url: '/code/balance/' + id,
    method: 'delete'
  })
}

// 根据老人ID查询余额
export function getBalanceByElderId(elderId: number): Promise<AjaxResult<Balance>> {
  return request({
    url: '/code/balance/elder/' + elderId,
    method: 'get'
  })
}
