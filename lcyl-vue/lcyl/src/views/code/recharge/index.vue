<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="90px">
      <el-form-item label="充值编号" prop="rechargeNo">
        <el-input
          v-model="queryParams.rechargeNo"
          placeholder="请输入充值编号"
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
          type="primary"
          plain
          icon="Plus"
          @click="openRechargeDialog"
          v-hasPermi="['code:recharge:add']"
        >
          预缴款充值
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['code:recharge:export']"
        >
          导出
        </el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="rechargeList">
      <el-table-column label="充值编号" align="center" prop="rechargeNo" min-width="180" />
      <el-table-column label="老人姓名" align="center" prop="elderName" min-width="120" />
      <el-table-column label="床位号" align="center" prop="bedNo" min-width="100" />
      <el-table-column label="充值方式" align="center" prop="rechargeMethod" min-width="120">
        <template #default="scope">
          <dict-tag :options="lc_pay_method" :value="scope.row.rechargeMethod" />
        </template>
      </el-table-column>
      <el-table-column label="充值金额(元)" align="center" prop="rechargeAmount" min-width="120" />
      <el-table-column label="状态" align="center" prop="status" min-width="100">
        <template #default="scope">
          <dict-tag v-if="hasRechargeStatusDict" :options="lc_recharge_status" :value="scope.row.status" />
          <span v-else>{{ scope.row.status ?? "-" }}</span>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template #default="scope">
          <span>{{ formatDateTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作人" align="center" prop="createBy" min-width="120" />
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />

    <el-dialog title="预缴款充值" v-model="open" width="560px" append-to-body>
      <el-form ref="rechargeRef" :model="form" :rules="rules" label-width="90px">
        <el-form-item label="老人姓名" prop="elderId">
          <el-select
            v-model="form.elderId"
            placeholder="请选择老人"
            clearable
            filterable
            style="width: 100%"
            @change="handleElderChange"
          >
            <el-option
              v-for="item in elderOptions"
              :key="item.elderId"
              :label="item.bedNo ? `${item.elderName} (${item.bedNo})` : item.elderName"
              :value="item.elderId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="床位号" prop="bedNo">
          <el-input v-model="form.bedNo" placeholder="选择老人后自动带出" readonly />
        </el-form-item>
        <el-form-item label="充值方式" prop="rechargeMethod">
          <el-select v-model="form.rechargeMethod" placeholder="请选择充值方式" style="width: 100%">
            <el-option
              v-for="dict in lc_pay_method"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="充值金额" prop="rechargeAmount">
          <el-input v-model="form.rechargeAmount" placeholder="请输入充值金额" />
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
              :on-success="handleRechargeVoucherSuccess"
              :on-error="handleVoucherUploadError"
            >
              <el-button type="primary">上传图片</el-button>
            </el-upload>
            <div v-if="form.voucherUrl" class="voucher-preview">
              <el-image
                :src="form.voucherUrl"
                :preview-src-list="[form.voucherUrl]"
                fit="cover"
                class="voucher-image"
              />
              <el-button link type="danger" @click="clearRechargeVoucher">删除</el-button>
            </div>
          </div>
        </el-form-item>
        <el-form-item label="充值备注" prop="rechargeRemark">
          <el-input
            v-model="form.rechargeRemark"
            type="textarea"
            :rows="3"
            maxlength="100"
            show-word-limit
            placeholder="请输入充值备注"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="cancel">取消</el-button>
          <el-button type="primary" @click="submitForm">确定</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts" name="Recharge">
import { getCurrentInstance, reactive, ref, toRefs } from "vue"
import { getToken } from "@/utils/auth"
import {
  listRecharge,
  rechargeBalance,
  listRechargeElderOptions,
  type RechargeElderOption
} from "@/api/code/recharge"

const { proxy } = getCurrentInstance() as any
const { lc_pay_method, lc_recharge_status } = proxy.useDict("lc_pay_method", "lc_recharge_status")

const rechargeList = ref<any[]>([])
const loading = ref<boolean>(true)
const showSearch = ref<boolean>(true)
const total = ref<number>(0)
const open = ref<boolean>(false)
const rechargeRef = ref()
const elderOptions = ref<RechargeElderOption[]>([])
const uploadAction = `${import.meta.env.VITE_APP_BASE_API}/upload`
const uploadHeaders = { Authorization: "Bearer " + getToken() }

const hasRechargeStatusDict = ref<boolean>(Array.isArray(lc_recharge_status) && lc_recharge_status.length > 0)

const data = reactive({
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    rechargeNo: undefined,
    elderName: undefined,
    bedNo: undefined
  },
  form: {
    id: undefined,
    rechargeNo: undefined,
    elderId: undefined as number | undefined,
    elderName: undefined as string | undefined,
    bedNo: undefined as string | undefined,
    rechargeMethod: undefined as string | undefined,
    rechargeAmount: undefined as string | undefined,
    voucherUrl: undefined as string | undefined,
    rechargeRemark: undefined as string | undefined,
    status: "0"
  },
  rules: {
    elderId: [{ required: true, message: "请选择老人", trigger: "change" }],
    rechargeMethod: [{ required: true, message: "请选择充值方式", trigger: "change" }],
    rechargeAmount: [{ required: true, message: "请输入充值金额", trigger: "blur" }]
  }
})

const { queryParams, form, rules } = toRefs(data)

function formatDateTime(value?: string) {
  if (!value) return "-"
  return proxy.parseTime(value, "{y}-{m}-{d} {h}:{i}:{s}")
}

function getList() {
  loading.value = true
  listRecharge(queryParams.value)
    .then((response: any) => {
      rechargeList.value = response.rows || []
      total.value = response.total || 0
    })
    .finally(() => {
      loading.value = false
    })
}

function loadElderOptions() {
  listRechargeElderOptions().then((response: any) => {
    elderOptions.value = response.data || []
  })
}

function reset() {
  form.value = {
    id: undefined,
    rechargeNo: undefined,
    elderId: undefined,
    elderName: undefined,
    bedNo: undefined,
    rechargeMethod: undefined,
    rechargeAmount: undefined,
    voucherUrl: undefined,
    rechargeRemark: undefined,
    status: "0"
  }
  proxy.resetForm("rechargeRef")
}

function openRechargeDialog() {
  reset()
  if (elderOptions.value.length === 0) {
    loadElderOptions()
  }
  open.value = true
}

function handleElderChange(elderId?: number) {
  const selected = elderOptions.value.find((item) => item.elderId === elderId)
  if (!selected) {
    form.value.elderId = undefined
    form.value.elderName = undefined
    form.value.bedNo = undefined
    return
  }
  form.value.elderId = selected.elderId
  form.value.elderName = selected.elderName
  form.value.bedNo = selected.bedNo
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

function handleRechargeVoucherSuccess(res: any) {
  proxy.$modal.closeLoading()
  if (res?.code === 200 && res?.data) {
    form.value.voucherUrl = res.data
    return
  }
  proxy.$modal.msgError(res?.message || "图片上传失败")
}

function handleVoucherUploadError() {
  proxy.$modal.closeLoading()
  proxy.$modal.msgError("图片上传失败")
}

function clearRechargeVoucher() {
  form.value.voucherUrl = undefined
}

function cancel() {
  open.value = false
  reset()
}

function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

function resetQuery() {
  proxy.resetForm("queryRef")
  handleQuery()
}

function submitForm() {
  rechargeRef.value.validate((valid: boolean) => {
    if (!valid) return

    rechargeBalance(form.value).then(() => {
      proxy.$modal.msgSuccess("充值成功")
      open.value = false
      getList()
    })
  })
}

function handleExport() {
  proxy.download(
    "code/recharge/export",
    {
      ...queryParams.value
    },
    `recharge_${new Date().getTime()}.xlsx`
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
