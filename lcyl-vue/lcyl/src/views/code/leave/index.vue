<template>
  <div class="app-container leave-page">
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
      <el-form-item label="请假时间" prop="leaveStartTime">
        <el-date-picker
          v-model="leaveStartTimeRange"
          type="datetimerange"
          range-separator="至"
          start-placeholder="开始时间"
          end-placeholder="结束时间"
          value-format="YYYY-MM-DD HH:mm:ss"
          date-format="YYYY-MM-DD"
          time-format="HH:mm:ss"
        />
      </el-form-item>
      <el-form-item>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
      </el-form-item>
    </el-form>

    <div class="status-bar">
      <el-radio-group v-model="activeStatus" @change="handleStatusChange">
        <el-radio-button v-for="item in statusTabs" :key="item.label" :label="item.value">{{ item.label }}</el-radio-button>
      </el-radio-group>
      <div class="status-actions">
        <el-button type="warning" plain icon="EditPen" @click="goResubmitPage" v-hasPermi="['code:leave:resubmit']">驳回修改</el-button>
        <el-button type="primary" plain icon="Tickets" @click="goTodoPage" v-hasPermi="['code:leave:approve']">待我审批</el-button>
        <el-button type="primary" icon="Plus" @click="handleAdd" v-hasPermi="['code:leave:submit']">发起请假申请</el-button>
        <right-toolbar v-model:showSearch="showSearch" @queryTable="getRecordList" />
      </div>
    </div>

    <el-table v-loading="loading" :data="leaveList">
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
      <el-table-column label="实际返回时间" align="center" prop="actualReturnTime" width="180">
        <template #default="scope">
          <span>{{ formatDateDisplay(scope.row.actualReturnTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="创建者" align="center" prop="createBy" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template #default="scope">
          <span>{{ formatDateDisplay(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="请假状态" align="center" prop="realStatus" width="120">
        <template #default="scope">
          <el-tag :type="getStatusTagType(scope.row.realStatus)" effect="plain">{{ getStatusText(scope.row.realStatus) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="operation-col" width="140">
        <template #default="scope">
          <el-button link type="primary" :disabled="!canReturn(scope.row)" @click="openReturnDialog(scope.row)" v-hasPermi="['code:leave:edit']">
            返回
          </el-button>
          <el-button link type="primary" @click="handleView(scope.row)">查看</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize"
      @pagination="getRecordList"
    />

    <el-dialog title="填写返回时间" v-model="returnOpen" width="760px" append-to-body>
      <el-form ref="returnRef" :model="returnForm" :rules="returnRules" label-width="120px">
        <el-form-item label="实际返回时间" prop="actualReturnTime">
          <el-date-picker
            v-model="returnForm.actualReturnTime"
            type="datetime"
            value-format="YYYY-MM-DD HH:mm:ss"
            placeholder="请选择实际返回时间"
            :disabled-date="disableBeforeLeaveStart"
            @change="handleActualReturnTimeChange"
          />
        </el-form-item>
        <el-form-item label="实际请假天数" prop="actualLeaveDays">
          <el-input v-model="returnForm.actualLeaveDays" placeholder="请假天数" disabled />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="returnForm.remark" type="textarea" maxlength="50" show-word-limit placeholder="请输入" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="returnOpen = false">取消</el-button>
          <el-button type="primary" @click="submitReturn">确定</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts" name="LeaveIndex">
import type { ElderLeave, LeaveQueryParams } from "@/types/api/code/leave"
import { listLeave, getLeave, updateLeave } from "@/api/code/leave"
import { parseTime } from "@/utils/ruoyi"

const { proxy } = getCurrentInstance()
const router = useRouter()
const route = useRoute()

const leaveList = ref<ElderLeave[]>([])
const loading = ref<boolean>(false)
const showSearch = ref<boolean>(true)
const total = ref<number>(0)
const leaveStartTimeRange = ref<string[]>([])
const activeStatus = ref<"leaving" | "returned" | "timeout" | "">("")
const returnOpen = ref<boolean>(false)
const currentReturnRow = ref<ElderLeave>({} as ElderLeave)
const statusTabs = [
  { label: "全部", value: "" },
  { label: "请假中", value: "leaving" },
  { label: "已返回", value: "returned" },
  { label: "超时未归", value: "timeout" }
]
const statusConfig: Record<string, { label: string; type: "" | "success" | "warning" | "danger" | "info" | "primary" }> = {
  leaving: { label: "请假中", type: "primary" },
  returned: { label: "已返回", type: "success" },
  timeout: { label: "超时未归", type: "danger" }
}
const returnForm = reactive({
  actualReturnTime: "",
  actualLeaveDays: "",
  remark: ""
})
const returnRules = {
  actualReturnTime: [{ required: true, message: "请选择实际返回时间", trigger: "change" }]
}
const data = reactive({
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    orderNo: undefined,
    elderName: undefined,
    elderIdCard: undefined,
    isReturned: undefined,
    status: ""
  } as LeaveQueryParams
})

const { queryParams } = toRefs(data)

function getRecordList() {
  loading.value = true
  const query = {
    ...queryParams.value
  }
  if (leaveStartTimeRange.value && leaveStartTimeRange.value.length === 2) {
    query.leaveStartTime = leaveStartTimeRange.value[0]
    query.plannedReturnTime = leaveStartTimeRange.value[1]
  } else {
    query.leaveStartTime = undefined
    query.plannedReturnTime = undefined
  }
  listLeave(query).then(async response => {
    leaveList.value = response.rows || []
    total.value = response.total || 0
    await enrichActualReturnTime()
  }).finally(() => {
    loading.value = false
  })
}

async function enrichActualReturnTime() {
  const needFillRows = leaveList.value.filter((item: ElderLeave) => item.id && item.isReturned === 1 && !item.actualReturnTime)
  if (!needFillRows.length) {
    return
  }
  await Promise.all(needFillRows.map(async (item: ElderLeave) => {
    try {
      const response = await getLeave(item.id!)
      item.actualReturnTime = response.data?.actualReturnTime || item.actualReturnTime
    } catch {
    }
  }))
}

function handleQuery() {
  queryParams.value.pageNum = 1
  getRecordList()
}

function resetQuery() {
  leaveStartTimeRange.value = []
  activeStatus.value = ""
  queryParams.value.status = ""
  queryParams.value.isReturned = undefined
  queryParams.value.leaveStartTime = undefined
  queryParams.value.plannedReturnTime = undefined
  proxy?.resetForm("queryRef")
  handleQuery()
}

function handleStatusChange(status: "leaving" | "returned" | "timeout" | "") {
  queryParams.value.status = status
  queryParams.value.isReturned = undefined
  handleQuery()
}

function goTodoPage() {
  router.push("/code/leave-todo/index")
}

function goResubmitPage() {
  router.push("/code/leave-resubmit/index")
}

function handleAdd() {
  router.push({ path: "/code/leave-detail/create", query: { from: route.fullPath } })
}

function handleView(row: ElderLeave) {
  if (!row.id) {
    return
  }
  router.push({ path: `/code/leave-detail/view/${row.id}`, query: { from: route.fullPath } })
}

function openReturnDialog(row: ElderLeave) {
  if (!row.id) {
    return
  }
  getLeave(row.id).then(response => {
    currentReturnRow.value = response.data || row
    returnForm.actualReturnTime = formatDateTime(new Date())
    returnForm.remark = ""
    handleActualReturnTimeChange()
    returnOpen.value = true
  })
}

function formatDateTime(date: Date) {
  const y = date.getFullYear()
  const m = `${date.getMonth() + 1}`.padStart(2, "0")
  const d = `${date.getDate()}`.padStart(2, "0")
  const h = `${date.getHours()}`.padStart(2, "0")
  const i = `${date.getMinutes()}`.padStart(2, "0")
  const s = `${date.getSeconds()}`.padStart(2, "0")
  return `${y}-${m}-${d} ${h}:${i}:${s}`
}

function parseDateTimeString(value: string) {
  const match = /^(\d{4})-(\d{2})-(\d{2}) (\d{2}):(\d{2}):(\d{2})$/.exec(value)
  if (!match) {
    return new Date(value)
  }
  const [, y, m, d, h, i, s] = match
  return new Date(Number(y), Number(m) - 1, Number(d), Number(h), Number(i), Number(s))
}

function calcActualLeaveDays() {
  if (!currentReturnRow.value.leaveStartTime || !returnForm.actualReturnTime) {
    returnForm.actualLeaveDays = ""
    return
  }
  const start = new Date(currentReturnRow.value.leaveStartTime).getTime()
  const end = parseDateTimeString(returnForm.actualReturnTime).getTime()
  if (end < start) {
    returnForm.actualLeaveDays = ""
    return
  }
  const oneDay = 24 * 60 * 60 * 1000
  const diff = end - start
  let days = Math.floor(diff / oneDay)
  if (diff % oneDay !== 0) {
    days += 1
  }
  if (days === 0) {
    days = 1
  }
  returnForm.actualLeaveDays = `${days}`
}

function handleActualReturnTimeChange() {
  calcActualLeaveDays()
}

function disableBeforeLeaveStart(date: Date) {
  if (!currentReturnRow.value.leaveStartTime) {
    return false
  }
  const start = new Date(currentReturnRow.value.leaveStartTime).getTime()
  return date.getTime() < start
}

function submitReturn() {
  proxy?.$refs["returnRef"]?.validate((valid: boolean) => {
    if (valid && currentReturnRow.value.id) {
      const actualReturnTime = parseDateTimeString(returnForm.actualReturnTime).toISOString()
      updateLeave({
        id: currentReturnRow.value.id,
        status: "returned",
        isReturned: 1,
        actualReturnTime,
        actualLeaveDays: returnForm.actualLeaveDays,
        remark: returnForm.remark
      } as ElderLeave).then(() => {
        proxy?.$modal.msgSuccess("返回登记成功")
        returnOpen.value = false
        getRecordList()
      })
    }
  })
}

function formatDateDisplay(value?: string) {
  if (!value) {
    return "—"
  }
  return parseTime(value, "{y}-{m}-{d} {h}:{i}:{s}") || value
}
/*

function getLeaveBizStatus(row: ElderLeave) {
  if (row.isReturned === 1 || row.status === "returned") {
    return "returned"
  }
  if (row.status === "approved") {
    if (!row.plannedReturnTime) {
      return "leaving"
    }
    const planned = new Date(row.plannedReturnTime).getTime()
    if (!Number.isNaN(planned) && planned < Date.now()) {
      return "timeout"
    }
    return "leaving"
  }
  if (row.status === "timeout") {
    return "timeout"
  }
  return "leaving"
}
*/

function canReturn(row: ElderLeave) {
  return row.realStatus === "leaving" || row.realStatus === "timeout"
}

function getStatusText(status?: string) {
  if (!status) {
    return "未知"
  }
  return statusConfig[status]?.label || "未知"
}

function getStatusTagType(status?: string) {
  if (!status) {
    return "info"
  }
  return statusConfig[status]?.type || "info"
}

function initPage() {
  const queryStatus = route.query.status
  if (queryStatus === "leaving" || queryStatus === "returned" || queryStatus === "timeout" || queryStatus === "") {
    activeStatus.value = queryStatus as any
    queryParams.value.status = queryStatus as any
  }
  getRecordList()
}

initPage()
</script>

<style scoped lang="scss">
.leave-page {
  .query-form {
    margin-bottom: 8px;

    :deep(.el-form-item__label) {
      white-space: nowrap;
    }
  }

  .status-bar {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 12px;
    gap: 12px;
    flex-wrap: wrap;
  }

  .status-actions {
    display: flex;
    align-items: center;
    gap: 10px;
  }

  :deep(.el-table th .cell) {
    white-space: nowrap;
  }

  :deep(.operation-col .cell) {
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
