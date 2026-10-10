import request from '@/utils/request'

// 查询入住办理列表
export function listCheckin(query) {
    return request({
        url: '/system/checkin/list',
        method: 'get',
        params: query
    })
}

// 查询入住办理详细
export function getCheckin(id) {
    return request({
        url: '/system/checkin/' + id,
        method: 'get'
    })
}

// 新增入住办理
export function addCheckin(data) {
    return request({
        url: '/system/checkin',
        method: 'post',
        data: data
    })
}

// 修改入住办理
export function updateCheckin(data) {
    return request({
        url: '/system/checkin',
        method: 'put',
        data: data
    })
}

// 删除入住办理
export function delCheckin(id) {
    return request({
        url: '/system/checkin/' + id,
        method: 'delete'
    })
}

// 响应待办任务列表（已认领的任务）
export function responseCheckin() {
    return request({
        url: '/system/checkin/response',
        method: 'get'
    })
}

// 获取候选任务列表（需要认领的任务）
export function getCandidateTasks() {
    return request({
        url: '/system/checkin/candidate-tasks',
        method: 'get'
    })
}

// 认领任务
export function claimTask(taskId) {
    return request({
        url: '/system/checkin/claim-task/' + taskId,
        method: 'post'
    })
}

// 完成签约（保存合同并结束流程）
export function completeContract(checkInId, data) {
    return request({
        url: '/system/checkin/complete-contract/' + checkInId,
        method: 'post',
        data: data
    })
}

// 获取已完成任务列表
export function getCompletedTasks() {
    return request({
        url: '/system/checkin/completed-tasks',
        method: 'get'
    })
}

export function diagnoseTaskVisibility() {
    return request({
        url: '/system/checkin/task-diagnose',
        method: 'get'
    })
}

/**
 * 发起入住申请
 */
export function applyCheckin(data) {
    return request({
        url: '/system/checkin/apply',
        method: 'post',
        data: data
    })
}

/**
 * 完成评估任务，进入审批阶段
 * @param {*} checkInId 入住办理ID
 * @param {*} evaluation 评估数据（JSON字符串）
 */
export function evaluateCheckin(checkInId, evaluation) {
    return request({
        url: '/system/checkin/evaluate/' + checkInId,
        method: 'post',
        data: { evaluation }  // 传递 evaluation 参数
    })
}

/**
 * 完成审批任务，进入入住办理阶段
 * @param {*} checkInId 入住办理ID
 * @param {*} vars 审批参数：{ approveResult: '同意/拒绝/驳回', approveRemark: '审批意见' }
 */
export function approveCheckin(checkInId, vars) {
    return request({
        url: '/system/checkin/approve/' + checkInId,
        method: 'post',
        data: vars
    })
}

/**
 * 重新申请入住（处理被驳回的申请）
 * @param {*} checkInId 入住办理ID
 * @param {*} data 申请数据：{ otherApplyInfo: {}, reviewInfo: {} }
 */
export function reapplyCheckin(checkInId, data) {
    return request({
        url: '/system/checkin/reapply/' + checkInId,
        method: 'post',
        data: data
    })

}

export function addElder(data) {
    return request({
        url: '/system/elder/add',
        method: 'post',
        data: data
    })
}

