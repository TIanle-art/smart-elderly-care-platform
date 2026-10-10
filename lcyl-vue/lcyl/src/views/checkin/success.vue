<template>
  <div class="success-container">
    <el-result icon="success" title="提交成功" :sub-title="successMessage">
      <template #extra>
        <div class="success-info">
          <el-descriptions :column="2" border>
            <el-descriptions-item label="申请编号">{{ applyInfo.checkInCode || '-' }}</el-descriptions-item>
            <el-descriptions-item label="申请标题">{{ applyInfo.title || '-' }}</el-descriptions-item>
            <el-descriptions-item label="老人姓名">{{ applyInfo.elderName || '-' }}</el-descriptions-item>
            <el-descriptions-item label="提交时间">{{ applyInfo.createTime || '-' }}</el-descriptions-item>
            <el-descriptions-item label="当前状态" :span="2">
              <el-tag type="success">{{ getStatusText(applyInfo.flowStatus) }}</el-tag>
            </el-descriptions-item>
          </el-descriptions>
        </div>

        <div class="action-buttons">
          <el-button type="primary" @click="viewDetail">查看详情</el-button>
          <el-button @click="backToList">返回列表</el-button>
          <el-button @click="continueApply">继续申请</el-button>
        </div>
      </template>
    </el-result>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getCheckin } from '@/api/system/checkin.js'
import { ElMessage } from 'element-plus'

const route = useRoute()
const router = useRouter()

const applyInfo = ref({})
const successMessage = ref('您的入住申请已成功提交，我们将尽快为您安排评估。')

// 获取流程状态文本
const getStatusText = (status) => {
  const statusMap = {
    0: '待评估',
    1: '评估中',
    2: '审批中',
    3: '配置中',
    4: '待签约',
    5: '已完成'
  }
  return statusMap[status] || '处理中'
}

// 查看详情（只读模式）
const viewDetail = () => {
  router.push({
    path: '/checkin',
    query: {
      businessId: applyInfo.value.id,
      checkInId: applyInfo.value.id,
      mode: 'view' // 只读模式标识
    }
  })
}

// 返回列表
const backToList = () => {
  router.push('/system/checkin/list')
}

// 继续申请（新建）
const continueApply = () => {
  router.push('/checkin')
}

// 加载申请信息
onMounted(async () => {
  const checkInId = route.query.checkInId || route.query.businessId
  
  console.log('成功页面 URL 参数:', route.query)
  console.log('获取到的 checkInId:', checkInId)
  
  if (!checkInId) {
    ElMessage.warning('缺少申请信息')
    // 即使没有 ID，也显示基本的成功信息
    return
  }

  try {
    const res = await getCheckin(checkInId)
    console.log('接口返回数据:', res)
    
    if (res.code === 200 && res.data) {
      applyInfo.value = res.data
      
      console.log('加载的申请信息:', applyInfo.value)
      
      // 解析 otherApplyInfo 获取老人姓名
      if (res.data.otherApplyInfo) {
        try {
          const otherInfo = JSON.parse(res.data.otherApplyInfo)
          console.log('解析的 otherApplyInfo:', otherInfo)
          
          if (otherInfo.basicForm && otherInfo.basicForm.name) {
            applyInfo.value.elderName = otherInfo.basicForm.name
            successMessage.value = `${otherInfo.basicForm.name} 的入住申请已成功提交，我们将尽快为您安排评估。`
          }
        } catch (e) {
          console.error('解析 otherApplyInfo 失败:', e)
        }
      }
    } else {
      ElMessage.error(res.msg || '加载申请信息失败')
    }
  } catch (error) {
    console.error('加载申请信息失败:', error)
    ElMessage.error('加载申请信息失败')
  }
})
</script>

<style scoped lang="scss">
.success-container {
  min-height: 100vh;
  background: #f5f7fa;
  padding: 40px 20px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.success-info {
  margin: 30px 0;
  max-width: 600px;
}

.action-buttons {
  display: flex;
  gap: 12px;
  justify-content: center;
  margin-top: 30px;
}

:deep(.el-result) {
  padding: 40px 20px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

:deep(.el-result__icon svg) {
  width: 80px;
  height: 80px;
}

:deep(.el-result__title) {
  font-size: 24px;
  font-weight: 600;
  margin-top: 20px;
}

:deep(.el-result__subtitle) {
  font-size: 14px;
  margin-top: 12px;
  color: #606266;
}

:deep(.el-descriptions) {
  margin-top: 20px;
}
</style>
