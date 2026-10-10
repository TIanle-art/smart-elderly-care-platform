import { exportDefault } from './../../utils/index';
import request from '@/utils/request'
import type { AjaxResult, TableDataInfo, ReservationQueryParams, LcReservation } from '@/types'
import { id } from 'element-plus/es/locales.mjs'

// 查询预约来访列表
export function listReservation(query: ReservationQueryParams): Promise<TableDataInfo<LcReservation[]>> {
  return request({
    url: '/code/reservation/list',
    method: 'get',
    params: query
  })
}

// 查询预约来访详细
export function getReservation(id: number): Promise<AjaxResult<LcReservation>> {
  return request({
    url: '/code/reservation/' + id,
    method: 'get'
  })
}

// 新增预约来访
export function addReservation(data: LcReservation): Promise<AjaxResult> {
  return request({
    url: '/code/reservation',
    method: 'post',
    data: data
  })
}

// 修改预约来访
export function updateReservation(data: LcReservation): Promise<AjaxResult> {
  return request({
    url: '/code/reservation',
    method: 'put',
    data: data
  })
}

// 删除预约来访
export function delReservation(id: number | number[]): Promise<AjaxResult> {
  return request({
    url: '/code/reservation/' + id,
    method: 'delete'
  })
}

export function getTypeOptions(id: number): Promise<TableDataInfo<LcReservation[]>> {
  return request({
    // 用模板字符串拼接路径参数：/typeOptions/0 或 /typeOptions/1
    url: `/code/reservation/typeOptions/${id}`, 
    method: 'get'
    // 注意：路径参数不用写params，直接拼在URL里
  });
}

//过期状态
// export function checkTimeStatus(): Promise<AjaxResult> {
//   return request({
//     url: '/code/reservation/checkTimeStatus',
//     method: 'post'
//   })
// }

//到院时间
export function updateArrivalTime(data: LcReservation): Promise<AjaxResult> {
  return request({
    url: '/code/reservation/updateArrivalTime',
    method: 'put',
    data: data
  })
}


