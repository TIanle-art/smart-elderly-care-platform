<template>
  <div class="contract-release-page">
    <el-steps :active="6" finish-status="success" align-center class="steps-container">
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
        <!-- 基本信息卡片 -->
        <el-card class="info-card" shadow="never">
          <h3 class="card-title">基本信息</h3>
          <el-descriptions :column="2" label-align="right" class="info-desc">
            <el-descriptions-item label="单据编号">{{ data.retreatCode }}</el-descriptions-item>
            <el-descriptions-item label="老人姓名">{{ data.oldManName }}</el-descriptions-item>
            <el-descriptions-item label="老人身份证号">{{ data.idCard }}</el-descriptions-item>
            <el-descriptions-item label="联系方式">{{ data.phone }}</el-descriptions-item>
            <el-descriptions-item label="护理等级">{{ data.nursingLevel }}</el-descriptions-item>
            <el-descriptions-item label="入住床位">{{ data.bedNo }}</el-descriptions-item>
            <el-descriptions-item label="签约合同" :span="2">
              <span>{{ contractInfo.contractName }}</span>
              <el-link type="primary" class="view-link" @click="previewContract">查看</el-link>
            </el-descriptions-item>
            <el-descriptions-item label="养老顾问">{{ data.counselor }}</el-descriptions-item>
            <el-descriptions-item label="护理员">{{ data.nurse }}</el-descriptions-item>
          </el-descriptions>
        </el-card>

        <!-- 申请信息卡片 -->
        <el-card class="info-card" shadow="never" style="margin-top: 20px">
          <h3 class="card-title">申请信息</h3>
          <el-descriptions :column="2" label-align="right" class="info-desc">
            <el-descriptions-item label="退住日期">{{ data.checkoutDate }}</el-descriptions-item>
            <el-descriptions-item label="退住原因">{{ data.reason }}</el-descriptions-item>
            <el-descriptions-item label="备注" :span="2">{{ data.remark }}</el-descriptions-item>
            <el-descriptions-item label="申请人">{{ data.applicant }}</el-descriptions-item>
            <el-descriptions-item label="申请时间">{{ data.applyTime }}</el-descriptions-item>
          </el-descriptions>
        </el-card>

        <!-- 解除记录卡片 -->
        <el-card class="info-card release-card" shadow="never" style="margin-top: 20px">
          <h3 class="card-title">解除记录</h3>
          <div class="release-item">
            <div class="release-label">合同编号</div>
            <div class="release-value">{{ contractInfo.contractNo }}</div>
          </div>
          <div class="release-item">
            <div class="release-label">解除日期</div>
            <div class="release-value">{{ contractInfo.releaseDate }}</div>
          </div>
          <div class="release-item">
            <div class="release-label">解除协议</div>
            <div class="release-value">
              <span>{{ contractInfo.contractName }}</span>
              <el-link type="primary" class="view-link" @click="previewContract">查看</el-link>
            </div>
          </div>
        </el-card>

        <!-- 费用清算 -->
        <el-card class="info-card" shadow="never" style="margin-top:20px">
          <h3 class="card-title">费用清算</h3>
          <el-descriptions :column="1" label-align="right" class="info-desc" style="max-width: 500px">
            <el-descriptions-item label="最终退款金额">
              <span class="final-amount">{{ formatAmount(finalRefundAmount) }} 元</span>
            </el-descriptions-item>

            <el-descriptions-item label="退款方式">
              <el-select v-model="refundForm.refundMethod" placeholder="请选择" style="width: 280px">
                <el-option label="现金" value="现金" />
                <el-option label="支付宝" value="支付宝" />
                <el-option label="微信" value="微信" />
              </el-select>
            </el-descriptions-item>

            <el-descriptions-item label="退款凭证">
              <el-upload
                  v-model:file-list="fileList"
                  :limit="1"
                  :http-request="handlePdfUpload"
              >
                <el-button type="primary" size="small">上传凭证</el-button>
              </el-upload>
<!--              <span style="margin-left:10px;font-size:12px;color:#999">上传图片</span>-->
            </el-descriptions-item>

            <el-descriptions-item label="退款备注">
              <el-input
                  v-model="refundForm.refundRemark"
                  type="textarea"
                  rows="2"
                  placeholder="请输入退款备注"
                  style="width: 280px"
                  maxlength="100"
                  show-word-limit
              />
            </el-descriptions-item>
          </el-descriptions>

          <!-- 提交按钮 -->
          <div style="margin-top:20px; text-align:right;">
            <el-button @click="handleBack">返回</el-button>
            <el-button type="primary" @click="handleSubmit">提交</el-button>
          </div>
        </el-card>
      </el-col>

      <!-- 右侧：标准操作记录 -->
      <el-col :span="8">
        <el-card class="log-card" shadow="never">
          <h3 class="card-title">操作记录</h3>
          <div class="timeline">
            <div class="timeline-item" v-for="(item, idx) in historyList" :key="idx">
              <div class="timeline-dot done"><el-icon size="12"><Check /></el-icon></div>
              <div class="timeline-content">
                <p class="timeline-title">{{ item.operationName }}</p>
                <p class="timeline-desc">{{ item.operatorName }}</p>
                <p class="timeline-time">{{ item.createTime }}</p>
                <div class="timeline-opinion" v-if="item.opinion">{{ item.opinion }}</div>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import {ref, reactive, computed, onMounted} from 'vue'
import {Check} from '@element-plus/icons-vue'
import {ElMessage, ElMessageBox} from 'element-plus'
import {useRoute, useRouter} from "vue-router"
import {getCheckout, completeTask, contractDetail, upload, getRetreatHistory} from "@/api/code/checkout"
import {getBalanceByElderId} from '@/api/code/balance'
import {getPendingBills, listServiceOrder} from '@/api/code/bill'

const router = useRouter()
const route = useRoute()
const taskId = route.query.taskId
const businessId = route.query.businessId

// 基础信息
const data = reactive({
  retreatCode: '', oldManName: '', idCard: '', phone: '', nursingLevel: '', bedNo: '',
  counselor: '', nurse: '', checkoutDate: '', reason: '', remark: '', applicant: '', applyTime: '', elderId: null
})

// 合同信息
const contractInfo = reactive({
  contractNo: '', releaseDate: '', contractUrl: '', contractName: ''
})

// 退款表单
const refundForm = reactive({
  refundMethod: '',
  refundVoucher: '',
  refundRemark: ''
})
const fileList = ref([])

// 仅计算金额用
const refundFormOld = reactive({
  billNo: '', billMonth: '', actualDays: 0, refundDays: 0, actualRefundAmount: 0, billAmount: 0, remark: ''
})
const nursingItems = ref([])
const arrearsList = ref([])
const balanceInfo = reactive({
  depositAmount: 0, actualRefundAmount: 0, prepaidAmount: 0, remark: ''
})

// 操作记录（统一标准）
const historyList = ref([])

// 时间格式化
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

// 计算属性
const refundTotalAmount = computed(() => nursingItems.value.reduce((s, i) => s + parseFloat(i.orderAmount || 0), 0))
const totalArrears = computed(() => {
  const month = parseFloat(refundFormOld.actualRefundAmount) || 0
  const hist = arrearsList.value.reduce((s, i) => s + parseFloat(i.payableAmount || 0), 0)
  return month + hist
})
const balanceTotalAmount = computed(() =>
    parseFloat(balanceInfo.actualRefundAmount || 0) + parseFloat(balanceInfo.prepaidAmount || 0)
)
const finalRefundAmount = computed(() => refundTotalAmount.value - totalArrears.value + balanceTotalAmount.value)

const formatAmount = (v) => isNaN(parseFloat(v)) ? '0.00' : parseFloat(v).toFixed(2)

// 加载数据
const loadBillData = async (elderId) => {
  if (!elderId) return
  try {
    const balanceRes = await getBalanceByElderId(elderId)
    if (balanceRes?.code === 200) {
      const b = balanceRes.data
      balanceInfo.depositAmount = b?.depositAmount || 0
      balanceInfo.actualRefundAmount = b?.depositAmount || 0
      balanceInfo.prepaidAmount = b?.prepaidBalance || 0
    }
    const pendingRes = await getPendingBills(elderId)
    if (pendingRes?.code === 200) {
      const data = pendingRes.data || []
      arrearsList.value = data.filter(item => item.tradeStatus == '0')
      if (data.length > 0) {
        refundFormOld.billAmount = data[0].billAmount || 0
      }
    }
    const nursingRes = await listServiceOrder(elderId)
    if (nursingRes.code === 200) {
      nursingItems.value = nursingRes.data || []
    }
  } catch (e) {
    console.error("数据加载失败", e)
  }
}

// 初始化
onMounted(async () => {
  try {
    const res = await getCheckout(businessId)
    if (res.data) {
      data.retreatCode = res.data.retreatCode || ''
      data.oldManName = res.data.name || ''
      data.idCard = res.data.idCardNo || ''
      data.phone = res.data.phone || ''
      data.nursingLevel = res.data.nursingLevelName || ''
      data.bedNo = res.data.bedNo || ''
      data.counselor = res.counselor || ''
      data.nurse = res.data.nursingName || ''
      data.checkoutDate = res.data.checkOutTime || ''
      data.reason = res.data.reason || ''
      data.remark = res.data.remark || ''
      data.applicant = res.data.applicatName || ''
      data.applyTime = res.data.createTime || ''
      data.elderId = res.data.elderId
      loadBillData(res.data.elderId)
    }

    const contractRes = await contractDetail(businessId)
    if (contractRes.data) {
      contractInfo.contractNo = contractRes.data.contractNo || ''
      contractInfo.releaseDate = contractRes.data.terminateDate?.split('T')[0] || ''
      contractInfo.contractUrl = contractRes.data.contractUrl || ''
      contractInfo.contractName = contractRes.data.contractName || ''
    }

    // 加载真实操作记录
    const historyRes = await getRetreatHistory(businessId)
    let lastHistory = null
    if (historyRes.data && historyRes.data.length > 0) {
      const last = historyRes.data[historyRes.data.length - 1]
      lastHistory = {
        operationName: getNodeName(last.fromNode),
        operatorName: last.operatorName + (last.action === 1 ? '（已通过）' : '（已驳回）'),
        createTime: formatTime(last.createTime),
        opinion: last.opinion || ''
      }
    }

    const firstLine = {
      operationName: '发起申请-申请退住',
      operatorName: data.applicant + '（已发起）',
      createTime: formatTime(data.applyTime),
      opinion: ''
    }
    historyList.value = [firstLine]
    if (lastHistory) historyList.value.push(lastHistory)

  } catch (e) {
    console.error("页面初始化失败", e)
  }
})

// 预览合同
const previewContract = () => {
  contractInfo.contractUrl ? window.open(contractInfo.contractUrl, '_blank') : ElMessage.warning('合同文件不存在')
}

// 上传图片
const handlePdfUpload = async (options) => {
  try {
    const formData = new FormData()
    formData.append('file', options.file)
    const res = await upload(formData)
    refundForm.refundVoucher = res.data
    options.onSuccess(res)
    ElMessage.success('凭证上传成功')
  } catch (err) {
    options.onError(err)
    ElMessage.error('上传失败')
  }
}

// 返回
const handleBack = () => {
  router.push('/system/my-to/index')
}

// 提交清算
const handleSubmit = () => {
  if (!refundForm.refundMethod) {
    ElMessage.warning('请选择退款方式')
    return
  }
  if (!refundForm.refundVoucher) {
    ElMessage.warning('请上传退款凭证')
    return
  }
  if (!refundForm.refundRemark) {
    ElMessage.warning('请填写退款备注')
    return
  }

  ElMessageBox.confirm('确认提交费用清算？').then(() => {
    const dto = {
      taskId,
      action: 1,
      opinion: '费用清算完成',
      businessData: {
        businessId,
        finalRefundAmount: finalRefundAmount.value,
        refundMethod: refundForm.refundMethod,
        refundVoucher: refundForm.refundVoucher,
        refundRemark: refundForm.refundRemark
      }
    }
    completeTask(dto).then(() => {
      ElMessage.success('提交成功')
      router.push('/system/my-to/index')
    }).catch(() => ElMessage.error('提交失败'))
  })
}
</script>

<style scoped lang="scss">
.contract-release-page {
  padding: 20px;
  background: #f5f7fa;
  min-height: 100vh;
}

.steps-container {
  margin-bottom: 20px;
  background: #fff;
  padding: 20px;
  border-radius: 8px;
}

.card-title {
  font-size: 16px;
  font-weight: 500;
  margin: 0 0 16px;
  border-left: 4px solid #409eff;
  padding-left: 12px;
}

.info-desc {
  --el-descriptions-label-width: 120px;
}

.view-link {
  margin-left: 8px;
}

.final-amount {
  font-size: 16px;
  font-weight: bold;
  color: #F56C6C;
}

.release-card .release-item {
  display: flex;
  align-items: flex-start;
  margin-bottom: 20px;
  padding-bottom: 20px;
  border-bottom: 1px solid #ebeef5;
}

.release-card .release-item:last-child {
  border-bottom: none;
  margin-bottom: 0;
  padding-bottom: 0;
}

.release-label {
  width: 80px;
  color: #909399;
  font-size: 14px;
}

.release-value {
  flex: 1;
  color: #303133;
  font-size: 14px;
}

.log-card .timeline {
  position: relative;
  padding-left: 24px;
}

.log-card .timeline::before {
  content: '';
  position: absolute;
  left: 11px;
  top: 0;
  bottom: 0;
  width: 2px;
  background: #e5e6eb;
}

.timeline-item {
  position: relative;
  margin-bottom: 24px;
}

.timeline-dot {
  position: absolute;
  left: -24px;
  top: 4px;
  width: 22px;
  height: 22px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  background: #409eff;
}

.timeline-dot.done {
  background: #67c23a;
}

.timeline-title {
  margin: 0 0 4px;
  color: #333;
  font-weight: 500;
}

.timeline-desc {
  margin: 0 0 4px;
  color: #666;
}

.timeline-time {
  font-size: 12px;
  color: #999;
  float: right;
}
</style>