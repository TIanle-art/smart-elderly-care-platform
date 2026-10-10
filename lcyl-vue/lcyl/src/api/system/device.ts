import request from '@/utils/request'
import type { AjaxResult, TableDataInfo, DeviceQueryParams, Device } from '@/types'

// 查询设备管理列表
export function listDevice(query: DeviceQueryParams): Promise<TableDataInfo<Device[]>> {
  return request({
    url: '/system/device/list',
    method: 'get',
    params: query
  })
}

// 查询设备管理详细
export function getDevice(id: number): Promise<AjaxResult<Device>> {
  return request({
    url: '/system/device/' + id,
    method: 'get'
  })
}

// 新增设备管理
export function addDevice(data: Device): Promise<AjaxResult> {
  return request({
    url: '/system/device',
    method: 'post',
    data: data
  })
}

// 修改设备管理
export function updateDevice(data: Device): Promise<AjaxResult> {
  return request({
    url: '/system/device',
    method: 'put',
    data: data
  })
}

// 删除设备管理
export function delDevice(id: number | number[]): Promise<AjaxResult> {
  return request({
    url: '/system/device/' + id,
    method: 'delete'
  })
}


