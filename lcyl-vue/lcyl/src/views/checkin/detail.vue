<template>
  <div class="detail-container">
    <!-- 步骤条 -->
    <div class="steps-header">
      <el-steps :active="activeStep" finish-status="success" align-center>
        <el-step title="申请入住" />
        <el-step title="入住评估" />
        <el-step title="入住审批" />
        <el-step title="入住配置" />
        <el-step title="签约办理" />
      </el-steps>
    </div>

    <el-card class="main-card">
      <template #header>
        <div class="card-title">入住申请详情</div>
      </template>

      <el-tabs v-model="activeTab" class="custom-tabs">
        <!-- 基本信息 -->
        <el-tab-pane label="基本信息" name="basic">
          <el-descriptions :column="2" border>
            <el-descriptions-item label="老人姓名">{{ basicForm.name || '-' }}</el-descriptions-item>
            <el-descriptions-item label="身份证号">{{ basicForm.idCard || '-' }}</el-descriptions-item>
            <el-descriptions-item label="性别">{{ getGenderLabel(basicForm.gender) || '-' }}</el-descriptions-item>
            <el-descriptions-item label="出生日期">{{ basicForm.birthday || '-' }}</el-descriptions-item>
            <el-descriptions-item label="年龄">{{ basicForm.age || '-' }}</el-descriptions-item>
            <el-descriptions-item label="联系电话">{{ basicForm.phone || '-' }}</el-descriptions-item>
            <el-descriptions-item label="紧急联系人">{{ basicForm.emergencyContact || '-' }}</el-descriptions-item>
            <el-descriptions-item label="紧急联系人电话">{{ basicForm.emergencyPhone || '-' }}</el-descriptions-item>
            <el-descriptions-item label="家庭住址" :span="2">{{ basicForm.address || '-' }}</el-descriptions-item>
          </el-descriptions>
        </el-tab-pane>

        <!-- 家属信息 -->
        <el-tab-pane label="家属信息" name="family">
          <el-descriptions :column="2" border>
            <el-descriptions-item label="家属关系">{{ familyForm.relationship || '-' }}</el-descriptions-item>
            <el-descriptions-item label="家属姓名">{{ familyForm.name || '-' }}</el-descriptions-item>
            <el-descriptions-item label="家属联系电话">{{ familyForm.phone || '-' }}</el-descriptions-item>
            <el-descriptions-item label="家属身份证号">{{ familyForm.idCard || '-' }}</el-descriptions-item>
            <el-descriptions-item label="家属工作单位" :span="2">{{ familyForm.workUnit || '-' }}</el-descriptions-item>
            <el-descriptions-item label="家属住址" :span="2">{{ familyForm.address || '-' }}</el-descriptions-item>
          </el-descriptions>
        </el-tab-pane>

        <!-- 入住信息 -->
        <el-tab-pane label="入住信息" name="checkin">
          <el-descriptions :column="2" border>
            <el-descriptions-item label="预计入住时间">{{ checkinForm.expectedCheckInDate || '-' }}</el-descriptions-item>
            <el-descriptions-item label="入住类型">{{ checkinForm.checkInType || '-' }}</el-descriptions-item>
            <el-descriptions-item label="特殊需求" :span="2">
              <el-tag 
                v-for="need in checkinForm.specialNeeds" 
                :key="need" 
                style="margin-right: 8px; margin-bottom: 8px;"
              >
                {{ need }}
              </el-tag>
              <span v-if="!checkinForm.specialNeeds || checkinForm.specialNeeds.length === 0">-</span>
            </el-descriptions-item>
            <el-descriptions-item label="其他需求" :span="2">{{ checkinForm.otherNeeds || '-' }}</el-descriptions-item>
          </el-descriptions>
        </el-tab-pane>

        <!-- 上传材料 -->
        <el-tab-pane label="上传材料" name="upload">
          <div class="upload-section">
            <div class="upload-item" v-for="(image, index) in imagePaths" :key="index">
              <div class="upload-label">{{ getImageLabel(image.fieldName) }}</div>
              <el-image 
                :src="getImageUrl(image.filePath)" 
                :preview-src-list="[getImageUrl(image.filePath)]"
                fit="contain"
                style="width: 200px; height: 150px; border: 1px solid #dcdfe6; border-radius: 4px;"
                :preview-teleported="true"
              />
            </div>
            <div class="upload-item" v-if="checkResultFiles.length">
              <div class="upload-label">体检报告</div>
              <div>
                <el-link
                  v-for="(file, idx) in checkResultFiles"
                  :key="idx"
                  :href="getFileUrl(file)"
                  target="_blank"
                  type="primary"
                  style="margin-right: 12px;"
                >
                  查看{{ checkResultFiles.length > 1 ? `体检报告${idx + 1}` : '体检报告' }}
                </el-link>
              </div>
            </div>
            <div v-if="!imagePaths || imagePaths.length === 0" class="no-images">
              暂无上传材料
            </div>
          </div>
        </el-tab-pane>

        <!-- 健康评估 -->
        <el-tab-pane label="健康评估" name="health">
          <el-descriptions :column="2" border>
            <el-descriptions-item label="疾病诊断" :span="2">
              <el-tag 
                v-for="disease in healthForm.diseases" 
                :key="disease" 
                type="danger"
                style="margin-right: 8px; margin-bottom: 8px;"
              >
                {{ disease }}
              </el-tag>
              <span v-if="!healthForm.diseases || healthForm.diseases.length === 0">-</span>
            </el-descriptions-item>
            <el-descriptions-item label="用药情况">{{ healthForm.medication || '-' }}</el-descriptions-item>
            <el-descriptions-item label="过敏史">{{ healthForm.allergies || '-' }}</el-descriptions-item>
            <el-descriptions-item label="跌倒情况">{{ healthForm.fallStatus || '-' }}</el-descriptions-item>
            <el-descriptions-item label="走失情况">{{ healthForm.lostStatus || '-' }}</el-descriptions-item>
            <el-descriptions-item label="噎食情况">{{ healthForm.chokeStatus || '-' }}</el-descriptions-item>
            <el-descriptions-item label="自杀倾向">{{ healthForm.suicideStatus || '-' }}</el-descriptions-item>
            <el-descriptions-item label="压力性损伤" :span="2">
              <el-tag 
                v-for="pressure in healthForm.pressure" 
                :key="pressure" 
                type="warning"
                style="margin-right: 8px; margin-bottom: 8px;"
              >
                {{ pressure }}
              </el-tag>
              <span v-if="!healthForm.pressure || healthForm.pressure.length === 0">-</span>
            </el-descriptions-item>
            <el-descriptions-item label="特殊皮肤护理" :span="2">
              <el-tag 
                v-for="skin in healthForm.skinCare" 
                :key="skin" 
                type="info"
                style="margin-right: 8px; margin-bottom: 8px;"
              >
                {{ skin }}
              </el-tag>
              <span v-if="!healthForm.skinCare || healthForm.skinCare.length === 0">-</span>
            </el-descriptions-item>
            <el-descriptions-item label="健康状况评估" :span="2">{{ healthForm.healthAssessment || '-' }}</el-descriptions-item>
            <el-descriptions-item label="体检报告" :span="2">
              <template v-if="checkResultFiles.length">
                <el-link
                  v-for="(file, idx) in checkResultFiles"
                  :key="idx"
                  :href="getFileUrl(file)"
                  target="_blank"
                  type="primary"
                  style="margin-right: 12px;"
                >
                  查看{{ checkResultFiles.length > 1 ? `体检报告${idx + 1}` : '体检报告' }}
                </el-link>
              </template>
              <span v-else>-</span>
            </el-descriptions-item>
          </el-descriptions>
        </el-tab-pane>

        <!-- 能力评估 -->
        <el-tab-pane label="能力评估" name="ability">
          <div class="ability-section">
            <el-descriptions title="评估结果汇总" :column="2" border size="default">
              <el-descriptions-item label="自理能力总分">
                <el-tag type="primary" size="large">{{ calculateAbilityScore() }} 分</el-tag>
              </el-descriptions-item>
              <el-descriptions-item label="能力等级">
                <el-tag :type="getAbilityLevelType()" size="large">{{ getAbilityLevel() }}</el-tag>
              </el-descriptions-item>
              <el-descriptions-item label="精神状态">
                {{ mentalForm.cognitive.length > 0 ? '已评估' : '未评估' }}
              </el-descriptions-item>
              <el-descriptions-item label="感知觉与沟通">
                {{ getPerceptionLevel() }}
              </el-descriptions-item>
              <el-descriptions-item label="社会参与">
                {{ getSocialLevel() }}
              </el-descriptions-item>
              <el-descriptions-item label="护理建议等级">
                <el-tag type="warning">{{ getNursingLevel() }}</el-tag>
              </el-descriptions-item>
            </el-descriptions>

            <div class="ability-questions">
              <h4>日常生活活动能力评估 (ADL)</h4>
              <div class="question-item" v-for="(question, index) in abilityQuestions" :key="index">
                <div class="question-text">{{ question.question }}</div>
                <div class="question-score">
                  得分: 
                  <el-tag :type="getScoreTagType(question.score)">
                    {{ question.score }}分
                  </el-tag>
                </div>
              </div>
            </div>
          </div>
        </el-tab-pane>

        <!-- 评估报告 -->
        <el-tab-pane label="评估报告" name="report">
          <div class="report-section">
            <h4>评估总结</h4>
            <div class="report-content">
              {{ reportContent || '暂无评估报告内容' }}
            </div>
            
            <h4 style="margin-top: 30px;">评估师信息</h4>
            <el-descriptions :column="2" border>
              <el-descriptions-item label="评估师">{{ reportForm.evaluator || '-' }}</el-descriptions-item>
              <el-descriptions-item label="评估时间">{{ reportForm.evaluateTime || '-' }}</el-descriptions-item>
              <el-descriptions-item label="评估机构">{{ reportForm.evaluateOrg || '-' }}</el-descriptions-item>
            </el-descriptions>
          </div>
        </el-tab-pane>
      </el-tabs>

      <!-- 底部按钮 -->
      <div class="bottom-buttons">
        <el-button @click="goBack" size="large">返回</el-button>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getCheckin } from '@/api/system/checkin.js'
import { ElMessage } from 'element-plus'

const route = useRoute()
const router = useRouter()

// 当前步骤（默认为申请阶段）
const activeStep = ref(0)
const activeTab = ref('basic')

// 表单数据（只读）
const basicForm = reactive({
  name: '',
  idCard: '',
  gender: '',
  birthDate: '',
  age: null,
  phone: '',
  emergencyContact: '',
  emergencyPhone: '',
  address: ''
})

const familyForm = reactive({
  relationship: '',
  name: '',
  phone: '',
  idCard: '',
  workUnit: '',
  address: ''
})

const checkinForm = reactive({
  expectedCheckInDate: '',
  checkInType: '',
  specialNeeds: [],
  otherNeeds: ''
})

const healthForm = reactive({
  diseases: [],
  medication: '',
  allergies: '',
  fallStatus: '',
  lostStatus: '',
  chokeStatus: '',
  suicideStatus: '',
  pressure: [],
  skinCare: [],
  healthAssessment: '',
  checkResult: ''
})

const mentalForm = reactive({
  cognitive: [],
  other: ''
})

const perceptionForm = reactive({
  vision: '',
  hearing: '',
  expression: ''
})

const socialForm = reactive({
  livingSkills: [],
  workSkills: [],
  communication: [],
  socialAdaptation: []
})

const reportForm = reactive({
  evaluator: '',
  evaluateTime: '',
  evaluateOrg: ''
})

// 能力评估问题列表
const abilityQuestions = ref([
  {
    question: '进食：使用适当的器具将食物送入嘴中并咽下',
    score: null,
    options: [
      { label: 'A. 独立完成，无需他人帮助（0分）', value: 0 },
      { label: 'B. 需要部分帮助，如切割食物（1分）', value: 1 },
      { label: 'C. 需要他人协助喂食或管饲喂养（2分）', value: 2 },
      { label: 'D. 主要依赖他人协助（3分）', value: 3 },
      { label: 'E. 完全依赖他人帮助（5分）', value: 5 }
    ]
  },
  {
    question: '洗澡：清洗全身或淋浴',
    score: null,
    options: [
      { label: 'A. 独立完成（0分）', value: 0 },
      { label: 'B. 需要部分帮助（1分）', value: 1 },
      { label: 'C. 在他人协助下完成洗浴或淋浴（2分）', value: 2 },
      { label: 'D. 主要依赖他人协助（3分）', value: 3 },
      { label: 'E. 完全依赖他人帮助（5分）', value: 5 }
    ]
  },
  {
    question: '修饰：梳头、刷牙、洗脸、剃须等',
    score: null,
    options: [
      { label: 'A. 独立完成（0分）', value: 0 },
      { label: 'B. 需要部分帮助（1分）', value: 1 },
      { label: 'C. 在他人协助下完成修饰或清洁（2分）', value: 2 },
      { label: 'D. 主要依赖他人协助（3分）', value: 3 },
      { label: 'E. 完全依赖他人帮助（5分）', value: 5 }
    ]
  },
  {
    question: '穿衣：穿脱衣服、系扣子、拉拉链等',
    score: null,
    options: [
      { label: 'A. 独立完成（0分）', value: 0 },
      { label: 'B. 需要部分帮助（1分）', value: 1 },
      { label: 'C. 需要他人协助穿脱衣服（2分）', value: 2 },
      { label: 'D. 主要依赖他人协助（3分）', value: 3 },
      { label: 'E. 完全依赖他人帮助（5分）', value: 5 }
    ]
  },
  {
    question: '大便控制：控制大便排出的能力',
    score: null,
    options: [
      { label: 'A. 能够控制（0分）', value: 0 },
      { label: 'B. 偶尔失禁，每周1次（1分）', value: 1 },
      { label: 'C. 经常失禁，每周2次以上（2分）', value: 2 },
      { label: 'D. 完全失禁（5分）', value: 5 }
    ]
  },
  {
    question: '小便控制：控制小便排出的能力',
    score: null,
    options: [
      { label: 'A. 能够控制（0分）', value: 0 },
      { label: 'B. 偶尔失禁，每周1次（1分）', value: 1 },
      { label: 'C. 经常失禁，每周2次以上（2分）', value: 2 },
      { label: 'D. 完全失禁（5分）', value: 5 }
    ]
  },
  {
    question: '如厕：使用便器或去卫生间，清洁自己',
    score: null,
    options: [
      { label: 'A. 独立完成（0分）', value: 0 },
      { label: 'B. 需要部分帮助（1分）', value: 1 },
      { label: 'C. 需要他人协助（2分）', value: 2 },
      { label: 'D. 主要依赖他人协助（3分）', value: 3 },
      { label: 'E. 完全依赖他人帮助（5分）', value: 5 }
    ]
  },
  {
    question: '床椅转移：在床和椅子之间转移',
    score: null,
    options: [
      { label: 'A. 独立完成（0分）', value: 0 },
      { label: 'B. 需要部分帮助或使用辅助器具（1分）', value: 1 },
      { label: 'C. 需要他人协助（2分）', value: 2 },
      { label: 'D. 需要他人较多协助（3分）', value: 3 },
      { label: 'E. 完全依赖他人帮助（5分）', value: 5 }
    ]
  },
  {
    question: '平地行走：在平地上行走（可使用辅助器具）',
    score: null,
    options: [
      { label: 'A. 独立完成（0分）', value: 0 },
      { label: 'B. 需要使用辅助器具（1分）', value: 1 },
      { label: 'C. 需要他人协助（2分）', value: 2 },
      { label: 'D. 需要他人较多协助（3分）', value: 3 },
      { label: 'E. 完全依赖他人帮助或使用轮椅（5分）', value: 5 }
    ]
  },
  {
    question: '上下楼梯：上下整段楼梯',
    score: null,
    options: [
      { label: 'A. 独立完成（0分）', value: 0 },
      { label: 'B. 需要使用辅助器具或扶手（1分）', value: 1 },
      { label: 'C. 需要他人协助（2分）', value: 2 },
      { label: 'D. 需要他人较多协助（3分）', value: 3 },
      { label: 'E. 无法上下楼梯（5分）', value: 5 }
    ]
  }
])

// 其他数据
const imagePaths = ref([])
const reportContent = ref('')
const baseApi = import.meta.env.VITE_APP_BASE_API || ''
const checkResultFiles = computed(() => {
  const value = healthForm.checkResult
  if (!value) return []
  if (Array.isArray(value)) return value.filter(Boolean)
  return String(value)
    .split(',')
    .map(s => s.trim())
    .filter(Boolean)
})

// 获取性别标签
const getGenderLabel = (genderValue) => {
  const genderMap = {
    '1': '男',
    '0': '女'
  }
  return genderMap[genderValue] || ''
}

// 获取图片标签
const getImageLabel = (fieldName) => {
  const labelMap = {
    'photo': '一寸照片',
    'idCardFront': '身份证正面',
    'idCardBack': '身份证反面',
    'medicalCard': '医保卡',
    'other': '其他材料'
  }
  return labelMap[fieldName] || fieldName
}

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

const relationLabel = (value) => {
  const map = {
    '1': '子女',
    '2': '配偶',
    '3': '亲属',
    '4': '朋友'
  }
  return map[String(value)] || String(value || '')
}

// 获取图片URL
const getImageUrl = (filePath) => {
  if (!filePath) return ''
  let url = String(filePath).trim()
  url = url.replace(/\\/g, '/')

  const joinBaseApi = (path) => {
    let p = path || ''
    if (!p.startsWith('/')) p = '/' + p
    if (baseApi && p.startsWith(baseApi)) return encodeURI(p)
    if (!baseApi) return encodeURI(p)
    return encodeURI(`${baseApi}${p}`)
  }

  if (url.startsWith('http://') || url.startsWith('https://')) {
    try {
      const u = new URL(url)
      return joinBaseApi(u.pathname)
    } catch (e) {
      return encodeURI(url)
    }
  }

  return joinBaseApi(url)
}

const getFileUrl = (filePath) => {
  return getImageUrl(filePath)
}

// 计算能力评估总分
const calculateAbilityScore = () => {
  const scores = abilityQuestions.value.map(q => q.score).filter(score => score !== null)
  return scores.reduce((sum, score) => sum + score, 0)
}

// 获取能力等级
const getAbilityLevel = () => {
  const totalScore = calculateAbilityScore()
  if (totalScore <= 20) return '能力完好'
  if (totalScore <= 40) return '轻度失能'
  if (totalScore <= 60) return '中度失能'
  return '重度失能'
}

// 获取能力等级标签类型
const getAbilityLevelType = () => {
  const level = getAbilityLevel()
  const typeMap = {
    '能力完好': 'success',
    '轻度失能': 'warning',
    '中度失能': 'warning',
    '重度失能': 'danger'
  }
  return typeMap[level] || 'info'
}

// 获取得分标签类型
const getScoreTagType = (score) => {
  if (score === 0) return 'success'
  if (score <= 2) return 'warning'
  return 'danger'
}

// 获取感知觉等级
const getPerceptionLevel = () => {
  if (!perceptionForm.vision && !perceptionForm.hearing && !perceptionForm.expression) {
    return '未评估'
  }
  return '已评估'
}

// 获取社会参与等级
const getSocialLevel = () => {
  if ((!socialForm.livingSkills || socialForm.livingSkills.length === 0) && 
      (!socialForm.workSkills || socialForm.workSkills.length === 0) &&
      (!socialForm.communication || socialForm.communication.length === 0) &&
      (!socialForm.socialAdaptation || socialForm.socialAdaptation.length === 0)) {
    return '未评估'
  }
  return '已评估'
}

// 获取护理等级建议
const getNursingLevel = () => {
  const totalScore = calculateAbilityScore()
  if (totalScore <= 20) return '无需护理'
  if (totalScore <= 40) return '一级护理'
  if (totalScore <= 60) return '二级护理'
  return '三级护理'
}

// 返回上一页
const goBack = () => {
  router.go(-1)
}

// 加载入住申请详情
const loadCheckInDetail = async () => {
  try {
    const checkInId = route.query.checkInId || route.query.businessId || route.params.id
    
    console.log('获取入住申请详情，checkInId:', checkInId)
    
    if (!checkInId) {
      ElMessage.warning('缺少入住申请信息')
      return
    }
    
    const res = await getCheckin(checkInId)
    
    console.log('获取入住申请详情返回结果:', res)
    
    if (res.code === 200 && res.data) {
      console.log('入住申请详情数据:', res.data)
      
      // 更新步骤
      if (res.data.flowStatus !== undefined) {
        activeStep.value = parseInt(res.data.flowStatus)
        console.log('更新步骤:', activeStep.value)
      }
      
      const otherInfo = parseJsonValue(res.data.otherApplyInfo)
      if (otherInfo?.basicForm) {
        Object.assign(basicForm, otherInfo.basicForm)
      }
      if (otherInfo?.checkinForm) {
        Object.assign(checkinForm, otherInfo.checkinForm)
      }

      const reviewInfo = parseJsonValue(res.data.reviewInfo)
      if (reviewInfo?.familyList && Array.isArray(reviewInfo.familyList) && reviewInfo.familyList.length > 0) {
        const first = reviewInfo.familyList[0] || {}
        familyForm.relationship = relationLabel(first.relation)
        familyForm.name = first.name || ''
        familyForm.phone = first.phone || ''
      }

      if (reviewInfo?.imagePaths && typeof reviewInfo.imagePaths === 'object') {
        const list = Object.keys(reviewInfo.imagePaths)
          .map((fieldName) => ({ fieldName, filePath: reviewInfo.imagePaths[fieldName] }))
          .filter(item => item.filePath)
        imagePaths.value = list
      }
      
      // 解析 evaluation 填充评估信息
      if (res.data.evaluation) {
        try {
          // 尝试解析 evaluation，处理可能的双重编码
          let evaluation = res.data.evaluation
          console.log('原始 evaluation 数据:', evaluation)
          if (typeof evaluation === 'string') {
            try {
              evaluation = JSON.parse(evaluation)
              // 检查是否还需要再次解析
              if (typeof evaluation === 'string') {
                evaluation = JSON.parse(evaluation)
              }
            } catch (parseError) {
              console.error('解析 evaluation 失败:', parseError)
              ElMessage.error('解析评估信息失败')
              return
            }
          }
          console.log('解析 evaluation 结果:', evaluation)

          if (evaluation && evaluation.evaluation != null) {
            const inner = parseJsonValue(evaluation.evaluation)
            if (inner) {
              evaluation = inner
              console.log('解包 evaluation.evaluation 结果:', evaluation)
            }
          }
          
          // 检查 evaluation 字段的结构
          console.log('evaluation 字段的所有属性:', Object.keys(evaluation))
          
          // 填充健康评估
          console.log('开始填充健康评估...')
          
          // 检查 healthForm 字段是否存在
          if (evaluation.healthForm) {
            console.log('healthForm 字段存在，开始处理...')
            console.log('healthForm 字段的所有属性:', Object.keys(evaluation.healthForm))
            
            // 处理 medications 字段（复数形式）到 medication 字段（单数形式）的映射
            if (evaluation.healthForm.medications) {
              console.log('处理 medications 字段:', evaluation.healthForm.medications)
              // 将 medications 数组转换为字符串
              healthForm.medication = evaluation.healthForm.medications.map(med => `${med.name} (${med.dosage}, ${med.method})`).join('; ')
              console.log('medication 字段值:', healthForm.medication)
            }
            // 处理其他字段
            if (evaluation.healthForm.diseases) {
              console.log('处理 diseases 字段:', evaluation.healthForm.diseases)
              healthForm.diseases = evaluation.healthForm.diseases
              console.log('diseases 字段值:', healthForm.diseases)
            }
            if (evaluation.healthForm.fallStatus) {
              console.log('处理 fallStatus 字段:', evaluation.healthForm.fallStatus)
              healthForm.fallStatus = evaluation.healthForm.fallStatus
              console.log('fallStatus 字段值:', healthForm.fallStatus)
            }
            if (evaluation.healthForm.lostStatus) {
              console.log('处理 lostStatus 字段:', evaluation.healthForm.lostStatus)
              healthForm.lostStatus = evaluation.healthForm.lostStatus
              console.log('lostStatus 字段值:', healthForm.lostStatus)
            }
            if (evaluation.healthForm.chokeStatus) {
              console.log('处理 chokeStatus 字段:', evaluation.healthForm.chokeStatus)
              healthForm.chokeStatus = evaluation.healthForm.chokeStatus
              console.log('chokeStatus 字段值:', healthForm.chokeStatus)
            }
            if (evaluation.healthForm.suicideStatus) {
              console.log('处理 suicideStatus 字段:', evaluation.healthForm.suicideStatus)
              healthForm.suicideStatus = evaluation.healthForm.suicideStatus
              console.log('suicideStatus 字段值:', healthForm.suicideStatus)
            }
            if (evaluation.healthForm.pressure) {
              console.log('处理 pressure 字段:', evaluation.healthForm.pressure)
              healthForm.pressure = evaluation.healthForm.pressure
              console.log('pressure 字段值:', healthForm.pressure)
            }
            if (evaluation.healthForm.skinCare) {
              console.log('处理 skinCare 字段:', evaluation.healthForm.skinCare)
              healthForm.skinCare = evaluation.healthForm.skinCare
              console.log('skinCare 字段值:', healthForm.skinCare)
            }
            if (evaluation.healthForm.checkResult) {
              console.log('处理 checkResult 字段:', evaluation.healthForm.checkResult)
              healthForm.checkResult = evaluation.healthForm.checkResult
              console.log('checkResult 字段值:', healthForm.checkResult)
            }
            console.log('填充健康评估:', healthForm)
          } else {
            console.log('healthForm 字段不存在')
            // 尝试直接从 evaluation 字段获取健康评估信息
            console.log('尝试直接从 evaluation 字段获取健康评估信息...')
            if (evaluation.medications) {
              console.log('直接处理 medications 字段:', evaluation.medications)
              healthForm.medication = evaluation.medications.map(med => `${med.name} (${med.dosage}, ${med.method})`).join('; ')
              console.log('medication 字段值:', healthForm.medication)
            }
            if (evaluation.diseases) {
              console.log('直接处理 diseases 字段:', evaluation.diseases)
              healthForm.diseases = evaluation.diseases
              console.log('diseases 字段值:', healthForm.diseases)
            }
            // 其他字段的处理...
            console.log('直接填充健康评估:', healthForm)
          }
          
          // 填充能力评估问题
          if (evaluation.abilityQuestions && Array.isArray(evaluation.abilityQuestions)) {
            const scoreMap = new Map()
            evaluation.abilityQuestions.forEach((q) => {
              if (q && q.question != null) {
                scoreMap.set(String(q.question).trim(), q.score)
              }
            })
            abilityQuestions.value.forEach((q) => {
              const key = String(q.question || '').trim()
              if (scoreMap.has(key)) {
                q.score = scoreMap.get(key)
              }
            })
            console.log('填充能力评估问题分数')
          }
          
          // 填充精神状态
          if (evaluation.mentalForm) {
            Object.assign(mentalForm, evaluation.mentalForm)
            console.log('填充精神状态:', mentalForm)
          }
          
          // 填充感知觉与沟通
          if (evaluation.perceptionForm) {
            Object.assign(perceptionForm, evaluation.perceptionForm)
            console.log('填充感知觉与沟通:', perceptionForm)
          }
          
          // 填充社会参与
          if (evaluation.socialForm) {
            // 处理 lifeAbility 字段到 livingSkills 字段的映射
            if (evaluation.socialForm.lifeAbility) {
              socialForm.livingSkills = [evaluation.socialForm.lifeAbility]
            }
            console.log('填充社会参与:', socialForm)
          }
          
          // 填充报告内容
          if (evaluation.reportContent) {
            reportContent.value = evaluation.reportContent
            console.log('填充报告内容:', reportContent.value)
          }
        } catch (e) {
          console.error('解析 evaluation 失败:', e)
          ElMessage.error('解析评估信息失败')
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
  loadCheckInDetail()
})
</script>

<style scoped lang="scss">
.detail-container {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: 100vh;
}

.steps-header {
  background: #fff;
  padding: 30px 40px;
  margin-bottom: 20px;
  border-radius: 4px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.main-card {
  max-width: 1200px;
  margin: 0 auto;
  
  :deep(.el-card__header) {
    background: #f7f8fa;
    border-bottom: 1px solid #e4e7ed;
    padding: 15px 20px;
  }
}

.card-title {
  font-weight: 600;
  font-size: 18px;
  color: #303133;
}

.custom-tabs {
  margin-top: 20px;
  
  :deep(.el-tabs__nav-wrap::after) {
    height: 1px;
  }
}

.upload-section {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  
  .upload-item {
    display: flex;
    flex-direction: column;
    align-items: center;
    margin-bottom: 20px;
    
    .upload-label {
      margin-bottom: 10px;
      font-weight: 500;
      color: #606266;
    }
  }
  
  .no-images {
    color: #909399;
    font-style: italic;
    padding: 40px;
    text-align: center;
  }
}

.ability-section {
  .ability-questions {
    margin-top: 30px;
    
    .question-item {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 12px 0;
      border-bottom: 1px solid #f0f0f0;
      
      .question-text {
        flex: 1;
        color: #606266;
      }
      
      .question-score {
        flex-shrink: 0;
      }
    }
  }
}

.report-section {
  .report-content {
    padding: 15px;
    background: #f8f9fa;
    border-radius: 4px;
    border: 1px solid #ebeef5;
    min-height: 100px;
    white-space: pre-wrap;
    line-height: 1.6;
  }
}

.bottom-buttons {
  margin-top: 40px;
  padding-top: 20px;
  border-top: 1px solid #ebeef5;
  text-align: center;
  
  .el-button {
    min-width: 100px;
    margin: 0 10px;
  }
}

:deep(.el-descriptions__label) {
  font-weight: 600;
  background: #fafafa;
}
</style>
