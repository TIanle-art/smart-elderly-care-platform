import request from '@/utils/request'
import type { AjaxResult, TableDataInfo, ContractQueryParams, Contract } from '@/types'

// 查询合同跟踪列表
export function listContract(query: ContractQueryParams): Promise<TableDataInfo<Contract[]>> {
  return request({
    url: '/system/contract/list',
    method: 'get',
    params: query
  })
}

// 查询合同跟踪详细
export function getContract(id: number): Promise<AjaxResult<Contract>> {
  return request({
    url: '/system/contract/' + id,
    method: 'get'
  })
}

// 新增合同跟踪
export function addContract(data: Contract): Promise<AjaxResult> {
  return request({
    url: '/system/contract',
    method: 'post',
    data: data
  })
}

// 修改合同跟踪
export function updateContract(data: Contract): Promise<AjaxResult> {
  return request({
    url: '/system/contract',
    method: 'put',
    data: data
  })
}

// 删除合同跟踪
export function delContract(id: number | number[]): Promise<AjaxResult> {
  return request({
    url: '/system/contract/' + id,
    method: 'delete'
  })
}

// // 上传PDF
// export function uploadContractPdf(data: FormData): Promise<AjaxResult<string>> {
//   return request({
//     url: '/uploadContractPdf',
//     method: 'post',
//     headers: { 'Content-Type': 'multipart/form-data' },
//     data: data
//   })
// }
//
// // 下载PDF
// export function downloadContractPdf(pdfUrl: string) {
//   return request({
//     url: '/downloadFile',
//     method: 'get',
//     params: { pdfUrl },
//     responseType: 'blob'
//   })
// }


