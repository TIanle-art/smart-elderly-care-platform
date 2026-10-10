<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="退款编号" prop="refundNo">
        <el-input
            v-model="queryParams.refundNo"
            placeholder="请输入退款编号"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="订单编号" prop="orderNo">
        <el-input
            v-model="queryParams.orderNo"
            placeholder="请输入订单编号"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="退款状态" prop="refundStatus">
        <el-select v-model="queryParams.refundStatus" placeholder="请选择退款状态" clearable>
          <el-option
              v-for="dict in lc_refund_status"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="申请人" prop="applicantName">
        <el-input
            v-model="queryParams.applicantName"
            placeholder="请输入申请人姓名"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="申请时间" style="width: 308px">
        <el-date-picker
            v-model="daterangeApplyTime"
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
            v-hasPermi="['code:refund:export']"
        >
          导出
        </el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" />
    </el-row>

    <el-tabs :model-value="activeRefundStatus" @tab-change="handleRefundStatusChange" class="mb8">
      <el-tab-pane label="全部" name="all" />
      <el-tab-pane label="退款处理中" name="0" />
      <el-tab-pane label="退款成功" name="1" />
      <el-tab-pane label="退款失败" name="2" />
    </el-tabs>

    <el-table v-loading="loading" :data="refundList">
      <el-table-column label="退款编号" align="center" prop="refundNo" min-width="230" />
      <el-table-column label="订单编号" align="center" prop="orderNo" min-width="180" />
      <el-table-column label="退款金额" align="center" prop="refundAmount" min-width="100" />
      <el-table-column label="订单状态" align="center" prop="orderStatus" min-width="100">
        <template #default="scope">
          <dict-tag :options="lc_order_status" :value="scope.row.orderStatus" />
        </template>
      </el-table-column>

      <el-table-column label="退款状态" align="center" prop="refundStatus" min-width="120">
        <template #default="scope">
          <dict-tag :options="lc_refund_status" :value="scope.row.refundStatus" />
        </template>
      </el-table-column>
      <el-table-column label="申请人姓名" align="center" prop="applicantName" min-width="120" />
      <el-table-column label="申请时间" align="center" prop="applyTime" width="140">
        <template #default="scope">
          <span>{{ parseTime(scope.row.applyTime, "{y}-{m}-{d}") }}</span>
        </template>
      </el-table-column>
      <el-table-column label="退款时间" align="center" prop="refundTime" width="140">
        <template #default="scope">
          <span>{{ parseTime(scope.row.refundTime, "{y}-{m}-{d}") }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="220">
        <template #default="scope">
          <el-button link type="primary" @click="handleView(scope.row)">查看</el-button>
          <el-button
              v-if="scope.row.refundStatus === '0'"
              link
              type="success"
              @click="handleRefundSuccess(scope.row)"
              v-hasPermi="['code:refund:edit']"
          >
            退款成功
          </el-button>
          <el-button
              v-if="scope.row.refundStatus === '0'"
              link
              type="danger"
              @click="openFailDialog(scope.row)"
              v-hasPermi="['code:refund:edit']"
          >
            退款失败
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

    <el-dialog title="查看退款记录" v-model="open" width="680px" append-to-body>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="退款编号">{{ form.refundNo }}</el-descriptions-item>
        <el-descriptions-item label="订单编号">{{ form.orderNo }}</el-descriptions-item>
        <el-descriptions-item label="退款状态">
          <dict-tag :options="lc_refund_status" :value="form.refundStatus" />
        </el-descriptions-item>
        <el-descriptions-item label="申请人">{{ form.applicantName }}</el-descriptions-item>
        <el-descriptions-item label="申请时间">{{ parseTime(form.applyTime, "{y}-{m}-{d} {h}:{i}:{s}") }}</el-descriptions-item>
        <el-descriptions-item label="退款时间">{{ parseTime(form.refundTime, "{y}-{m}-{d} {h}:{i}:{s}") }}</el-descriptions-item>
        <el-descriptions-item label="退款金额">{{ form.refundAmount }}</el-descriptions-item>
        <el-descriptions-item label="退款方式">{{ form.refundMethod }}</el-descriptions-item>
        <el-descriptions-item label="退款渠道">{{ form.refundChannel }}</el-descriptions-item>
        <el-descriptions-item label="失败状态码">{{ form.failCode }}</el-descriptions-item>
        <el-descriptions-item label="退款原因" :span="2">{{ form.refundReason }}</el-descriptions-item>
        <el-descriptions-item label="失败原因" :span="2">{{ form.failReason }}</el-descriptions-item>
      </el-descriptions>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="open = false">关闭</el-button>
        </div>
      </template>
    </el-dialog>

    <el-dialog title="填写退款失败信息" v-model="failOpen" width="500px" append-to-body>
      <el-form ref="failRef" :model="failForm" :rules="failRules" label-width="90px">
        <el-form-item label="失败状态码" prop="failCode">
          <el-input v-model="failForm.failCode" placeholder="请输入失败状态码" />
        </el-form-item>
        <el-form-item label="失败原因" prop="failReason">
          <el-input
              v-model="failForm.failReason"
              type="textarea"
              :rows="4"
              placeholder="请输入失败原因"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="failOpen = false">取消</el-button>
          <el-button type="primary" @click="submitRefundFail">确定</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts" name="Refund">
import type { ServiceOrderRefund, RefundQueryParams } from "@/types/api/code/refund"
import { listRefund, getRefund, refundSuccess, refundFail } from "@/api/code/refund"

const { proxy } = getCurrentInstance() as any
const { lc_refund_status, lc_order_status } = proxy.useDict("lc_refund_status", "lc_order_status")


const refundList = ref<ServiceOrderRefund[]>([])
const open = ref<boolean>(false)
const failOpen = ref<boolean>(false)
const loading = ref<boolean>(true)
const showSearch = ref<boolean>(true)
const total = ref<number>(0)
const activeRefundStatus = ref<string>("all")
const daterangeApplyTime = ref<string[]>([])
const failRef = ref()
const currentRefundId = ref<number | undefined>(undefined)

const failForm = reactive({
  failCode: "",
  failReason: ""
})

const failRules = {
  failReason: [
    { required: true, message: "请输入失败原因", trigger: "blur" }
  ]
}

const data = reactive({
  form: {} as ServiceOrderRefund,
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    refundNo: undefined,
    orderNo: undefined,
    refundStatus: undefined,
    applicantName: undefined,
    applyTime: undefined,
    params: {}
  } as RefundQueryParams
})

const { queryParams, form } = toRefs(data)

/** 查询退款列表 */
function getList() {
  loading.value = true
  queryParams.value.params = {}

  if (daterangeApplyTime.value && daterangeApplyTime.value.length === 2) {
    queryParams.value.params.beginApplyTime = daterangeApplyTime.value[0]
    queryParams.value.params.endApplyTime = daterangeApplyTime.value[1]
  }

  listRefund(queryParams.value)
      .then((response: any) => {
        refundList.value = response.rows || []
        total.value = response.total || 0
      })
      .finally(() => {
        loading.value = false
      })
}

/** 搜索 */
function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

/** 重置 */
function resetQuery() {
  daterangeApplyTime.value = []
  activeRefundStatus.value = "all"
  proxy.resetForm("queryRef")
  queryParams.value.refundStatus = undefined
  handleQuery()
}

/** 状态切换 */
function handleRefundStatusChange(name: string | number) {
  activeRefundStatus.value = String(name)
  queryParams.value.refundStatus = activeRefundStatus.value === "all" ? undefined : activeRefundStatus.value
  queryParams.value.pageNum = 1
  getList()
}

/** 查看详情 */
function handleView(row: ServiceOrderRefund) {
  if (!row.id) return
  getRefund(row.id).then((response: any) => {
    form.value = response.data
    open.value = true
  })
}

/** 退款成功 */
function handleRefundSuccess(row: ServiceOrderRefund) {
  if (!row.id) return
  proxy.$modal
      .confirm(`确认将退款编号“${row.refundNo}”处理为退款成功吗？`)
      .then(() => refundSuccess(row.id!))
      .then(() => {
        proxy.$modal.msgSuccess("操作成功")
        getList()
      })
      .catch(() => {})
}

/** 打开退款失败弹窗 */
function openFailDialog(row: ServiceOrderRefund) {
  currentRefundId.value = row.id
  failForm.failCode = ""
  failForm.failReason = ""
  failOpen.value = true
}

/** 提交退款失败 */
function submitRefundFail() {
  failRef.value.validate((valid: boolean) => {
    if (!valid || !currentRefundId.value) {
      return
    }

    refundFail({
      refundId: currentRefundId.value,
      failCode: failForm.failCode,
      failReason: failForm.failReason
    }).then(() => {
      proxy.$modal.msgSuccess("操作成功")
      failOpen.value = false
      getList()
    })
  })
}

/** 导出 */
function handleExport() {
  proxy.download(
      "code/refund/export",
      {
        ...queryParams.value
      },
      `refund_${new Date().getTime()}.xlsx`
  )
}

getList()
</script>
