<!-- views/checkin/index.vue -->
<template>
  <div class="checkin-wrapper">
    <!-- 自定义面包屑 -->
    <div class="custom-breadcrumb">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item :to="{ path: '/index' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item>{{ currentBreadcrumb }}</el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <!-- 流程进度头部 -->
    <div v-if="showProgress" class="progress-header">
      <el-card shadow="never">
        <div class="task-info">
          <h3>{{ taskInfo.title || '入住申请办理' }}</h3>
          <el-descriptions :column="3" size="small">
            <el-descriptions-item label="老人姓名">{{ taskInfo.elderName || '-' }}</el-descriptions-item>
            <el-descriptions-item label="当前任务">
              <el-tag type="primary">{{ taskInfo.currentTaskName || '申请入住' }}</el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="任务ID">{{ taskInfo.taskId || '-' }}</el-descriptions-item>
          </el-descriptions>
        </div>
      </el-card>
    </div>

    <!-- 动态组件加载 -->
    <component :is="currentComponent" :taskData="taskInfo" :viewMode="isViewMode" />
  </div>
</template>

<script setup>
import { ref, onMounted, computed, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getCheckin } from '@/api/system/checkin.js'
import { ElMessage } from 'element-plus'

// 引入所有阶段组件
import apply from './apply.vue'
import evaluate from './evaluate.vue'
import approve from './approve.vue'
import config from './config.vue'
import sign from './sign.vue'
import reapply from './reapply.vue'
import detail from './detail.vue'

const route = useRoute()
const router = useRouter()
const currentComponent = ref(null)
const taskInfo = ref({})
const showProgress = ref(false)
const currentBreadcrumb = ref('发起入住申请')  // 当前面包屑标题
const isViewMode = ref(false)  // 是否为只读模式

// 任务 key → 组件映射表（核心）
const componentMap = {
  applyTask: apply,
  evaluateTask: evaluate,
  approveTask: approve,
  configTask: config,
  signTask: sign,
  reapplyTask: reapply,
  detailTask: detail
}

// 任务名称到 key 的映射（备用）
const taskNameMap = {
  '发起入住申请': 'applyTask',
  '入住评估-处理': 'evaluateTask',
  '入住审批-处理': 'approveTask',
  '副院长审批-处理': 'approveTask',  // 副院长审批也映射到 approveTask
  '入住选配-处理': 'configTask',
  '签约办理-处理': 'signTask',
  '重新申请-处理': 'reapplyTask',  // 重新申请映射到 reapplyTask
  '详情查看-处理': 'detailTask'  // 详情查看映射到 detailTask
}

// 任务名称到面包屑标题的映射
const taskNameToBreadcrumb = {
  '发起入住申请': '发起入住申请',
  '入住评估-处理': '入住评估',
  '入住审批-处理': '入住审批',
  '副院长审批-处理': '入住审批',
  '入住选配-处理': '入住配置',
  '签约办理-处理': '签约办理',
  '重新申请-处理': '重新申请',
  '详情查看-处理': '详情查看'
}

// 获取当前任务ID
const currentTaskId = computed(() => {
  return route.query.currentTaskId || route.query.taskId || route.params.taskId
})

// 获取当前业务ID
const businessId = computed(() => {
  return route.query.businessId || route.query.checkInId || route.params.businessId
})

/**
 * 根据 currentTaskId 或 currentTaskName 映射到对应的组件
 */
function mapTaskIdToComponent(taskId, taskName) {
  // 优先尝试直接匹配 taskId
  if (taskId && componentMap[taskId]) {
    return componentMap[taskId]
  }
  
  // 尝试通过任务名称映射
  if (taskName && taskNameMap[taskName]) {
    return componentMap[taskNameMap[taskName]]
  }
  
  // 尝试将 taskId 当作任务名称处理
  if (taskId && taskNameMap[taskId]) {
    return componentMap[taskNameMap[taskId]]
  }
  
  // 默认返回 apply 组件
  return componentMap.applyTask
}

/**
 * 更新面包屑标题
 */
function updateBreadcrumb(taskName) {
  const breadcrumbTitle = taskNameToBreadcrumb[taskName] || '发起入住申请'
  
  // 直接更新响应式变量
  currentBreadcrumb.value = breadcrumbTitle
  
  // 更新浏览器标题
  document.title = `${breadcrumbTitle} - 绿城养老`
  
  console.log('面包屑更新为:', breadcrumbTitle)
}

onMounted(async () => {
  try {
    // 检查是否为只读模式
    if (route.query.mode === 'view') {
      isViewMode.value = true
      currentBreadcrumb.value = '查看详情'
    }
    
    // 优先从 URL 参数中获取任务信息
    const urlParams = route.query
    
    // 如果 URL 中携带了完整的任务信息，直接使用
    if (urlParams.currentTaskId || urlParams.taskId) {
      taskInfo.value = {
        checkInId: urlParams.checkInId || urlParams.businessId,
        taskId: urlParams.taskId,
        currentTaskId: urlParams.currentTaskId,
        currentTaskName: urlParams.currentTaskName,
        flowStatus: urlParams.flowStatus,
        title: urlParams.title,
        elderName: urlParams.elderName
      }
      showProgress.value = true
      
      // 根据 currentTaskId 和 currentTaskName 加载对应的组件
      currentComponent.value = mapTaskIdToComponent(urlParams.currentTaskId, urlParams.currentTaskName)
      
      // 更新面包屑标题
      updateBreadcrumb(urlParams.currentTaskName)
      
      console.log('从 URL 加载任务，当前任务ID:', urlParams.currentTaskId, '任务名称:', urlParams.currentTaskName)
      return
    }
    
    // 如果有 businessId 但没有任务信息，调用接口获取
    const id = businessId.value
    if (id) {
      const res = await getCheckin(id)
      if (res.code === 200 && res.data) {
        taskInfo.value = {
          checkInId: res.data.id,
          taskId: res.data.taskId,
          currentTaskId: res.data.currentTaskId,
          currentTaskName: res.data.currentTaskName,
          flowStatus: res.data.flowStatus,
          title: res.data.title,
          elderName: res.data.elderName
        }
        showProgress.value = true
        
        // 根据接口返回的 currentTaskId 和 currentTaskName 加载组件
        const taskKey = res.data.currentTaskId || res.data.currentTaskKey
        const taskName = res.data.currentTaskName
        currentComponent.value = mapTaskIdToComponent(taskKey, taskName)
        
        // 更新面包屑标题
        updateBreadcrumb(taskName)
        
        console.log('从接口加载任务，当前任务ID:', taskKey, '任务名称:', taskName)
      } else {
        ElMessage.error(res.msg || '获取任务详情失败')
        // 失败时加载默认组件
        currentComponent.value = componentMap.applyTask
      }
    } else {
      // 没有 businessId，说明是新发起的申请，默认加载 apply 组件
      currentComponent.value = componentMap.applyTask
      showProgress.value = false
      
      // 新申请时也更新面包屑
      updateBreadcrumb('发起入住申请')
      
      console.log('新发起申请，加载 apply 组件')
    }
  } catch (error) {
    console.error('加载任务失败:', error)
    ElMessage.error('加载任务失败：' + error.message)
    // 错误时加载默认组件
    currentComponent.value = componentMap.applyTask
  }
})
</script>

<style scoped>
.checkin-wrapper {
  min-height: 100vh;
  background-color: #f5f7fa;
}

.custom-breadcrumb {
  background: #fff;
  padding: 12px 20px;
  margin-bottom: 0;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.08);
}

.custom-breadcrumb :deep(.el-breadcrumb) {
  font-size: 14px;
  line-height: 1;
}

.custom-breadcrumb :deep(.el-breadcrumb__item:last-child .el-breadcrumb__inner) {
  color: #97a8be;
  font-weight: 400;
}

.progress-header {
  padding: 20px 20px 0;
}

.task-info h3 {
  margin: 0 0 15px 0;
  color: #303133;
  font-size: 18px;
  font-weight: 600;
}

:deep(.el-card__body) {
  padding: 20px;
}

:deep(.el-descriptions__label) {
  font-weight: 500;
}
</style>