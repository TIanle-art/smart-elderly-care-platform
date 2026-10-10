import request from '../../utils/request'
import type { AjaxResult, TableDataInfo, NursingLevelQueryParams, NursingLevel } from '../../types'

// 查询护理等级列表
export function listNursingLevel(query: NursingLevelQueryParams): Promise<TableDataInfo<NursingLevel[]>> {
  return request({
    url: '/nursingLevel/nursingLevel/list',
    method: 'get',
    params: query
  })
}

// 查询护理等级详细
export function getNursingLevel(id: number): Promise<AjaxResult<NursingLevel>> {
  return request({
    url: '/nursingLevel/nursingLevel/' + id,
    method: 'get'
  })
}

// 新增护理等级
export function addNursingLevel(data: NursingLevel): Promise<AjaxResult> {
  return request({
    url: '/nursingLevel/nursingLevel',
    method: 'post',
    data: data
  })
}

// 修改护理等级
export function updateNursingLevel(data: NursingLevel): Promise<AjaxResult> {
  return request({
    url: '/nursingLevel/nursingLevel',
    method: 'put',
    data: data
  })
}

// 删除护理等级
export function delNursingLevel(id: number | number[]): Promise<AjaxResult> {
  return request({
    url: '/nursingLevel/nursingLevel/' + id,
    method: 'delete'
  })
}

// 校验护理等级名称是否唯一
export function checkLevelNameUnique(levelName: string,excludeId?: number) {
  return request({
    url: '/nursingLevel/nursingLevel/checkLevelNameUnique',
    method: 'get',
    params: { levelName,excludeId }
  })
}

// 校验护理计划是否被引用
export function checkNursingLevelIsReferenced(params: { nursingLevelId: number }): Promise<AjaxResult<boolean>> {
  return request({
    url: '/nursingLevel/nursingLevel/checkNursingLevelIsReferenced',
    method: 'get',
    params: params
  })
}


