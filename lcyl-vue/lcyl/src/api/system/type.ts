import request from '@/utils/request'
import type { AjaxResult, TableDataInfo, TypeQueryParams, LcRoomType } from '@/types'

// 查询房型设置列表
export function listType(query: TypeQueryParams): Promise<TableDataInfo<LcRoomType[]>> {
  return request({
    url: '/system/type/list',
    method: 'get',
    params: query
  })
}

// 查询房型设置详细
export function getType(id: number): Promise<AjaxResult<LcRoomType>> {
  return request({
    url: '/system/type/' + id,
    method: 'get'
  })
}

// 新增房型设置
export function addType(data: LcRoomType): Promise<AjaxResult> {
  return request({
    url: '/system/type',
    method: 'post',
    data: data
  })
}

// 修改房型设置
export function updateType(data: LcRoomType): Promise<AjaxResult> {
  return request({
    url: '/system/type',
    method: 'put',
    data: data
  })
}

// 删除房型设置
export function delType(id: number | number[]): Promise<AjaxResult> {
  return request({
    url: '/system/type/' + id,
    method: 'delete'
  })
}


