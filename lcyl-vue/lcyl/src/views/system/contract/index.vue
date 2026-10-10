<template>
  <!-- 列表区域 -->
  <div v-if="!showDetail" class="app-container">
    <el-form
        :model="queryParams"
        ref="queryRef"
        :inline="true"
        v-show="showSearch"
        label-width="68px"
        class="search-form"
    >
      <el-form-item label="合同编号" prop="contractNo">
        <el-input
            v-model="queryParams.contractNo"
            placeholder="请输入"
            clearable
            @keyup.enter="handleQuery"
            style="width: 180px"
        />
      </el-form-item>
      <el-form-item label="老人姓名" prop="elderName">
        <el-input
            v-model="queryParams.elderName"
            placeholder="请输入"
            clearable
            @keyup.enter="handleQuery"
            style="width: 180px"
        />
      </el-form-item>
      <el-form-item label="合同状态" prop="status">
        <el-select
            v-model="queryParams.status"
            placeholder="请选择"
            clearable
            style="width: 180px"
        >
          <el-option label="未生效" :value="0" />
          <el-option label="已生效" :value="1" />
          <el-option label="已过期" :value="2" />
          <el-option label="已失效" :value="3" />
        </el-select>
      </el-form-item>
      <el-form-item label="合同期限" prop="contractPeriod">
        <el-date-picker
            v-model="daterangePeriod"
            value-format="YYYY-MM-DD"
            type="daterange"
            range-separator="-"
            start-placeholder="开始时间"
            end-placeholder="结束时间"
            style="width: 220px"
            @change="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
        <el-button type="primary" @click="handleQuery" style="background: #0066FF; border: none">搜索</el-button>
      </el-form-item>
    </el-form>

    <el-table
        v-loading="loading"
        :data="contractList"
        border
        :header-cell-style="{background: '#F5F7FA'}"
        class="contract-table"
    >
      <el-table-column label="序号" align="center" width="60">
        <template #default="scope">
          {{ (queryParams.pageNum - 1) * queryParams.pageSize + scope.$index + 1 }}
        </template>
      </el-table-column>
      <el-table-column label="合同编号" align="center" prop="contractNo" />
      <el-table-column label="合同名称" align="center" prop="name" width="150" />
      <el-table-column label="老人姓名" align="center">
        <template #default="scope">
          {{ scope.row.elder?.name }}
        </template>
      </el-table-column>
      <el-table-column label="老人身份证号" align="center" width="180">
        <template #default="scope">
          {{ scope.row.elder?.idCardNo }}
        </template>
      </el-table-column>
      <el-table-column label="合同期限" align="center" width="200">
        <template #default="scope">
          <span>{{ parseTime(scope.row.startTime, '{y}-{m}-{d}') }} — {{ parseTime(scope.row.endTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="合同状态" align="center" width="110">
        <template #default="scope">
          <el-tag :type="getStatusType(scope.row)" size="small">
            {{ getStatusText(scope.row) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="创建人" align="center" prop="createBy" />
      <el-table-column label="签约时间" align="center" prop="createTime">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>

      <el-table-column label="操作" align="center" width="200">
        <template #default="scope">
          <el-button link type="primary" @click="handleView(scope.row)">查看</el-button>
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" style="margin-left:10px" v-hasPermi="['system:contract:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" style="margin-left:10px" v-hasPermi="['system:contract:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div class="pagination-container" v-show="total>0">
      <span class="total-text">共 {{ total }} 项数据</span>
      <el-pagination
          background
          :current-page="queryParams.pageNum"
          :page-size="queryParams.pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="total"
          layout="sizes, prev, pager, next, jumper, ->, goto"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
      >
        <template #goto>
          <span class="goto-text">
            跳至 <el-input v-model.number="gotoPage" size="mini" style="width: 60px; margin: 0 5px" @keyup.enter="jumpToPage" /> /{{ Math.ceil(total / queryParams.pageSize) }}页
          </span>
        </template>
      </el-pagination>
    </div>
  </div>

  <div v-if="showDetail" class="app-container" style="max-width:1200px; margin:0 auto; padding:20px;">
    <el-card>
      <div style="font-size:16px; font-weight:500; margin-bottom:15px;">{{ title }}</div>

      <template v-if="isView">
        <el-descriptions :column="1" border style="margin-bottom:20px;">
          <el-descriptions-item label="合同编号">{{ form.contractNo }}</el-descriptions-item>
          <el-descriptions-item label="合同名称">{{ form.name }}</el-descriptions-item>
          <el-descriptions-item label="老人姓名">{{ form.elder?.name || '' }}</el-descriptions-item>
          <el-descriptions-item label="老人身份证号">{{ form.elder?.idCardNo || '' }}</el-descriptions-item>
          <el-descriptions-item label="入住单号">{{ form.checkInNo }}</el-descriptions-item>
          <el-descriptions-item label="合同期限">
            {{ parseTime(form.startTime, '{y}-{m}-{d}') }} — {{ parseTime(form.endTime, '{y}-{m}-{d}') }}
          </el-descriptions-item>
          <el-descriptions-item label="合同状态">
            <el-tag :type="getStatusType(form)" size="small">{{ getStatusText(form) }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="签约日期">{{ parseTime(form.signDate, '{y}-{m}-{d}') }}</el-descriptions-item>

          <template v-if="form.status === 3">
            <el-descriptions-item label="丙方姓名">{{ form.memberName || '' }}</el-descriptions-item>
            <el-descriptions-item label="丙方联系方式">{{ form.memberPhone || '' }}</el-descriptions-item>
          </template>

          <el-descriptions-item label="合同文件">
            <div v-if="form.pdfUrl" style="display: flex; align-items: center; gap: 10px;">
              <el-link type="primary" @click="downloadPdf">{{ getPdfFileName(form.pdfUrl) }}</el-link>
            </div>
            <span v-else>暂无PDF</span>
          </el-descriptions-item>
        </el-descriptions>

        <el-descriptions v-if="form.status === 3" :column="1" border title="解除记录" style="margin-bottom:20px;">
          <el-descriptions-item label="提交人">{{ form.releaseSubmitter || '' }}</el-descriptions-item>
          <el-descriptions-item label="解除日期">{{ parseTime(form.releaseDate, '{y}-{m}-{d}') }}</el-descriptions-item>
          <el-descriptions-item label="解除协议">
            <div v-if="form.releasePdfUrl" style="display: flex; align-items: center; gap: 10px;">
              <el-link type="primary" @click="downloadReleasePdf">{{ getPdfFileName(form.releasePdfUrl) }}</el-link>
            </div>
            <span v-else>暂无解除协议</span>
          </el-descriptions-item>
        </el-descriptions>
      </template>

      <template v-else>
        <el-form ref="contractRef" :model="form" :rules="rules" label-width="120px" size="default">
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="合同编号" prop="contractNo">
                <el-input v-model="form.contractNo" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="合同名称" prop="name">
                <el-input v-model="form.name" />
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="老人姓名">
                <el-input v-model="form.elderName" readonly />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="老人ID">
                <el-input v-model="form.elderId" readonly />
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="丙方姓名">
                <el-input v-model="form.memberName" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="丙方电话">
                <el-input v-model="form.memberPhone" />
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="入住单号">
                <el-input v-model="form.checkInNo" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="等级描述">
                <el-input v-model="form.levelDesc" />
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="开始时间" prop="startTime">
                <el-date-picker v-model="form.startTime" type="date" value-format="YYYY-MM-DD" style="width:100%" @change="autoComputeStatus" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="结束时间" prop="endTime">
                <el-date-picker v-model="form.endTime" type="date" value-format="YYYY-MM-DD" style="width:100%" @change="autoComputeStatus" />
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="合同状态" prop="status">
                <el-select v-model="form.status" style="width:100%">
                  <el-option label="未生效" :value="0" />
                  <el-option label="已生效" :value="1" />
                  <el-option label="已过期" :value="2" />
                  <el-option label="已失效" :value="3" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="签约日期">
                <el-date-picker v-model="form.signDate" type="date" value-format="YYYY-MM-DD" style="width:100%" />
              </el-form-item>
            </el-col>
          </el-row>

          <el-form-item label="合同PDF">
            <el-upload
                action="http://localhost:8080/uploadContractPdf"
                :headers="{}"
                :with-credentials="false"
                :show-file-list="false"
                :on-success="handlePdfUploadSuccess"
                accept=".pdf"
            >
              <el-button type="primary" size="small">上传PDF</el-button>
            </el-upload>
            <el-link v-if="form.pdfUrl" type="primary" style="margin-left:10px" @click="downloadPdf">
              {{ getPdfFileName(form.pdfUrl) }}
            </el-link>
          </el-form-item>
        </el-form>
      </template>

      <div style="text-align:center; margin-top:30px;">
        <el-button v-if="!isView" type="primary" @click="submitForm">确定</el-button>
        <el-button @click="goBack">返回</el-button>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts" name="ContractList">
import type { Contract, ContractQueryParams } from "@/types/api/system/contract"
import { listContract, delContract, addContract, updateContract } from "@/api/system/contract"

const { proxy } = getCurrentInstance() as any

const contractList = ref<Contract[]>([])
const loading = ref(true)
const showSearch = ref(true)
const total = ref(0)
const title = ref('')
const daterangePeriod = ref<string[]>([])
const gotoPage = ref(1)
const isView = ref(false)
const showDetail = ref(false)

const statusMap = {
  0: { text: '未生效', type: 'info' },
  1: { text: '已生效', type: 'success' },
  2: { text: '已过期', type: 'danger' },
  3: { text: '已失效', type: 'warning' }
}

// 实时计算合同状态
function getRealStatus(row) {
  if (row.status === 3) {
    return 3;
  }
  const now = Date.now();
  const start = new Date(row.startTime).getTime();
  const end = new Date(row.endTime).getTime();
  if (now < start) return 0;
  if (now > end) return 2;
  return 1;
}

function getStatusText(row) {
  const realStatus = getRealStatus(row);
  return statusMap[realStatus]?.text || '未知';
}

function getStatusType(row) {
  const realStatus = getRealStatus(row);
  return statusMap[realStatus]?.type || 'info';
}

function getPdfFileName(url: string): string {
  if (!url) return '';
  return url.split('/').pop() || '';
}

const data = reactive({
  form: {} as Contract,
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    contractNo: undefined,
    elderId: undefined,
    elderName: undefined,
    status: undefined,
    startTime: undefined,
    endTime: undefined,
  } as ContractQueryParams,
  rules: {
    contractNo: [{ required: true, message: '请输入合同编号', trigger: 'blur' }],
    name: [{ required: true, message: '请输入合同名称', trigger: 'blur' }],
    startTime: [{ required: true, message: '请选择合同开始时间', trigger: 'change' }],
    endTime: [{ required: true, message: '请选择合同结束时间', trigger: 'change' }],
  }
})

const { queryParams, form, rules } = toRefs(data)

function downloadPdf() {
  if (!form.value.pdfUrl) {
    proxy.$modal.msgWarning("暂无PDF文件")
    return
  }
  window.open(form.value.pdfUrl, '_blank')
}

function downloadReleasePdf() {
  if (!form.value.releasePdfUrl) {
    proxy.$modal.msgWarning("暂无解除协议文件")
    return
  }
  window.open(form.value.releasePdfUrl, '_blank')
}

function handlePdfUploadSuccess(res: any) {
  if (res.code === 200) {
    form.value.pdfUrl = res.data
    proxy.$modal.msgSuccess("上传成功")
  } else {
    proxy.$modal.msgError("上传失败")
  }
}

function autoComputeStatus() {
  if (isView.value) return
  if (form.value.status === 3) return
  const now = new Date()
  const start = new Date(form.value.startTime)
  const end = new Date(form.value.endTime)
  if (end < now) form.value.status = 2
  else if (start > now) form.value.status = 0
  else form.value.status = 1
}

function getList() {
  loading.value = true
  listContract(queryParams.value).then(res => {
    contractList.value = res.rows
    total.value = res.total
    loading.value = false
  }).catch(() => loading.value = false)
}

function handleSizeChange(val: number) {
  queryParams.value.pageSize = val; getList()
}

function handleCurrentChange(val: number) {
  queryParams.value.pageNum = val; gotoPage.value = val; getList()
}

function jumpToPage() {
  queryParams.value.pageNum = gotoPage.value; getList()
}

function handleQuery() {
  if (daterangePeriod.value && daterangePeriod.value.length === 2) {
    queryParams.value.startTime = daterangePeriod.value[0]
    queryParams.value.endTime = daterangePeriod.value[1]
  } else {
    queryParams.value.startTime = undefined
    queryParams.value.endTime = undefined
  }
  queryParams.value.pageNum = 1
  gotoPage.value = 1
  getList()
}

function resetQuery() {
  daterangePeriod.value = []
  queryParams.value = {
    pageNum: 1,
    pageSize: 10,
    contractNo: undefined,
    elderName: undefined,
    status: undefined,
    startTime: undefined,
    endTime: undefined
  }
  handleQuery()
}

function goBack() {
  showDetail.value = false
}

function handleUpdate(row: Contract) {
  form.value = { ...row }
  autoComputeStatus()
  title.value = '修改合同'
  isView.value = false
  showDetail.value = true
}

function handleView(row: Contract) {
  form.value = { ...row }
  title.value = '合同详情'
  isView.value = true
  showDetail.value = true
}

function submitForm() {
  proxy.$refs["contractRef"].validate((valid: boolean) => {
    if (!valid) return
    autoComputeStatus()
    const req = form.value.id ? updateContract(form.value) : addContract(form.value)
    req.then(() => {
      proxy.$modal.msgSuccess("操作成功")
      goBack()
      getList()
    })
  })
}

function handleDelete(row: Contract) {
  proxy.$modal.confirm('确认删除？').then(() => delContract(row.id!)).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  })
}

getList()
</script>

<style scoped>
.app-container {
  padding: 16px;
  background: #fff;
}
.search-form {
  margin-bottom: 16px;
}
.contract-table {
  font-size: 14px;
  margin-bottom: 16px;
}
.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>