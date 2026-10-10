import request from '@/utils/request'
import type { AjaxResult, TableDataInfo } from '@/types'

export interface BillPayment {
  id?: number
  paymentNo?: string
  billId?: number
  billNo?: string
  payerName?: string
  payerPhone?: string
  payTime?: string
  payChannel?: string
  payMethod?: string
  wechatOrderNo?: string
  payAmount?: number
  voucherUrl?: string
  payRemark?: string
  operatorId?: number
  operatorName?: string
  createBy?: string
  createTime?: string
}

export interface BillPaymentQueryParams {
  pageNum?: number
  pageSize?: number
  paymentNo?: string
  billId?: number
  billNo?: string
  payerName?: string
  payChannel?: string
  payMethod?: string
  params?: any
}

/** 查询账单支付记录列表 */
export function listBillPayment(query: BillPaymentQueryParams): Promise<TableDataInfo<BillPayment[]>> {
  return request({
    url: '/code/payment/list',
    method: 'get',
    params: query
  })
}

/** 查询账单支付记录详细 */
export function getBillPayment(id: number): Promise<AjaxResult<BillPayment>> {
  return request({
    url: '/code/payment/' + id,
    method: 'get'
  })
}
