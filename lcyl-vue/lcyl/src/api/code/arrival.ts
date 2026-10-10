import request from '@/utils/request'
import type { AjaxResult, TableDataInfo, ArrivalQueryParams, Arrival } from '@/types'

// 查询来访登记列表
export function listArrival(query: ArrivalQueryParams): Promise<TableDataInfo<Arrival[]>> {
  return request({
    url: '/code/arrival/list',
    method: 'get',
    params: query
  })
}

// 查询来访登记详细
export function getArrival(id: number): Promise<AjaxResult<Arrival>> {
  return request({
    url: '/code/arrival/' + id,
    method: 'get'
  })
}

// 新增来访登记
export function addArrival(data: Arrival): Promise<AjaxResult> {
  return request({
    url: '/code/arrival',
    method: 'post',
    data: data
  })
}

// 修改来访登记
export function updateArrival(data: Arrival): Promise<AjaxResult> {
  return request({
    url: '/code/arrival',
    method: 'put',
    data: data
  })
}

// 删除来访登记
export function delArrival(id: number | number[]): Promise<AjaxResult> {
  return request({
    url: '/code/arrival/' + id,
    method: 'delete'
  })
}

export function getTypeOptions(id: number): Promise<TableDataInfo<Arrival[]>> {
  return request({
    url: `/code/arrival/typeOptions/${id}`, 
    method: 'get'
  });
}

export function getOlderList(query: ArrivalQueryParams): Promise<TableDataInfo<Arrival[]>> {
  return request({
    url: '/code/arrival/olderList',
    method: 'get',
    params: query
  })
}


