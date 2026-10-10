<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="70px">
      <el-form-item label="订单编号" prop="orderNo">
        <el-input
          v-model="queryParams.orderNo"
          placeholder="请输入订单编号"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="老人姓名" prop="elderName">
        <el-input
          v-model="queryParams.elderName"
          placeholder="请输入老人姓名"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="下单人" prop="applicantName">
        <el-input
          v-model="queryParams.applicantName"
          placeholder="请输入下单人姓名"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="创建时间" style="width: 308px">
        <el-date-picker
          v-model="daterangeCreateTime"
          value-format="YYYY-MM-DD"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['code:orders:export']"
        >
          导出
        </el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" />
    </el-row>

    <el-tabs v-model="activeStatus" @tab-change="handleStatusChange" class="mb8">
      <el-tab-pane label="全部" name="all" />
      <el-tab-pane label="待支付" name="0" />
      <el-tab-pane label="待执行" name="1" />
      <el-tab-pane label="已执行" name="2" />
      <el-tab-pane label="已完成" name="3" />
      <el-tab-pane label="已关闭" name="5" />
    </el-tabs>

    <el-table v-loading="loading" :data="ordersList">
      <el-table-column label="订单编号" align="center" prop="orderNo" min-width="180" />
      <el-table-column label="老人姓名" align="center" prop="elderName" min-width="100" />
      <el-table-column label="床位号" align="center" prop="bedNo" min-width="80" />
      <el-table-column label="护理项目" align="center" prop="projectName" min-width="120" />
      <el-table-column label="订单金额" align="center" prop="orderAmount" min-width="100">
        <template #default="scope">
          <span>{{ formatMoney(scope.row.orderAmount) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="期望服务时间" align="center" prop="expectedServiceTime" width="140">
        <template #default="scope">
          <span>{{ parseTime(scope.row.expectedServiceTime, "{y}-{m}-{d}") }}</span>
        </template>
      </el-table-column>
      <el-table-column label="下单人" align="center" prop="applicantName" min-width="100" />
      <el-table-column label="订单状态" align="center" prop="orderStatus" min-width="100">
        <template #default="scope">
          <dict-tag :options="lc_order_status" :value="scope.row.orderStatus" />
        </template>
      </el-table-column>
      <el-table-column label="交易状态" align="center" prop="tradeStatus" min-width="100">
        <template #default="scope">
          <dict-tag :options="lc_trade_status" :value="scope.row.tradeStatus" />
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="140">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime, "{y}-{m}-{d}") }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="300">
        <template #default="scope">
          <el-button link type="primary" @click="handleView(scope.row)">
            查看
          </el-button>
          <el-button
            link
            type="success"
            :disabled="!canGenerateExpenseBill(scope.row)"
            @click="handleGenerateExpenseBill(scope.row)"
            v-hasPermi="['code:orders:edit']"
          >
            生成费用账单
          </el-button>
          <el-button
            link
            type="warning"
            :disabled="scope.row.orderStatus !== '0'"
            @click="openCancelDialog(scope.row)"
            v-hasPermi="['code:orders:edit']"
          >
            取消
          </el-button>
          <el-button
            link
            type="danger"
            :disabled="!canRefund(scope.row)"
            @click="handleRefund(scope.row)"
            v-hasPermi="['code:orders:edit']"
          >
            退款
          </el-button>
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

    <el-dialog title="填写取消原因" v-model="cancelOpen" width="500px" append-to-body>
      <el-form ref="cancelRef" :model="cancelForm" :rules="cancelRules" label-width="90px">
        <el-form-item label="取消原因" prop="cancelReason">
          <el-select v-model="cancelForm.cancelReason" placeholder="请选择取消原因" style="width: 100%">
            <el-option
              v-for="item in cancelReasonOptions"
              :key="item.value"
              :label="item.label"
              :value="item.label"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="cancelOpen = false">取消</el-button>
          <el-button type="primary" @click="submitCancel">确定</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts" name="Orders">
import { ElMessageBox } from "element-plus"
import { useRouter } from "vue-router"
import type { OrdersQueryParams, ServiceOrder } from "@/types/api/code/orders"
import { listOrders, cancelOrder, applyRefund, generateExpenseBill } from "@/api/code/orders"

const router = useRouter()
const { proxy } = getCurrentInstance() as any
const { lc_order_status, lc_trade_status } = proxy.useDict("lc_order_status", "lc_trade_status")

const ordersList = ref<ServiceOrder[]>([])
const loading = ref<boolean>(true)
const showSearch = ref<boolean>(true)
const total = ref<number>(0)
const daterangeCreateTime = ref<string[]>([])
const activeStatus = ref<string>("all")

const cancelOpen = ref<boolean>(false)
const cancelOrderId = ref<number | undefined>(undefined)
const cancelRef = ref()

const cancelForm = reactive({
  cancelReason: ""
})

const cancelRules = {
  cancelReason: [{ required: true, message: "请选择取消原因", trigger: "change" }]
}

const cancelReasonOptions = [
  { label: "不需要此项服务了", value: "1" },
  { label: "费用有点贵", value: "2" },
  { label: "临时有事", value: "3" },
  { label: "不方便服务", value: "4" },
  { label: "信息填写错误", value: "5" },
  { label: "重复下单", value: "6" }
]

const data = reactive({
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    orderNo: undefined,
    elderName: undefined,
    applicantName: undefined,
    createTime: undefined,
    orderStatus: undefined,
    params: {}
  } as OrdersQueryParams
})

const { queryParams } = toRefs(data)

function formatMoney(value?: number | string | null) {
  const amount = Number(value ?? 0)
  return Number.isFinite(amount) ? amount.toFixed(2) : "0.00"
}

function getList() {
  loading.value = true
  queryParams.value.params = {}

  if (daterangeCreateTime.value && daterangeCreateTime.value.length === 2) {
    queryParams.value.params.beginCreateTime = daterangeCreateTime.value[0]
    queryParams.value.params.endCreateTime = daterangeCreateTime.value[1]
  }

  listOrders(queryParams.value)
    .then((response: any) => {
      ordersList.value = response.rows || []
      total.value = response.total || 0
    })
    .finally(() => {
      loading.value = false
    })
}

function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

function resetQuery() {
  daterangeCreateTime.value = []
  activeStatus.value = "all"
  proxy.resetForm("queryRef")
  queryParams.value.orderStatus = undefined
  handleQuery()
}

function handleStatusChange() {
  queryParams.value.orderStatus = activeStatus.value === "all" ? undefined : activeStatus.value
  queryParams.value.pageNum = 1
  getList()
}

function canRefund(row: ServiceOrder) {
  return ["1", "2"].includes(row.orderStatus || "") && !["2", "3"].includes(row.tradeStatus || "")
}

function canGenerateExpenseBill(row: ServiceOrder) {
  return row.orderStatus === "3" && row.tradeStatus === "1"
}

function handleView(row: ServiceOrder) {
  router.push({
    path: `/code/orders-detail/index/${row.id}`
  })
}

function openCancelDialog(row: ServiceOrder) {
  cancelOrderId.value = row.id
  cancelForm.cancelReason = ""
  cancelOpen.value = true
}

function submitCancel() {
  cancelRef.value.validate((valid: boolean) => {
    if (!valid || !cancelOrderId.value) return

    cancelOrder({
      orderId: cancelOrderId.value,
      cancelReason: cancelForm.cancelReason
    }).then(() => {
      proxy.$modal.msgSuccess("取消成功")
      cancelOpen.value = false
      getList()
    })
  })
}

function handleRefund(row: ServiceOrder) {
  if (!row.id) return

  ElMessageBox.prompt("请输入退款原因", "填写退款原因", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    inputType: "textarea",
    inputPlaceholder: "请输入退款原因",
    inputValidator: (value: string) => {
      const text = value?.trim()
      if (!text) return "退款原因不能为空"
      if (text.length > 100) return "退款原因不能超过100个字"
      return true
    }
  })
    .then(({ value }) => {
      return applyRefund({
        orderId: row.id,
        refundReason: value.trim(),
        applicantType: "2"
      })
    })
    .then(() => {
      proxy.$modal.msgSuccess("退款申请提交成功")
      getList()
    })
    .catch(() => {})
}

function handleGenerateExpenseBill(row: ServiceOrder) {
  if (!row.id) return

  proxy.$modal
    .confirm(`确认根据订单【${row.orderNo}】生成费用账单吗？`)
    .then(() => generateExpenseBill({ orderId: row.id! }))
    .then(() => {
      proxy.$modal.msgSuccess("费用账单生成成功")
    })
    .catch(() => {})
}

function handleExport() {
  proxy.download(
    "code/orders/export",
    {
      ...queryParams.value
    },
    `orders_${new Date().getTime()}.xlsx`
  )
}

getList()
</script>
