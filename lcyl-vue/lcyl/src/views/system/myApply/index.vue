<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="单据编号" prop="documentNo">
        <el-input
          v-model="queryParams.documentNo"
          placeholder="请输入"
          clearable
          @keyup.enter="handleQuery"
          style="width: 200px"
        />
      </el-form-item>
      <el-form-item label="单据类别" prop="category">
        <el-select v-model="queryParams.category" placeholder="请选择" clearable style="width: 160px">
          <el-option label="入住" value="checkin" />
          <el-option label="退住" value="retreat" />
          <el-option label="请假" value="leave" />
        </el-select>
      </el-form-item>
      <el-form-item label="申请时间" prop="applyTimeRange">
        <el-date-picker
          v-model="applyTimeRange"
          type="daterange"
          value-format="YYYY-MM-DD"
          range-separator="至"
          start-placeholder="开始时间"
          end-placeholder="结束时间"
          style="width: 320px"
        />
      </el-form-item>
      <el-form-item>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8" style="align-items: center;">
      <el-col :span="20">
        <el-button-group>
          <el-button :type="statusTab === 'all' ? 'primary' : ''" @click="setStatusTab('all')">全部</el-button>
          <el-button :type="statusTab === 'processing' ? 'primary' : ''" @click="setStatusTab('processing')">申请中</el-button>
          <el-button :type="statusTab === 'finished' ? 'primary' : ''" @click="setStatusTab('finished')">已完成</el-button>
          <el-button :type="statusTab === 'closed' ? 'primary' : ''" @click="setStatusTab('closed')">已关闭</el-button>
        </el-button-group>
      </el-col>
      <el-col :span="4" style="text-align: right;">
        <el-button type="primary" icon="Plus" @click="openCreateDialog">发起申请</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="list">
      <el-table-column label="序号" align="center" width="80">
        <template #default="scope">
          <span>{{ (queryParams.pageNum - 1) * queryParams.pageSize + scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column label="单据编号" align="center" prop="documentNo" width="220" />
      <el-table-column label="单据标题" align="center" prop="title" min-width="220" />
      <el-table-column label="单据类别" align="center" prop="category" width="120" />
      <el-table-column label="申请人" align="center" prop="applicant" width="120" />
      <el-table-column label="申请时间" align="center" prop="applyTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.applyTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="完成时间" align="center" prop="finishTime" width="180">
        <template #default="scope">
          <span v-if="scope.row.finishTime">{{ parseTime(scope.row.finishTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
          <span v-else>—</span>
        </template>
      </el-table-column>
      <el-table-column label="流程状态" align="center" prop="flowStatus" width="120">
        <template #default="scope">
          <el-tag :type="flowTagType(scope.row.flowStatus)">{{ scope.row.flowStatus }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="150">
        <template #default="scope">
          <el-button
            link
            type="primary"
            :disabled="!scope.row.canCancel"
            @click="handleCancel(scope.row)"
          >
            撤销
          </el-button>
          <el-button link type="primary" @click="handleView(scope.row)">查看</el-button>
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

    <el-dialog v-model="createDialogOpen" title="选择申请单据类别" width="700px" append-to-body>
      <div class="type-select">
        <el-radio-group v-model="createCategory">
          <div class="type-item">
            <el-radio label="checkin">入住</el-radio>
          </div>
          <div class="type-item">
            <el-radio label="retreat">退住</el-radio>
          </div>
          <div class="type-item">
            <el-radio label="leave">请假</el-radio>
          </div>
        </el-radio-group>
      </div>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="createDialogOpen = false">取消</el-button>
          <el-button type="primary" @click="confirmCreate">确定</el-button>
        </div>
      </template>
    </el-dialog>

    <el-dialog v-model="leaveDetailOpen" title="请假申请详情" width="700px" append-to-body>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="单据编号">{{ leaveDetail.documentNo || '-' }}</el-descriptions-item>
        <el-descriptions-item label="老人姓名">{{ leaveDetail.elderName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="请假类型">{{ leaveDetail.leaveType || '-' }}</el-descriptions-item>
        <el-descriptions-item label="请假状态">{{ leaveDetail.leaveStatus || '-' }}</el-descriptions-item>
        <el-descriptions-item label="请假开始时间">{{ parseTime(leaveDetail.leaveStartTime, '{y}-{m}-{d}') || '-' }}</el-descriptions-item>
        <el-descriptions-item label="预计结束时间">{{ parseTime(leaveDetail.expectedEndTime, '{y}-{m}-{d}') || '-' }}</el-descriptions-item>
        <el-descriptions-item label="请假原因" :span="2">{{ leaveDetail.leaveReason || '-' }}</el-descriptions-item>
        <el-descriptions-item label="陪同人" :span="2">{{ leaveDetail.escortName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="陪同人联系方式" :span="2">{{ leaveDetail.escortContact || '-' }}</el-descriptions-item>
        <el-descriptions-item label="返回备注" :span="2">{{ leaveDetail.returnRemark || '-' }}</el-descriptions-item>
      </el-descriptions>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="leaveDetailOpen = false">关闭</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="MyApply">
import { ref, reactive, toRefs, getCurrentInstance } from 'vue'
import { useRouter } from 'vue-router'
import { listMyApply, cancelMyApply } from '@/api/system/myApply'
import { getLeave } from '@/api/code/leave.js'
import { ElMessage, ElMessageBox } from 'element-plus'
import { parseTime } from '@/utils/ruoyi'

const { proxy } = getCurrentInstance()
const router = useRouter()

const list = ref([])
const total = ref(0)
const loading = ref(false)
const showSearch = ref(true)
const statusTab = ref('all')
const applyTimeRange = ref([])

const createDialogOpen = ref(false)
const createCategory = ref('checkin')

const leaveDetailOpen = ref(false)
const leaveDetail = ref({})

const data = reactive({
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    documentNo: null,
    category: null
  }
})
const { queryParams } = toRefs(data)

function flowTagType(status) {
  if (status === '已完成') return 'success'
  if (status === '已关闭') return 'danger'
  return 'warning'
}

function categoryKey(row) {
  if (row.category === '入住') return 'checkin'
  if (row.category === '退住') return 'retreat'
  if (row.category === '请假') return 'leave'
  return ''
}

function setStatusTab(tab) {
  statusTab.value = tab
  handleQuery()
}

function getList() {
  loading.value = true
  const params = {
    ...queryParams.value,
    statusTab: statusTab.value
  }
  if (applyTimeRange.value && applyTimeRange.value.length === 2) {
    params.beginTime = applyTimeRange.value[0]
    params.endTime = applyTimeRange.value[1]
  }
  listMyApply(params).then(res => {
    list.value = res.rows || []
    total.value = res.total || 0
  }).finally(() => {
    loading.value = false
  })
}

function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

function resetQuery() {
  proxy.resetForm('queryRef')
  applyTimeRange.value = []
  statusTab.value = 'all'
  handleQuery()
}

function openCreateDialog() {
  createCategory.value = 'checkin'
  createDialogOpen.value = true
}

function confirmCreate() {
  createDialogOpen.value = false
  if (createCategory.value === 'checkin') {
    router.push('/checkin')
    return
  }
  if (createCategory.value === 'retreat') {
    router.push('/system/tuizhu')
    return
  }
  if (createCategory.value === 'leave') {
    router.push('/system/qingjia/index')
  }
}

function handleCancel(row) {
  if (!row.canCancel) return
  ElMessageBox.confirm('此操作将撤销已提交信息，是否继续？', '确认撤销', {
    confirmButtonText: '确定撤销',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    const key = categoryKey(row)
    if (!key) {
      ElMessage.error('单据类别不支持撤销')
      return
    }
    await cancelMyApply(key, row.businessId)
    ElMessage.success('撤销成功')
    getList()
  }).catch(() => {})
}

async function handleView(row) {
  const key = categoryKey(row)
  if (key === 'checkin') {
    router.push({
      path: '/checkin/detail',
      query: {
        businessId: row.businessId,
        checkInId: row.businessId,
        readonly: true
      }
    })
    return
  }
  if (key === 'retreat') {
    router.push({
      path: '/system/tuizhu/index2',
      query: { id: row.businessId }
    })
    return
  }
  if (key === 'leave') {
    const res = await getLeave(row.businessId)
    leaveDetail.value = res.data || {}
    leaveDetailOpen.value = true
  }
}

getList()
</script>

<style scoped>
.type-select {
  display: flex;
  justify-content: center;
}
.type-item {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 180px;
  height: 120px;
  border: 1px solid #ebeef5;
  border-radius: 6px;
  margin: 0 12px;
}
</style>
