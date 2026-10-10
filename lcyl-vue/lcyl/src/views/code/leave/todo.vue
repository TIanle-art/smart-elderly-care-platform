<template>
  <div class="app-container leave-todo-page">
    <div class="todo-header">
      <span class="todo-title">待我审批</span>
      <el-button type="primary" icon="Refresh" plain @click="getTodoList">刷新待办</el-button>
    </div>
    <el-table v-loading="todoLoading" :data="todoList">
      <el-table-column type="index" label="序号" width="70" align="center" />
      <el-table-column label="单据编号" align="center" min-width="220" class-name="order-no-col">
        <template #default="scope">
          <span>{{ getTodoField(scope.row, "orderNo") }}</span>
        </template>
      </el-table-column>
      <el-table-column label="老人姓名" align="center" min-width="120">
        <template #default="scope">
          <span>{{ getTodoField(scope.row, "elderName") }}</span>
        </template>
      </el-table-column>
      <el-table-column label="请假开始时间" prop="leaveStartTime" align="center" width="180">
        <template #default="scope">
          <span>{{ formatDateDisplay(getTodoField(scope.row, "leaveStartTime")) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="预计返回时间" prop="plannedReturnTime" align="center" width="180">
        <template #default="scope">
          <span>{{ formatDateDisplay(getTodoField(scope.row, "plannedReturnTime")) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="请假天数" align="center" width="100">
        <template #default="scope">
          <span>{{ getTodoField(scope.row, "leaveDays") }}</span>
        </template>
      </el-table-column>
      <el-table-column label="申请时间" prop="applyTime" align="center" width="180">
        <template #default="scope">
          <span>{{ formatDateDisplay(getTodoField(scope.row, "applyTime")) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="当前节点" prop="taskName" align="center" min-width="140" />
      <el-table-column label="状态" prop="status" align="center" width="120">
        <template #default="scope">
          <el-tag type="warning" effect="plain">{{ getFlowStatusText(scope.row.status) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="120">
        <template #default="scope">
          <el-button link type="primary" @click="handleApprove(scope.row)">审批</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-empty v-if="!todoLoading && !todoList.length" description="当前没有待审批请假单" />
  </div>
</template>

<script setup lang="ts" name="LeaveTodo">
import { ref } from "vue"
import { useRoute, useRouter } from "vue-router"
import type { ElderLeaveTodo } from "@/types/api/code/leave"
import { getLeave, listLeaveTodo } from "@/api/code/leave"
import { parseTime } from "@/utils/ruoyi"

const router = useRouter()
const route = useRoute()

const todoList = ref<ElderLeaveTodo[]>([])
const todoLoading = ref<boolean>(false)
const flowStatusTextMap: Record<string, string> = {
  draft: "草稿",
  approving: "审批中",
  approved: "已审批通过",
  leaving: "请假中",
  timeout: "超时未归",
  rejected: "已拒绝",
  back: "已驳回",
  returned: "已返回"
}

function getTodoList() {
  todoLoading.value = true
  listLeaveTodo().then(response => {
    const list = response.data || []
    return Promise.all(list.map(async item => {
      if (item.orderNo && item.elderName && item.leaveStartTime && item.plannedReturnTime && item.leaveDays != null && item.applyTime) {
        return item
      }
      if (!item.leaveId) {
        return item
      }
      try {
        const detailRes = await getLeave(item.leaveId)
        const detail = detailRes.data
        if (!detail) {
          return item
        }
        return {
          ...item,
          orderNo: item.orderNo || detail.orderNo || "",
          elderName: item.elderName || detail.elderName || "",
          leaveStartTime: item.leaveStartTime || detail.leaveStartTime || "",
          plannedReturnTime: item.plannedReturnTime || detail.plannedReturnTime || "",
          leaveDays: item.leaveDays ?? detail.leaveDays ?? 0,
          applyTime: item.applyTime || detail.applyTime || ""
        }
      } catch {
        return item
      }
    })).then(enrichedList => {
      todoList.value = enrichedList
    })
  }).finally(() => {
    todoLoading.value = false
  })
}

function getTodoField(row: ElderLeaveTodo, key: keyof ElderLeaveTodo) {
  // 待办接口已返回列表展示所需字段，直接读当前行可避免 N+1 详情请求。
  const rawValue = row[key as keyof ElderLeaveTodo]
  if (rawValue !== undefined && rawValue !== null && rawValue !== "") {
    return rawValue
  }
  return "—"
}

function formatDateDisplay(value?: string) {
  if (!value || value === "—") {
    return "—"
  }
  return parseTime(value, "{y}-{m}-{d} {h}:{i}:{s}") || value
}

function getFlowStatusText(status?: string) {
  if (!status) {
    return "审批中"
  }
  return flowStatusTextMap[status] || status
}

function handleApprove(row: ElderLeaveTodo) {
  router.push({
    path: `/code/leave-detail/approve/${row.leaveId}`,
    query: { from: route.fullPath, taskId: row.taskId }
  })
}

getTodoList()
</script>

<style scoped lang="scss">
.leave-todo-page {
  .todo-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 12px;
  }

  .todo-title {
    font-size: 16px;
    font-weight: 600;
    color: #303133;
  }

  :deep(.el-table th .cell) {
    white-space: nowrap;
  }

  :deep(.order-no-col .cell) {
    white-space: nowrap;
    word-break: normal;
    overflow: visible;
    text-overflow: clip;
  }
}
</style>
