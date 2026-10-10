<template>
  <div class="approval-container">
    <!-- 步骤条 -->
    <div class="steps-box">
      <el-steps :active="currentStep" finish-status="success" align-center>
        <el-step title="申请入住" />
        <el-step title="入住评估" />
        <el-step title="入住审批" />
        <el-step title="入住配置" />
        <el-step title="签约办理" />
      </el-steps>
    </div>

    <!-- 主内容区 -->
    <div class="main-content">
      <!-- 左侧：审批详情 -->
      <div class="left-panel">
        <el-card shadow="never" class="detail-card">
          <template #header>
            <div class="card-title">入住审批详情</div>
          </template>

          <el-tabs v-model="activeTab" class="detail-tabs">
            <!-- Tab1: 审批处理 -->
            <el-tab-pane label="审批处理" name="approve">
              <el-form :model="approveForm" :rules="approveRules" ref="approveFormRef" label-width="100px" class="approve-form">
                <el-form-item label="审批结论" prop="approveResult" required>
                  <el-radio-group v-model="approveForm.approveResult">
                    <el-radio label="同意">
                      <el-icon><CircleCheck /></el-icon> 审批通过
                    </el-radio>
                    <el-radio label="拒绝">
                      <el-icon><CircleClose /></el-icon> 审批拒绝
                    </el-radio>
                    <el-radio label="驳回">
                      <el-icon><RefreshLeft /></el-icon> 驳回修改
                    </el-radio>
                  </el-radio-group>
                </el-form-item>

                <el-form-item label="审批意见" prop="approveRemark" required>
                  <el-input
                    v-model="approveForm.approveRemark"
                    type="textarea"
                    :rows="5"
                    placeholder="请输入详细的审批意见..."
                    maxlength="200"
                    show-word-limit
                  />
                </el-form-item>

                <el-form-item label="下一环节">
                  <el-input value="入住配置" disabled style="width: 200px;" />
                </el-form-item>
              </el-form>
            </el-tab-pane>

            <!-- Tab2: 基本资料预览 -->
            <el-tab-pane label="基本资料预览" name="info">
              <div class="info-section">
                <div class="section-title">
                  <el-icon color="#f56c6c"><InfoFilled /></el-icon>
                  基本信息
                </div>
                <el-descriptions :column="2" border size="default">
                  <el-descriptions-item label="老人姓名">{{ checkInInfo.elderName || '-' }}</el-descriptions-item>
                  <el-descriptions-item label="性别">{{ checkInInfo.sex || '-' }}</el-descriptions-item>
                  <el-descriptions-item label="身份证号">{{ checkInInfo.idCardNo || '-' }}</el-descriptions-item>
                  <el-descriptions-item label="出生日期">{{ checkInInfo.birthDate || '-' }}</el-descriptions-item>
                  <el-descriptions-item label="年龄">{{ checkInInfo.age || '-' }}</el-descriptions-item>
                  <el-descriptions-item label="联系电话">{{ checkInInfo.phone || '-' }}</el-descriptions-item>
                </el-descriptions>
              </div>

              <div class="info-section">
                <div class="section-title">
                  <el-icon color="#67c23a"><Document /></el-icon>
                  健康状况
                </div>
                <el-descriptions :column="2" border size="default">
                  <el-descriptions-item label="疾病诊断">{{ checkInInfo.diseases || '-' }}</el-descriptions-item>
                  <el-descriptions-item label="用药情况">{{ checkInInfo.medication || '-' }}</el-descriptions-item>
                  <el-descriptions-item label="过敏史">{{ checkInInfo.allergies || '-' }}</el-descriptions-item>
                  <el-descriptions-item label="跌倒情况">{{ checkInInfo.fallStatus || '-' }}</el-descriptions-item>
                  <el-descriptions-item label="身体健康评估" :span="2">{{ checkInInfo.healthAssessment || '-' }}</el-descriptions-item>
                </el-descriptions>
              </div>

              <div class="info-section">
                <div class="section-title">
                  <el-icon color="#409eff"><TrendCharts /></el-icon>
                  能力评估
                </div>
                <el-descriptions :column="2" border size="default">
                  <el-descriptions-item label="自理能力评分">{{ checkInInfo.selfCareScore || '-' }}</el-descriptions-item>
                  <el-descriptions-item label="能力等级">
                    <el-tag :type="getAbilityTagType(checkInInfo.abilityLevel)">
                      {{ checkInInfo.abilityLevel || '-' }}
                    </el-tag>
                  </el-descriptions-item>
                  <el-descriptions-item label="精神状态">{{ checkInInfo.mentalStatus || '-' }}</el-descriptions-item>
                  <el-descriptions-item label="感知觉">{{ checkInInfo.perception || '-' }}</el-descriptions-item>
                  <el-descriptions-item label="社会参与">{{ checkInInfo.socialParticipation || '-' }}</el-descriptions-item>
                  <el-descriptions-item label="护理等级建议">
                    <el-tag type="warning">{{ checkInInfo.nursingLevel || '-' }}</el-tag>
                  </el-descriptions-item>
                </el-descriptions>
              </div>
            </el-tab-pane>
          </el-tabs>

          <!-- 底部按钮 -->
          <div class="bottom-btns">
            <el-button @click="goBack" size="large">取消</el-button>
            <el-button type="primary" @click="submitApproval" size="large" :loading="submitLoading">
              提交审批
            </el-button>
          </div>
        </el-card>
      </div>

      <!-- 右侧：操作记录 -->
      <div class="right-panel">
        <el-card shadow="never" class="record-card">
          <template #header>
            <div class="card-title">操作记录</div>
          </template>
          
          <el-timeline>
            <el-timeline-item
              v-for="(record, index) in operationRecords"
              :key="index"
              :timestamp="record.time"
              :type="record.type"
              placement="top"
            >
              <div class="record-title">{{ record.title }}</div>
              <div class="record-operator">操作人：{{ record.operator }}</div>
              <div v-if="record.remark" class="record-remark">{{ record.remark }}</div>
            </el-timeline-item>

            <el-timeline-item timestamp="现在" type="primary" hollow>
              <div class="record-title">待审批</div>
              <div class="record-operator">当前环节：{{ checkInInfo.currentTaskName || '院办审批' }}</div>
            </el-timeline-item>
          </el-timeline>
        </el-card>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { CircleCheck, CircleClose, RefreshLeft, InfoFilled, Document, TrendCharts } from '@element-plus/icons-vue'
import { getCheckin, approveCheckin } from '@/api/system/checkin.js'
import { formatDate } from '@/utils/index.js'

const route = useRoute()
const router = useRouter()

// Props接收
const props = defineProps({
  taskData: {
    type: Object,
    default: () => ({})
  },
  viewMode: {
    type: Boolean,
    default: false
  }
})

// 当前步骤（入住审批 = 2）
const currentStep = ref(2)
const activeTab = ref('approve')
const submitLoading = ref(false)

// 入住申请信息
const checkInInfo = ref({})
const currentCheckInId = ref(null)
const reviewInfo = ref({})

// 审批表单
const approveForm = reactive({
  approveResult: '同意',
  approveRemark: ''
})

// 表单校验规则
const approveRules = {
  approveResult: [
    { required: true, message: '请选择审批结论', trigger: 'change' }
  ],
  approveRemark: [
    { required: true, message: '请填写审批意见', trigger: 'blur' },
    { min: 3, max: 200, message: '审批意见长度在 3 到 200 个字符', trigger: 'blur' }
  ]
}

const approveFormRef = ref(null)

// 操作记录（初始值，将从后端加载）
const operationRecords = ref([])

const parseJsonValue = (value) => {
  if (!value) return null
  if (typeof value === 'object') return value
  try {
    const parsed = JSON.parse(value)
    if (typeof parsed === 'string') return JSON.parse(parsed)
    return parsed
  } catch (e) {
    return null
  }
}

const sexLabel = (value) => {
  const v = String(value ?? '')
  if (v === '0') return '男'
  if (v === '1') return '女'
  return value || '-'
}

// 获取能力等级标签类型
const getAbilityTagType = (level) => {
  const typeMap = {
    '能力完好': 'success',
    '轻度失能': 'warning',
    '中度失能': 'danger',
    '重度失能': 'danger'
  }
  return typeMap[level] || 'info'
}

// 返回上一页
const goBack = () => {
  router.back()
}

// 提交审批
const submitApproval = () => {
  approveFormRef.value.validate((valid) => {
    if (!valid) {
      return false
    }

    // 根据审批结果确定提示信息
    let confirmTitle = '确认提交';
    let confirmMessage = `确认提交审批结果吗？<br/>审批结论：<strong>${approveForm.approveResult}</strong>`;
    
    if (approveForm.approveResult === '驳回') {
      confirmTitle = '确认驳回';
      confirmMessage = `确认驳回该申请吗？<br/>审批结论：<strong>${approveForm.approveResult}</strong><br/>申请人将收到通知并可修改后重新提交。`;
    }

    ElMessageBox.confirm(
      confirmMessage,
      confirmTitle,
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: approveForm.approveResult === '驳回' ? 'warning' : 'warning',
        dangerouslyUseHTMLString: true
      }
    ).then(async () => {
      try {
        submitLoading.value = true

        console.log('准备提交审批，参数:', {
          checkInId: currentCheckInId.value,
          approveResult: approveForm.approveResult,
          approveRemark: approveForm.approveRemark
        })

        // 调用后端审批接口
        const res = await approveCheckin(currentCheckInId.value, {
          approveResult: approveForm.approveResult,
          approveRemark: approveForm.approveRemark
        })

        // 审批通过时老人由后端根据申请单自动创建并关联，无需前端再调 addElder
        console.log('审批接口返回:', res)

        if (res.code === 200) {
          // 根据审批结果给出不同提示
          if (approveForm.approveResult === '驳回') {
            ElMessage.success('申请已驳回，已通知申请人修改后重新提交');
          } else {
            ElMessage.success('审批已提交，流程已流转至下一环节');
          }

          // 延迟跳转到任务列表
          setTimeout(() => {
            router.push('/system/checkin/list')
          }, 1500)
        } else {
          ElMessage.error(res.msg || '审批提交失败')
        }
      } catch (error) {
        console.error('审批提交失败:', error)
        ElMessage.error('审批提交失败：' + error.message)
      } finally {
        submitLoading.value = false
      }
    }).catch(() => {
      console.log('用户取消审批')
    })
  })
}

// 加载入住申请详情
const loadCheckInDetail = async () => {
  try {
    // 优先从 props 获取
    currentCheckInId.value = props.taskData?.checkInId || props.taskData?.id || route.query.checkInId || route.query.businessId

    console.log('审批页面 - 获取的 checkInId:', currentCheckInId.value)
    console.log('审批页面 - props.taskData:', props.taskData)
    console.log('审批页面 - route.query:', route.query)

    if (!currentCheckInId.value) {
      ElMessage.warning('缺少入住申请信息')
      return
    }

    const res = await getCheckin(currentCheckInId.value)
    console.log('审批页面 - 接口返回数据:', res)

    if (res.code === 200 && res.data) {
      checkInInfo.value = res.data

      // 解析 otherApplyInfo（JSON 字符串）
      if (res.data.otherApplyInfo && res.data.reviewInfo) {
        try {
          const otherInfo = parseJsonValue(res.data.otherApplyInfo)
          console.log('解析的 otherApplyInfo:', otherInfo)
          const reviewInfo = parseJsonValue(res.data.reviewInfo)
          console.log('解析的 reviewInfo:', reviewInfo)

          // 基本信息
          if (otherInfo.basicForm) {
            checkInInfo.value.elderName = otherInfo.basicForm.name
            checkInInfo.value.idCardNo = otherInfo.basicForm.idCard
            checkInInfo.value.sex = sexLabel(otherInfo.basicForm.gender)
            checkInInfo.value.birthDate = otherInfo.basicForm.birthday
            checkInInfo.value.age = otherInfo.basicForm.age
            checkInInfo.value.phone = otherInfo.basicForm.phone
            checkInInfo.value.createBy= res.data.applicat
            checkInInfo.value.createTime = res.data.createTime
            console.log(res.data.createTime)
          }
        } catch (e) {
          console.error('解析 otherApplyInfo 失败:', e)

        }
      }

      if (res.data.reviewInfo) {
        try {
          const reviewInfo = parseJsonValue(res.data.reviewInfo)
          console.log('解析的 reviewInfo:', reviewInfo)

          // 基本信息
          if (reviewInfo.imagePaths) {
            checkInInfo.value.image = reviewInfo.imagePaths.photo
          }
        } catch (e) {
          console.error('解析 reviewInfo 失败:', e)
        }
      }

      // 解析 evaluation 字段（评估信息）
      if (res.data.evaluation) {
        try {
          let evaluation = parseJsonValue(res.data.evaluation)
          if (evaluation && evaluation.evaluation != null) {
            const inner = parseJsonValue(evaluation.evaluation)
            if (inner) evaluation = inner
          }

          console.log('解析的 evaluation 数据:', evaluation)

          // 健康状况
          if (evaluation.healthForm) {
            // 疾病诊断
            checkInInfo.value.diseases = Array.isArray(evaluation.healthForm?.diseases)
              ? evaluation.healthForm.diseases.join('、')
              : (evaluation.healthForm?.diseases || '-')

            checkInInfo.value.medication = Array.isArray(evaluation.healthForm?.medications) && evaluation.healthForm.medications.length
              ? evaluation.healthForm.medications
                  .map(m => {
                    const name = m?.name || '未知药物'
                    const method = m?.method ? `（${m.method}` : ''
                    const dosage = m?.dosage ? `${m?.method ? '，' : '（'}${m.dosage}` : ''
                    const suffix = (method || dosage) ? `${method}${dosage}）` : ''
                    return `${name}${suffix}`
                  })
                  .join('、')
              : '-'

            checkInInfo.value.allergies = evaluation.healthForm?.allergies || '-'
            checkInInfo.value.fallStatus = evaluation.healthForm?.fallStatus || '-'
            checkInInfo.value.healthAssessment = evaluation.reportContent || evaluation.healthForm?.healthAssessment || '-'

          }

          // 能力评估
          if (evaluation.abilityQuestions && Array.isArray(evaluation.abilityQuestions)) {
            // 计算自理能力总分
            const totalScore = evaluation.abilityQuestions.reduce((sum, q) => sum + (q.score || 0), 0)
            checkInInfo.value.selfCareScore = totalScore || '-'
            
            // 根据总分计算能力等级
            if (totalScore === 0) {
              checkInInfo.value.abilityLevel = '能力完好'
            } else if (totalScore <= 20) {
              checkInInfo.value.abilityLevel = '轻度失能'
            } else if (totalScore <= 40) {
              checkInInfo.value.abilityLevel = '中度失能'
            } else {
              checkInInfo.value.abilityLevel = '重度失能'
            }

          }

          // 精神状态评估
          if (evaluation.mentalForm) {
            checkInInfo.value.mentalStatus = evaluation.mentalForm.cognitive?.join('、') || '-'
          }

          // 感知觉与沟通
          if (evaluation.perceptionForm) {
            checkInInfo.value.perception = `视力: ${evaluation.perceptionForm.vision || '-'}; 听力: ${evaluation.perceptionForm.hearing || '-'}; 沟通: ${evaluation.perceptionForm.expression || '-'}`
          }

          // 社会参与
          if (evaluation.socialForm) {
            checkInInfo.value.socialParticipation = evaluation.socialForm.lifeAbility || '-'
          }

          // 护理等级
          if (evaluation.abilityQuestions && Array.isArray(evaluation.abilityQuestions)) {
            const totalScore = evaluation.abilityQuestions.reduce((sum, q) => sum + (q.score || 0), 0)
            if (totalScore === 0) {
              checkInInfo.value.nursingLevel = '特级护理'
            } else if (totalScore >= 1 && totalScore <= 15) {
              checkInInfo.value.nursingLevel = '一级护理'
            } else if (totalScore >= 16 && totalScore <= 30) {
              checkInInfo.value.nursingLevel = '二级护理'
            } else if (totalScore >= 31) {
              checkInInfo.value.nursingLevel = '三级护理'
            } else {
              checkInInfo.value.nursingLevel = '待评估'
            }
          }

          // 操作记录（从评估数据中提取）
          if (evaluation.reportForm && evaluation.reportForm.evaluator) {
            operationRecords.value = [
              {
                title: '发起申请',
                operator: res.data.applicat || '申请人',
                time: res.data.createTime || '未知时间',
                type: 'success'
              },
              {
                title: '完成评估',
                operator: evaluation.reportForm.evaluator || '评估师',
                time: evaluation.reportForm.evaluateTime || '评估时间',
                type: 'success',
                remark: `评估结果：${evaluation.abilityForm?.abilityLevel || '待评估'}`
              }
            ]
          }
        } catch (e) {
          console.error('解析 evaluation 失败:', e)
        }
      }

      // 更新步骤条
      if (res.data.flowStatus !== undefined) {
        currentStep.value = parseInt(res.data.flowStatus)
      }

      // 如果 evaluation 中没有操作记录，则尝试从 reviewInfo 解析
      if (operationRecords.value.length === 0 && res.data.reviewInfo) {
        try {
          const reviewData = JSON.parse(res.data.reviewInfo)
          if (reviewData.records && Array.isArray(reviewData.records)) {
            operationRecords.value = reviewData.records
          }
        } catch (e) {
          console.error('解析 reviewInfo 失败:', e)
        }
      }
    } else {
      ElMessage.error(res.msg || '加载入住申请信息失败')
    }
  } catch (error) {
    console.error('加载入住申请详情失败:', error)
    ElMessage.error('加载入住申请详情失败')
  }
}

onMounted(() => {
  console.log('审批组件已挂载')
  loadCheckInDetail()
})
</script>

<style scoped lang="scss">
.approval-container {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: 100vh;
}

.steps-box {
  background: #fff;
  padding: 30px;
  margin-bottom: 20px;
  border-radius: 4px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
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
  width: 320px;
}

.detail-card,
.record-card {
  border-radius: 4px;
  
  :deep(.el-card__header) {
    background: #f7f8fa;
    border-bottom: 1px solid #e4e7ed;
    padding: 15px 20px;
  }
}

.card-title {
  font-weight: 600;
  font-size: 16px;
  color: #303133;
  border-left: 4px solid #409eff;
  padding-left: 12px;
}

.detail-tabs {
  margin-top: 20px;
}

.approve-form {
  padding: 20px 0;
  
  :deep(.el-radio) {
    display: flex;
    align-items: center;
    margin-right: 30px;
    margin-bottom: 12px;
    
    .el-icon {
      margin-right: 5px;
    }
  }
}

.info-section {
  margin-bottom: 30px;
  
  &:last-child {
    margin-bottom: 0;
  }
}

.section-title {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 15px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 15px;
  padding-bottom: 10px;
  border-bottom: 2px solid #f0f2f5;
}

.bottom-btns {
  margin-top: 40px;
  padding-top: 20px;
  border-top: 1px solid #ebeef5;
  text-align: right;
  
  .el-button {
    min-width: 100px;
  }
}

.record-title {
  font-weight: 600;
  color: #303133;
  margin-bottom: 5px;
  font-size: 14px;
}

.record-operator {
  font-size: 13px;
  color: #909399;
}

.record-remark {
  font-size: 13px;
  color: #606266;
  margin-top: 5px;
  padding: 5px 10px;
  background: #f5f7fa;
  border-radius: 4px;
}

:deep(.el-tabs__nav-wrap::after) {
  height: 1px;
}

:deep(.el-descriptions__label) {
  font-weight: 600;
  background: #fafafa;
}

:deep(.el-timeline-item__timestamp) {
  font-size: 12px;
  color: #909399;
}
</style>
