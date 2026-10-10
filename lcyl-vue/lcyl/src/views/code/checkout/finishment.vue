<template>
  <div class="contract-release-page">
    <el-steps :active="7" finish-status="success" align-center class="steps-container">
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
<!--              <span>{{ contractInfo.contractName }}</span>-->
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
<!--              <span>{{ contractInfo.contractName }}</span>-->
              <el-link type="primary" class="view-link" @click="previewContract">查看</el-link>
            </div>
          </div>
        </el-card>

        <!-- 费用清算（只读展示） -->
        <el-card class="info-card" shadow="never" style="margin-top:20px">
          <h3 class="card-title">费用清算</h3>
          <el-descriptions :column="1" label-align="right" class="info-desc" style="max-width: 500px">
            <el-descriptions-item label="最终退款金额">
              <span class="final-amount">{{ formatAmount(finalRefundAmount) }} 元</span>
            </el-descriptions-item>

            <el-descriptions-item label="退款方式">
              <span style="font-size:14px">{{ refundForm.refundMethod || '未填写' }}</span>
            </el-descriptions-item>

            <el-descriptions-item label="退款凭证">
              <template v-if="refundForm.refundVoucher">
                <el-image
                    style="width: 100px; height: 100px"
                    :src="refundForm.refundVoucher"
                    :preview-src-list="[refundForm.refundVoucher]"
                >
                  <template #error>
                    <div style="color:#666;font-size:13px">
                      <a :href="refundForm.refundVoucher" target="_blank">
                        点击下载凭证
                      </a>
                    </div>
                  </template>
                </el-image>
              </template>
              <span v-else style="font-size:14px;color:#999">未上传</span>
            </el-descriptions-item>

            <el-descriptions-item label="退款备注">
              <span style="font-size:14px; white-space: pre-wrap;">{{ refundForm.refundRemark || '无' }}</span>
            </el-descriptions-item>
          </el-descriptions>

          <div style="margin-top:20px; text-align:right;">
            <el-button @click="handleBack">返回</el-button>
          </div>
        </el-card>
      </el-col>

      <el-col :span="8"></el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useRoute, useRouter } from "vue-router"
import { getCheckout, contractDetail, getRetreatBill } from "@/api/code/checkout"
import { getBalanceByElderId } from '@/api/code/balance'
import { getPendingBills, listServiceOrder } from '@/api/code/bill'

const router = useRouter()
const route = useRoute()
const businessId = route.query.businessId || route.query.id

const data = reactive({
  retreatCode: '', oldManName: '', idCard: '', phone: '', nursingLevel: '', bedNo: '',
  counselor: '', nurse: '', checkoutDate: '', reason: '', remark: '', applicant: '', applyTime: '', elderId: null
})

const contractInfo = reactive({
  contractNo: '', releaseDate: '', contractUrl: '', contractName: ''
})

const refundForm = reactive({
  refundMethod: '',
  refundVoucher: '',
  refundRemark: ''
})

const nursingItems = ref([])
const arrearsList = ref([])
const balanceInfo = reactive({ depositAmount:0, actualRefundAmount:0, prepaidAmount:0 })

const refundTotalAmount = computed(() => nursingItems.value.reduce((s,i)=>s+parseFloat(i.orderAmount||0),0))
const totalArrears = computed(() => arrearsList.value.reduce((s,i)=>s+parseFloat(i.payableAmount||0),0))
const balanceTotalAmount = computed(() => parseFloat(balanceInfo.actualRefundAmount||0) + parseFloat(balanceInfo.prepaidAmount||0))
const finalRefundAmount = computed(() => refundTotalAmount.value - totalArrears.value + balanceTotalAmount.value)

const formatAmount = (v) => isNaN(parseFloat(v)) ? '0.00' : parseFloat(v).toFixed(2)

const loadBillData = async (elderId) => {
  if(!elderId) return
  try {
    const balanceRes = await getBalanceByElderId(elderId)
    if(balanceRes?.code === 200){
      balanceInfo.depositAmount = balanceRes.data.depositAmount||0
      balanceInfo.actualRefundAmount = balanceRes.data.depositAmount||0
      balanceInfo.prepaidAmount = balanceRes.data.prepaidBalance||0
    }
    const pendingRes = await getPendingBills(elderId)
    if(pendingRes?.code === 200) arrearsList.value = pendingRes.data||[]
    const nursingRes = await listServiceOrder(elderId)
    if(nursingRes.code === 200) nursingItems.value = nursingRes.data||[]
  }catch(e){}
}

onMounted(async () => {
  try {
    const res = await getCheckout(businessId)
    if(res.data){
      data.retreatCode = res.data.retreatCode||''
      data.oldManName = res.data.name||''
      data.idCard = res.data.idCardNo||''
      data.phone = res.data.phone||''
      data.nursingLevel = res.data.nursingLevelName||''
      data.bedNo = res.data.bedNo||''
      data.counselor = res.data.counselor||''
      data.nurse = res.data.nursingName||''
      data.checkoutDate = res.data.checkOutTime||''
      data.reason = res.data.reason||''
      data.remark = res.data.remark||''
      data.applicant = res.data.applicat||''
      data.applyTime = res.data.createTime||''
      data.elderId = res.data.elderId
      loadBillData(res.data.elderId)
    }

    // 👇 加载费用清算信息（关键！）
    const billRes = await getRetreatBill(businessId)
    if(billRes.data){
      refundForm.refundMethod = billRes.data.tradingChannel || '银行转账'
      refundForm.refundVoucher = billRes.data.refundVoucherUrl || ''
      refundForm.refundRemark = billRes.data.remark || '退住费用已清算完成'
    }

    const contractRes = await contractDetail(businessId)
    if(contractRes.data){
      contractInfo.contractNo = contractRes.data.contractNo||''
      contractInfo.releaseDate = contractRes.data.terminateDate?.split('T')[0]||''
      contractInfo.contractUrl = contractRes.data.contractUrl||''
      contractInfo.contractName = contractRes.data.contractName||''
    }
  }catch(e){}
})

const previewContract = () => {
  if(contractInfo.contractUrl) window.open(contractInfo.contractUrl,'_blank')
}

const handleBack = () => {
  router.push('/retreat/checkout')
}
</script>

<style scoped lang="scss">
.contract-release-page { padding: 20px; background: #f5f7fa; min-height: 100vh; }
.steps-container { margin-bottom: 20px; background: #fff; padding: 20px; border-radius: 8px; }
.card-title { font-size: 16px; font-weight:500; margin:0 0 16px; border-left:4px solid #409eff; padding-left:12px; }
.info-desc { --el-descriptions-label-width: 120px; }
.final-amount { font-size:16px; font-weight:bold; color:#F56C6C; }
.release-item { display:flex; margin-bottom:20px; padding-bottom:20px; border-bottom:1px solid #ebeef5; }
.release-label { width:80px; color:#909399; }
.release-value { flex:1; }
</style>