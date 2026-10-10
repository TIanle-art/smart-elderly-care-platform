import request from '../../utils/request'
import type { AjaxResult, TableDataInfo, ItemQueryParams, NursingItem } from '../../types'

// 查询护理项目列表
export function listItem(query: ItemQueryParams): Promise<TableDataInfo<NursingItem[]>> {
  return request({
    url: '/nursingItem/item/list',
    method: 'get',
    params: query
  })
}

// 查询护理项目详细
export function getItem(id: number): Promise<AjaxResult<NursingItem>> {
  return request({
    url: '/nursingItem/item/' + id,
    method: 'get'
  })
}

// 新增护理项目
export function addItem(data: NursingItem): Promise<AjaxResult> {
  return request({
    url: '/nursingItem/item',
    method: 'post',
    data: data
  })
}

// 修改护理项目
export function updateItem(data: NursingItem): Promise<AjaxResult> {
  return request({
    url: '/nursingItem/item',
    method: 'put',
    data: data
  })
}

// 删除护理项目
export function delItem(id: number | number[]): Promise<AjaxResult> {
  return request({
    url: '/nursingItem/item/' + id,
    method: 'delete'
  })
}
// 校验护理项目名称是否唯一
export function checkItemNameUnique(itemName: string) {
  return request({
    url: '/nursingItem/item/checkItemNameUnique',
    method: 'get',
    params: { itemName }
  })
}

