<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="编号" prop="checkInCode">
        <el-input
          v-model="queryParams.checkInCode"
          placeholder="请输入编号"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="标题" prop="title">
        <el-input
          v-model="queryParams.title"
          placeholder="请输入标题"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="入住时间" prop="checkInTime">
        <el-date-picker clearable
          v-model="queryParams.checkInTime"
          type="date"
          value-format="YYYY-MM-DD"
          placeholder="请选择入住时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="申请人" prop="applicat">
        <el-input
          v-model="queryParams.applicat"
          placeholder="请输入申请人"
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
          @click="ask"
          v-hasPermi="['system:checkin:add']"
        >发起入住申请</el-button>
      </el-col>

      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['system:checkin:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="checkinList" >
      <el-table-column label="编号" align="center" prop="checkInCode" width="200"/>
      <el-table-column label="标题" align="center" prop="title" />
      <el-table-column label="老人姓名" align="center" prop="name" />
      <el-table-column label="身份证号" align="center" prop="idCardNo"  width="180"/>
      <el-table-column label="入住时间" align="center" prop="checkInTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.checkInTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="申请人" align="center" prop="applicat" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary"  @click="handleView(scope.row)" v-hasPermi="['system:checkin:query']">查看</el-button>

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

    <!-- 添加或修改入住办理对话框 -->
    <el-dialog :title="title" v-model="open" width="800px" append-to-body>
      <el-form ref="checkinRef" :model="form" :rules="isView ? {} : rules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="编号" prop="checkInCode">
              <el-input v-model="form.checkInCode" :disabled="isView" placeholder="请输入编号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="标题" prop="title">
              <el-input v-model="form.title" :disabled="isView" placeholder="请输入标题" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="老人ID" prop="elderId">
              <el-input v-model="form.elderId" :disabled="isView" placeholder="请输入老人ID" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="老人姓名" prop="elderName">
              <el-input v-model="form.elderName" :disabled="isView" placeholder="请输入老人姓名" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="老人身份证" prop="elderCardId">
              <el-input v-model="form.elderCardId" :disabled="isView" placeholder="请输入老人身份证" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="入住时间" prop="checkInTime">
              <el-date-picker clearable
                v-model="form.checkInTime"
                type="date"
                value-format="YYYY-MM-DD"
                :disabled="isView"
                placeholder="请选择入住时间">
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="申请人" prop="applicat">
              <el-input v-model="form.applicat" :disabled="isView" placeholder="请输入申请人" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="申请人ID" prop="applicatId">
              <el-input v-model="form.applicatId" :disabled="isView" placeholder="请输入申请人ID" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="部门编号" prop="deptNo">
              <el-input v-model="form.deptNo" :disabled="isView" placeholder="请输入部门编号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="顾问" prop="counselor">
              <el-input v-model="form.counselor" :disabled="isView" placeholder="请输入顾问" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="流程状态" prop="flowStatus">
              <el-select v-model="form.flowStatus" :disabled="isView" placeholder="请选择流程状态" style="width: 100%">
                <el-option label="未开始" value="0" />
                <el-option label="进行中" value="1" />
                <el-option label="已完成" value="2" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="审核状态" prop="status">
              <el-select v-model="form.status" :disabled="isView" placeholder="请选择审核状态" style="width: 100%">
                <el-option label="待审核" value="0" />
                <el-option label="已通过" value="1" />
                <el-option label="已驳回" value="2" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="当前任务ID" prop="currentTaskId">
              <el-input v-model="form.currentTaskId" :disabled="isView" placeholder="请输入当前任务ID" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="流程ID" prop="processId">
              <el-input v-model="form.processId" :disabled="isView" placeholder="请输入流程ID" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="当前任务名称" prop="currentTaskName">
              <el-input v-model="form.currentTaskName" :disabled="isView" placeholder="请输入当前任务名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="创建时间" prop="createTime">
              <el-date-picker clearable
                v-model="form.createTime"
                type="datetime"
                value-format="YYYY-MM-DD HH:mm:ss"
                :disabled="isView"
                placeholder="请选择创建时间">
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="入住原因" prop="reason">
              <el-input v-model="form.reason" type="textarea" :disabled="isView" placeholder="请输入入住原因" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="其他申请信息" prop="otherApplyInfo">
              <el-input v-model="form.otherApplyInfo" type="textarea" :disabled="isView" placeholder="请输入其他申请信息" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="审核信息" prop="reviewInfo">
              <el-input v-model="form.reviewInfo" type="textarea" :disabled="isView" placeholder="请输入审核信息" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="备注" prop="remark">
              <el-input v-model="form.remark" type="textarea" :disabled="isView" placeholder="请输入备注" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm" v-if="!isView">确 定</el-button>
          <el-button @click="cancel">{{ isView ? '关 闭' : '取 消' }}</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="Checkin">
import { listCheckin, getCheckin, delCheckin, addCheckin, updateCheckin } from "@/api/system/checkin"
import { useRouter } from 'vue-router'
import { getCurrentInstance } from 'vue'

const { proxy } = getCurrentInstance()
const router = useRouter()

const checkinList = ref([])
const open = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const total = ref(0)
const title = ref("")
const isView = ref(false)

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    checkInCode: null,
    title: null,
    checkInTime: null,
    applicat: null,
    flowStatus: 5,
    status: 2,
  },
  rules: {
    checkInCode: [
      { required: true, message: "编号不能为空", trigger: "blur" }
    ],
    title: [
      { required: true, message: "标题不能为空", trigger: "blur" }
    ],
    elderId: [
      { required: true, message: "老人id不能为空", trigger: "blur" }
    ],
    applicat: [
      { required: true, message: "申请人不能为空", trigger: "blur" }
    ],
    deptNo: [
      { required: true, message: "部门编号不能为空", trigger: "blur" }
    ],
    applicatId: [
      { required: true, message: "申请人id不能为空", trigger: "blur" }
    ],
    createTime: [
      { required: true, message: "创建时间不能为空", trigger: "blur" }
    ],
    flowStatus: [
      { required: true, message: "流程状态不能为空", trigger: "change" }
    ],
    status: [
      { required: true, message: "审核状态不能为空", trigger: "change" }
    ],
  }
})

const { queryParams, form, rules } = toRefs(data)

/** 查询入住办理列表 */
function getList() {
  loading.value = true
  listCheckin({ ...queryParams.value, flowStatus: 5, status: 2 }).then(response => {
    checkinList.value = response.rows
    total.value = response.total
    loading.value = false
  })
}

// 取消按钮
function cancel() {
  open.value = false
  reset()
}

function ask() {
  // 跳转到入住申请页面
  router.push('/checkin')
}

// 表单重置
function reset() {
  form.value = {
    id: null,
    checkInCode: null,
    title: null,
    elderId: null,
    counselor: null,
    checkInTime: null,
    reason: null,
    remark: null,
    applicat: null,
    deptNo: null,
    applicatId: null,
    createTime: null,
    flowStatus: null,
    status: null,
    otherApplyInfo: null,
    reviewInfo: null,
    updateTime: null,
    elderName: null,
    elderCardId: null,
    currentTaskId: null,
    processId: null,
    currentTaskName: null
  }
  proxy.resetForm("checkinRef")
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm("queryRef")
  queryParams.value.flowStatus = 5
  queryParams.value.status = 2
  handleQuery()
}

// 多选框选中数据
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.id)
  single.value = selection.length != 1
  multiple.value = !selection.length
}





/** 查看按钮操作 */
function handleView(row) {
  reset()
  isView.value = true
  const _id = row.id || ids.value
  getCheckin(_id).then(response => {
    form.value = response.data
    open.value = true
    title.value = "查看入住办理"
  })
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["checkinRef"].validate(valid => {
    if (valid) {
      if (form.value.id != null) {
        updateCheckin(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addCheckin(form.value).then(response => {
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
  proxy.download('system/checkin/export', {
    ...queryParams.value
  }, `checkin_${new Date().getTime()}.xlsx`)
}

getList()
</script>
