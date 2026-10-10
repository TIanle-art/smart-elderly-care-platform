import request from '../../utils/request'
import type { AjaxResult, TableDataInfo, ItemQueryParams, NursingItem } from '../../types'

// 查询我的负责老人
export function listMyElder() {
    return request({
        url: '/elder/responsible/myList',
        method: 'get'
    })
}
// 单个老人设置护理员
export function assignNurse(elderId: number, nurseIds: number[]) {
    return request({
        url: '/elder/responsible/assign',
        method: 'post',
        data: {
            elderId,
            nurseIds
        }
    })
}

// 批量设置护理员
export function batchAssignNurse(roomId: number, nurseIds: number[]) {
    return request({
        url: '/elder/responsible/batchAssign',
        method: 'post',
        data: {
            roomId,
            nurseIds
        }
    })
}