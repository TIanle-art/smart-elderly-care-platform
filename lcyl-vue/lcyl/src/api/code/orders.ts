import request from '@/utils/request'
import type {
  AjaxResult,
  TableDataInfo,
  OrdersQueryParams,
  ServiceOrder,
  OrderRefundApplyForm
} from '@/types'
import type { ServiceOrderExecution } from '@/types/api/code/orders'

export function listOrders(query: OrdersQueryParams): Promise<TableDataInfo<ServiceOrder[]>> {
  return request({
    url: '/code/orders/list',
    method: 'get',
    params: query
  })
}

export function getOrders(id: number): Promise<AjaxResult<ServiceOrder>> {
  return request({
    url: '/code/orders/' + id,
    method: 'get'
  })
}

export function getOrderExecution(orderId: number): Promise<AjaxResult<ServiceOrderExecution>> {
  return request({
    url: '/code/orders/execution/' + orderId,
    method: 'get'
  })
}

export function addOrders(data: ServiceOrder): Promise<AjaxResult> {
  return request({
    url: '/code/orders',
    method: 'post',
    data
  })
}

export function updateOrders(data: ServiceOrder): Promise<AjaxResult> {
  return request({
    url: '/code/orders',
    method: 'put',
    data
  })
}

export function delOrders(id: number | number[]): Promise<AjaxResult> {
  return request({
    url: '/code/orders/' + id,
    method: 'delete'
  })
}

export function cancelOrder(data: { orderId: number; cancelReason: string }): Promise<AjaxResult> {
  return request({
    url: '/code/orders/cancel',
    method: 'post',
    data
  })
}

export function applyRefund(data: OrderRefundApplyForm): Promise<AjaxResult> {
  return request({
    url: '/code/orders/refund/apply',
    method: 'post',
    data
  })
}

export function generateExpenseBill(data: { orderId: number }): Promise<AjaxResult> {
  return request({
    url: '/code/orders/generateExpenseBill',
    method: 'post',
    data
  })
}
