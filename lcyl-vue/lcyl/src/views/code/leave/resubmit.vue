<template>
  <div class="app-container leave-resubmit-page">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="98px" class="query-form">
      <el-form-item label="单据编号" prop="orderNo">
        <el-input v-model="queryParams.orderNo" placeholder="请输入单据编号" clearable @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="老人姓名" prop="elderName">
        <el-input v-model="queryParams.elderName" placeholder="请输入老人姓名" clearable @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="老人身份证号" prop="elderIdCard">
        <el-input v-model="queryParams.elderIdCard" placeholder="请输入老人身份证号" clearable @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
      </el-form-item>
    </el-form>

    <div class="header-bar">
      <span class="header-title">驳回后待修改</span>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" />
    </div>

    <el-table v-loading="loading" :data="list">
      <el-table-column type="index" label="序号" width="70" align="center" />
      <el-table-column label="单据编号" align="center" prop="orderNo" min-width="220" class-name="order-no-col" />
      <el-table-column label="老人姓名" align="center" prop="elderName" />
      <el-table-column label="老人身份证号" align="center" prop="elderIdCard" min-width="220" class-name="id-card-col" />
      <el-table-column label="请假开始时间" align="center" prop="leaveStartTime" width="180">
        <template #default="scope">
          <span>{{ formatDateDisplay(scope.row.leaveStartTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="预计返回时间" align="center" prop="plannedReturnTime" width="180">
        <template #default="scope">
          <span>{{ formatDateDisplay(scope.row.plannedReturnTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template #default="scope">
          <span>{{ formatDateDisplay(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" width="100">
        <template #default>
          <el-tag type="warning" effect="plain">已驳回</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="150">
        <template #default="scope">
          <el-button link type="primary" @click="handleResubmit(scope.row)">修改并重提</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />
    <el-empty v-if="!loading && !list.length" description="暂无驳回记录" />
  </div>
</template>

<script setup lang="ts" name="LeaveResubmit">
import type { ElderLeave, LeaveQueryParams } from "@/types/api/code/leave"
import { listLeave } from "@/api/code/leave"
import { parseTime } from "@/utils/ruoyi"

const router = useRouter()
const route = useRoute()

const loading = ref(false)
const showSearch = ref(true)
const total = ref(0)
const list = ref<ElderLeave[]>([])
const data = reactive({
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    orderNo: undefined,
    elderName: undefined,
    elderIdCard: undefined,
    status: "back"
  } as LeaveQueryParams
})

const { queryParams } = toRefs(data)

function getList() {
  loading.value = true
  listLeave(queryParams.value).then(response => {
    list.value = response.rows || []
    total.value = response.total || 0
  }).finally(() => {
    loading.value = false
  })
}

function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

function resetQuery() {
  queryParams.value.orderNo = undefined
  queryParams.value.elderName = undefined
  queryParams.value.elderIdCard = undefined
  queryParams.value.status = "back"
  proxy?.resetForm("queryRef")
  handleQuery()
}

const { proxy } = getCurrentInstance()
function handleResubmit(row: ElderLeave) {
  if (!row.id) {
    return
  }
  router.push({ path: `/code/leave-detail/view/${row.id}`, query: { from: route.fullPath } })
}

function formatDateDisplay(value?: string) {
  if (!value) {
    return "—"
  }
  return parseTime(value, "{y}-{m}-{d} {h}:{i}:{s}") || value
}

getList()
</script>

<style scoped lang="scss">
.leave-resubmit-page {
  .query-form {
    margin-bottom: 8px;
  }

  .header-bar {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 12px;
  }

  .header-title {
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

  :deep(.id-card-col .cell) {
    white-space: nowrap;
    word-break: normal;
    overflow: visible;
    text-overflow: clip;
  }
}
</style>
