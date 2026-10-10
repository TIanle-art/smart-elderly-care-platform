<template>
  <div class="app-container leave-detail-page">
    <el-steps :active="stepActive" finish-status="success" align-center class="leave-steps" :class="resultStepClass">
      <el-step title="申请请假" />
      <el-step title="申请审批" />
      <el-step :title="resultStepTitle" :status="resultStepStatus" />
    </el-steps>

    <el-row :gutter="16">
      <el-col :span="showSidePanel ? 15 : 24">
        <el-form ref="leaveRef" :model="form" :rules="rules" label-width="110px" :disabled="isReadonlyForm" class="detail-form">
          <div class="section-title">基本信息</div>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="老人姓名" prop="elderId">
                <el-select
                  v-if="!isReadonlyForm"
                  v-model="form.elderId"
                  filterable
                  clearable
                  placeholder="请选择老人"
                  style="width: 100%"
                  @change="handleElderChange"
                >
                  <el-option v-for="item in elderOptions" :key="item.elderId" :label="item.elderName" :value="item.elderId" />
                </el-select>
                <el-input v-else :model-value="form.elderName || '-'" disabled />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="老人身份证号">
                <el-input v-model="form.elderIdCard" disabled />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="联系方式">
                <el-input v-model="form.phone" disabled />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="护理等级">
                <el-input v-model="form.nursingLevel" disabled />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="入住床位">
                <el-input v-model="form.bedNo" disabled />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="护理员">
                <el-input v-model="form.nurseNames" disabled />
              </el-form-item>
            </el-col>
          </el-row>

          <div class="section-title">申请信息</div>
          <el-row :gutter="20">
            <el-col :span="24">
              <el-form-item label="陪同人" prop="companionType">
                <el-radio-group v-model="form.companionType">
                  <el-radio label="家属">家属</el-radio>
                  <el-radio label="护理人员">护理人员</el-radio>
                  <el-radio label="其他">其他</el-radio>
                  <el-radio label="无">无</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="陪同人姓名" prop="companionName">
                <el-input v-model="form.companionName" placeholder="请输入陪同人姓名" maxlength="10" show-word-limit />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="陪同人联系方式" prop="companionPhone">
                <el-input v-model="form.companionPhone" placeholder="请输入陪同人联系方式" maxlength="11" show-word-limit />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="请假周期" prop="leaveRange">
                <el-date-picker
                  v-model="leaveRange"
                  type="datetimerange"
                  range-separator="至"
                  start-placeholder="请选择请假开始时间"
                  end-placeholder="请选择预计返回时间"
                  value-format="YYYY-MM-DD HH:mm:ss"
                  style="width: 100%"
                  @change="handleLeaveRangeChange"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="请假天数">
                <el-input :model-value="form.leaveDays ? `${form.leaveDays}天` : ''" disabled />
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="请假原因" prop="leaveReason">
                <el-input v-model="form.leaveReason" type="textarea" maxlength="200" show-word-limit placeholder="请输入请假原因" />
              </el-form-item>
            </el-col>
            <el-col v-if="showRevokeSection" :span="24">
              <div class="section-title section-title-sm">销假记录</div>
            </el-col>
            <el-col v-if="showRevokeSection" :span="12">
              <el-form-item label="状态">
                <el-input value="已返回" disabled />
              </el-form-item>
            </el-col>
            <el-col v-if="showRevokeSection" :span="12">
              <el-form-item label="实际返回时间">
                <el-input :model-value="formatDateDisplay(form.actualReturnTime) || '-'" disabled />
              </el-form-item>
            </el-col>
            <el-col v-if="showRevokeSection" :span="12">
              <el-form-item label="实际请假天数">
                <el-input :model-value="form.actualLeaveDays || '-'" disabled />
              </el-form-item>
            </el-col>
            <el-col v-if="showRevokeSection" :span="12">
              <el-form-item label="备注">
                <el-input :model-value="form.remark || '-'" disabled />
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </el-col>

      <el-col v-if="showSidePanel" :span="9">
        <div class="side-panel">
          <div v-if="isApproveMode" class="approve-card">
            <div class="section-title">审批结果</div>
            <el-form ref="approveRef" :model="approveForm" :rules="approveRules" label-width="90px">
              <el-form-item label="审批结果" prop="approveResult">
                <el-radio-group v-model="approveForm.approveResult">
                  <el-radio label="approved">审批通过</el-radio>
                  <el-radio label="rejected">审批拒绝</el-radio>
                  <el-radio label="back">驳回</el-radio>
                </el-radio-group>
              </el-form-item>
              <el-form-item label="审批意见" prop="approveOpinion">
                <el-input v-model="approveForm.approveOpinion" type="textarea" maxlength="200" show-word-limit placeholder="请输入审批意见" />
              </el-form-item>
            </el-form>
          </div>

          <div v-else-if="form.status === 'approving'" class="approve-card status-card">
            <div class="section-title">审批结果</div>
            <div class="status-title">审批中</div>
            <div class="status-desc">请假申请正在审批中，如长时间未处理，请联系当前审批节点处理人。</div>
          </div>

          <div class="record-card">
            <div class="section-title">操作记录</div>
            <div v-if="operationRecords.length" class="record-list">
              <div v-for="(item, index) in operationRecords" :key="`${item.nodeName}-${index}`" class="record-item">
                <div class="record-line" />
                <div class="record-dot" :class="`record-dot-${item.result}`" />
                <div class="record-content">
                  <div class="record-title">{{ item.nodeName }}</div>
                  <div class="record-subtitle">{{ item.roleName }} {{ item.userName }}</div>
                  <div v-if="item.opinion" class="record-opinion">{{ item.opinion }}</div>
                </div>
                <div class="record-time">{{ formatDateDisplay(item.time) }}</div>
              </div>
            </div>
            <el-empty v-else description="暂无操作记录" />
          </div>
        </div>
      </el-col>
    </el-row>

    <div class="footer-actions">
      <el-button @click="handleBack">返回</el-button>
      <el-button v-if="showSubmitButton" type="primary" @click="submitForm">{{ submitButtonText }}</el-button>
    </div>
  </div>
</template>

<script setup lang="ts" name="LeaveDetail">
import { computed, getCurrentInstance, reactive, ref, watch } from "vue"
import { useRoute, useRouter } from "vue-router"
import type { FormInstance, FormRules, FormItemRule } from "element-plus"
import type {
  ElderLeave,
  ElderOption,
  LeaveOperationRecord,
  ElderLeaveTodo,
  ElderLeaveApproveRecordItem,
  ElderLeaveSubmitPayload,
  ElderLeaveResubmitPayload,
  ElderLeaveApprovePayload
} from "@/types/api/code/leave"
import {
  getLeave,
  listElderOptions,
  getLeaveFormInfo,
  listLeaveTodo,
  listLeaveApproveRecords,
  submitLeave,
  resubmitLeave,
  approveLeave
} from "@/api/code/leave"
import { parseTime } from "@/utils/ruoyi"

const { proxy } = getCurrentInstance()
const route = useRoute()
const router = useRouter()
const mode = computed<"create" | "view" | "approve">(() => {
  if (route.path.includes("/approve/")) {
    return "approve"
  }
  if (route.path.includes("/view/")) {
    return "view"
  }
  return "create"
})
const isCreateMode = computed(() => mode.value === "create")
const isApproveMode = computed(() => mode.value === "approve")
const isViewMode = computed(() => mode.value === "view")
const backPath = computed(() => {
  const from = route.query.from
  if (typeof from === "string" && from) {
    return from
  }
  return "/code/leave"
})
const leaveRange = ref<string[]>([])
const elderOptions = ref<ElderOption[]>([])
const todoList = ref<ElderLeaveTodo[]>([])
const approveRecordList = ref<ElderLeaveApproveRecordItem[]>([])
const taskId = ref("")
const leaveRef = ref<FormInstance>()
const approveRef = ref<FormInstance>()
const form = reactive<ElderLeave>({
  elderId: undefined,
  orderNo: "",
  elderName: "",
  elderIdCard: "",
  phone: "",
  nursingLevel: "",
  bedNo: "",
  nurseNames: "",
  companionType: "家属",
  companionName: "",
  companionPhone: "",
  leaveStartTime: "",
  plannedReturnTime: "",
  leaveDays: undefined,
  leaveReason: "",
  actualReturnTime: "",
  actualLeaveDays: "",
  remark: "",
  isReturned: 0
})
const approveForm = reactive({
  approveResult: "approved" as "approved" | "rejected" | "back",
  approveOpinion: ""
})
const rules: FormRules = {
  elderId: [{ required: true, message: "请选择老人", trigger: "change" }],
  companionType: [{ required: true, message: "请选择陪同人", trigger: "change" }],
  companionName: [{
    validator: (_rule: FormItemRule, value: string, callback: (error?: Error) => void) => {
      if (form.companionType !== "无" && !value) {
        callback(new Error("请输入陪同人姓名"))
        return
      }
      callback()
    },
    trigger: "blur"
  }],
  companionPhone: [{
    validator: (_rule: FormItemRule, value: string, callback: (error?: Error) => void) => {
      if (form.companionType !== "无" && !value) {
        callback(new Error("请输入陪同人联系方式"))
        return
      }
      callback()
    },
    trigger: "blur"
  }],
  leaveRange: [{
    validator: (_rule: FormItemRule, _value: unknown, callback: (error?: Error) => void) => {
      if (!leaveRange.value || leaveRange.value.length !== 2) {
        callback(new Error("请选择请假周期"))
        return
      }
      callback()
    },
    trigger: "change"
  }],
  leaveReason: [{ required: true, message: "请输入请假原因", trigger: "blur" }]
}
const approveRules: FormRules = {
  approveResult: [{ required: true, message: "请选择审批结果", trigger: "change" }]
}
const statusRoleMap: Record<string, string> = {
  护理组主管审批: "护理组主管",
  护理部部长审批: "护理部部长",
  护理员发起申请: "护理员"
}
const approvedLikeStatuses = new Set(["approved", "leaving", "timeout", "returned"])
const canResubmitPerm = computed(() => proxy?.$auth?.hasPermi("code:leave:resubmit") ?? false)
const isReadonlyForm = computed(() => isApproveMode.value || (isViewMode.value && !canResubmit.value))
const showSidePanel = computed(() => isApproveMode.value || isViewMode.value)
const showRevokeSection = computed(() => isViewMode.value && form.isReturned === 1)
const canResubmit = computed(() => isViewMode.value && form.status === "back" && canResubmitPerm.value)
const showSubmitButton = computed(() => isCreateMode.value || isApproveMode.value || canResubmit.value)
const submitButtonText = computed(() => {
  if (isApproveMode.value) {
    return "提交审批"
  }
  if (canResubmit.value) {
    return "重新提交"
  }
  return "提交"
})
const stepActive = computed(() => {
  // 进度条规则：新建态停留在第 1 步；审批已给出最终结果时进入第 3 步；其余保持第 2 步。
  if (isCreateMode.value) {
    return 0
  }
  if (isApprovedLikeStatus(form.status) || form.isReturned === 1) {
    return 2
  }
  if (form.status === "rejected" || form.status === "back") {
    return 2
  }
  return 1
})
const resultStepStatus = computed(() => {
  // 最终结果态映射到步骤条状态，确保“请假中/超时未归/已返回”统一展示为通过态。
  if (isApprovedLikeStatus(form.status) || form.isReturned === 1) {
    return "success"
  }
  if (form.status === "rejected") {
    return "error"
  }
  if (form.status === "back") {
    return "process"
  }
  return "wait"
})
const resultStepTitle = computed(() => {
  if (isApprovedLikeStatus(form.status) || form.isReturned === 1) {
    return "通过"
  }
  if (form.status === "rejected") {
    return "拒绝"
  }
  if (form.status === "back") {
    return "驳回"
  }
  return "审批结果"
})
const resultStepClass = computed(() => {
  if (form.status === "back") {
    return "is-back"
  }
  if (form.status === "rejected") {
    return "is-rejected"
  }
  if (isApprovedLikeStatus(form.status) || form.isReturned === 1) {
    return "is-approved"
  }
  return ""
})
const operationRecords = computed<LeaveOperationRecord[]>(() => {
  const records: LeaveOperationRecord[] = []
  if (form.applyUserName || form.applyTime) {
    records.push({
      nodeName: "发起申请-申请请假",
      roleName: "护理员",
      userName: form.applyUserName || "-",
      time: form.applyTime,
      result: "submitted"
    })
  }
  approveRecordList.value.forEach(item => {
    records.push({
      nodeName: item.nodeName || "审批节点",
      roleName: item.approveRoleName || statusRoleMap[item.nodeName] || "审批人",
      userName: item.approveUserName || "-",
      time: item.approveTime || item.createTime,
      result: mapApproveResult(item.approveResult),
      opinion: item.approveOpinion || ""
    })
  })
  if (form.currentTaskName && form.status === "approving") {
    records.push({
      nodeName: `${form.currentTaskName}`,
      roleName: statusRoleMap[form.currentTaskName] || "审批人",
      userName: "待处理",
      result: "pending"
    })
  }
  return records
})

function mapApproveResult(result?: string): LeaveOperationRecord["result"] {
  if (result === "approved" || result === "rejected" || result === "back") {
    return result
  }
  if (result === "submit") {
    return "submitted"
  }
  return "submitted"
}

function isApprovedLikeStatus(status?: string) {
  return !!status && approvedLikeStatuses.has(status)
}

function calcLeaveDays(startTime?: string, endTime?: string) {
  if (!startTime || !endTime) {
    form.leaveDays = undefined
    return
  }
  const start = new Date(startTime).getTime()
  const end = new Date(endTime).getTime()
  if (end < start) {
    form.leaveDays = undefined
    return
  }
  const oneDay = 24 * 60 * 60 * 1000
  const diff = end - start
  let days = Math.floor(diff / oneDay)
  if (diff % oneDay !== 0) {
    days += 1
  }
  if (days === 0) {
    days = 1
  }
  form.leaveDays = days
}

function handleLeaveRangeChange() {
  form.leaveStartTime = leaveRange.value?.[0] || ""
  form.plannedReturnTime = leaveRange.value?.[1] || ""
  calcLeaveDays(form.leaveStartTime, form.plannedReturnTime)
}

function formatDateDisplay(value?: string) {
  if (!value) {
    return "—"
  }
  return parseTime(value, "{y}-{m}-{d} {h}:{i}:{s}") || value
}

function normalizeDateTime(value?: string) {
  if (!value) {
    return ""
  }
  const dateOnlyReg = /^\d{4}-\d{2}-\d{2}$/
  if (dateOnlyReg.test(value)) {
    return `${value} 00:00:00`
  }
  const minuteReg = /^\d{4}-\d{2}-\d{2}[ T]\d{2}:\d{2}$/
  if (minuteReg.test(value)) {
    return `${value.replace("T", " ")}:00`
  }
  return value.replace("T", " ")
}

function loadElderOptions() {
  listElderOptions().then(response => {
    elderOptions.value = response.data || []
  })
}

function handleElderChange(elderId: number) {
  if (!elderId) {
    form.elderName = ""
    form.elderIdCard = ""
    form.phone = ""
    form.nursingLevel = ""
    form.bedNo = ""
    form.nurseNames = ""
    return
  }
  const matched = elderOptions.value.find(item => item.elderId === elderId)
  if (matched) {
    form.elderName = matched.elderName
  }
  getLeaveFormInfo(elderId).then(response => {
    const data = response.data
    if (!data) {
      return
    }
    form.elderId = data.elderId
    form.elderName = data.elderName
    form.elderIdCard = data.elderIdCard
    form.phone = data.phone
    form.nursingLevel = data.nursingLevel
    form.bedNo = data.bedNo
    form.nurseNames = data.nurseNames
  })
}

function loadTodoContext() {
  // 审批页在缺少 taskId 时，才回源待办列表兜底查找，避免重复请求。
  if (!isApproveMode.value || taskId.value || !form.id) {
    return
  }
  listLeaveTodo().then(response => {
    todoList.value = response.data || []
    const current = todoList.value.find(item => item.leaveId === form.id)
    if (current) {
      taskId.value = current.taskId
    }
  })
}

function loadApproveRecords() {
  if (!form.id) {
    approveRecordList.value = []
    return
  }
  listLeaveApproveRecords(form.id).then(response => {
    approveRecordList.value = response.data || []
  })
}

function loadDetail() {
  if (isCreateMode.value) {
    return
  }
  const id = Number(route.params.id)
  if (!id) {
    return
  }
  getLeave(id).then(response => {
    Object.assign(form, response.data)
    if (form.leaveStartTime && form.plannedReturnTime) {
      leaveRange.value = [form.leaveStartTime, form.plannedReturnTime]
    }
  })
}

function submitForm() {
  if (isApproveMode.value) {
    approveRef.value?.validate((valid: boolean) => {
      if (!valid || !form.id || !taskId.value) {
        if (!taskId.value) {
          proxy?.$modal.msgError("未获取到审批任务，请返回待办列表重试")
        }
        return
      }
      const payload: ElderLeaveApprovePayload = {
        taskId: taskId.value,
        leaveId: form.id,
        approveResult: approveForm.approveResult,
        approveOpinion: approveForm.approveOpinion
      }
      approveLeave(payload).then(() => {
        proxy?.$modal.msgSuccess("审批提交成功")
        router.push(backPath.value)
      })
    })
    return
  }

  leaveRef.value?.validate((valid: boolean) => {
    if (valid) {
      if (!form.elderId || !form.leaveStartTime || !form.plannedReturnTime) {
        proxy?.$modal.msgError("请完善请假信息后再提交")
        return
      }
      const submitPayload: ElderLeaveSubmitPayload = {
        elderId: form.elderId,
        companionType: form.companionType || "无",
        companionName: form.companionName || "",
        companionPhone: form.companionPhone || "",
        leaveStartTime: normalizeDateTime(form.leaveStartTime),
        plannedReturnTime: normalizeDateTime(form.plannedReturnTime),
        leaveReason: form.leaveReason || ""
      }
      if (canResubmit.value && form.id) {
        const resubmitPayload: ElderLeaveResubmitPayload = {
          leaveId: form.id,
          ...submitPayload
        }
        if (taskId.value) {
          resubmitPayload.taskId = taskId.value
        }
        resubmitLeave(resubmitPayload).then(() => {
          proxy?.$modal.msgSuccess("重新提交成功")
          router.push(backPath.value)
        })
        return
      }
      submitLeave(submitPayload).then(() => {
        proxy?.$modal.msgSuccess("提交成功")
        router.push(backPath.value)
      })
    }
  })
}

function handleBack() {
  router.push(backPath.value)
}

function initPage() {
  // taskId 优先使用路由参数，保证从待办页跳转审批时无需额外查询。
  const queryTaskId = route.query.taskId
  if (typeof queryTaskId === "string" && queryTaskId) {
    taskId.value = queryTaskId
  }
  if (!isApproveMode.value) {
    loadElderOptions()
  }
  if (isCreateMode.value) {
    return
  }
  loadDetail()
}

watch(() => form.companionType, value => {
  if (value === "无") {
    form.companionName = ""
    form.companionPhone = ""
  }
})

watch(() => form.id, value => {
  if (value && !isCreateMode.value) {
    if (isApproveMode.value && !taskId.value) {
      loadTodoContext()
    }
    loadApproveRecords()
  }
})

initPage()
</script>

<style scoped lang="scss">
.leave-detail-page {
  .leave-steps {
    margin-bottom: 24px;
  }

  .leave-steps.is-back {
    :deep(.el-step.is-process .el-step__head.is-text) {
      border-color: #e6a23c;
      color: #e6a23c;
    }

    :deep(.el-step.is-process .el-step__title) {
      color: #e6a23c;
    }

    :deep(.el-step.is-process .el-step__line-inner) {
      border-color: #e6a23c;
    }

    :deep(.el-step:nth-child(2) .el-step__line-inner) {
      border-color: #e6a23c;
    }
  }

  .leave-steps.is-rejected {
    :deep(.el-step.is-process .el-step__head.is-text) {
      border-color: #f56c6c;
      color: #f56c6c;
    }

    :deep(.el-step.is-process .el-step__title) {
      color: #f56c6c;
    }

    :deep(.el-step:nth-child(2) .el-step__line-inner) {
      border-color: #f56c6c;
    }
  }

  .detail-form {
    border: 1px solid #f0f0f0;
    border-radius: 4px;
    padding: 16px 16px 0 16px;
    min-height: 560px;
  }

  .section-title {
    font-size: 18px;
    font-weight: 600;
    margin-bottom: 16px;
  }

  .section-title-sm {
    margin-top: 8px;
    font-size: 16px;
  }

  .side-panel {
    display: flex;
    flex-direction: column;
    gap: 16px;
  }

  .approve-card,
  .record-card {
    border: 1px solid #f0f0f0;
    border-radius: 4px;
    padding: 16px;
    background: #fff;
  }

  .status-card {
    .status-title {
      text-align: center;
      font-size: 28px;
      font-weight: 600;
      margin: 36px 0 12px;
      color: #303133;
    }

    .status-desc {
      text-align: center;
      font-size: 14px;
      color: #909399;
      margin-bottom: 24px;
      line-height: 1.8;
    }
  }

  .record-list {
    position: relative;
  }

  .record-item {
    position: relative;
    display: flex;
    align-items: flex-start;
    gap: 10px;
    padding: 10px 0 10px 2px;
    min-height: 52px;
  }

  .record-line {
    position: absolute;
    top: 20px;
    left: 9px;
    width: 1px;
    bottom: -8px;
    background: #e5e7eb;
  }

  .record-item:last-child {
    .record-line {
      display: none;
    }
  }

  .record-dot {
    position: relative;
    width: 14px;
    height: 14px;
    border-radius: 50%;
    margin-top: 3px;
    flex-shrink: 0;
  }

  .record-dot-submitted {
    background: #67c23a;
  }

  .record-dot-pending {
    background: #409eff;
  }

  .record-dot-approved {
    background: #67c23a;
  }

  .record-dot-rejected {
    background: #f56c6c;
  }

  .record-dot-back {
    background: #e6a23c;
  }

  .record-content {
    flex: 1;
    min-width: 0;
  }

  .record-title {
    font-size: 14px;
    color: #303133;
    line-height: 1.4;
  }

  .record-subtitle {
    font-size: 13px;
    color: #909399;
    margin-top: 2px;
  }

  .record-opinion {
    margin-top: 4px;
    font-size: 12px;
    color: #606266;
    line-height: 1.4;
  }

  .record-time {
    white-space: nowrap;
    font-size: 12px;
    color: #909399;
    padding-left: 8px;
  }

  .footer-actions {
    margin-top: 20px;
    text-align: center;
  }
}
</style>
