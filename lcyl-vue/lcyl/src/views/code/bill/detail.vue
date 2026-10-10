<template>
  <div class="app-container" v-loading="loading">
    <el-card shadow="never" class="mb16">
      <template #header>
        <div class="card-title">账单信息</div>
      </template>

      <el-descriptions :column="2" border>
        <el-descriptions-item label="账单编号">{{ form.billNo || "-" }}</el-descriptions-item>
        <el-descriptions-item label="账单类型">
          <dict-tag :options="lc_bill_type" :value="form.billType" />
        </el-descriptions-item>

        <el-descriptions-item label="账单月份">{{ form.billMonth || "-" }}</el-descriptions-item>
        <el-descriptions-item label="老人姓名">{{ form.elderName || "-" }}</el-descriptions-item>

        <el-descriptions-item label="老人身份证号">{{ form.elderIdCard || "-" }}</el-descriptions-item>
        <el-descriptions-item label="床位号">{{ form.bedNo || "-" }}</el-descriptions-item>

        <el-descriptions-item label="账单金额">{{ formatMoney(form.billAmount) }} 元</el-descriptions-item>
        <el-descriptions-item label="应付金额">{{ formatMoney(form.payableAmount) }} 元</el-descriptions-item>

        <el-descriptions-item label="账单周期">
          {{ formatDate(form.startDate) }} 至 {{ formatDate(form.endDate) }}
        </el-descriptions-item>
        <el-descriptions-item label="共计天数">{{ form.daysCount ?? 0 }} 天</el-descriptions-item>

        <el-descriptions-item label="交易状态">
          <dict-tag :options="lc_bill_trade_status" :value="form.tradeStatus" />
        </el-descriptions-item>
        <el-descriptions-item label="支付截止时间">{{ formatDateTime(form.payDeadline) }}</el-descriptions-item>

        <el-descriptions-item label="创建人">{{ form.creatorName || "-" }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ formatDateTime(form.createTime) }}</el-descriptions-item>

        <el-descriptions-item label="备注" :span="2">{{ form.remark || "-" }}</el-descriptions-item>
      </el-descriptions>
    </el-card>

    <el-card shadow="never" class="mb16">
      <template #header>
        <div class="card-title">账单明细</div>
      </template>

      <el-table v-if="billItemList.length > 0" :data="billItemList" border>
        <el-table-column label="明细类型" align="center" prop="itemType" min-width="100">
          <template #default="scope">
            <dict-tag :options="lc_bill_item_type" :value="scope.row.itemType" />
          </template>
        </el-table-column>
        <el-table-column label="费用项目" align="center" prop="feeName" min-width="180" />
        <el-table-column label="服务内容" align="center" prop="serviceContent" min-width="220" show-overflow-tooltip />
        <el-table-column label="金额(元)" align="center" prop="amount" min-width="120">
          <template #default="scope">
            <span>{{ formatMoney(scope.row.amount) }}</span>
          </template>
        </el-table-column>
      </el-table>
      <el-empty v-else description="暂无账单明细" />
    </el-card>

    <el-card v-if="paymentRecord" shadow="never" class="mb16">
      <template #header>
        <div class="card-title">支付记录</div>
      </template>

      <el-descriptions :column="2" border>
        <el-descriptions-item label="支付记录编号">{{ paymentRecord.paymentNo || "-" }}</el-descriptions-item>
        <el-descriptions-item label="账单编号">{{ paymentRecord.billNo || "-" }}</el-descriptions-item>

        <el-descriptions-item label="支付人">{{ paymentRecord.payerName || "-" }}</el-descriptions-item>
        <el-descriptions-item label="支付人手机号">{{ paymentRecord.payerPhone || "-" }}</el-descriptions-item>

        <el-descriptions-item label="支付时间">{{ formatDateTime(paymentRecord.payTime) }}</el-descriptions-item>
        <el-descriptions-item label="支付渠道">
          <dict-tag :options="lc_pay_channel" :value="paymentRecord.payChannel" />
        </el-descriptions-item>

        <el-descriptions-item label="支付方式">{{ paymentRecord.payMethod || "-" }}</el-descriptions-item>
        <el-descriptions-item label="支付金额">{{ formatMoney(paymentRecord.payAmount) }} 元</el-descriptions-item>

        <el-descriptions-item label="微信支付订单号" :span="2">
          {{ paymentRecord.wechatOrderNo || "-" }}
        </el-descriptions-item>

        <el-descriptions-item label="操作人">{{ paymentRecord.operatorName || "-" }}</el-descriptions-item>
        <el-descriptions-item label="记录创建时间">{{ formatDateTime(paymentRecord.createTime) }}</el-descriptions-item>

        <el-descriptions-item label="支付凭证" :span="2">
          <img
            v-if="paymentRecord.voucherUrl"
            :src="paymentRecord.voucherUrl"
            alt="支付凭证"
            style="max-width: 220px; border-radius: 6px;"
          />
          <span v-else>-</span>
        </el-descriptions-item>

        <el-descriptions-item label="支付备注" :span="2">
          {{ paymentRecord.payRemark || "-" }}
        </el-descriptions-item>
      </el-descriptions>
    </el-card>

    <el-card v-if="showCancelRecord" shadow="never" class="mb16">
      <template #header>
        <div class="card-title">取消记录</div>
      </template>

      <el-descriptions :column="2" border>
        <el-descriptions-item label="取消时间">{{ formatDateTime(form.cancelTime) }}</el-descriptions-item>
        <el-descriptions-item label="交易状态">
          <dict-tag :options="lc_bill_trade_status" :value="form.tradeStatus" />
        </el-descriptions-item>
        <el-descriptions-item label="取消原因" :span="2">{{ form.cancelReason || "-" }}</el-descriptions-item>
      </el-descriptions>
    </el-card>

    <div class="footer-btn">
      <el-button @click="goBack">返回</el-button>
    </div>
  </div>
</template>

<script setup lang="ts" name="BillDetail">
import { useRoute, useRouter } from "vue-router"
import type { Bill } from "@/types/api/code/bill"
import { getBill } from "@/api/code/bill"
import { listBillItem } from "@/api/code/item"
import { listBillPayment } from "@/api/code/payment"

interface BillItem {
  id?: number
  billId?: number
  billNo?: string
  itemType?: string
  feeName?: string
  serviceContent?: string
  amount?: number | string
  sort?: number
  remark?: string
}

interface BillPayment {
  id?: number
  paymentNo?: string
  billId?: number
  billNo?: string
  payerName?: string
  payerPhone?: string
  payTime?: string
  payChannel?: string
  payMethod?: string
  wechatOrderNo?: string
  payAmount?: number | string
  voucherUrl?: string
  payRemark?: string
  operatorId?: number
  operatorName?: string
  createBy?: string
  createTime?: string
}

const route = useRoute()
const router = useRouter()
const { proxy } = getCurrentInstance() as any
const { lc_bill_type, lc_bill_trade_status, lc_pay_channel, lc_bill_item_type } = proxy.useDict(
  "lc_bill_type",
  "lc_bill_trade_status",
  "lc_pay_channel",
  "lc_bill_item_type"
)

const loading = ref<boolean>(true)
const form = ref<Bill>({} as Bill)
const billItemList = ref<BillItem[]>([])
const paymentRecord = ref<BillPayment | null>(null)

const showCancelRecord = computed(() => {
  return form.value.tradeStatus === "2" && !!form.value.cancelReason
})

function formatDateTime(value?: string) {
  if (!value) return "-"
  return proxy.parseTime(value, "{y}-{m}-{d} {h}:{i}:{s}")
}

function formatDate(value?: string) {
  if (!value) return "-"
  return proxy.parseTime(value, "{y}-{m}-{d}")
}

function formatMoney(value?: number | string | null) {
  const amount = Number(value ?? 0)
  return Number.isFinite(amount) ? amount.toFixed(2) : "0.00"
}

function goBack() {
  router.back()
}

async function getDetail() {
  loading.value = true
  try {
    const id = Number(route.params.id)
    const res = await getBill(id)
    form.value = res.data || {}

    const [itemRes, paymentRes] = await Promise.all([
      listBillItem({
        pageNum: 1,
        pageSize: 100,
        billId: id
      } as any),
      listBillPayment({
        pageNum: 1,
        pageSize: 10,
        billId: id
      } as any)
    ])

    billItemList.value = itemRes.rows || []
    const paymentRows = paymentRes.rows || []
    paymentRecord.value = paymentRows.length > 0 ? paymentRows[0] : null
  } finally {
    loading.value = false
  }
}

getDetail()
</script>

<style scoped>
.mb16 {
  margin-bottom: 16px;
}

.card-title {
  font-size: 16px;
  font-weight: 600;
}

.footer-btn {
  text-align: center;
}
</style>
