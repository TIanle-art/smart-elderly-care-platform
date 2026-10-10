import request from '@/utils/request'
import type { AjaxResult, TableDataInfo, ItemQueryParams, BillItem } from '@/types'

// 查询账单明细列表
export function listItem(query: ItemQueryParams): Promise<TableDataInfo<BillItem[]>> {
  return request({
    url: '/code/item/list',
    method: 'get',
    params: query
  })
}

// 查询账单明细详细
export function getItem(id: number): Promise<AjaxResult<BillItem>> {
  return request({
    url: '/code/item/' + id,
    method: 'get'
  })
}

// 新增账单明细
export function addItem(data: BillItem): Promise<AjaxResult> {
  return request({
    url: '/code/item',
    method: 'post',
    data: data
  })
}

// 修改账单明细
export function updateItem(data: BillItem): Promise<AjaxResult> {
  return request({
    url: '/code/item',
    method: 'put',
    data: data
  })
}

// 删除账单明细
export function delItem(id: number | number[]): Promise<AjaxResult> {
  return request({
    url: '/code/item/' + id,
    method: 'delete'
  })
}

/** 兼容账单详情页中的账单明细查询命名 */
export function listBillItem(query: ItemQueryParams): Promise<TableDataInfo<BillItem[]>> {
  return listItem(query)
}

// 查询老人护理项目
export function getNursingItemsByElderId(elderId: number): Promise<AjaxResult<BillItem[]>> {
  return request({
    url: `/code/item/nursingItems/${elderId}`,
    method: 'get'
  })
}

