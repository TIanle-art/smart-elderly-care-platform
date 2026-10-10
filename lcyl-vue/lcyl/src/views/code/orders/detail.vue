<template>
  <div class="app-container" v-loading="loading">
    <el-card shadow="never" class="mb16">
      <template #header>
        <div class="card-title">订单进度</div>
      </template>

      <div class="progress-wrap">
        <div v-for="(item, index) in progressNodes" :key="index" class="progress-item">
          <div class="progress-node" :class="{ active: item.active }">
            {{ index + 1 }}
          </div>
          <div class="progress-text">
            <div class="progress-label" :class="{ active: item.active }">{{ item.label }}</div>
            <div class="progress-time">{{ item.time || "" }}</div>
          </div>
          <div v-if="index !== progressNodes.length - 1" class="progress-line" />
        </div>
      </div>
    </el-card>

    <el-card shadow="never" class="mb16">
      <template #header>
        <div class="card-title">订单信息</div>
      </template>

      <el-descriptions :column="2" border>
        <el-descriptions-item label="订单编号">{{ form.orderNo || "-" }}</el-descriptions-item>
        <el-descriptions-item label="期望服务时间">{{ formatDateTime(form.expectedServiceTime) }}</el-descriptions-item>
        <el-descriptions-item label="老人姓名">{{ form.elderName || "-" }}</el-descriptions-item>
        <el-descriptions-item label="床位号">{{ form.bedNo || "-" }}</el-descriptions-item>
        <el-descriptions-item label="护理项目">{{ form.projectName || "-" }}</el-descriptions-item>
        <el-descriptions-item label="订单金额">{{ formatMoney(form.orderAmount) }} 元</el-descriptions-item>
        <el-descriptions-item label="下单人">{{ form.applicantName || "-" }}</el-descriptions-item>
        <el-descriptions-item label="下单人手机号">{{ form.applicantPhone || "-" }}</el-descriptions-item>
        <el-descriptions-item label="订单状态">
          <dict-tag :options="lc_order_status" :value="form.orderStatus" />
        </el-descriptions-item>
        <el-descriptions-item label="交易状态">
          <dict-tag :options="lc_trade_status" :value="form.tradeStatus" />
        </el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ formatDateTime(form.createTime) }}</el-descriptions-item>
        <el-descriptions-item label="备注">{{ form.remark || "-" }}</el-descriptions-item>
      </el-descriptions>
    </el-card>

    <el-card shadow="never" class="mb16">
      <template #header>
        <div class="card-title">支付记录</div>
      </template>

      <el-descriptions :column="2" border>
        <el-descriptions-item label="交易状态">
          <dict-tag :options="lc_trade_status" :value="payDisplayStatus" />
        </el-descriptions-item>
        <el-descriptions-item label="支付渠道">
          {{ ["1", "2", "3", "4"].includes(payDisplayStatus) ? "线上支付" : "-" }}
        </el-descriptions-item>
        <el-descriptions-item label="支付方式">
          {{ ["1", "2", "3", "4"].includes(payDisplayStatus) ? "微信" : "-" }}
        </el-descriptions-item>
        <el-descriptions-item label="支付金额">
          {{ ["1", "2", "3", "4"].includes(payDisplayStatus) ? `${formatMoney(form.orderAmount)} 元` : "-" }}
        </el-descriptions-item>
      </el-descriptions>
    </el-card>

    <el-card v-if="showCancelRecord" shadow="never" class="mb16">
      <template #header>
        <div class="card-title">取消记录</div>
      </template>

      <el-descriptions :column="2" border>
        <el-descriptions-item label="取消人">{{ form.applicantName || "后台用户" }}</el-descriptions-item>
        <el-descriptions-item label="取消时间">{{ formatDateTime(form.cancelTime) }}</el-descriptions-item>
        <el-descriptions-item label="取消原因" :span="2">{{ form.cancelReason || "-" }}</el-descriptions-item>
      </el-descriptions>
    </el-card>

    <el-card v-if="showExecuteRecord" shadow="never" class="mb16">
      <template #header>
        <div class="card-title">执行记录</div>
      </template>

      <template v-if="executionInfo">
        <el-descriptions :column="1" border>
          <el-descriptions-item label="执行人">
            {{ executionInfo.executorName || "-" }}
          </el-descriptions-item>
          <el-descriptions-item label="执行图片" :span="2">
            <el-image
              v-if="executionInfo.executeImage"
              :src="executionInfo.executeImage"
              :preview-src-list="[executionInfo.executeImage]"
              fit="cover"
              class="execute-image"
            />
            <span v-else>-</span>
          </el-descriptions-item>
          <el-descriptions-item label="执行记录" :span="2">
            {{ executionInfo.executeRecord || "暂无执行说明" }}
          </el-descriptions-item>
        </el-descriptions>
      </template>
      <el-empty v-else description="暂无执行记录" />
    </el-card>

    <el-card v-if="latestRefund" shadow="never" class="mb16">
      <template #header>
        <div class="card-title">退款记录</div>
      </template>

      <el-descriptions :column="2" border>
        <el-descriptions-item label="退款状态">
          <dict-tag :options="lc_refund_status" :value="latestRefund.refundStatus" />
        </el-descriptions-item>
        <el-descriptions-item label="申请人">
          {{ latestRefund.applicantName || "-" }}
          <el-tag size="small" type="warning" style="margin-left: 8px">
            {{ latestRefund.applicantType === "1" ? "前台客户" : "后台用户" }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="申请时间">{{ formatDateTime(latestRefund.applyTime) }}</el-descriptions-item>
        <el-descriptions-item label="退款时间">{{ formatDateTime(latestRefund.refundTime) }}</el-descriptions-item>
        <el-descriptions-item label="退款原因" :span="2">{{ latestRefund.refundReason || "-" }}</el-descriptions-item>
        <el-descriptions-item label="退款渠道">{{ latestRefund.refundChannel || "原路退回" }}</el-descriptions-item>
        <el-descriptions-item label="退款方式">{{ latestRefund.refundMethod || "微信" }}</el-descriptions-item>
        <el-descriptions-item v-if="latestRefund.refundStatus === '1'" label="退款编号">
          {{ latestRefund.refundNo || "-" }}
        </el-descriptions-item>
        <el-descriptions-item v-if="latestRefund.refundStatus === '1'" label="退款金额">
          {{ formatMoney(latestRefund.refundAmount) }} 元
        </el-descriptions-item>
        <el-descriptions-item v-if="latestRefund.refundStatus === '2'" label="失败状态码">
          {{ latestRefund.failCode || "-" }}
        </el-descriptions-item>
        <el-descriptions-item v-if="latestRefund.refundStatus === '2'" label="失败原因" :span="2">
          {{ latestRefund.failReason || "-" }}
        </el-descriptions-item>
      </el-descriptions>
    </el-card>

    <div class="footer-wrap">
      <el-button @click="goBack">返回</el-button>
    </div>
  </div>
</template>

<script setup lang="ts" name="OrderDetail">
import { computed, getCurrentInstance, ref } from "vue"
import { useRoute, useRouter } from "vue-router"
import { getOrderExecution, getOrders } from "@/api/code/orders"
import { listRefund } from "@/api/code/refund"
import type { ServiceOrder, ServiceOrderExecution } from "@/types/api/code/orders"
import type { ServiceOrderRefund } from "@/types/api/code/refund"

const route = useRoute()
const router = useRouter()
const { proxy } = getCurrentInstance() as any
const { lc_order_status, lc_trade_status, lc_refund_status } = proxy.useDict(
  "lc_order_status",
  "lc_trade_status",
  "lc_refund_status"
)

const loading = ref(true)
const form = ref<ServiceOrder>({})
const executionInfo = ref<ServiceOrderExecution | null>(null)
const latestRefund = ref<ServiceOrderRefund | null>(null)

const showCancelRecord = computed(() => form.value.orderStatus === "5" && !!form.value.cancelReason)
const showExecuteRecord = computed(() => !!executionInfo.value || ["2", "3"].includes(form.value.orderStatus || ""))

const payDisplayStatus = computed(() => {
  if (form.value.tradeStatus === "0") return "0"
  if (form.value.tradeStatus === "5") return "5"
  return "1"
})

const progressNodes = computed(() => {
  const order = form.value
  const nodes: Array<{ label: string; active: boolean; time: string }> = []

  const createTime = formatDateTime(order.createTime)
  const payTime = formatDateTime(order.payTime || order.createTime)
  const executeTime = formatDateTime(order.executeTime || executionInfo.value?.executeTime)
  const finishTime = formatDateTime(order.finishTime)
  const cancelTime = formatDateTime(order.cancelTime)

  if (order.orderStatus === "5") {
    nodes.push({ label: "已下单", active: true, time: createTime })
    nodes.push({ label: "已关闭", active: true, time: cancelTime })
    return nodes
  }

  if (latestRefund.value && latestRefund.value.refundStatus === "1") {
    nodes.push({ label: "已下单", active: true, time: createTime })
    nodes.push({ label: "已支付", active: true, time: payTime })
    if (order.executeTime || executionInfo.value?.executeTime) {
      nodes.push({ label: "已执行", active: true, time: executeTime })
    }
    nodes.push({
      label: "已退款",
      active: true,
      time: formatDateTime(latestRefund.value.refundTime || latestRefund.value.applyTime)
    })
    return nodes
  }

  if (order.orderStatus === "0") {
    nodes.push({ label: "已下单", active: true, time: createTime })
    nodes.push({ label: "已支付", active: false, time: "" })
    nodes.push({ label: "已执行", active: false, time: "" })
    nodes.push({ label: "已完成", active: false, time: "" })
    return nodes
  }

  if (order.orderStatus === "1") {
    nodes.push({ label: "已下单", active: true, time: createTime })
    nodes.push({ label: "已支付", active: true, time: payTime })
    nodes.push({ label: "已执行", active: false, time: "" })
    nodes.push({ label: "已完成", active: false, time: "" })
    return nodes
  }

  if (order.orderStatus === "2") {
    nodes.push({ label: "已下单", active: true, time: createTime })
    nodes.push({ label: "已支付", active: true, time: payTime })
    nodes.push({ label: "已执行", active: true, time: executeTime })
    nodes.push({ label: "已完成", active: false, time: "" })
    return nodes
  }

  if (order.orderStatus === "3") {
    nodes.push({ label: "已下单", active: true, time: createTime })
    nodes.push({ label: "已支付", active: true, time: payTime })
    nodes.push({ label: "已执行", active: true, time: executeTime })
    nodes.push({ label: "已完成", active: true, time: finishTime })
    return nodes
  }

  return nodes
})

function formatDateTime(value?: string) {
  if (!value) return "-"
  return proxy.parseTime(value, "{y}-{m}-{d} {h}:{i}:{s}")
}

function formatMoney(value?: string | number) {
  const amount = Number(value ?? 0)
  return Number.isNaN(amount) ? "0.00" : amount.toFixed(2)
}

function goBack() {
  router.back()
}

async function getDetail() {
  loading.value = true
  try {
    const id = Number(route.params.id)
    const orderRes = await getOrders(id)
    form.value = orderRes.data || {}

    const [executionRes, refundRes] = await Promise.all([
      getOrderExecution(id),
      form.value.orderNo
        ? listRefund({
            pageNum: 1,
            pageSize: 10,
            orderNo: form.value.orderNo
          } as any)
        : Promise.resolve({ rows: [] })
    ])

    executionInfo.value = executionRes.data || null
    const rows = (refundRes as any).rows || []
    latestRefund.value = rows.length ? rows[rows.length - 1] : null
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

.progress-wrap {
  display: flex;
  align-items: flex-start;
  overflow-x: auto;
}

.progress-item {
  display: flex;
  align-items: center;
  min-width: 180px;
}

.progress-node {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background: #dcdfe6;
  color: #606266;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 600;
}

.progress-node.active {
  background: #1d6fff;
  color: #fff;
}

.progress-text {
  margin-left: 12px;
  min-width: 90px;
}

.progress-label {
  color: #909399;
  font-weight: 600;
}

.progress-label.active {
  color: #1d6fff;
}

.progress-time {
  margin-top: 8px;
  font-size: 12px;
  color: #606266;
}

.progress-line {
  width: 80px;
  height: 1px;
  background: #dcdfe6;
  margin: 16px 16px 0;
}

.execute-image {
  width: 140px;
  height: 140px;
  border-radius: 8px;
  overflow: hidden;
}

.footer-wrap {
  text-align: center;
}
</style>
