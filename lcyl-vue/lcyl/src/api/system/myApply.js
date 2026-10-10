import request from '@/utils/request'

export function listMyApply(query) {
  return request({
    url: '/system/my-apply/list',
    method: 'get',
    params: query
  })
}

export function cancelMyApply(category, businessId) {
  return request({
    url: `/system/my-apply/cancel/${category}/${businessId}`,
    method: 'post'
  })
}

