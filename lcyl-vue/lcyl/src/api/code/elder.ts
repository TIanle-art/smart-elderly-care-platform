import request from '@/utils/request'
import type {AjaxResult, TableDataInfo} from '@/types'
import {Elder} from "@/types/api/code/elder";

/**
 * 获取所有老人列表
 */
export function getAllElder(): Promise<AjaxResult<Elder[]>> {
    return request({
        url: '/system/elder/getElderList',
        method: 'get'
    });
}