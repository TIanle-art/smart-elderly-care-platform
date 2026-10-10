<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="老人姓名" prop="elderName">
        <el-input
          v-model="queryParams.elderName"
          placeholder="请输入老人姓名"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="床位号" prop="bedNo">
        <el-input
          v-model="queryParams.bedNo"
          placeholder="请输入床位号"
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
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['code:balance:export']"
        >
          导出
        </el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="balanceList">
      <el-table-column label="老人姓名" align="center" prop="elderName" min-width="120" />
      <el-table-column label="床位号" align="center" prop="bedNo" min-width="100" />
      <el-table-column label="预缴款余额(元)" align="center" prop="prepaidBalance" min-width="140">
        <template #default="scope">
          <span>{{ formatMoney(scope.row.prepaidBalance) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="押金金额(元)" align="center" prop="depositAmount" min-width="140">
        <template #default="scope">
          <span>{{ formatMoney(scope.row.depositAmount) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="欠费金额(元)" align="center" min-width="140">
        <template #default="scope">
          <span :style="{ color: Number(scope.row.arrearsAmount || 0) > 0 ? '#f56c6c' : '#606266' }">
            {{ formatMoney(scope.row.arrearsAmount) }}
          </span>
        </template>
      </el-table-column>
      <el-table-column label="支付截止时间" align="center" prop="paymentDeadline" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.paymentDeadline, "{y}-{m}-{d} {h}:{i}:{s}") }}</span>
        </template>
      </el-table-column>
      <el-table-column label="更新时间" align="center" prop="updateTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.updateTime, "{y}-{m}-{d} {h}:{i}:{s}") }}</span>
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
  </div>
</template>

<script setup lang="ts" name="Balance">
import type { Balance, BalanceQueryParams } from "@/types/api/code/balance"
import { listBalance } from "@/api/code/balance"

const { proxy } = getCurrentInstance() as any

const balanceList = ref<Balance[]>([])
const loading = ref<boolean>(true)
const showSearch = ref<boolean>(true)
const total = ref<number>(0)

const data = reactive({
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    elderName: undefined,
    bedNo: undefined
  } as BalanceQueryParams
})

const { queryParams } = toRefs(data)

function formatMoney(value?: number | string | null) {
  const amount = Number(value ?? 0)
  return Number.isFinite(amount) ? amount.toFixed(2) : "0.00"
}

function getList() {
  loading.value = true
  listBalance(queryParams.value)
    .then((response: any) => {
      balanceList.value = response.rows || []
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
  proxy.resetForm("queryRef")
  handleQuery()
}

function handleExport() {
  proxy.download(
    "code/balance/export",
    {
      ...queryParams.value
    },
    `balance_${new Date().getTime()}.xlsx`
  )
}

getList()
</script>
