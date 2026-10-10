<template>
  <div class="app-container">
    <el-tabs v-model="activeTab" @tab-click="handleTabClick" class="task-tabs">
      <!-- 待办任务Tab -->
      <el-tab-pane label="待办任务" name="todo">
        <template #label>
          <span class="tab-label">
            <el-icon><Bell /></el-icon>
            待办任务
            <el-badge v-if="todoList.length > 0" :value="todoList.length" class="tab-badge" />
          </span>
        </template>
        
        <el-table v-loading="todoLoading" :data="todoList" border>
          <el-table-column label="编号" align="center" prop="checkInCode" width="180" />
          <el-table-column label="标题" align="center" prop="title" />
          <el-table-column label="老人姓名" align="center" prop="elderName" width="120" />
          <el-table-column label="当前任务" align="center" prop="currentTaskName" width="150">
            <template #default="scope">
              <el-tag type="primary">{{ scope.row.currentTaskName }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="申请人" align="center" prop="applicat" width="120" />
          <el-table-column label="创建时间" align="center" prop="createTime" width="180">
            <template #default="scope">
              <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}') }}</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center" width="150" fixed="right">
            <template #default="scope">
              <el-button link type="primary" @click="handleTask(scope.row)">
                <el-icon><Document /></el-icon>
                办理
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>

      <!-- 候选任务Tab -->
      <el-tab-pane label="候选任务" name="candidate">
        <template #label>
          <span class="tab-label">
            <el-icon><User /></el-icon>
            候选任务
            <el-badge v-if="candidateList.length > 0" :value="candidateList.length" class="tab-badge" />
          </span>
        </template>
        
        <el-table v-loading="candidateLoading" :data="candidateList" border>
          <el-table-column label="编号" align="center" prop="checkInCode" width="180" />
          <el-table-column label="标题" align="center" prop="title" />
          <el-table-column label="老人姓名" align="center" prop="elderName" width="120" />
          <el-table-column label="当前任务" align="center" prop="currentTaskName" width="150">
            <template #default="scope">
              <el-tag type="warning">{{ scope.row.currentTaskName }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="申请人" align="center" prop="applicat" width="120" />
          <el-table-column label="创建时间" align="center" prop="createTime" width="180">
            <template #default="scope">
              <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}') }}</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center" width="150" fixed="right">
            <template #default="scope">
              <el-button link type="success" @click="handleClaim(scope.row)">
                <el-icon><Select /></el-icon>
                认领
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>

      <!-- 已完成任务Tab -->
      <el-tab-pane label="已完成" name="completed">
        <template #label>
          <span class="tab-label">
            <el-icon><CircleCheck /></el-icon>
            已完成
          </span>
        </template>
        
        <el-table v-loading="completedLoading" :data="completedList" border>
          <el-table-column label="编号" align="center" prop="checkInCode" width="180" />
          <el-table-column label="标题" align="center" prop="title" />
          <el-table-column label="老人姓名" align="center" prop="elderName" width="120" />
          <el-table-column label="当前状态" align="center" prop="currentTaskName" width="150">
            <template #default="scope">
              <el-tag v-if="scope.row.currentTaskName" type="success">{{ scope.row.currentTaskName }}</el-tag>
              <el-tag v-else type="info">流程已结束</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="申请人" align="center" prop="applicat" width="120" />
          <el-table-column label="更新时间" align="center" prop="updateTime" width="180">
            <template #default="scope">
              <span>{{ parseTime(scope.row.updateTime, '{y}-{m}-{d} {h}:{i}') }}</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center" width="150" fixed="right">
            <template #default="scope">
              <el-button link type="primary" @click="handleView(scope.row)">
                <el-icon><View /></el-icon>
                查看
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup name="CheckinList">
import { ref, onMounted, getCurrentInstance } from 'vue'
import { useRouter } from 'vue-router'
import { responseCheckin, getCandidateTasks, claimTask, getCompletedTasks, diagnoseTaskVisibility } from '@/api/system/checkin'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Bell, User, CircleCheck, Document, Select, View } from '@element-plus/icons-vue'
import { parseTime } from '@/utils/ruoyi'

const router = useRouter()
const { proxy } = getCurrentInstance()

// Tab相关
const activeTab = ref('todo')

// 待办任务列表
const todoList = ref([])
const todoLoading = ref(false)

// 候选任务列表
const candidateList = ref([])
const candidateLoading = ref(false)

// 已完成任务列表
const completedList = ref([])
const completedLoading = ref(false)

/** 加载待办任务 */
function loadTodoList() {
  console.log('开始加载待办任务...')
  todoLoading.value = true
  responseCheckin().then(response => {
    console.log('待办任务接口返回:', response)
    todoList.value = response.data || []
    todoLoading.value = false
  }).catch((error) => {
    console.error('加载待办任务失败:', error)
    todoLoading.value = false
  })
}

/** 加载候选任务（签约办理等需认领的任务） */
function loadCandidateList() {
  console.log('开始加载候选任务...')
  candidateLoading.value = true
  getCandidateTasks().then(response => {
    console.log('候选任务接口返回:', response)
    candidateList.value = response.data != null ? response.data : []
    if (candidateList.value.length === 0) {
      diagnoseTaskVisibility().then(diag => {
        console.log('候选任务为空，后端诊断信息:', diag)
      }).catch(err => {
        console.error('获取任务诊断信息失败:', err)
      })
    }
    candidateLoading.value = false
  }).catch((err) => {
    console.error('加载候选任务失败:', err)
    candidateList.value = []
    candidateLoading.value = false
  })
}

/** 加载已完成任务 */
function loadCompletedList() {
  completedLoading.value = true
  console.log('开始加载已完成任务列表...')
  
  getCompletedTasks().then(response => {
    console.log('已完成任务接口返回:', response)
    console.log('已完成任务数据:', response.data)
    console.log('已完成任务数量:', response.data?.length || 0)
    
    if (response && response.data) {
      completedList.value = response.data || []
      console.log('设置已完成任务列表后的数量:', completedList.value.length)
    } else {
      console.warn('已完成任务接口返回数据为空或格式不正确:', response)
      completedList.value = []
    }
    completedLoading.value = false
  }).catch(error => {
    console.error('加载已完成任务失败:', error)
    console.error('错误详情:', error)
    completedList.value = []
    completedLoading.value = false
  })
}

/** Tab切换 */
function handleTabClick(tab) {
  console.log('Tab切换到:', tab.props.name)
  if (tab.props.name === 'todo') {
    loadTodoList()
  } else if (tab.props.name === 'candidate') {
    loadCandidateList()
  } else if (tab.props.name === 'completed') {
    loadCompletedList()
  }
}

/** 办理待办任务 */
function handleTask(row) {
  // 跳转到对应的业务页面，携带完整的任务数据
  router.push({
    path: '/checkin',
    query: {
      businessId: row.id,
      checkInId: row.id,
      taskId: row.taskId,
      currentTaskId: row.currentTaskId,
      currentTaskName: row.currentTaskName,
      flowStatus: row.flowStatus,
      title: row.title,
      elderName: row.elderName
    }
  })
}

/** 认领候选任务 */
function handleClaim(row) {
  ElMessageBox.confirm('确认认领该任务？认领后将进入您的待办任务列表。', '任务认领', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    claimTask(row.taskId).then(response => {
      ElMessage.success('任务认领成功')
      // 刷新候选任务列表和待办任务列表
      loadCandidateList()
      loadTodoList()
    }).catch(error => {
      ElMessage.error('任务认领失败：' + (error.message || '未知错误'))
    })
  }).catch(() => {})
}

/** 查看已完成任务 */
function handleView(row) {
  // 根据流程状态决定跳转路径
  let targetPath = '/checkin';
  let queryParams = {
    businessId: row.id,
    readonly: true
  };

  // 如果任务已经完成评估（流程状态>=1），则跳转到详情页面
  if (row.flowStatus >= 1) {
    targetPath = '/checkin/detail'; // 使用详情页面作为详情页面
    queryParams = {
      ...queryParams,
      checkInId: row.id,
      readonly: true
    };
  }

  router.push({
    path: targetPath,
    query: queryParams
  })
}

// 初始化加载当前激活tab的数据，默认是待办任务
onMounted(() => {
  console.log('CheckinList 组件已挂载')
  console.log('初始激活的tab:', activeTab.value)
  // 根据当前激活的tab加载对应数据，默认是待办任务
  if (activeTab.value === 'todo') {
    console.log('加载待办任务...')
    loadTodoList()
  } else if (activeTab.value === 'candidate') {
    console.log('加载候选任务...')
    loadCandidateList()
  } else if (activeTab.value === 'completed') {
    console.log('加载已完成任务...')
    loadCompletedList()
  }
})
</script>

<style scoped>
.app-container {
  padding: 20px;
}

.task-tabs {
  background: #fff;
  padding: 20px;
  border-radius: 4px;
}

.tab-label {
  display: flex;
  align-items: center;
  gap: 5px;
}

.tab-badge {
  margin-left: 5px;
}

:deep(.el-table) {
  margin-top: 10px;
}

:deep(.el-tabs__item) {
  font-size: 15px;
  padding: 0 25px;
}

:deep(.el-tabs__nav-wrap::after) {
  height: 1px;
}
</style>
