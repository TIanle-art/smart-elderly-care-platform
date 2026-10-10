import request from '@/utils/request'
import type { AjaxResult, TableDataInfo, SmartBedQueryParams, SmartBed } from '@/types'

// 查询智能床位列表
export function listSmartBed(query: SmartBedQueryParams): Promise<TableDataInfo<SmartBed[]>> {
  return request({
    url: '/system/SmartBed/list',
    method: 'get',
    params: query
  })
}

// 查询智能床位详细
export function getSmartBed(id: number): Promise<AjaxResult<SmartBed>> {
  return request({
    url: '/system/SmartBed/' + id,
    method: 'get'
  })
}

// 新增智能床位
export function addSmartBed(data: SmartBed): Promise<AjaxResult> {
  return request({
    url: '/system/SmartBed',
    method: 'post',
    data: data
  })
}

// 修改智能床位
export function updateSmartBed(data: SmartBed): Promise<AjaxResult> {
  return request({
    url: '/system/SmartBed',
    method: 'put',
    data: data
  })
}

// 删除智能床位
export function delSmartBed(id: number | number[]): Promise<AjaxResult> {
  return request({
    url: '/system/SmartBed/' + id,
    method: 'delete'
  })
}


