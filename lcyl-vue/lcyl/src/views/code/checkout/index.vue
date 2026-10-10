<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="单据编号" prop="name">
        <el-input
            v-model="queryParams.name"
            placeholder="请输入单据编号"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="姓名" prop="name">
        <el-input
            v-model="queryParams.name"
            placeholder="请输入姓名"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="身份证号" prop="idCardNo">
        <el-input
            v-model="queryParams.idCardNo"
            placeholder="请输入身份证号"
            clearable
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="退住时间" prop="checkOutTime">
        <el-date-picker clearable
                        v-model="queryParams.checkOutTime"
                        type="date"
                        value-format="YYYY-MM-DD"
                        placeholder="请选择退住时间">
        </el-date-picker>
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
            v-hasPermi="['code:checkout:add']"
            v-hasRole="['nurse']"
        >发起退住申请</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="warning"
            plain
            icon="Download"
            @click="handleExport"
            v-hasPermi="['code:checkout:export']"
        >导出</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="primary" size="mini" @click="handleApprove">我的审批</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="checkoutList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序号" align="center" prop="id" />
      <el-table-column label="单据编号" align="center" prop="retreatCode" />
      <el-table-column label="老人姓名" align="center" prop="name" />
      <el-table-column label="老人身份证号" align="center" prop="idCardNo" width="180" />
      <el-table-column label="退住时间" align="center" prop="checkOutTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.checkOutTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="创建人" align="center" prop="applicat" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}') }}</span>
        </template>
      </el-table-column>

      <!-- 审批状态 -->
      <el-table-column label="审批状态" align="center" width="120">
        <template #default="scope">
          <el-tag :type="getStatusTagType(scope.row.flowStatus)">
            {{ getStatusText(scope.row.flowStatus) }}
          </el-tag>
        </template>
      </el-table-column>

      <!-- 右侧 操作按钮 -->
      <el-table-column label="操作" align="center" width="120">
        <template #default="scope">
          <!-- 已成功退住 → 可点击 查看 -->
          <el-button
              type="success"
              size="mini"
              v-if="scope.row.flowStatus === 7"
              @click="handleView(scope.row)"
          >
            查看
          </el-button>

          <el-button
              type="danger"
              size="mini"
              v-else-if="scope.row.flowStatus === 0"
              @click="handleView(scope.row)"
              disabled
          >
            请重新申请
          </el-button>

          <!-- 审批中 → 按钮显示，但不可点击、禁用 -->
          <el-button
              type="primary"
              size="mini"
              v-else
              disabled
          >
          审批中
          </el-button>
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
  </div>
</template>

<script setup lang="ts" name="Config">
import type { Retreat, CheckoutQueryParams } from "@/types/api/code/checkout"
import { listCheckout, addCheckout, updateCheckout } from "@/api/code/checkout"
import { useRouter } from 'vue-router'

const router = useRouter()
const { proxy } = getCurrentInstance()

const checkoutList = ref<Retreat[]>([])
const open = ref<boolean>(false)
const loading = ref<boolean>(true)
const showSearch = ref<boolean>(true)
const ids = ref<number[]>([])
const single = ref<boolean>(true)
const multiple = ref<boolean>(true)
const total = ref<number>(0)
const title = ref<string>("")

const data = reactive({
  form: {} as Retreat,
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    retreatCode: undefined,
    title: undefined,
    elderId: undefined,
    name: undefined,
    idCardNo: undefined,
    phone: undefined,
    checkInStartTime: undefined,
    checkInEndTime: undefined,
    nursingLevelName: undefined,
    bedNo: undefined,
    contractName: undefined,
    contractUrl: undefined,
    contractNo: undefined,
    counselor: undefined,
    nursingName: undefined,
    checkOutTime: undefined,
    reason: undefined,
    applicat: undefined,
    applicatId: undefined,
    deptNo: undefined,
    flowStatus: undefined,
    status: undefined,
  } as CheckoutQueryParams,
  rules: {}
})

const { queryParams, form, rules } = toRefs(data)

/** 查询退住列表 */
function getList() {
  loading.value = true
  listCheckout(queryParams.value).then(response => {
    checkoutList.value = response.rows
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
    retreatCode: null,
    title: null,
    elderId: null,
    name: null,
    idCardNo: null,
    phone: null,
    checkInStartTime: null,
    checkInEndTime: null,
    nursingLevelName: null,
    bedNo: null,
    contractName: null,
    contractUrl: null,
    contractNo: null,
    counselor: null,
    nursingName: null,
    checkOutTime: null,
    reason: null,
    remark: null,
    applicat: null,
    applicatId: null,
    deptNo: null,
    createTime: null,
    flowStatus: null,
    status: null,
    createBy: null,
    updateBy: null,
    updateTime: null
  }
  proxy.resetForm("checkoutRef")
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm("queryRef")
  handleQuery()
}

// 多选框选中数据
function handleSelectionChange(selection: Retreat[]) {
  ids.value = selection.map(item => item.id)
  single.value = selection.length != 1
  multiple.value = !selection.length
}

const handleApprove = ()=>{
  router.push('/system/my-to/index')
}

/** 进入审批详情页面 */
function handleApproveDetail(row: Retreat) {
  router.push({
    name: "Finishment",
    query: {
      businessId: row.id,
      id: row.id
    }
  })
}

/** 查看成功退住详情 */
function handleView(row: Retreat) {
  router.push({
    name: "Finishment",
    query: {
      businessId: row.id,
      id: row.id
    }
  })
}

/** 新增按钮操作 */
function handleAdd() {
  router.push('/code/checkout-apply/checkoutApply')
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["checkoutRef"].validate((valid: boolean) => {
    if (valid) {
      if (form.value.id != null) {
        updateCheckout(form.value).then(() => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addCheckout(form.value).then(() => {
          proxy.$modal.msgSuccess("新增成功")
          open.value = false
          getList()
        })
      }
    }
  })
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('code/checkout/export', {
    ...queryParams.value
  }, `checkout_${new Date().getTime()}.xlsx`)
}

function getStatusText(status) {
  if (status === 7) return '已成功退住'
  if (status == 0) return '已驳回'
  const map = {
    1: '护理组长审批',
    2: '法务审核',
    3: '结算员审批',
    4: '结算主管审批',
    5: '副院长审批',
    6: '结算员清算',
    7: '已成功退住',
  }
  return map[status] || '未知状态'
}

function getStatusTagType(status) {
  // 已驳回 → 红色
  if (status === 0) return 'danger'
  // 已成功退住 → 绿色
  if (status === 7) return 'success'
  // 审批中 → 黄色
  const map = {
    1: 'warning',
    2: 'warning',
    3: 'warning',
    4: 'warning',
    5: 'warning',
    6: 'primary'
  }
  return map[status] || 'info'
}

getList()
</script>