<template>
  <div class="contract-release-page">
    <el-steps :active="2" finish-status="success" align-center class="steps-container">
      <el-step title="申请退住" />
      <el-step title="申请审批" />
      <el-step title="解除合同" />
      <el-step title="调整账单" />
      <el-step title="账单审批" />
      <el-step title="退住审批" />
      <el-step title="费用清算" />
    </el-steps>

    <el-row :gutter="20">
      <el-col :span="16">
        <el-card class="info-card" shadow="never">
          <h3 class="card-title">基本信息</h3>
          <el-descriptions :column="1" label-align="right" class="info-desc">
            <el-descriptions-item label="单据编号">{{ data.retreatCode }}</el-descriptions-item>
            <el-descriptions-item label="老人姓名">{{ data.oldManName }}</el-descriptions-item>
            <el-descriptions-item label="老人身份证号">{{ data.idCard }}</el-descriptions-item>
            <el-descriptions-item label="联系方式">{{ data.phone }}</el-descriptions-item>
            <el-descriptions-item label="护理等级">{{ data.nursingLevel }}</el-descriptions-item>
            <el-descriptions-item label="入住床位">{{ data.bedNo }}</el-descriptions-item>
            <el-descriptions-item label="签约合同">
              <span>{{ data.contractName }}</span>
              <el-link type="primary" @click="previewContract">查看合同</el-link>
            </el-descriptions-item>
            <el-descriptions-item label="养老顾问">{{ data.counselor }}</el-descriptions-item>
            <el-descriptions-item label="护理员">{{ data.nurse }}</el-descriptions-item>
          </el-descriptions>
        </el-card>

        <el-card class="info-card" shadow="never" style="margin-top: 20px">
          <h3 class="card-title">申请信息</h3>
          <el-descriptions :column="1" label-align="right" class="info-desc">
            <el-descriptions-item label="退住日期">{{ data.checkoutDate }}</el-descriptions-item>
            <el-descriptions-item label="退住原因">{{ data.reason }}</el-descriptions-item>
            <el-descriptions-item label="备注">{{ data.remark }}</el-descriptions-item>
            <el-descriptions-item label="申请人">{{ data.applicatName }}</el-descriptions-item>
            <el-descriptions-item label="申请时间">{{ data.applyTime }}</el-descriptions-item>
          </el-descriptions>
        </el-card>

        <div class="info-block" style="margin-top:20px">
          <h3 class="block-title">上传解除协议</h3>
          <el-table :data="[{}]" :show-header="false" style="width: 100%">
            <el-table-column width="120"><template #default><span class="label-text">合同编号</span></template></el-table-column>
            <el-table-column><template #default>{{ form.businessData.contractNo }}</template></el-table-column>
          </el-table>
          <el-table :data="[{}]" :show-header="false" style="width: 100%; margin-top: -1px">
            <el-table-column width="120"><template #default><span class="label-text">解除日期</span></template></el-table-column>
            <el-table-column>
              <template #default>
                <el-date-picker v-model="form.businessData.releaseDate" value-format="YYYY-MM-DD" type="date" placeholder="选择解除日期" style="width: 280px" />
              </template>
            </el-table-column>
          </el-table>
          <el-table :data="[{}]" :show-header="false" style="width: 100%; margin-top: -1px">
            <el-table-column width="120"><template #default><span class="label-text">解除协议</span></template></el-table-column>
            <el-table-column>
              <template #default>
                <div class="upload-inline">
                  <el-upload v-model:file-list="fileList" accept=".pdf" :limit="1" :before-upload="beforeUpload" :http-request="uploadHttpRequest" :on-success="handleUploadSuccess" :on-error="handleUploadError">
                    <el-button type="primary">点击上传</el-button>
                  </el-upload>
                  <span class="tip-text">仅支持PDF，≤60M</span>
                </div>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-col>

      <el-col :span="8">
        <el-card class="log-card" shadow="never">
          <h3 class="card-title">操作记录</h3>
          <div class="timeline">
            <div class="timeline-item" v-for="(item, idx) in historyList" :key="idx">
              <div class="timeline-dot done"><el-icon size="12"><Check /></el-icon></div>
              <div class="timeline-content">
                <p class="timeline-title">{{ item.operationName }}</p>
                <p class="timeline-desc">{{ item.operatorName }}<span v-if="item.actionName">（{{ item.actionName }}）</span></p>
                <p class="timeline-time">{{ item.createTime }}</p>
                <div class="timeline-opinion" v-if="item.opinion">{{ item.opinion }}</div>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <div class="btn-group" style="text-align:center; margin-top:30px">
      <el-button @click="handleBack">返回</el-button>
      <el-button type="primary" @click="handleSubmit">提交</el-button>
    </div>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { Check } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getCheckout, uploadContractPdf, completeTask, getRetreatHistory } from "@/api/code/checkout.ts"
import { useRoute, useRouter } from "vue-router";

const router = useRouter()
const route = useRoute();
const taskId = route.query.taskId;
const businessId = route.query.businessId;

const form = reactive({
  approvalResult: 1,
  approvalOpinion: '',
  businessData: {
    contractNo: '',
    releaseDate: null,
    contractUrl: '',
    contractName: '解除协议'
  }
})

const fileList = ref([])
const historyList = ref([])

const data = reactive({
  retreatCode: '', oldManName: '', idCard: '', phone: '', nursingLevel: '', bedNo: '',
  contractName: '', counselor: '', nurse: '', checkoutDate: '', reason: '', remark: '',
  applicatName: '', applyTime: ''
});

// 时间格式化（统一去掉 T，显示 2026-04-07 05:55）
function formatTime(time) {
  if (!time) return ''
  return time.replace('T', ' ').substring(0, 16)
}

// 节点名称映射
function getNodeName(node) {
  const map = {
    0: '发起申请',
    1: '申请审批',
    2: '解除合同',
    3: '调整账单',
    4: '账单审批',
    5: '退住审批',
    6: '费用清算'
  }
  return map[node] || '流程节点'
}

onMounted(async () => {
  const res = await getCheckout(businessId)
  if (res.data) {
    data.retreatCode = res.data.retreatCode || ''
    data.oldManName = res.data.name || ''
    data.idCard = res.data.idCardNo || ''
    data.phone = res.data.phone || ''
    data.nursingLevel = res.data.nursingLevelName || ''
    data.bedNo = res.data.bedNo || ''
    data.contractName = res.data.contractName || ''
    data.counselor = res.data.counselor || ''
    data.nurse = res.data.nursingName || ''
    data.checkoutDate = res.data.checkOutTime || ''
    data.reason = res.data.reason || ''
    data.remark = res.data.remark || ''
    data.applicatName = res.data.applicatName || ''
    data.applyTime = formatTime(res.data.createTime || '')
    form.businessData.contractNo = res.data.retreatCode?.replace(/^RT/, 'HT') || ''
  }

  // 加载审批历史（只取最后一条 = 上一节点）
  const historyRes = await getRetreatHistory(businessId)
  let lastHistory = null

  if (historyRes.data && historyRes.data.length > 0) {
    const last = historyRes.data[historyRes.data.length - 1]
    lastHistory = {
      ...last,
      operationName: getNodeName(last.fromNode),
      actionName: last.action === 1 ? '已通过' : '已驳回',
      createTime: formatTime(last.createTime),
      operatorName: last.operatorName
    }
  }

  // 固定第一条（已去掉多余括号）
  const firstLine = {
    operationName: '发起申请-申请退住',
    operatorName: data.applicatName + '（已发起）',
    createTime: data.applyTime,
    opinion: ''
  }

  // 最终只显示 2 条
  historyList.value = [firstLine]
  if (lastHistory) {
    historyList.value.push(lastHistory)
  }
})

const previewContract = () => {
  data.contractUrl ? window.open(data.contractUrl, '_blank') : ElMessage.warning('无合同文件')
}

const uploadHttpRequest = async (options) => {
  const formData = new FormData()
  formData.append('file', options.file)
  uploadContractPdf(formData).then(res => {
    form.businessData.contractUrl = res.data
    options.onSuccess(res)
  })
}

const handleSubmit = () => {
  if (!form.businessData.releaseDate) {
    ElMessage.warning('请选择解除日期')
    return
  }
  if (!form.businessData.contractUrl) {
    ElMessage.warning('请上传解除协议')
    return
  }

  ElMessageBox.confirm('确认提交该审批？', '提示', { type: 'warning' }).then(() => {
    const dto = {
      taskId,
      action: 1,
      opinion: form.approvalOpinion || '同意',
      businessData: {
        contractNo: form.businessData.contractNo,
        contractUrl: form.businessData.contractUrl,
        contractName: '解除协议',
        terminateDate: form.businessData.releaseDate
      }
    }
    completeTask(dto).then(() => {
      ElMessage.success('提交成功')
      router.push('/system/my-to/index')
    })
  })
}

const handleBack = () => router.push('/system/my-to/index')
</script>

<style scoped>
.contract-release-page { padding: 20px; background: #fff; min-height: 100vh; }
.steps-container { margin-bottom: 20px; }
.card-title { font-size: 16px; font-weight: 500%; margin: 0 0 16px; color: #333; }
.info-desc { --el-descriptions-label-width: 120px; }
.timeline { position: relative; padding-left: 24px; }
.timeline::before { content: ''; position: absolute; left: 11px; top: 0; bottom: 0; width: 2px; background: #e5e6eb; }
.timeline-item { position: relative; margin-bottom: 24px; }
.timeline-dot { position: absolute; left: -24px; top: 4px; width: 22px; height: 22px; border-radius: 50%; display: flex; align-items: center; justify-content: center; color: #fff; background: #67c23a; z-index: 1; }
.timeline-content { font-size: 14px; }
.timeline-title { margin: 0 0 4px; color: #333; }
.timeline-desc { margin: 0 0 4px; color: #666; }
.timeline-time { font-size: 12px; color: #999; }
.timeline-opinion { margin-top: 8px; padding: 12px; background: #f2f3f5; border-radius: 6px; color: #333; }
</style>