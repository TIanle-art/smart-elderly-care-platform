<template>
  <div class="admission-container">
    <!-- 步骤条 -->
    <div class="steps-header">
      <el-steps :active="activeStep" finish-status="success" align-center>
        <el-step title="重新申请" />
        <el-step title="入住评估" />
        <el-step title="入住审批" />
        <el-step title="入住配置" />
        <el-step title="签约办理" />
      </el-steps>
    </div>

    <el-card class="main-card">
      <template #header>
        <div class="card-title">
          重新申请入住 - 修改申请资料
          <el-tag type="warning" size="small" style="margin-left: 10px;">申请已被驳回</el-tag>
        </div>
      </template>

      <el-alert
        title="申请被驳回说明"
        type="warning"
        description="您的入住申请已被审批人员驳回，请根据审批意见修改申请资料后重新提交。"
        show-icon
        :closable="false"
        style="margin-bottom: 20px;"
      />

      <el-alert
        :title="'驳回原因：' + (checkInInfo.approveRemark || '无') "
        type="error"
        :closable="false"
        show-icon
        style="margin-bottom: 20px;"
      />

      <el-tabs v-model="activeTab" class="custom-tabs">
        <!-- 基本信息标签页 -->
        <el-tab-pane label="基本信息" name="basic">
          <el-form :model="basicForm" :rules="basicRules" ref="basicFormRef" label-width="120px" class="form-content">
            <el-form-item label="老人姓名" prop="name">
              <el-input v-model="basicForm.name" placeholder="请输入" maxlength="10" show-word-limit />
            </el-form-item>

            <el-form-item label="老人身份证号" prop="idCard">
              <el-input v-model="basicForm.idCard" placeholder="请输入" maxlength="18" show-word-limit @blur="handleIdCardBlur" />
            </el-form-item>

            <el-form-item label="性别">
              <el-radio-group v-model="basicForm.gender">
                <el-radio label="1">男</el-radio>
                <el-radio label="2">女</el-radio>
              </el-radio-group>
            </el-form-item>

            <el-form-item label="出生日期" prop="birthDate">
              <el-date-picker
                v-model="basicForm.birthDate"
                type="date"
                placeholder="请选择出生日期"
                format="YYYY-MM-DD"
                value-format="YYYY-MM-DD"
                :picker-options="pickerOptions"
              />
              <div class="form-tip">用于自动计算年龄</div>
            </el-form-item>

            <el-form-item label="年龄" prop="age">
              <el-input v-model.number="basicForm.age" placeholder="自动计算" readonly>
                <template #append>岁</template>
              </el-input>
            </el-form-item>

            <el-form-item label="联系电话" prop="phone">
              <el-input v-model="basicForm.phone" placeholder="请输入联系电话" maxlength="11" />
            </el-form-item>

            <el-form-item label="紧急联系人" prop="emergencyContact">
              <el-input v-model="basicForm.emergencyContact" placeholder="请输入紧急联系人姓名" maxlength="10" />
            </el-form-item>

            <el-form-item label="紧急联系人电话" prop="emergencyPhone">
              <el-input v-model="basicForm.emergencyPhone" placeholder="请输入紧急联系人电话" maxlength="11" />
            </el-form-item>

            <el-form-item label="家庭住址" prop="address">
              <el-input
                v-model="basicForm.address"
                type="textarea"
                :rows="3"
                placeholder="请输入详细的家庭住址"
                maxlength="200"
                show-word-limit
              />
            </el-form-item>
          </el-form>
        </el-tab-pane>

        <!-- 家属信息标签页 -->
        <el-tab-pane label="家属信息" name="family">
          <el-form :model="familyForm" :rules="familyRules" ref="familyFormRef" label-width="120px" class="form-content">
            <el-form-item label="家属关系" prop="relationship">
              <el-select v-model="familyForm.relationship" placeholder="请选择家属与老人的关系" style="width: 100%">
                <el-option label="配偶" value="配偶" />
                <el-option label="子女" value="子女" />
                <el-option label="兄弟姐妹" value="兄弟姐妹" />
                <el-option label="其他亲属" value="其他亲属" />
                <el-option label="朋友" value="朋友" />
                <el-option label="监护人" value="监护人" />
              </el-select>
            </el-form-item>

            <el-form-item label="家属姓名" prop="name">
              <el-input v-model="familyForm.name" placeholder="请输入家属姓名" maxlength="10" />
            </el-form-item>

            <el-form-item label="家属联系电话" prop="phone">
              <el-input v-model="familyForm.phone" placeholder="请输入家属联系电话" maxlength="11" />
            </el-form-item>

            <el-form-item label="家属身份证号" prop="idCard">
              <el-input v-model="familyForm.idCard" placeholder="请输入家属身份证号" maxlength="18" />
            </el-form-item>

            <el-form-item label="家属工作单位" prop="workUnit">
              <el-input
                v-model="familyForm.workUnit"
                type="textarea"
                :rows="2"
                placeholder="请输入家属工作单位"
                maxlength="100"
                show-word-limit
              />
            </el-form-item>

            <el-form-item label="家属住址" prop="address">
              <el-input
                v-model="familyForm.address"
                type="textarea"
                :rows="2"
                placeholder="请输入家属住址"
                maxlength="200"
                show-word-limit
              />
            </el-form-item>
          </el-form>
        </el-tab-pane>

        <!-- 入住信息标签页 -->
        <el-tab-pane label="入住信息" name="checkin">
          <el-form :model="checkinForm" :rules="checkinRules" ref="checkinFormRef" label-width="120px" class="form-content">
            <el-form-item label="预计入住时间" prop="expectedCheckInDate">
              <el-date-picker
                v-model="checkinForm.expectedCheckInDate"
                type="date"
                placeholder="请选择预计入住时间"
                format="YYYY-MM-DD"
                value-format="YYYY-MM-DD"
              />
            </el-form-item>

            <el-form-item label="入住类型" prop="checkInType">
              <el-radio-group v-model="checkinForm.checkInType">
                <el-radio label="短期">短期（3个月以内）</el-radio>
                <el-radio label="长期">长期（3个月以上）</el-radio>
                <el-radio label="临时">临时（探亲等）</el-radio>
              </el-radio-group>
            </el-form-item>

            <el-form-item label="特殊需求">
              <el-checkbox-group v-model="checkinForm.specialNeeds">
                <el-checkbox label="无障碍设施">无障碍设施</el-checkbox>
                <el-checkbox label="单人间">单人间</el-checkbox>
                <el-checkbox label="双人间">双人间</el-checkbox>
                <el-checkbox label="护理型床位">护理型床位</el-checkbox>
                <el-checkbox label="医疗设备">医疗设备</el-checkbox>
                <el-checkbox label="其他">其他</el-checkbox>
              </el-checkbox-group>
            </el-form-item>

            <el-form-item label="其他需求" prop="otherNeeds">
              <el-input
                v-model="checkinForm.otherNeeds"
                type="textarea"
                :rows="3"
                placeholder="请输入其他特殊需求"
                maxlength="200"
                show-word-limit
              />
            </el-form-item>
          </el-form>
        </el-tab-pane>
      </el-tabs>

      <!-- 底部按钮 -->
      <div class="bottom-buttons">
        <el-button @click="goBack" size="large">取消</el-button>
        <el-button @click="prevStep" :disabled="activeTab === 'basic'" size="large">上一步</el-button>
        <el-button v-if="activeTab !== 'checkin'" @click="nextStep" type="primary" size="large">下一步</el-button>
        <el-button v-else @click="submitReapplication" type="primary" size="large" :loading="submitting">
          提交重新申请
        </el-button>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getCheckin, reapplyCheckin } from '@/api/system/checkin.js'

const route = useRoute()
const router = useRouter()

// Props接收
const props = defineProps({
  taskData: {
    type: Object,
    default: () => ({})
  }
})

// 当前步骤（重新申请 = 0）
const activeStep = ref(0)
const activeTab = ref('basic')
const submitting = ref(false)

// 表单数据
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
  checkInType: '长期',
  specialNeeds: [],
  otherNeeds: ''
})

// 表单引用
const basicFormRef = ref(null)
const familyFormRef = ref(null)
const checkinFormRef = ref(null)

// 入住申请信息
const checkInInfo = ref({})

// 计算年龄的选项
const pickerOptions = {
  disabledDate(time) {
    return time.getTime() > Date.now() // 禁用未来日期
  }
}

// 基本信息校验规则
const basicRules = {
  name: [
    { required: true, message: '请输入老人姓名', trigger: 'blur' },
    { min: 2, max: 10, message: '姓名长度在 2 到 10 个字符', trigger: 'blur' }
  ],
  idCard: [
    { required: true, message: '请输入身份证号', trigger: 'blur' },
    { pattern: /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/, message: '身份证号格式不正确', trigger: 'blur' }
  ],
  birthDate: [
    { required: true, message: '请选择出生日期', trigger: 'change' }
  ],
  phone: [
    { required: true, message: '请输入联系电话', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '手机号码格式不正确', trigger: 'blur' }
  ],
  emergencyPhone: [
    { required: true, message: '请输入紧急联系人电话', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '手机号码格式不正确', trigger: 'blur' }
  ]
}

// 家属信息校验规则
const familyRules = {
  relationship: [
    { required: true, message: '请选择家属关系', trigger: 'change' }
  ],
  name: [
    { required: true, message: '请输入家属姓名', trigger: 'blur' },
    { min: 2, max: 10, message: '姓名长度在 2 到 10 个字符', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入家属联系电话', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '手机号码格式不正确', trigger: 'blur' }
  ],
  idCard: [
    { required: true, message: '请输入家属身份证号', trigger: 'blur' },
    { pattern: /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/, message: '身份证号格式不正确', trigger: 'blur' }
  ]
}

// 入住信息校验规则
const checkinRules = {
  expectedCheckInDate: [
    { required: true, message: '请选择预计入住时间', trigger: 'change' }
  ],
  checkInType: [
    { required: true, message: '请选择入住类型', trigger: 'change' }
  ]
}

// 根据身份证号计算年龄
const handleIdCardBlur = () => {
  const idCard = basicForm.idCard
  if (idCard && /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/.test(idCard)) {
    const birthYear = idCard.length === 15 ? '19' + idCard.substring(6, 8) : idCard.substring(6, 10)
    const birthMonth = idCard.length === 15 ? idCard.substring(8, 10) : idCard.substring(10, 12)
    const birthDay = idCard.length === 15 ? idCard.substring(10, 12) : idCard.substring(12, 14)
    
    const birthDate = new Date(`${birthYear}-${birthMonth}-${birthDay}`)
    const today = new Date()
    let age = today.getFullYear() - birthDate.getFullYear()
    
    if (today.getMonth() < birthDate.getMonth() || 
        (today.getMonth() === birthDate.getMonth() && today.getDate() < birthDate.getDate())) {
      age--
    }
    
    basicForm.age = age
  }
}

// 上一步
const prevStep = () => {
  const tabs = ['basic', 'family', 'checkin']
  const currentIndex = tabs.indexOf(activeTab.value)
  if (currentIndex > 0) {
    activeTab.value = tabs[currentIndex - 1]
  }
}

// 下一步
const nextStep = async () => {
  let currentFormRef = null
  
  switch (activeTab.value) {
    case 'basic':
      currentFormRef = basicFormRef
      break
    case 'family':
      currentFormRef = familyFormRef
      break
    case 'checkin':
      currentFormRef = checkinFormRef
      break
  }
  
  if (currentFormRef) {
    try {
      await currentFormRef.value.validate()
      const tabs = ['basic', 'family', 'checkin']
      const currentIndex = tabs.indexOf(activeTab.value)
      if (currentIndex < tabs.length - 1) {
        activeTab.value = tabs[currentIndex + 1]
      }
    } catch (error) {
      ElMessage.error('请填写完整且正确的信息')
    }
  }
}

// 提交重新申请
const submitReapplication = async () => {
  try {
    // 验证所有表单
    await Promise.all([
      basicFormRef.value.validate(),
      familyFormRef.value.validate(),
      checkinFormRef.value.validate()
    ])
    
    // 确认提交
    await ElMessageBox.confirm(
      '确认提交重新申请吗？提交后将等待审批人员审核。',
      '确认提交',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    submitting.value = true
    
    // 准备提交数据
    const submitData = {
      otherApplyInfo: {
        basicForm: { ...basicForm },
        familyForm: { ...familyForm },
        checkinForm: { ...checkinForm }
      },
      reviewInfo: checkInInfo.value.reviewInfo || '{}',
      isReapply: true // 标识这是重新申请
    }
    
    console.log('准备提交重新申请，数据:', submitData)
    
    // 调用重新申请接口
    const res = await reapplyCheckin(checkInInfo.value.id, submitData)
    
    if (res.code === 200) {
      ElMessage.success('重新申请已提交，等待审批人员审核')
      router.push('/system/checkin/list')
    } else {
      ElMessage.error(res.msg || '重新申请提交失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('重新申请提交失败:', error)
      ElMessage.error('重新申请提交失败：' + (error.message || '未知错误'))
    }
  } finally {
    submitting.value = false
  }
}

// 返回上一页
const goBack = () => {
  router.go(-1)
}

// 加载入住申请详情
const loadCheckInDetail = async () => {
  try {
    // 从路由参数获取入住申请ID
    const checkInId = route.query.checkInId || props.taskData?.id || props.taskData?.checkInId
    
    if (!checkInId) {
      ElMessage.warning('缺少入住申请信息')
      return
    }
    
    const res = await getCheckin(checkInId)
    
    if (res.code === 200 && res.data) {
      checkInInfo.value = res.data
      
      // 如果状态不是已驳回，提示用户
      if (res.data.status !== 4) { // 4表示已驳回
        ElMessage.warning('此申请未被驳回，无法进行重新申请')
        router.push('/system/checkin/list')
        return
      }
      
      // 解析 otherApplyInfo 字段并填充表单
      if (res.data.otherApplyInfo) {
        try {
          const otherInfo = JSON.parse(res.data.otherApplyInfo)
          
          // 填充基本信息
          if (otherInfo.basicForm) {
            Object.assign(basicForm, otherInfo.basicForm)
          }
          
          // 填充家属信息
          if (otherInfo.familyForm) {
            Object.assign(familyForm, otherInfo.familyForm)
          }
          
          // 填充入住信息
          if (otherInfo.checkinForm) {
            Object.assign(checkinForm, otherInfo.checkinForm)
          }
        } catch (e) {
          console.error('解析 otherApplyInfo 失败:', e)
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
.admission-container {
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
  max-width: 1000px;
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
  display: flex;
  align-items: center;
}

.custom-tabs {
  margin-top: 20px;
  
  :deep(.el-tabs__nav-wrap::after) {
    height: 1px;
  }
}

.form-content {
  max-width: 600px;
  margin: 0 auto;
  
  .form-tip {
    font-size: 12px;
    color: #909399;
    margin-top: 5px;
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

:deep(.el-form-item__label) {
  font-weight: 600;
  color: #606266;
}
</style>