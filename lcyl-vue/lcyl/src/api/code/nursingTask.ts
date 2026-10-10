import request from '@/utils/request'

// 查询护理任务列表（全部从订单表）
export function listTask(query: any) {
    return request({
        url: '/nursing/task/list',
        method: 'get',
        params: query
    })
}

// 任务详情
export function getTaskDetail(id: number) {
    return request({
        url: `/nursing/task/detail/${id}`,
        method: 'get'
    })
}

// 取消任务
export function cancelTask(id: number, cancelReason: string) {
    return request({ url: `/nursing/task/cancel/${id}`, method: 'put', data: { cancelReason } })
}

// 执行任务
export function executeTask(id: number, data: any) {
    return request({ url: `/nursing/task/execute/${id}`, method: 'put', data })
}

// 改期任务
export function rescheduleTask(id: number, data: any) {
    return request({ url: `/nursing/task/reschedule/${id}`, method: 'put', data })
}

// 完成任务
export function completedTask(id: number) {
    return request({ url: `/nursing/task/completed/${id}`, method: 'put' })
}