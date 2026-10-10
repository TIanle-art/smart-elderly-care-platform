import request from '@/utils/request';
import type { AjaxResult, TableDataInfo } from '@/types';
import type { Retreat, RetreatStartDTO, RetreatTaskCompletedDTO, CheckoutQueryParams } from '@/types/api/code/checkout';

/**
 * 发起退住申请（启动工作流）
 */
export function startApplication(data: RetreatStartDTO): Promise<AjaxResult> {
  return request({
    url: '/code/checkout/start',
    method: 'post',
    data
  });
}

/**
 * 获取当前用户的待办任务列表
 */
export function getTodoList(): Promise<AjaxResult<Retreat[]>> {
  return request({
    url: '/code/checkout/todo-list',
    method: 'get'
  });
}

/**
 * 完成任务（审批、上传合同、结算等）
 */
export function completeTask(data: RetreatTaskCompletedDTO): Promise<AjaxResult> {
  return request({
    url: '/code/checkout/complete-task',
    method: 'post',
    data
  });
}

/**
 * 分页查询退住列表（管理后台用）
 */
export function listCheckout(query: CheckoutQueryParams): Promise<TableDataInfo<Retreat[]>> {
  return request({
    url: '/code/checkout/list',
    method: 'get',
    params: query
  });
}

/**
 * 获取退住详情
 */
export function getCheckout(id: number): Promise<AjaxResult<Retreat>> {
  return request({
    url: '/code/checkout/' + id,
    method: 'get'
  });
}

/**
 * 新增退住（仅数据，不启动流程）
 */
export function addCheckout(data: Retreat): Promise<AjaxResult> {
  return request({
    url: '/code/checkout',
    method: 'post',
    data
  });
}

/**
 * 修改退住
 */
export function updateCheckout(data: Retreat): Promise<AjaxResult> {
  return request({
    url: '/code/checkout',
    method: 'put',
    data
  });
}

/**
 * 删除退住
 */
export function delCheckout(id: number | number[]): Promise<AjaxResult> {
  return request({
    url: '/code/checkout/' + id,
    method: 'delete'
  });
}

export function elderDetail(id: number):Promise<AjaxResult> {
  return request({
    url: '/system/elder/fullInfo/' + id,
    method: 'get'
  });
}

// 上传解除合同 PDF
export function uploadContractPdf(file: FormData): Promise<AjaxResult> {
  return request({
    url: '/uploadContractPdf',
    method: 'post',
    headers: {
      'Content-Type': 'multipart/form-data'
    },
    data: file
  })
}

export function upload(file: FormData): Promise<AjaxResult> {
  return request({
    url: '/upload',
    method: 'post',
    headers: {
      'Content-Type': 'multipart/form-data'
    },
    data: file
  })
}

export function contractDetail(id: number):Promise<AjaxResult> {
  return request({
    url: '/code/checkout/contract/' + id,
    method: 'get'
  });
}

// 获取退住账单（费用清算）
export function getRetreatBill(retreatId: number): Promise<AjaxResult> {
  return request({
    url: '/code/checkout/retreatBill/' + retreatId,
    method: 'get'
  });
}

// 查询退住审批历史
export function getRetreatHistory(retreatId: number): Promise<AjaxResult> {
  return request({
    url: '/code/checkout/history/' + retreatId,
    method: 'get'
  });
}