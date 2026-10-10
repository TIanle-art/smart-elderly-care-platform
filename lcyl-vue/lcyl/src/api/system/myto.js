import request from '@/utils/request'

// 获取我的待办（自动取用户信息）
export function getMyTodoList() {
    return request({
        url: '/my/apply/todoList',
        method: 'get',
        timeout: 30000
    })
}