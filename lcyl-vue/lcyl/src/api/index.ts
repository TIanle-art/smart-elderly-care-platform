import request from '@/utils/request'
import type { AjaxResult, TableDataInfo, ArrivalQueryParams, Arrival } from '@/types'
export function listArrival(query: ArrivalQueryParams): Promise<TableDataInfo<Arrival[]>> {
  return request({
    url: '/code/index/count',
    method: 'get',
    params: query
  })
}

export function getCheckInOutStat(data) {
  return request({
    url: '/code/index/checkInOutStat',
    method: 'post',
    data: data  
  })
}

// 护理等级统计
export function getNursingLevelStat() {
  return request({
    url: '/code/index/nursingLevelStat',
    method: 'get'
  })
}

// 年龄性别统计
export function getAgeGenderStat() {
  return request({
    url: '/code/index/ageGenderStat',
    method: 'get'
  })
}

// 服务能力统计
export function getServiceAbilityStat() {
  return request({
    url: '/code/index/serviceAbilityStat',
    method: 'get'
  })
}


// 收益情况统计（带时间筛选）
export function getIncomeStat(params) {
  return request({
    url: '/code/index/incomeStat',
    method: 'get',
    params
  })
}

// 服务情况统计（按执行时间）
export function getServiceStat(params) {
  return request({
    url: '/code/index/getServiceStat',
    method: 'get',
    params
  })
}