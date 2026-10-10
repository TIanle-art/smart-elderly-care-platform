import request from '@/utils/request'
import type { AjaxResult, TableDataInfo } from '@/types'
import type {
  LeaveQueryParams,
  ElderLeave,
  ElderOption,
  ElderLeaveFormInfo,
  ElderLeaveTodo,
  ElderLeaveApproveRecordItem,
  ElderLeaveSubmitPayload,
  ElderLeaveApprovePayload,
  ElderLeaveResubmitPayload
} from '@/types/api/code/leave'

// 查询老人请假申请列表
export function listLeave(query: LeaveQueryParams): Promise<TableDataInfo<ElderLeave[]>> {
  return request({
    url: '/code/leave/list',
    method: 'get',
    params: query
  })
}

// 查询老人请假申请详细
export function getLeave(id: number): Promise<AjaxResult<ElderLeave>> {
  return request({
    url: '/code/leave/' + id,
    method: 'get'
  })
}

// 新增老人请假申请
export function addLeave(data: ElderLeave): Promise<AjaxResult> {
  return request({
    url: '/code/leave',
    method: 'post',
    data: data
  })
}

// 修改老人请假申请
export function updateLeave(data: ElderLeave): Promise<AjaxResult> {
  return request({
    url: '/code/leave',
    method: 'put',
    data: data
  })
}

// 删除老人请假申请
export function delLeave(id: number | number[]): Promise<AjaxResult> {
  return request({
    url: '/code/leave/' + id,
    method: 'delete'
  })
}

export function submitLeave(data: ElderLeaveSubmitPayload): Promise<AjaxResult<number>> {
  return request({
    url: '/code/leave/submit',
    method: 'post',
    data: data
  })
}

export function getLeaveFormInfo(elderId: number): Promise<AjaxResult<ElderLeaveFormInfo>> {
  return request({
    url: '/code/leave/formInfo/' + elderId,
    method: 'get'
  })
}

export function listLeaveTodo(): Promise<AjaxResult<ElderLeaveTodo[]>> {
  return request({
    url: '/code/leave/todoList',
    method: 'get'
  })
}

export function approveLeave(data: ElderLeaveApprovePayload): Promise<AjaxResult<number>> {
  return request({
    url: '/code/leave/approve',
    method: 'post',
    data: data
  })
}

export function resubmitLeave(data: ElderLeaveResubmitPayload): Promise<AjaxResult<number>> {
  return request({
    url: '/code/leave/resubmit',
    method: 'post',
    data: data
  })
}

export function listElderOptions(): Promise<AjaxResult<ElderOption[]>> {
  return request({
    url: '/code/leave/options',
    method: 'get'
  })
}

export function listLeaveApproveRecords(leaveId: number): Promise<AjaxResult<ElderLeaveApproveRecordItem[]>> {
  return request({
    url: '/code/leave/records/' + leaveId,
    method: 'get'
  })
}


