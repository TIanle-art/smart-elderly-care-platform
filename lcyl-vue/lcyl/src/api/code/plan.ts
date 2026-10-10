import request from '../../utils/request'
import type { AjaxResult, TableDataInfo, PlanQueryParams, NursingPlan } from '../../types'

// 查询护理计划列表
export function listPlan(query: PlanQueryParams): Promise<TableDataInfo<NursingPlan[]>> {
  return request({
    url: '/nursingPlan/plan/list',
    method: 'get',
    params: query
  })
}

// 查询护理计划详细
export function getPlan(id: number): Promise<AjaxResult<NursingPlan>> {
  return request({
    url: '/nursingPlan/plan/' + id,
    method: 'get'
  })
}

// 新增护理计划
export function addPlan(data: NursingPlan): Promise<AjaxResult> {
  return request({
    url: '/nursingPlan/plan',
    method: 'post',
    data: data
  })
}

// 修改护理计划
export function updatePlan(data: NursingPlan): Promise<AjaxResult> {
  return request({
    url: '/nursingPlan/plan',
    method: 'put',
    data: data
  })
}

// 删除护理计划
export function delPlan(id: number | number[]): Promise<AjaxResult> {
  return request({
    url: '/nursingPlan/plan/' + id,
    method: 'delete'
  })
}

// 校验护理项目是否被护理计划引用
export function checkItemIsReferenced(params: { itemId: number }): Promise<AjaxResult<boolean>> {
  return request({
    url: '/nursingPlan/plan/checkItemIsReferenced', 
    method: 'get', 
    params: params 
  })
}
// 校验护理计划名称是否唯一
export function checkPlanNameUnique(planName: string, excludeId?: number) {
  return request({
    url: '/nursingPlan/plan/checkPlanNameUnique',
    method: 'get',
    params: { planName, excludeId }
  })
}
