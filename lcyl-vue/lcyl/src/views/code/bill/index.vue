<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="100px">
      <el-form-item label="账单编号" prop="billNo">
        <el-input
          v-model="queryParams.billNo"
          placeholder="请输入账单编号"
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
      <el-form-item label="老人身份证号" prop="elderIdCard">
        <el-input
          v-model="queryParams.elderIdCard"
          placeholder="请输入老人身份证号"
          clearable
          @keyup.enter="handleQuery"
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
          type="primary"
          plain
          icon="Plus"
          @click="openGenerateDialog"
          v-hasPermi="['code:bill:add']"
        >
          生成月度账单
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['code:bill:export']"
        >
          导出
        </el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" />
    </el-row>

    <el-tabs v-model="activeStatus" @tab-change="handleStatusChange" class="mb8">
      <el-tab-pane label="全部" name="all" />
      <el-tab-pane label="待支付" name="0" />
      <el-tab-pane label="已支付" name="1" />
      <el-tab-pane label="已关闭" name="2" />
    </el-tabs>

    <el-table v-loading="loading" :data="billList">
      <el-table-column label="账单编号" align="center" prop="billNo" min-width="180" />
      <el-table-column label="账单类型" align="center" prop="billType" min-width="110">
        <template #default="scope">
          <dict-tag :options="lc_bill_type" :value="scope.row.billType" />
        </template>
      </el-table-column>
      <el-table-column label="账单月份" align="center" prop="billMonth" min-width="100" />
      <el-table-column label="老人姓名" align="center" prop="elderName" min-width="100" />
      <el-table-column label="老人身份证号" align="center" prop="elderIdCard" min-width="180" />
      <el-table-column label="账单金额(元)" align="center" prop="billAmount" min-width="110">
        <template #default="scope">
          <span>{{ formatMoney(scope.row.billAmount) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="应付金额(元)" align="center" prop="payableAmount" min-width="110">
        <template #default="scope">
          <span>{{ formatMoney(scope.row.payableAmount) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="支付截止时间" align="center" prop="payDeadline" width="180">
        <template #default="scope">
          <span>{{ formatDateTime(scope.row.payDeadline) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="交易状态" align="center" prop="tradeStatus" min-width="100">
        <template #default="scope">
          <dict-tag :options="lc_bill_trade_status" :value="scope.row.tradeStatus" />
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template #default="scope">
          <span>{{ formatDateTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="180">
        <template #default="scope">
          <el-button
            link
            type="primary"
            :disabled="!canPay(scope.row)"
            @click="openPayDialog(scope.row)"
            v-hasPermi="['code:bill:edit']"
          >
            支付
          </el-button>
          <el-button
            link
            type="warning"
            :disabled="!canCancel(scope.row)"
            @click="openCancelDialog(scope.row)"
            v-hasPermi="['code:bill:edit']"
          >
            取消
          </el-button>
          <el-button
            link
            type="primary"
            @click="handleView(scope.row)"
            v-hasPermi="['code:bill:query']"
          >
            查看
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

    <el-dialog title="生成月度账单" v-model="generateOpen" width="520px" append-to-body>
      <el-form ref="generateRef" :model="generateForm" :rules="generateRules" label-width="90px">
        <el-form-item label="老人姓名" prop="elderId">
          <el-select v-model="generateForm.elderId" placeholder="请选择老人" style="width: 100%">
            <el-option
              v-for="item in elderOptions"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="账单月份" prop="billMonth">
          <el-date-picker
            v-model="generateForm.billMonth"
            type="month"
            value-format="YYYY-MM"
            placeholder="请选择账单月份"
            style="width: 100%"
            :disabled-date="disableCurrentAndFutureMonth"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="generateOpen = false">取消</el-button>
          <el-button type="primary" @click="submitGenerate">确定</el-button>
        </div>
      </template>
    </el-dialog>

    <el-dialog title="上传支付凭证" v-model="payOpen" width="560px" append-to-body>
      <el-form ref="payRef" :model="payForm" :rules="payRules" label-width="90px">
        <el-form-item label="支付方式" prop="payMethod">
          <el-select v-model="payForm.payMethod" placeholder="请选择支付方式" style="width: 100%">
            <el-option
              v-for="dict in lc_pay_method"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="支付凭证" prop="voucherUrl">
          <div class="voucher-upload">
            <el-upload
              :action="uploadAction"
              :headers="uploadHeaders"
              name="file"
              :show-file-list="false"
              accept=".png,.jpg,.jpeg"
              :before-upload="beforeVoucherUpload"
              :on-success="handlePayVoucherSuccess"
              :on-error="handleVoucherUploadError"
            >
              <el-button type="primary">上传图片</el-button>
            </el-upload>
            <div v-if="payForm.voucherUrl" class="voucher-preview">
              <el-image
                :src="payForm.voucherUrl"
                :preview-src-list="[payForm.voucherUrl]"
                fit="cover"
                class="voucher-image"
              />
              <el-button link type="danger" @click="clearPayVoucher">删除</el-button>
            </div>
          </div>
        </el-form-item>
        <el-form-item label="支付备注" prop="payRemark">
          <el-input
            v-model="payForm.payRemark"
            type="textarea"
            :rows="3"
            maxlength="50"
            show-word-limit
            placeholder="请输入支付备注"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="payOpen = false">取消</el-button>
          <el-button type="primary" @click="submitPay">确定</el-button>
        </div>
      </template>
    </el-dialog>

    <el-dialog title="填写取消原因" v-model="cancelOpen" width="520px" append-to-body>
      <el-form ref="cancelRef" :model="cancelForm" :rules="cancelRules" label-width="90px">
        <el-form-item label="取消原因" prop="cancelReason">
          <el-input
            v-model="cancelForm.cancelReason"
            type="textarea"
            :rows="3"
            maxlength="100"
            show-word-limit
            placeholder="请输入取消原因"
          />
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

<script setup lang="ts" name="Bill">
import { getCurrentInstance, reactive, ref, toRefs } from "vue"
import { useRouter } from "vue-router"
import { getToken } from "@/utils/auth"
import type { Bill, BillQueryParams } from "@/types/api/code/bill"
import { listBill, generateMonthlyBill, cancelBill, payBill, listBillElderOptions, type BillElderOption } from "@/api/code/bill"

const router = useRouter()
const { proxy } = getCurrentInstance() as any
const { lc_bill_trade_status, lc_bill_type, lc_pay_method } = proxy.useDict(
  "lc_bill_trade_status",
  "lc_bill_type",
  "lc_pay_method"
)

const billList = ref<Bill[]>([])
const loading = ref<boolean>(true)
const showSearch = ref<boolean>(true)
const total = ref<number>(0)
const activeStatus = ref<string>("all")

const generateOpen = ref<boolean>(false)
const payOpen = ref<boolean>(false)
const cancelOpen = ref<boolean>(false)

const generateRef = ref()
const payRef = ref()
const cancelRef = ref()
const uploadAction = `${import.meta.env.VITE_APP_BASE_API}/upload`
const uploadHeaders = { Authorization: "Bearer " + getToken() }

const currentRow = ref<Bill | null>(null)

const elderOptions = ref<BillElderOption[]>([])

const generateForm = reactive({
  elderId: undefined as number | undefined,
  billMonth: ""
})

const payForm = reactive({
  payMethod: "",
  voucherUrl: "",
  payRemark: ""
})

const cancelForm = reactive({
  cancelReason: ""
})

const generateRules = {
  elderId: [{ required: true, message: "请选择老人", trigger: "change" }],
  billMonth: [{ required: true, message: "请选择账单月份", trigger: "change" }]
}

const payRules = {
  payMethod: [{ required: true, message: "请选择支付方式", trigger: "change" }],
  voucherUrl: [{ required: true, message: "请上传支付凭证", trigger: "change" }],
  payRemark: [{ required: true, message: "请输入支付备注", trigger: "blur" }]
}

const cancelRules = {
  cancelReason: [{ required: true, message: "请输入取消原因", trigger: "blur" }]
}

const data = reactive({
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    billNo: undefined,
    elderName: undefined,
    elderIdCard: undefined,
    tradeStatus: undefined
  } as BillQueryParams
})

const { queryParams } = toRefs(data)

function formatMoney(value?: number | string | null) {
  const amount = Number(value ?? 0)
  return Number.isFinite(amount) ? amount.toFixed(2) : "0.00"
}

function formatDateTime(value?: string) {
  if (!value) return "-"
  return proxy.parseTime(value, "{y}-{m}-{d} {h}:{i}:{s}")
}

function getPreviousMonthValue() {
  const now = new Date()
  const previousMonth = new Date(now.getFullYear(), now.getMonth() - 1, 1)
  const year = previousMonth.getFullYear()
  const month = String(previousMonth.getMonth() + 1).padStart(2, "0")
  return `${year}-${month}`
}

function disableCurrentAndFutureMonth(date: Date) {
  const currentMonthStart = new Date()
  currentMonthStart.setDate(1)
  currentMonthStart.setHours(0, 0, 0, 0)

  const candidateMonthStart = new Date(date.getFullYear(), date.getMonth(), 1)
  return candidateMonthStart.getTime() >= currentMonthStart.getTime()
}

function getList() {
  loading.value = true
  listBill(queryParams.value)
    .then((response: any) => {
      billList.value = response.rows || []
      total.value = response.total || 0
    })
    .finally(() => {
      loading.value = false
    })
}

function loadElderOptions() {
  listBillElderOptions().then((response: any) => {
    elderOptions.value = response.rows || []
  })
}
// 查询
function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}
// 重置
function resetQuery() {
  proxy.resetForm("queryRef")
  activeStatus.value = "all"
  queryParams.value.tradeStatus = undefined
  handleQuery()
}
// 状态选择
function handleStatusChange() {
  queryParams.value.tradeStatus = activeStatus.value === "all" ? undefined : activeStatus.value
  queryParams.value.pageNum = 1
  getList()
}
// 支付
function canPay(row: Bill) {
  return row.tradeStatus === "0" && row.billType === "1"
}

function canCancel(row: Bill) {
  return row.tradeStatus === "0"
}
// 生成账单
function openGenerateDialog() {
  generateForm.elderId = undefined
  generateForm.billMonth = getPreviousMonthValue()
  if (!elderOptions.value.length) {
    loadElderOptions()
  }
  generateOpen.value = true
}

function openPayDialog(row: Bill) {
  currentRow.value = row
  payForm.payMethod = ""
  payForm.voucherUrl = ""
  payForm.payRemark = ""
  payOpen.value = true
}

function beforeVoucherUpload(file: File) {
  const isImage = ["image/jpeg", "image/png", "image/jpg"].includes(file.type) || /\.(png|jpe?g)$/i.test(file.name)
  if (!isImage) {
    proxy.$modal.msgError("请上传 jpg、jpeg、png 格式图片")
    return false
  }
  const isLt5M = file.size / 1024 / 1024 < 5
  if (!isLt5M) {
    proxy.$modal.msgError("图片大小不能超过 5MB")
    return false
  }
  proxy.$modal.loading("正在上传图片，请稍候...")
  return true
}

function handlePayVoucherSuccess(res: any) {
  proxy.$modal.closeLoading()
  if (res?.code === 200 && res?.data) {
    payForm.voucherUrl = res.data
    return
  }
  proxy.$modal.msgError(res?.message || "图片上传失败")
}

function handleVoucherUploadError() {
  proxy.$modal.closeLoading()
  proxy.$modal.msgError("图片上传失败")
}

function clearPayVoucher() {
  payForm.voucherUrl = ""
}

function openCancelDialog(row: Bill) {
  currentRow.value = row
  cancelForm.cancelReason = ""
  cancelOpen.value = true
}

function submitGenerate() {
  generateRef.value.validate((valid: boolean) => {
    if (!valid) return

    generateMonthlyBill({
      elderId: generateForm.elderId!,
      billMonth: generateForm.billMonth
    }).then(() => {
      proxy.$modal.msgSuccess("生成成功")
      generateOpen.value = false
      getList()
    })
  })
}

function submitPay() {
  payRef.value.validate((valid: boolean) => {
    if (!valid || !currentRow.value?.id) return

    payBill({
      billId: currentRow.value.id,
      payMethod: payForm.payMethod,
      voucherUrl: payForm.voucherUrl,
      payRemark: payForm.payRemark
    }).then(() => {
      proxy.$modal.msgSuccess("支付成功")
      payOpen.value = false
      getList()
    })
  })
}

function submitCancel() {
  cancelRef.value.validate((valid: boolean) => {
    if (!valid || !currentRow.value?.id) return

    cancelBill({
      billId: currentRow.value.id,
      cancelReason: cancelForm.cancelReason
    }).then(() => {
      proxy.$modal.msgSuccess("取消成功")
      cancelOpen.value = false
      getList()
    })
  })
}

function handleView(row: Bill) {
  router.push({
    path: `/code/bill-detail/index/${row.id}`
  })
}

function handleExport() {
  proxy.download(
    "code/bill/export",
    {
      ...queryParams.value
    },
    `bill_${new Date().getTime()}.xlsx`
  )
}

getList()
loadElderOptions()
</script>

<style scoped>
.voucher-upload {
  width: 100%;
}

.voucher-preview {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-top: 12px;
}

.voucher-image {
  width: 96px;
  height: 96px;
  border-radius: 6px;
  border: 1px solid #dcdfe6;
}
</style>
