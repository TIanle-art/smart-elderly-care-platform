import request from '@/utils/request'
import type { AjaxResult, TableDataInfo, ItemQueryParams, NursingPlanItem } from '@/types'

// 查询护理计划-项目关联列表
export function listItem(query: ItemQueryParams): Promise<TableDataInfo<NursingPlanItem[]>> {
    return request({
        url: '/planItem/item/list',
        method: 'get',
        params: query
    })
}

// 查询护理计划-项目关联详细
export function getItem(id: number): Promise<AjaxResult<NursingPlanItem>> {
    return request({
        url: '/planItem/item/' + id,
        method: 'get'
    })
}

// 新增护理计划-项目关联
export function addItem(data: NursingPlanItem): Promise<AjaxResult> {
    return request({
        url: '/planItem/item',
        method: 'post',
        data: data
    })
}

// 修改护理计划-项目关联
export function updateItem(data: NursingPlanItem): Promise<AjaxResult> {
    return request({
        url: '/planItem/item',
        method: 'put',
        data: data
    })
}

// 删除护理计划-项目关联
export function delItem(id: number | number[]): Promise<AjaxResult> {
    return request({
        url: '/planItem/item/' + id,
        method: 'delete'
    })
}


