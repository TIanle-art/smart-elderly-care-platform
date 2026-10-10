<template>
  <div>
    <el-card>
      <el-table :data="todoList" border stripe>
        <el-table-column label="序号" type="index" width="60" />
        <el-table-column label="类型" prop="category" width="120" />
        <el-table-column label="任务名称" prop="taskName" />
        <el-table-column label="创建时间" prop="applyTime" width="240" />
        <el-table-column label="状态" prop="flowStatus" />
        <el-table-column label="操作" width="160">
          <template #default="scope">
            <el-button type="primary" size="mini" @click="handleApprove(scope.row)">审批</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getMyTodoList } from '@/api/system/myto.js'
import { ElMessage } from 'element-plus'
import router from '@/router'

const todoList = ref([])

onMounted(() => {
  loadTodoList()
})

const formatDateTime = (dateTime) => {
  if (!dateTime) return '-'
  const date = new Date(dateTime)
  const year = date.getFullYear()
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')
  const hours = String(date.getHours()).padStart(2, '0')
  const minutes = String(date.getMinutes()).padStart(2, '0')
  const seconds = String(date.getSeconds()).padStart(2, '0')
  return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`
}

const loadTodoList = async () => {
  const res = await getMyTodoList()
  if (res.code === 200) {
    console.log(res.data)
    todoList.value = res.data
    const formattedData = res.data.map(item => ({
      ...item,
      applyTime: formatDateTime(item.applyTime)
    }))
    todoList.value = formattedData
  } else {
    ElMessage.error('加载失败')
  }
}

const handleApprove = (row) => {
  const data = {
    taskId: row.taskId,
    businessId: row.businessId
  }
  console.log(row)
  switch (row.category) {
    case '退住':
      if (row.taskName === "护理组长审批") {
        // 护理组长审批 - 退住流程的第一个审批节点
        router.push({
          // path: '/code/checkout-apply/approvalAdmin',
          name:'ApprovalAdmin',
          query: data
        })
      } else if (row.taskName === "法务解除合同") {
        // 法务解除合同审批
        router.push({
          // path: '/code/checkout-apply/contractTerm',
          name:'ContractTerm',
          query: data
        })
      } else if (row.taskName === "结算员调整账单") {
        // 结算员调整账单
        router.push({
          // path: '/code/checkout-apply/billAdjustment',
          name:'BillAdjustment',
          query: data
        })
      } else if (row.taskName === "结算组长审批") {
        // 结算组长审批
        router.push({
          name: 'BillApproval',
          query: data
        })
      } else if (row.taskName === "副院长审批") {
        // 副院长审批
        router.push({
          name: 'CheckoutApproval',
          query: data
        })
      } else if (row.taskName === "结算员结清费用") {
        // 结算员结清费用
        router.push({
          name: 'FinalSettlement',
          query: data
        })
      }
      break;

    case '入住':
      // 入住流程的任务节点
      router.push({
        path: 'views/system/checkin/list'
      })
      break;

    case '请假':
      router.push({
        path: '/code/leave-todo/index'
      })
      break;
  }
}
</script>