<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="支付记录编号" prop="paymentNo">
        <el-input
          v-model="queryParams.paymentNo"
          placeholder="请输入支付记录编号"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="账单ID" prop="billId">
        <el-input
          v-model="queryParams.billId"
          placeholder="请输入账单ID"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="账单编号" prop="billNo">
        <el-input
          v-model="queryParams.billNo"
          placeholder="请输入账单编号"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="支付人" prop="payerName">
        <el-input
          v-model="queryParams.payerName"
          placeholder="请输入支付人"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="支付时间" style="width: 308px">
        <el-date-picker
          v-model="daterangePayTime"
          value-format="YYYY-MM-DD"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label="支付渠道(1线上支付 2线下支付)" prop="payChannel">
        <el-select v-model="queryParams.payChannel" placeholder="请选择支付渠道(1线上支付 2线下支付)" clearable>
          <el-option
            v-for="dict in lc_pay_channel"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="支付方式(微信/支付宝/网银/现金/预缴款/其他)" prop="payMethod">
        <el-select v-model="queryParams.payMethod" placeholder="请选择支付方式(微信/支付宝/网银/现金/预缴款/其他)" clearable>
          <el-option
            v-for="dict in lc_pay_method"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
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
          @click="handleAdd"
          v-hasPermi="['code:payment:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['code:payment:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['code:payment:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['code:payment:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="paymentList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="主键ID" align="center" prop="id" />
      <el-table-column label="支付记录编号" align="center" prop="paymentNo" />
      <el-table-column label="账单编号" align="center" prop="billNo" />
      <el-table-column label="支付人" align="center" prop="payerName" />
      <el-table-column label="支付时间" align="center" prop="payTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.payTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="支付渠道(1线上支付 2线下支付)" align="center" prop="payChannel">
        <template #default="scope">
          <dict-tag :options="lc_pay_channel" :value="scope.row.payChannel"/>
        </template>
      </el-table-column>
      <el-table-column label="支付方式(微信/支付宝/网银/现金/预缴款/其他)" align="center" prop="payMethod">
        <template #default="scope">
          <dict-tag :options="lc_pay_method" :value="scope.row.payMethod"/>
        </template>
      </el-table-column>
      <el-table-column label="支付金额" align="center" prop="payAmount" />
      <el-table-column label="操作人姓名" align="center" prop="operatorName" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['code:payment:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['code:payment:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <pagination
      v-show="total>0"
      :total="total"
      v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改账单支付记录对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="paymentRef" :model="form" :rules="rules" label-width="80px">
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts" name="Config">
import type { BillPayment, PaymentQueryParams } from "@/types/api/code/payment"
import { listPayment, getPayment, delPayment, addPayment, updatePayment } from "@/api/code/payment"

const { proxy } = getCurrentInstance()
const { lc_pay_channel, lc_pay_method } = proxy.useDict('lc_pay_channel', 'lc_pay_method')

const paymentList = ref<BillPayment[]>([])
const open = ref<boolean>(false)
const loading = ref<boolean>(true)
const showSearch = ref<boolean>(true)
const ids = ref<number[]>([])
const single = ref<boolean>(true)
const multiple = ref<boolean>(true)
const total = ref<number>(0)
const title = ref<string>("")
const daterangePayTime = ref<string[]>([])

const data = reactive({
  form: {} as BillPayment,
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    paymentNo: undefined,
    billId: undefined,
    billNo: undefined,
    payerName: undefined,
    payTime: undefined,
    payChannel: undefined,
    payMethod: undefined,
  } as PaymentQueryParams,
  rules: {
    payChannel: [
      { required: true, message: "支付渠道(1线上支付 2线下支付)不能为空", trigger: "change" }
    ],
    payAmount: [
      { required: true, message: "支付金额不能为空", trigger: "blur" }
    ],
  }
})

const { queryParams, form, rules } = toRefs(data)

/** 查询账单支付记录列表 */
function getList() {
  loading.value = true
  queryParams.value.params = {}
  if (null != daterangePayTime.value && '' != daterangePayTime.value) {
    queryParams.value.params["beginPayTime"] = daterangePayTime.value[0]
    queryParams.value.params["endPayTime"] = daterangePayTime.value[1]
  }
  listPayment(queryParams.value).then(response => {
    paymentList.value = response.rows
    total.value = response.total
    loading.value = false
  })
}

// 取消按钮
function cancel() {
  open.value = false
  reset()
}

// 表单重置
function reset() {
  form.value = {
    id: null,
    paymentNo: null,
    billId: null,
    billNo: null,
    payerName: null,
    payerPhone: null,
    payTime: null,
    payChannel: null,
    payMethod: null,
    wechatOrderNo: null,
    payAmount: null,
    voucherUrl: null,
    payRemark: null,
    operatorId: null,
    operatorName: null,
    createBy: null,
    createTime: null
  }
  proxy.resetForm("paymentRef")
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

/** 重置按钮操作 */
function resetQuery() {
  daterangePayTime.value = []
  proxy.resetForm("queryRef")
  handleQuery()
}

// 多选框选中数据
function handleSelectionChange(selection: BillPayment[]) {
  ids.value = selection.map(item => item.id)
  single.value = selection.length != 1
  multiple.value = !selection.length
}

/** 新增按钮操作 */
function handleAdd() {
  reset()
  open.value = true
  title.value = "添加账单支付记录"
}

/** 修改按钮操作 */
function handleUpdate(row: BillPayment) {
  reset()
  const _id = row.id || ids.value[0]
  getPayment(_id).then(response => {
    form.value = response.data
    open.value = true
    title.value = "修改账单支付记录"
  })
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["paymentRef"].validate((valid: boolean) => {
    if (valid) {
      if (form.value.id != null) {
        updatePayment(form.value).then(() => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addPayment(form.value).then(() => {
          proxy.$modal.msgSuccess("新增成功")
          open.value = false
          getList()
        })
      }
    }
  })
}

/** 删除按钮操作 */
function handleDelete(row: BillPayment) {
  const _ids = row.id || ids.value
  proxy.$modal.confirm('是否确认删除账单支付记录编号为"' + _ids + '"的数据项？').then(function() {
    return delPayment(_ids)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('code/payment/export', {
    ...queryParams.value
  }, `payment_${new Date().getTime()}.xlsx`)
}

getList()
</script>
