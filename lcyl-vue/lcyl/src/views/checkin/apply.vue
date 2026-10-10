<template>
  <div class="admission-container">
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
        <div class="card-title">补全申请资料</div>
      </template>

      <el-tabs v-model="activeTab" class="custom-tabs">
        <el-tab-pane label="基本信息" name="basic">
          <el-form :model="basicForm" :rules="basicRules" ref="basicFormRef" label-width="120px" class="form-content">
            <el-form-item label="老人姓名" prop="name">
              <el-input v-model="basicForm.name" placeholder="请输入" maxlength="10" show-word-limit :disabled="props.viewMode" />
            </el-form-item>

            <el-form-item label="老人身份证号" prop="idCard">
              <el-input v-model="basicForm.idCard" placeholder="请输入" maxlength="18" show-word-limit @blur="handleIdCardBlur" :disabled="props.viewMode" />
            </el-form-item>

            <el-form-item label="性别">
              <el-radio-group v-model="basicForm.gender" disabled>
                <el-radio label="1">男</el-radio>
                <el-radio label="0">女</el-radio>
              </el-radio-group>
            </el-form-item>

            <el-form-item label="出生日期">
              <el-date-picker v-model="basicForm.birthday" type="date" placeholder="自动计算" disabled style="width: 100%" />
            </el-form-item>

            <el-form-item label="年龄">
              <el-input v-model="basicForm.age" placeholder="自动计算" disabled />
            </el-form-item>

            <el-form-item label="联系方式" prop="phone">
              <el-input v-model="basicForm.phone" placeholder="请输入" maxlength="11" show-word-limit />
            </el-form-item>

            <el-form-item label="所在地区" prop="region">
              <el-cascader
                  v-model="basicForm.region"
                  :options="regionOptions"
                  placeholder="请选择省/市/区"
                  style="width: 100%"
                  clearable
              />
            </el-form-item>

            <el-form-item label="详细住址" prop="address">
              <el-input
                  v-model="basicForm.address"
                  type="textarea"
                  :rows="2"
                  placeholder="请输入街道门牌号等详细信息"
                  maxlength="100"
                  show-word-limit
              />
            </el-form-item>
            <el-form-item label="民族">
              <el-select v-model="basicForm.nation" placeholder="请选择" style="width: 100%">
                <el-option v-for="item in nations" :key="item" :label="item" :value="item" />
              </el-select>
            </el-form-item>
            <el-form-item label="政治面貌">
              <el-select v-model="basicForm.politicalStatus" placeholder="请选择" style="width: 100%">
                <el-option v-for="item in politicalOptions" :key="item" :label="item" :value="item" />
              </el-select>
            </el-form-item>

            <el-form-item label="宗教信仰">
              <el-select v-model="basicForm.religion" placeholder="请选择" style="width: 100%">
                <el-option v-for="item in religionOptions" :key="item" :label="item" :value="item" />
              </el-select>
            </el-form-item>

            <el-form-item label="婚姻状况">
              <el-select v-model="basicForm.marriage" placeholder="请选择" style="width: 100%">
                <el-option v-for="item in marriageOptions" :key="item" :label="item" :value="item" />
              </el-select>
            </el-form-item>

            <el-form-item label="文化程度">
              <el-select v-model="basicForm.education" placeholder="请选择" style="width: 100%">
                <el-option v-for="item in educationOptions" :key="item" :label="item" :value="item" />
              </el-select>
            </el-form-item>

            <el-form-item label="经济来源">
              <el-select v-model="basicForm.incomeSource" placeholder="请选择" style="width: 100%">
                <el-option v-for="item in incomeOptions" :key="item" :label="item" :value="item" />
              </el-select>
            </el-form-item>

            <el-form-item label="特长爱好">
              <el-input
                  v-model="basicForm.hobby"
                  placeholder="请输入"
                  maxlength="20"
                  show-word-limit
              />
            </el-form-item>

            <el-form-item label="医疗保障">
              <el-select v-model="basicForm.medicalInsurance" placeholder="请选择" style="width: 100%">
                <el-option v-for="item in medicalInsuranceOptions" :key="item" :label="item" :value="item" />
              </el-select>
            </el-form-item>

            <el-form-item label="医保卡号">
              <el-input
                  v-model="basicForm.medicalCardNo"
                  placeholder="请输入"
                  maxlength="19"
                  show-word-limit
              />
            </el-form-item>
          </el-form>
        </el-tab-pane>

        <el-tab-pane label="家属信息" name="family">
          <el-table :data="familyList" border style="width: 100%">
            <el-table-column label="家属姓名">
              <template #default="scope">
                <el-input v-model="scope.row.name" maxlength="10" show-word-limit />
              </template>
            </el-table-column>
            <el-table-column label="家属联系方式">
              <template #default="scope">
                <el-input v-model="scope.row.phone" maxlength="11" show-word-limit />
              </template>
            </el-table-column>
            <el-table-column label="与老人关系">
              <template #default="scope">
                <el-select v-model="scope.row.relation" placeholder="请选择">
                  <el-option label="子女" value="1" />
                  <el-option label="配偶" value="2" />
                  <el-option label="亲属" value="3" />
                  <el-option label="朋友" value="4" />
                </el-select>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="100">
              <template #default="scope">
                <el-button v-if="familyList.length > 1" type="danger" link @click="removeFamily(scope.$index)">
                  <el-icon><Minus /></el-icon>
                </el-button>
                <el-button v-if="scope.$index === 0" type="primary" link @click="addFamily">
                  <el-icon><Plus /></el-icon>
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>

        <el-tab-pane label="资料上传" name="upload">
          <el-alert title="图片大小不超过2M，仅支持上传PNG JPG JPEG类型图片" type="info" show-icon :closable="false" />
          <div class="upload-section">
            <div class="upload-item">
              <span class="required-star">*</span>一寸照片
              <el-upload
                  class="uploader"
                  action="/dev-api/common/upload"
                  list-type="picture-card"
                  :file-list="fileLists.photo"
                  :on-success="(response, file, fileList) => handleUploadSuccess(response, file, fileList, 'photo')"
                  :on-remove="(file, fileList) => handleRemove(file, fileList, 'photo')"
                  :before-upload="beforeUpload"
                  :headers="{ 'Authorization': 'Bearer ' + getToken() }"
                  :limit="1"
              >
                <el-icon><Plus /></el-icon>
              </el-upload>
            </div>
            <div class="upload-item">
              <span class="required-star">*</span>身份证人像面
              <el-upload
                  class="uploader"
                  action="/dev-api/common/upload"
                  list-type="picture-card"
                  :file-list="fileLists.idCardFront"
                  :on-success="(response, file, fileList) => handleUploadSuccess(response, file, fileList, 'idCardFront')"
                  :on-remove="(file, fileList) => handleRemove(file, fileList, 'idCardFront')"
                  :before-upload="beforeUpload"
                  :headers="{ 'Authorization': 'Bearer ' + getToken() }"
                  :limit="1"
              >
                <el-icon><Plus /></el-icon>
              </el-upload>
            </div>
            <div class="upload-item">
              <span class="required-star">*</span>身份国徽面
              <el-upload
                  class="uploader"
                  action="/dev-api/common/upload"
                  list-type="picture-card"
                  :file-list="fileLists.idCardBack"
                  :on-success="(response, file, fileList) => handleUploadSuccess(response, file, fileList, 'idCardBack')"
                  :on-remove="(file, fileList) => handleRemove(file, fileList, 'idCardBack')"
                  :before-upload="beforeUpload"
                  :headers="{ 'Authorization': 'Bearer ' + getToken() }"
                  :limit="1"
              >
                <el-icon><Plus /></el-icon>
              </el-upload>
            </div>
          </div>
        </el-tab-pane>
      </el-tabs>

      <div class="footer-btns">
        <el-button @click="handleBack">{{ props.viewMode ? '关闭' : '返回' }}</el-button>
        <el-button v-if="!props.viewMode" type="primary" @click="handleNext">
          {{ activeTab === 'upload' ? '提交' : '保存' }}
        </el-button>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { Plus, Minus } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { regionData } from 'element-china-area-data'
import { applyCheckin, getCheckin, reapplyCheckin } from '@/api/system/checkin.js'
import { getToken } from '@/utils/auth.js'
import { useRoute, useRouter } from 'vue-router'

// 接收 props
const props = defineProps({
  viewMode: {
    type: Boolean,
    default: false
  },
  taskData: {
    type: Object,
    default: () => ({})
  }
})

const router = useRouter()
const route = useRoute()
/** * 提示：实际开发建议安装并在顶部引入地区数据
 * npm install element-china-area-data
 * import { regionData } from 'element-china-area-data'
 */
const regionOptions = ref(regionData)

const activeStep = computed(() => {
  return stepMap[0]
})
const activeTab = ref('basic')
const baseApi = import.meta.env.VITE_APP_BASE_API || ''
const currentCheckInId = ref(null)
const isReapplyMode = ref(false)

// 图片路径数据
const imagePaths = reactive({
  photo: '',        // 一寸照片路径
  idCardFront: '',  // 身份证人像面路径
  idCardBack: ''    // 身份证国徽面路径
})

// 上传文件列表
const fileLists = reactive({
  photo: [],        // 一寸照片文件列表
  idCardFront: [],  // 身份证人像面文件列表
  idCardBack: []    // 身份证国徽面文件列表
})
const stepMap = {
  apply: 0,
  evaluate: 1,
  approve: 2,
  config: 3,
  sign: 4
}

// 1. 基本信息数据
const basicForm = reactive({
  name: '',
  idCard: '',
  gender: '',
  birthday: '',
  age: '',
  phone: '',
  region: [],
  address: '',
  nation: '',

  // ===== 新增补齐字段 =====
  politicalStatus: '', // 政治面貌
  religion: '',        // 宗教信仰
  marriage: '',        // 婚姻状况
  education: '',       // 文化程度
  incomeSource: '',    // 经济来源
  hobby: '',           // 特长爱好
  medicalInsurance: '',// 医疗保障
  medicalCardNo: ''    // 医保卡号
})

const basicRules = {
  name: [{ required: true, message: '请输入老人姓名', trigger: 'blur' }],
  idCard: [{ required: true, pattern: /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/, message: '请输入正确的身份证号', trigger: 'blur' }],
  phone: [{ required: true, message: '请输入联系方式', trigger: 'blur' }],
  region: [{ required: true, message: '请选择所在地区', trigger: 'change' }],
  address: [{ required: true, message: '请输入详细住址', trigger: 'blur' }]
}

const nations =  [
  "汉族","蒙古族","回族","藏族","维吾尔族","苗族","彝族","壮族","布依族","朝鲜族",
  "满族","侗族","瑶族","白族","土家族","哈尼族","哈萨克族","傣族","黎族","僳僳族",
  "佤族","畲族","高山族","拉祜族","水族","东乡族","纳西族","景颇族","柯尔克孜族",
  "土族","达斡尔族","仫佬族","羌族","布朗族","撒拉族","毛南族","仡佬族","锡伯族",
  "阿昌族","普米族","塔吉克族","怒族","乌孜别克族","俄罗斯族","鄂温克族","德昂族",
  "保安族","裕固族","京族","塔塔尔族","独龙族","鄂伦春族","赫哲族","门巴族",
  "珞巴族","基诺族","张梦族"
];
const politicalOptions = [
  '群众','中共党员','中共预备党员','共青团员','民主党派','无党派人士','sb张梦'
]

const religionOptions = [
  '无','佛教','道教','伊斯兰教','基督教','天主教','张教','其他'
]

const marriageOptions = [
  '未婚','已婚','丧偶','离异'
]

const educationOptions = [
  '小学','初中','高中','中专','大专','本科','研究生及以上','文盲','张梦'
]

const incomeOptions = [
  '退休金','子女供养','社会救助','个人储蓄','其他'
]

const medicalInsuranceOptions = [
  '城镇职工医保','城乡居民医保','商业保险','无'
]


// 2. 家属信息数据
const familyList = ref([{ name: '', phone: '', relation: '' }])

// --- 逻辑处理 ---

// 身份证失去焦点逻辑：提取性别、生日、年龄
const handleIdCardBlur = () => {
  const id = basicForm.idCard
  if (id && id.length === 18) {
    const birthStr = `${id.substring(6, 10)}-${id.substring(10, 12)}-${id.substring(12, 14)}`
    basicForm.birthday = birthStr
    basicForm.gender = parseInt(id.charAt(16)) % 2 === 0 ? '0' : '1'
    const year = new Date().getFullYear()
    basicForm.age = year - parseInt(id.substring(6, 10))
  }
}

// 家属动态操作
const addFamily = () => familyList.value.push({ name: '', phone: '', relation: '' })
const removeFamily = (index) => familyList.value.splice(index, 1)

// 图片上传前的验证
const beforeUpload = (file) => {
  const isImage = file.type === 'image/jpeg' || file.type === 'image/png' || file.type === 'image/jpg'
  const isLt2M = file.size / 1024 / 1024 < 2

  if (!isImage) {
    ElMessage.error('只能上传 PNG/JPG/JPEG 格式的图片!')
    return false
  }
  if (!isLt2M) {
    ElMessage.error('图片大小不能超过 2MB!')
    return false
  }
  return true
}

// 图片上传成功处理
const handleUploadSuccess = (response, file, fileList, type) => {
  if (response.code === 200) {
    imagePaths[type] = response.fileName || response.url
    // 更新文件列表，确保只保留最新的文件
    fileLists[type] = fileList.map(item => ({
      name: item.name,
      url: item.url || response.url
    }))
    ElMessage.success('上传成功')
  } else {
    ElMessage.error(response.msg || '上传失败')
  }
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

const buildDisplayUrl = (path) => {
  if (!path) return ''
  let url = String(path).trim().replace(/\\/g, '/')
  const joinBaseApi = (p) => {
    let v = p || ''
    if (!v.startsWith('/')) v = '/' + v
    if (baseApi && v.startsWith(baseApi)) return encodeURI(v)
    if (!baseApi) return encodeURI(v)
    return encodeURI(`${baseApi}${v}`)
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

const loadForReapply = async (checkInId) => {
  const res = await getCheckin(checkInId)
  if (res.code !== 200 || !res.data) return
  const otherInfo = parseJsonValue(res.data.otherApplyInfo)
  if (otherInfo?.basicForm) {
    Object.assign(basicForm, otherInfo.basicForm)
  }
  const reviewInfo = parseJsonValue(res.data.reviewInfo)
  if (reviewInfo?.familyList && Array.isArray(reviewInfo.familyList) && reviewInfo.familyList.length > 0) {
    familyList.value = reviewInfo.familyList.map(item => ({
      name: item.name || '',
      phone: item.phone || '',
      relation: item.relation || ''
    }))
  }
  if (reviewInfo?.imagePaths && typeof reviewInfo.imagePaths === 'object') {
    Object.keys(imagePaths).forEach((k) => {
      imagePaths[k] = reviewInfo.imagePaths[k] || ''
      fileLists[k] = imagePaths[k]
        ? [{
            name: String(imagePaths[k]).split('/').pop(),
            url: buildDisplayUrl(imagePaths[k])
          }]
        : []
    })
  }
}

onMounted(async () => {
  if (props.viewMode) return
  const checkInId = props.taskData?.checkInId || route.query.checkInId || route.query.businessId
  if (!checkInId) return
  currentCheckInId.value = checkInId
  try {
    const res = await getCheckin(checkInId)
    if (res.code === 200 && res.data && res.data.status === 4) {
      isReapplyMode.value = true
      await loadForReapply(checkInId)
    }
  } catch (e) {
  }
})

// 处理文件删除
const handleRemove = (file, fileList, type) => {
  fileLists[type] = fileList
  if (fileList.length === 0) {
    imagePaths[type] = ''
  }
  return true
}

// 表单引用
const basicFormRef = ref(null)

// 验证家属信息
const validateFamily = () => {
  for (let i = 0; i < familyList.value.length; i++) {
    const family = familyList.value[i]
    if (!family.name) {
      ElMessage.error(`请填写第${i + 1}位家属的姓名`)
      return false
    }
    if (!family.phone) {
      ElMessage.error(`请填写第${i + 1}位家属的联系方式`)
      return false
    }
    if (!family.relation) {
      ElMessage.error(`请选择第${i + 1}位家属与老人的关系`)
      return false
    }
  }
  return true
}

// 验证资料上传
const validateUpload = () => {
  // 这里可以根据实际需求添加上传文件的验证逻辑

  return true
}

// 底部按钮逻辑
const handleNext = async () => {
  if (activeTab.value === 'basic') {
    // 验证基本信息表单
    if (!basicFormRef.value) return

    try {
      await basicFormRef.value.validate()
      activeTab.value = 'family'
      ElMessage.success('保存成功，进入下一环节')
    } catch (error) {
      // 验证失败，不执行后续操作
      return
    }
  } else if (activeTab.value === 'family') {
    // 验证家属信息

    activeTab.value = 'upload'
    ElMessage.success('保存成功，进入资料上传')
  } else {
    // 提交前验证所有信息
    // if (!validateUpload()) {
    //   return
    // }
    const payload = {
      otherApplyInfo: {
        basicForm: basicForm
      },
      reviewInfo: {
        familyList: familyList.value,
        imagePaths: imagePaths
      }
    }

    if (isReapplyMode.value && currentCheckInId.value) {
      reapplyCheckin(currentCheckInId.value, payload).then(res => {
        if (res.code === 200) {
          ElMessage.success('重新提交成功，流程继续流转')
          setTimeout(() => {
            router.push({
              path: '/checkin/processing',
              query: {
                checkInId: currentCheckInId.value
              }
            })
          }, 300)
        } else {
          ElMessage.error(res.msg || '重新提交失败')
        }
      }).catch(error => {
        console.error('重新提交失败:', error)
        ElMessage.error('重新提交失败，请重试')
      })
      return
    }

    // 提交申请（新开流程）
    applyCheckin(payload).then(res => {
      console.log('提交申请返回:', res)
      
      if (res.code === 200) {
        ElMessage.success('提交申请成功！')
        
        // 获取返回的 checkInId
        const checkInId = res.data?.checkInId || res.data?.id
        
        console.log('获取到的 checkInId:', checkInId)
        
        // 跳转到成功页面
        setTimeout(() => {
          router.push({
            path: '/checkin/success',
            query: {
              checkInId: checkInId
            }
          })
        }, 500)
      } else {
        ElMessage.error(res.msg)
      }
    }).catch(error => {
      console.error('提交失败:', error)
      ElMessage.error('提交失败，请重试')
    })
  }
}

const handleBack = () => {
  if (props.viewMode) {
    // 只读模式下直接返回
    router.back()
    return
  }
  
  if (activeTab.value === 'family') {
    activeTab.value = 'basic'
  } else if (activeTab.value === 'upload') {
    activeTab.value = 'family'
  } else {
    // 在基本信息页点击返回，可以返回列表页或执行其他操作
    // 这里暂时不做处理
  }
}
</script>

<style scoped>
/* 样式保持不变 */
.admission-container { padding: 20px; background: #f5f7fa; min-height: 100vh; }
.steps-header { background: #fff; padding: 30px; margin-bottom: 20px; }
.main-card { max-width: 1000px; margin: 0 auto; }
.card-title { font-weight: bold; border-left: 4px solid #409eff; padding-left: 10px; }
.form-content { max-width: 600px; margin: 30px 0; }
.upload-section { margin-top: 20px; display: flex; gap: 40px; }
.upload-item { position: relative; }
.required-star { color: #f56c6c; margin-right: 4px; }
.footer-btns { text-align: center; margin-top: 40px; border-top: 1px solid #eee; padding-top: 20px; }
:deep(.el-tabs__item) { font-size: 16px; }
</style>
