<template>
  <div class="sign-container">
    <div class="steps-header">
      <el-steps :active="activeStep" finish-status="success" align-center>
        <el-step title="申请入住" />
        <el-step title="入住评估" />
        <el-step title="入住审批" />
        <el-step title="入住配置" />
        <el-step title="签约办理" />
      </el-steps>
    </div>

    <div class="main-content">
      <div class="left-panel">
        <el-card shadow="never">
          <template #header>
            <div class="card-title">签约办理</div>
          </template>

          <el-tabs v-model="activeTab" class="custom-tabs">
            <el-tab-pane label="签约信息" name="contract">
              <el-form :model="signForm" label-width="120px" class="sign-form">
                <el-form-item label="合同编号" required>
                  <el-input v-model="signForm.contractNo" placeholder="自动生成" disabled />
                </el-form-item>

                <el-form-item label="签约日期" required>
                  <el-date-picker
                    v-model="signForm.signDate"
                    type="date"
                    placeholder="选择签约日期"
                    value-format="YYYY-MM-DD"
                    style="width: 100%"
                  />
                </el-form-item>

                <el-form-item label="合同期限" required>
                  <el-select v-model="signForm.contractPeriod" placeholder="请选择合同期限" style="width: 100%">
                    <el-option label="1年" value="1" />
                    <el-option label="2年" value="2" />
                    <el-option label="3年" value="3" />
                    <el-option label="长期" value="99" />
                  </el-select>
                </el-form-item>

                <el-form-item label="签约人" required>
                  <el-input v-model="signForm.signer" placeholder="请输入签约人姓名" />
                </el-form-item>

                <el-form-item label="签约人证件" required>
                  <el-input v-model="signForm.signerIdCard" placeholder="请输入身份证号" maxlength="18" />
                </el-form-item>

                <el-form-item label="签约人电话" required>
                  <el-input v-model="signForm.signerPhone" placeholder="请输入联系电话" maxlength="11" />
                </el-form-item>

                <el-form-item label="与老人关系" required>
                  <el-select v-model="signForm.relationship" placeholder="请选择" style="width: 100%">
                    <el-option label="子女" value="子女" />
                    <el-option label="配偶" value="配偶" />
                    <el-option label="兄弟姐妹" value="兄弟姐妹" />
                    <el-option label="其他" value="其他" />
                  </el-select>
                </el-form-item>

                <el-form-item label="缴费方式" required>
                  <el-radio-group v-model="signForm.payMethod">
                    <el-radio label="1">月付</el-radio>
                    <el-radio label="2">季付</el-radio>
                    <el-radio label="3">半年付</el-radio>
                    <el-radio label="4">年付</el-radio>
                  </el-radio-group>
                </el-form-item>

                <el-form-item label="押金" required>
                  <el-input v-model="signForm.deposit" disabled style="width: 100%">
                    <template #append>元</template>
                  </el-input>
                </el-form-item>

                <el-form-item label="合同文件" required>
                  <FileUpload
                    v-model="signForm.pdfUrl"
                    :limit="1"
                    :file-size="10"
                    :file-type="['pdf', 'jpg', 'png', 'jpeg']"
                    :is-show-tip="true"
                  />
                </el-form-item>

                <el-form-item label="签约备注">
                  <el-input
                    v-model="signForm.remark"
                    type="textarea"
                    :rows="4"
                    placeholder="请输入特殊说明或备注信息..."
                    maxlength="200"
                    show-word-limit
                  />
                </el-form-item>
              </el-form>
            </el-tab-pane>

            <el-tab-pane label="入住概览" name="summary">
              <el-descriptions :column="2" border size="small">
                <el-descriptions-item label="老人姓名">{{ elderInfo.name || '-' }}</el-descriptions-item>
                <el-descriptions-item label="性别">{{ getGenderLabel(elderInfo.gender) }}</el-descriptions-item>
                <el-descriptions-item label="身份证号">{{ elderInfo.idCard || '-' }}</el-descriptions-item>
                <el-descriptions-item label="联系电话">{{ elderInfo.phone || '-' }}</el-descriptions-item>
                <el-descriptions-item label="房间号">{{ roomInfo.roomNo || '-' }}</el-descriptions-item>
                <el-descriptions-item label="床位号">{{ roomInfo.bedNo || '-' }}</el-descriptions-item>
                <el-descriptions-item label="入住日期">{{ formatDateValue(roomInfo.checkInDate) }}</el-descriptions-item>
                <el-descriptions-item label="护理等级">{{ roomInfo.careLevel || '-' }}</el-descriptions-item>
                <el-descriptions-item label="月收费">{{ roomInfo.feeStandard ? `${roomInfo.feeStandard}元` : '-' }}</el-descriptions-item>
                <el-descriptions-item label="评估等级">{{ elderInfo.assessLevel || '-' }}</el-descriptions-item>
              </el-descriptions>
            </el-tab-pane>
          </el-tabs>

          <div class="bottom-btns">
            <el-button @click="goBack">取消</el-button>
            <el-button type="primary" @click="submitSign">完成签约</el-button>
          </div>
        </el-card>
      </div>

      <div class="right-panel">
        <el-card shadow="never">
          <template #header><span>流程记录</span></template>
          <el-timeline v-if="operationRecords.length">
            <el-timeline-item
              v-for="(record, index) in operationRecords"
              :key="index"
              :timestamp="record.time"
              :type="record.type"
              :hollow="record.hollow"
            >
              <h4>{{ record.title }}</h4>
              <p>{{ record.operator }}</p>
              <p v-if="record.remark">{{ record.remark }}</p>
            </el-timeline-item>
          </el-timeline>
          <el-empty v-else description="暂无流程记录" />
        </el-card>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getCheckin, completeContract } from '@/api/system/checkin.js'
import { listCheckInConfig } from '@/api/system/checkInConfig.js'
import { ElMessage, ElMessageBox } from 'element-plus'
import { parseTime } from '@/utils/ruoyi.js'
import FileUpload from '@/components/FileUpload/index.vue'

import useUserStore from '@/store/modules/user.js'

const route = useRoute()
const router = useRouter()

const activeStep = ref(4)
const activeTab = ref('contract')
const operationRecords = ref([])

const userStore = useUserStore()

const signForm = reactive({
  contractNo: '',
  signDate: '',
  contractPeriod: '',
  signer: '',
  signerIdCard: '',
  signerPhone: '',
  relationship: '',
  payMethod: '',
  deposit: '3000',
  pdfUrl: '',
  remark: ''
})

const elderInfo = reactive({
  name: '',
  gender: '',
  idCard: '',
  phone: '',
  assessLevel: ''
})

const roomInfo = reactive({
  roomNo: '',
  bedNo: '',
  checkInDate: '',
  careLevel: '',
  feeStandard: ''
})

const getGenderLabel = (value) => {
  if (value === '1') return '男'
  if (value === '0') return '女'
  return '-'
}

const formatDateValue = (value) => {
  if (!value) return '-'
  return parseTime(value, '{y}-{m}-{d}')
}

const parseJsonValue = (value) => {
  if (!value) return null
  if (typeof value === 'object') return value
  try {
    const parsed = JSON.parse(value)
    if (typeof parsed === 'string') {
      return JSON.parse(parsed)
    }
    return parsed
  } catch (e) {
    return null
  }
}

const formatDateTime = (value) => {
  if (!value) return ''
  return parseTime(value, '{y}-{m}-{d} {h}:{i}')
}

const buildOperationRecords = (data, evaluation) => {
  const records = []
  if (data?.createTime) {
    records.push({
      title: '发起申请',
      operator: `操作人：${data.applicat || '申请人'}`,
      time: formatDateTime(data.createTime),
      type: 'success',
      hollow: false
    })
  }
  if (evaluation?.reportForm?.evaluator) {
    records.push({
      title: '完成评估',
      operator: `操作人：${evaluation.reportForm.evaluator}`,
      time: formatDateTime(evaluation.reportForm.evaluateTime || data.updateTime),
      type: 'success',
      hollow: false,
      remark: evaluation.abilityForm?.abilityLevel ? `评估结果：${evaluation.abilityForm.abilityLevel}` : ''
    })
  }
  if (data?.currentTaskName) {
    records.push({
      title: data.currentTaskName,
      operator: data.evaluator ? `操作人：${data.evaluator}` : '操作人：-',
      time: formatDateTime(data.updateTime || data.createTime),
      type: 'primary',
      hollow: true
    })
  }
  operationRecords.value = records
}

const applyConfigToRoomInfo = (config, data, evaluation) => {
  const bedNo = config?.bedNo || ''
  roomInfo.bedNo = bedNo
  roomInfo.roomNo = bedNo
  roomInfo.checkInDate = config?.checkInStartTime || data?.checkInTime || ''
  if (evaluation?.abilityForm?.abilityLevel) {
    roomInfo.careLevel = evaluation.abilityForm.abilityLevel
  } else if (config?.nursingLevelId) {
    roomInfo.careLevel = `护理等级${config.nursingLevelId}`
  }
  const nursingCost = Number(config?.nursingCost || 0)
  const bedCost = Number(config?.bedCost || 0)
  const otherCost = Number(config?.otherCost || 0)
  const monthlyFee = nursingCost + bedCost + otherCost
  roomInfo.feeStandard = monthlyFee > 0 ? monthlyFee : ''
}

onMounted(async () => {
  const businessId = route.query.businessId || route.params.businessId
  if (businessId) {
    try {
      const res = await getCheckin(businessId)
      if (res.code === 200 && res.data) {
        const data = res.data
        signForm.deposit = '3000'
        if (data.flowStatus !== undefined && data.flowStatus !== null) {
          activeStep.value = parseInt(data.flowStatus)
        }
        signForm.contractNo = data.contractNo || data.checkInCode || (data.id ? `HT${data.id}` : signForm.contractNo)
        signForm.signDate = data.signDate || signForm.signDate
        const otherInfo = parseJsonValue(data.otherApplyInfo)
        if (otherInfo?.basicForm) {
          elderInfo.name = otherInfo.basicForm.name || elderInfo.name
          elderInfo.gender = otherInfo.basicForm.gender || elderInfo.gender
          elderInfo.idCard = otherInfo.basicForm.idCard || elderInfo.idCard
          elderInfo.phone = otherInfo.basicForm.phone || elderInfo.phone
        }
        elderInfo.name = data.elderName || elderInfo.name
        elderInfo.idCard = data.elderCardId || elderInfo.idCard
        const evaluation = parseJsonValue(data.evaluation)
        if (evaluation?.abilityForm?.abilityLevel) {
          elderInfo.assessLevel = evaluation.abilityForm.abilityLevel
        }
        buildOperationRecords(data, evaluation)
        if (data.elderId) {
          const configRes = await listCheckInConfig({ elderId: data.elderId })
          if (configRes.code === 200 && Array.isArray(configRes.rows) && configRes.rows.length > 0) {
            applyConfigToRoomInfo(configRes.rows[0], data, evaluation)
          } else {
            applyConfigToRoomInfo(null, data, evaluation)
          }
        } else {
          applyConfigToRoomInfo(null, data, evaluation)
        }
      }
    } catch (error) {
      console.error('加载签约信息失败:', error)
    }
  }
})

const submitSign = () => {
  if (!signForm.signDate || !signForm.contractPeriod || !signForm.signer) {
    ElMessage.warning('请完善签约信息')
    return
  }

  if (!signForm.signerIdCard || !signForm.signerPhone || !signForm.relationship) {
    ElMessage.warning('请完善签约人信息')
    return
  }

  if (!signForm.payMethod || !signForm.deposit) {
    ElMessage.warning('请完善缴费方式和押金')
    return
  }

  if (!signForm.pdfUrl) {
    ElMessage.warning('请上传合同文件')
    return
  }

  ElMessageBox.confirm('确认提交签约办理？提交后老人入住流程将完结。', '签约确认', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'success'
  }).then(async () => {
    const checkInId = route.query.businessId || route.query.checkInId || route.params.businessId
    if (!checkInId) {
      ElMessage.error('缺少入住办理ID')
      return
    }
    try {
      await completeContract(checkInId, {
        contractNo: signForm.contractNo,
        signDate: signForm.signDate,
        contractPeriod: signForm.contractPeriod,
        signer: signForm.signer,
        signerIdCard: signForm.signerIdCard,
        signerPhone: signForm.signerPhone,
        relationship: signForm.relationship,
        payMethod: signForm.payMethod,
        deposit: signForm.deposit,
        pdfUrl: signForm.pdfUrl,
        remark: signForm.remark,
        createTime: new Date()
      })
      ElMessage.success('签约办理完成！流程已结束')
      router.push('/system/checkin/list')
    } catch (error) {
      console.error('签约办理提交失败:', error)
      ElMessage.error(error?.message || '签约办理提交失败')
    }
  }).catch(() => {})
}

const goBack = () => {
  router.back()
}
</script>

<style scoped>
.sign-container {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: 100vh;
}

.steps-header {
  background: #fff;
  padding: 30px;
  margin-bottom: 20px;
  border-radius: 4px;
}

.main-content {
  display: flex;
  gap: 20px;
  max-width: 1400px;
  margin: 0 auto;
}

.left-panel {
  flex: 1;
}

.right-panel {
  width: 300px;
}

.card-title {
  font-weight: bold;
  border-left: 4px solid #409eff;
  padding-left: 10px;
}

.sign-form {
  padding: 20px 0;
  max-width: 600px;
}

.bottom-btns {
  margin-top: 30px;
  padding-top: 20px;
  border-top: 1px solid #eee;
  text-align: right;
}

:deep(.el-tabs__nav-wrap::after) {
  height: 1px;
}

.upload-demo {
  width: 100%;
}
</style>
