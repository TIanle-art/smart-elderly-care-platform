<template>
  <div class="processing-container">
    <el-result icon="info" title="审批中" :sub-title="processingMessage">
      <template #icon>
        <div class="custom-icon">
          <el-icon :size="80" color="#409eff">
            <Clock />
          </el-icon>
        </div>
      </template>
      
      <template #extra>
        <div class="processing-info">
          <el-descriptions :column="1" border>
            <el-descriptions-item label="申请编号">{{ applyInfo.checkInCode || '-' }}</el-descriptions-item>
            <el-descriptions-item label="申请标题">{{ applyInfo.title || '-' }}</el-descriptions-item>
            <el-descriptions-item label="老人姓名">{{ applyInfo.elderName || '-' }}</el-descriptions-item>
            <el-descriptions-item label="当前任务">
              <el-tag type="primary">{{ applyInfo.currentTaskName || '入住审批-处理' }}</el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="提交时间">{{ applyInfo.updateTime || '-' }}</el-descriptions-item>
          </el-descriptions>
        </div>

        <!-- 操作记录 -->
        <div class="operation-records">
          <h3>操作记录</h3>
          <el-timeline>
            <el-timeline-item
              v-for="(record, index) in operationRecords"
              :key="index"
              :timestamp="record.time"
              :type="record.type"
              placement="top"
            >
              <div class="record-title">{{ record.operator }} {{ record.action }}</div>
              <div class="record-desc">操作结果：{{ record.result }}</div>
            </el-timeline-item>
          </el-timeline>
        </div>

        <div class="action-buttons">
          <el-button type="primary" @click="backToList">返回列表</el-button>
          <el-button @click="viewDetail">查看详情</el-button>
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
import { Clock } from '@element-plus/icons-vue'

const route = useRoute()
const router = useRouter()

const applyInfo = ref({})
const processingMessage = ref('稍等，入住申请正在审批中，若您的申请通过后将会发送通知（审批急忙）！')
const operationRecords = ref([])

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

// 加载申请信息
onMounted(async () => {
  const checkInId = route.query.checkInId || route.query.businessId
  
  console.log('审批中页面 URL 参数:', route.query)
  console.log('获取到的 checkInId:', checkInId)
  
  if (!checkInId) {
    ElMessage.warning('缺少申请信息')
    return
  }

  try {
    const res = await getCheckin(checkInId)
    console.log('接口返回数据:', res)
    
    if (res.code === 200 && res.data) {
      applyInfo.value = res.data
      
      console.log('加载的申请信息:', applyInfo.value)
      
      // 根据老人姓名自定义消息
      if (res.data.elderName) {
        processingMessage.value = `${res.data.elderName} 的入住申请正在审批中，若您的申请通过后将会发送通知（审批急忙）！`
      }

      // 构造操作记录
      operationRecords.value = [
        {
          operator: res.data.applicat || '申请人',
          action: '申请入住',
          time: res.data.createTime || new Date().toLocaleString('zh-CN'),
          result: '已发起',
          type: 'success'
        }
      ]

      // 如果有评估记录
      if (res.data.flowStatus >= 1) {
        operationRecords.value.push({
          operator: '(职位) -申请-审批批',
          action: '遗忘任 (审批中)',
          time: res.data.updateTime || new Date().toLocaleString('zh-CN'),
          result: '审批中',
          type: 'primary'
        })
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
.processing-container {
  min-height: 100vh;
  background: #f5f7fa;
  padding: 40px 20px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.custom-icon {
  margin-bottom: 20px;
}

.processing-info {
  margin: 30px auto;
  max-width: 600px;
}

.operation-records {
  margin: 30px auto;
  max-width: 600px;
  
  h3 {
    margin-bottom: 20px;
    font-size: 16px;
    font-weight: 600;
    color: #303133;
  }
}

.record-title {
  font-weight: 500;
  color: #303133;
  margin-bottom: 5px;
  font-size: 14px;
}

.record-desc {
  font-size: 13px;
  color: #909399;
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
  max-width: 800px;
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
  line-height: 1.6;
}

:deep(.el-descriptions) {
  margin-top: 20px;
}

:deep(.el-timeline) {
  padding: 10px 0;
}
</style>
