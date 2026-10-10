import request from '@/utils/request'

/**
 * 入住配置相关 API（入住配置表 check_in_config）
 */

// 查询入住配置列表
export function listCheckInConfig(query) {
  return request({
    url: '/system/checkin-config/list',
    method: 'get',
    params: query
  })
}

// 查询入住配置详细
export function getCheckInConfig(id) {
  return request({
    url: '/system/checkin-config/' + id,
    method: 'get'
  })
}

// 新增入住配置
export function addCheckInConfig(data) {
  return request({
    url: '/system/checkin-config',
    method: 'post',
    data: data
  })
}

// 修改入住配置
export function updateCheckInConfig(data) {
  return request({
    url: '/system/checkin-config',
    method: 'put',
    data: data
  })
}

// 删除入住配置
export function delCheckInConfig(ids) {
  return request({
    url: '/system/checkin-config/' + ids,
    method: 'delete'
  })
}

/**
 * 提交入住配置：保存到入住配置表，并将老人关联到所选床位（占用床位）
 * @param {Object} data - { elderId, bedId, bedNo, checkInStartTime, checkInEndTime, nursingLevelId, costStartTime, costEndTime, depositAmount, nursingCost, bedCost, otherCost, medicalInsurancePayment, governmentSubsidy }
 */
export function submitCheckInConfig(data) {
  return request({
    url: '/system/checkin-config/submit',
    method: 'post',
    data: data
  })
}
