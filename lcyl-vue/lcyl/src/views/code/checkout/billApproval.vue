<template>
  <div class="contract-release-page">
    <el-steps :active="4" finish-status="success" align-center class="steps-container">
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

        <!-- 账单调整核心区域 → 已改为只读 -->
        <div class="bill-adjust-container">
          <!-- 1. 应退区域 -->
          <el-card class="bill-section-card" shadow="hover">
            <template #header>
              <div class="section-header">
                <span class="section-title">应退</span>
                <span class="section-right">
                  小计：<span class="total-amount-text">{{ formatAmount(refundTotalAmount) }}元</span>
                </span>
              </div>
            </template>
            <div v-for="(item, index) in nursingItems" :key="item.id || index" class="bill-item-card">
              <div class="bill-item-header">
                <span class="bill-no">{{ item.orderNo }}</span>
                <el-tag type="primary" size="small">费用账单</el-tag>
              </div>
              <div class="bill-item-row">
                <span class="bill-item-label">护理项目名称：</span>
                <span class="bill-item-value">{{ item.projectName }}</span>
              </div>
              <div class="bill-item-row">
                <span class="bill-item-label">可退金额：</span>
                <span class="bill-item-value amount-text">{{ formatAmount(item.orderAmount) }}元</span>
              </div>
            </div>
            <el-empty v-if="nursingItems.length === 0" description="暂无应退费用" />
          </el-card>

          <!-- 2. 欠费区域 -->
          <el-card class="bill-section-card" shadow="hover" style="margin-top: 20px">
            <template #header>
              <div class="section-header">
                <span class="section-title">欠费</span>
                <span class="section-right">
                  小计：<span class="total-amount-text danger">{{ formatAmount(totalArrears) }}元</span>
                </span>
              </div>
            </template>
            <div class="bill-item-card">
              <div class="bill-item-row">
                <span class="bill-item-label">账单月份：</span>
                <span class="bill-item-value">{{ refundForm.billMonth }}</span>
              </div>
              <div class="bill-item-row">
                <span class="bill-item-label">月度账单：</span>
                <span class="bill-item-value amount-text danger">{{ formatAmount(refundForm.billAmount) }}元</span>
              </div>
              <div class="bill-item-row">
                <span class="bill-item-label">本月天数：</span>
                <span class="bill-item-value">{{ refundForm.actualDays }}</span>
              </div>
              <div class="bill-item-row">
                <span class="bill-item-label">服务天数：</span>
                <span class="bill-item-value">{{ refundForm.refundDays }}</span>
              </div>
              <div class="bill-item-row">
                <span class="bill-item-label">实际金额：</span>
                <span class="bill-item-value">{{ formatAmount(refundForm.actualRefundAmount) }}元</span>
              </div>
              <div class="bill-item-row">
                <span class="bill-item-label">调整备注：</span>
                <span class="bill-item-value">{{ refundForm.remark }}</span>
              </div>
            </div>
            <div v-for="(item, index) in arrearsList" :key="item.id || index" class="bill-item-card" style="margin-top:12px">
              <div class="bill-item-row">
                <span class="bill-item-label">账单月份：</span>
                <span class="bill-item-value">{{ item.billMonth }}</span>
              </div>
              <div class="bill-item-row">
                <span class="bill-item-label">应付金额：</span>
                <span class="bill-item-value amount-text danger">{{ formatAmount(item.payableAmount) }}元</span>
              </div>
            </div>
          </el-card>

          <!-- 3. 余额区域 -->
          <el-card class="bill-section-card" shadow="hover" style="margin-top: 20px">
            <template #header>
              <div class="section-header">
                <span class="section-title">余额</span>
                <span class="section-right">
                  小计：<span class="total-amount-text success">{{ formatAmount(balanceTotalAmount) }}元</span>
                </span>
              </div>
            </template>
            <div class="bill-item-card">
              <div class="bill-item-row">
                <span class="bill-item-label">可退押金：</span>
                <span class="bill-item-value">{{ formatAmount(balanceInfo.depositAmount) }}元</span>
              </div>
              <div class="bill-item-row">
                <span class="bill-item-label">实退金额：</span>
                <span class="bill-item-value">{{ formatAmount(balanceInfo.actualRefundAmount) }}元</span>
              </div>
              <div class="bill-item-row">
                <span class="bill-item-label">调整备注：</span>
                <span class="bill-item-value">{{ balanceInfo.remark }}</span>
              </div>
            </div>
            <div class="bill-item-card" style="margin-top: 12px">
              <div class="bill-item-row">
                <span class="bill-item-label">预缴款：</span>
                <span class="bill-item-value">{{ formatAmount(balanceInfo.prepaidAmount) }}元</span>
              </div>
            </div>
          </el-card>

          <!-- 4. 最终结算明细 -->
          <el-card class="bill-section-card final-summary-card" shadow="hover" style="margin-top: 20px">
            <template #header>
              <div class="section-header">
                <span class="section-title">费用结算明细</span>
              </div>
            </template>
            <div class="bill-item-card">
              <div class="bill-item-row">
                <span class="bill-item-label">应退总金额：</span>
                <span class="bill-item-value amount-text">{{ formatAmount(refundTotalAmount) }} 元</span>
              </div>
              <div class="bill-item-row">
                <span class="bill-item-label">欠费总金额：</span>
                <span class="bill-item-value amount-text danger">{{ formatAmount(totalArrears) }} 元</span>
              </div>
              <div class="bill-item-row">
                <span class="bill-item-label">可用余额：</span>
                <span class="bill-item-value amount-text success">{{ formatAmount(balanceTotalAmount) }} 元</span>
              </div>
              <div class="bill-item-row final-row">
                <span class="bill-item-label final-label">最终退款金额：</span>
                <span class="bill-item-value final-amount">{{ formatAmount(finalRefundAmount) }} 元</span>
              </div>
            </div>
          </el-card>
        </div>
      </el-col>

      <!-- 右侧：审批面板（新增） -->
      <el-col :span="8">
        <div class="approval-panel">
          <div class="form-item">
            <span class="label">*审批结果</span>
            <el-radio-group v-model="approvalForm.approvalResult" label="number">
              <el-radio label="1">审批通过</el-radio>
              <el-radio label="2">驳回</el-radio>
            </el-radio-group>
          </div>
          <div class="form-item">
            <span class="label">*审批意见</span>
            <el-input
                v-model="approvalForm.approvalOpinion"
                type="textarea"
                :rows="3"
                placeholder="请输入审批意见"
                maxlength="200"
                show-word-limit
            />
          </div>
          <el-button type="primary" block @click="handleSubmitApproval">提交审批</el-button>
        </div>

        <!-- 操作记录 -->
        <el-card class="log-card" shadow="never" style="margin-top:20px">
          <h3 class="card-title">操作记录</h3>
          <div class="timeline">
            <div class="timeline-item">
              <div class="timeline-dot done"><el-icon size="12"><Check /></el-icon></div>
              <div class="timeline-content">
                <p class="timeline-title">发起申请-申请退住</p>
                <p class="timeline-desc">{{ data.applicant }}（已发起）</p>
                <span class="timeline-time">{{ data.applyTime }}</span>
              </div>
            </div>
            <div class="timeline-item">
              <div class="timeline-dot done"><el-icon size="12"><Check /></el-icon></div>
              <div class="timeline-content">
                <p class="timeline-title">账单调整已完成</p>
                <p class="timeline-desc">系统已保存结算数据</p>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { Check } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useRoute, useRouter } from "vue-router"
import { getCheckout, completeTask, contractDetail } from "@/api/code/checkout"
import { getBalanceByElderId } from '@/api/code/balance'
import { getPendingBills, listServiceOrder } from '@/api/code/bill'

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

// 账单数据
const refundForm = reactive({
  billNo: '', billMonth: '', actualDays: 0, refundDays: 0, actualRefundAmount: 0, billAmount: 0, remark: ''
})
const nursingItems = ref([])
const arrearsList = ref([])
const balanceInfo = reactive({
  depositAmount: 0, actualRefundAmount: 0, prepaidAmount: 0, remark: ''
})

// 审批表单
const approvalForm = reactive({
  approvalResult: 1,
  approvalOpinion: ''
})

// 计算属性
const refundTotalAmount = computed(() => nursingItems.value.reduce((s,i)=>s+parseFloat(i.orderAmount||0),0))
const totalArrears = computed(() => {
  const month = parseFloat(refundForm.actualRefundAmount)||0
  const hist = arrearsList.value.reduce((s,i)=>s+parseFloat(i.payableAmount||0),0)
  return month + hist
})
const balanceTotalAmount = computed(() =>
    parseFloat(balanceInfo.actualRefundAmount||0) + parseFloat(balanceInfo.prepaidAmount||0)
)
const finalRefundAmount = computed(() => refundTotalAmount.value - totalArrears.value + balanceTotalAmount.value)

const formatAmount = (v) => isNaN(parseFloat(v)) ? '0.00' : parseFloat(v).toFixed(2)

// 加载账单数据（保留原有方法）
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
        refundForm.billAmount = data[0].billAmount || 0
      }
    }

    const nursingRes = await listServiceOrder(elderId)
    if (nursingRes.code === 200) {
      nursingItems.value = nursingRes.data || []
    }

    refundForm.billNo = 'ZD' + new Date().getFullYear() + String(new Date().getMonth() + 1).padStart(2, '0') + '01015000001'
    refundForm.billMonth = new Date().getFullYear() + "-" + String(new Date().getMonth() + 1).padStart(2, '0')
    refundForm.actualDays = 30
    refundForm.refundDays = 12
  } catch (e) {
    console.error("账单数据加载失败", e)
  }
}

// 初始化页面
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
      data.counselor = res.data.counselor || ''
      data.nurse = res.data.nursingName || ''
      data.checkoutDate = res.data.checkOutTime || ''
      data.reason = res.data.reason || ''
      data.remark = res.data.remark || ''
      data.applicant = res.data.applicat || ''
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
  } catch (e) {
    console.error("页面初始化失败", e)
  }
})

// 预览合同
const previewContract = () => {
  contractInfo.contractUrl ? window.open(contractInfo.contractUrl, '_blank') : ElMessage.warning('合同文件不存在')
}

// 提交审批
const handleSubmitApproval = () => {
  if (!approvalForm.approvalOpinion) {
    ElMessage.warning('请输入审批意见')
    return
  }
  ElMessageBox.confirm('确认提交该审批？').then(() => {
    const dto = {
      taskId,
      action: approvalForm.approvalResult,
      opinion: approvalForm.approvalOpinion,
      businessData: { businessId }
    }
    completeTask(dto).then(() => {
      ElMessage.success('提交成功')
      router.push('/system/my-to/index')
    }).catch(() => ElMessage.error('提交失败'))
  })
}
</script>

<style scoped lang="scss">
.contract-release-page { padding: 20px; background: #f5f7fa; min-height: 100vh; }
.steps-container { margin-bottom: 20px; background: #fff; padding: 20px; border-radius: 8px; }
.card-title { font-size: 16px; font-weight: 500; margin: 0 0 16px; border-left: 4px solid #409eff; padding-left:12px; }

.approval-panel {
  background: #fff; padding: 20px; border-radius: 8px;
  .form-item { margin-bottom: 20px; }
  .label { display: block; margin-bottom: 8px; font-weight: 500; }
}

.info-desc { --el-descriptions-label-width: 100px; }
.view-link { margin-left: 8px; }
.release-card .release-item {
  display: flex; align-items: flex-start; margin-bottom: 20px; padding-bottom: 20px; border-bottom: 1px solid #ebeef5;
}
.release-card .release-item:last-child { border-bottom: none; margin-bottom: 0; padding-bottom: 0; }
.release-label { width: 80px; color: #909399; font-size: 14px; }
.release-value { flex: 1; color: #303133; font-size: 14px; }
.bill-adjust-container { margin-top: 20px; }
.bill-section-card {
  border-radius: 8px; border: 1px dashed #dcdfe6;
  .section-header {
    display: flex; align-items: center; justify-content: space-between;
    .section-title { font-size: 16px; font-weight: 600; color: #303133; flex: 1; }
    .section-right { font-size: 14px; color: #606266;
      .total-amount-text { color: #303133; font-weight: 600; font-size: 16px;
        &.danger { color: #f56c6c; }
        &.success { color: #67c23a; }
      }
    }
  }
  .bill-item-card {
    padding: 16px; background: #fafafa; border-radius: 6px; border: 1px solid #ebeef5;
    .bill-item-header { display: flex; align-items: center; gap: 8px; margin-bottom: 12px;
      .bill-no { font-size: 14px; font-weight: 500; color: #303133; }
    }
    .bill-item-row {
      display: flex; align-items: center; margin-bottom: 8px; gap: 8px;
      .bill-item-label { width: 120px; font-size: 14px; color: #606266; flex-shrink: 0; text-align: right; }
      .bill-item-value { font-size: 14px;
        &.amount-text { color: #f56c6c; }
        &.danger { color: #f56c6c; }
        &.success { color: #67c23a; }
      }
    }
  }
}
.final-summary-card {
  border: 1px solid #409eff; background: #f0f9ff;
  .final-row { margin-top: 10px; padding-top: 10px; border-top: 1px solid #dcdfe6; }
  .final-label { font-size: 15px; font-weight: bold; color: #303133; }
  .final-amount { font-size: 20px; font-weight: bold; color: #f56c6c; }
}
.log-card .timeline { position: relative; padding-left: 24px; }
.log-card .timeline::before { content: ''; position: absolute; left: 11px; top: 0; bottom: 0; width: 2px; background: #e5e6eb; }
.timeline-item { position: relative; margin-bottom: 24px; }
.timeline-dot { position: absolute; left: -24px; top: 4px; width: 22px; height: 22px; border-radius: 50%; display: flex; align-items: center; justify-content: center; color: #fff; background: #409eff; }
.timeline-dot.done { background: #67c23a; }
.timeline-title { margin: 0 0 4px; color: #333; font-weight: 500; }
.timeline-desc { margin: 0 0 4px; color: #666; }
.timeline-time { font-size: 12px; color: #999; float: right; }
.timeline-opinion { margin-top: 8px; padding: 12px; background: #f2f3f5; border-radius: 6px; }
</style>