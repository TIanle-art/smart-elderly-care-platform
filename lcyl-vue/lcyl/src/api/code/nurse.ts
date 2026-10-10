import request from '../../utils/request'
import type { AjaxResult, TableDataInfo, NurseQueryParams, Nurse } from '../../types'

// 查询护理员信息列表
export function listNurse(query: NurseQueryParams): Promise<TableDataInfo<Nurse[]>> {
  return request({
    url: '/nurse/nurse/list',
    method: 'get',
    params: query
  })
}

// 查询护理员信息详细
export function getNurse(id: number): Promise<AjaxResult<Nurse>> {
  return request({
    url: '/nurse/nurse/' + id,
    method: 'get'
  })
}

// 新增护理员信息
export function addNurse(data: Nurse): Promise<AjaxResult> {
  return request({
    url: '/nurse/nurse',
    method: 'post',
    data: data
  })
}

// 修改护理员信息
export function updateNurse(data: Nurse): Promise<AjaxResult> {
  return request({
    url: '/nurse/nurse',
    method: 'put',
    data: data
  })
}

// 删除护理员信息
export function delNurse(id: number | number[]): Promise<AjaxResult> {
  return request({
    url: '/nurse/nurse/' + id,
    method: 'delete'
  })
}


