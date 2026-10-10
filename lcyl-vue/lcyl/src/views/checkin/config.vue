<template>
  <div class="config-container">
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
            <div class="card-title">选择入住配置</div>
          </template>

          <!-- 1. 入住设置 -->
          <div class="section-title">入住设置</div>
          <el-form ref="configFormRef" :model="configForm" :rules="configRules" label-width="140px" class="config-form">
            <el-form-item label="入住期限" prop="checkInDateRange" required>
              <el-date-picker
                v-model="configForm.checkInDateRange"
                type="daterange"
                range-separator="-"
                start-placeholder="请选择开始日期"
                end-placeholder="请选择结束日期"
                value-format="YYYY-MM-DD"
                :disabled-date="disabledStartDate"
                style="width: 100%"
                @change="onCheckInDateRangeChange"
              />
            </el-form-item>
            <el-form-item label="护理等级" prop="nursingLevelId" required>
              <el-select v-model="configForm.nursingLevelId" placeholder="请选择" style="width: 100%" @change="onNursingLevelChange">
                <el-option
                  v-for="item in nursingLevelList"
                  :key="item.id"
                  :label="item.levelName"
                  :value="item.id"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="入住床位" prop="bedId" required>
              <div v-if="!selectedBed" class="bed-select-trigger">
                <el-button type="primary" @click="openBedSelect">选择入住床位</el-button>
              </div>
              <div v-else class="bed-selected">
                <span class="bed-text">{{ selectedBed.displayText }}</span>
                <el-button type="primary" link @click="openBedSelect">重新选择</el-button>
              </div>
            </el-form-item>
          </el-form>

          <!-- 2. 费用设置 -->
          <div class="section-title">费用设置</div>
          <el-alert type="info" :closable="false" show-icon class="fee-tip">
            该费用为月账单，每月1号收当月费用
          </el-alert>
          <el-form ref="configFormRef2" :model="configForm" :rules="configRules" label-width="160px" class="config-form">
            <el-form-item label="费用期限" prop="costDateRange" required>
              <el-date-picker
                v-model="configForm.costDateRange"
                type="daterange"
                range-separator="-"
                start-placeholder="请选择开始日期"
                end-placeholder="请选择结束日期"
                value-format="YYYY-MM-DD"
                :disabled-date="disabledStartDate"
                style="width: 100%"
                @change="onCostDateRangeChange"
              />
            </el-form-item>
            <el-form-item label="押金(元)">
              <el-input-number v-model="configForm.depositAmount" :min="0" :precision="2" :controls="false" style="width: 100%" placeholder="3000.00" />
            </el-form-item>
            <el-form-item label="护理费用(元/月)" prop="nursingCost" required>
              <el-input-number
                v-model="configForm.nursingCost"
                :min="0"
                :precision="2"
                :controls="false"
                style="width: 100%"
                placeholder="0.00"
                :disabled="!canEditNursingCost"
              />
              <div v-if="nursingCostError" class="form-error">{{ nursingCostError }}</div>
            </el-form-item>
            <el-form-item label="床位费用(元/月)" prop="bedCost" required>
              <el-input-number
                v-model="configForm.bedCost"
                :min="0"
                :precision="2"
                :controls="false"
                style="width: 100%"
                placeholder="0.00"
                :disabled="!canEditBedCost"
              />
              <div v-if="bedCostError" class="form-error">{{ bedCostError }}</div>
            </el-form-item>
            <el-form-item label="其他费用(元/月)">
              <el-input-number v-model="configForm.otherCost" :min="0" :precision="2" :controls="false" style="width: 100%" />
            </el-form-item>
            <el-form-item label="医保支付(元/月)">
              <el-input-number v-model="configForm.medicalInsurancePayment" :min="0" :precision="2" :controls="false" style="width: 100%" />
            </el-form-item>
            <el-form-item label="政府补贴(元/月)">
              <el-input-number v-model="configForm.governmentSubsidy" :min="0" :precision="2" :controls="false" style="width: 100%" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" plain @click="openBillPreview">账单预览</el-button>
            </el-form-item>
          </el-form>

          <div class="bottom-btns">
            <el-button @click="goBack">返回</el-button>
            <el-button type="primary" @click="submitConfig">提交</el-button>
          </div>
        </el-card>
      </div>

      <div class="right-panel">
        <el-card shadow="never">
          <template #header><span>操作记录</span></template>
          <el-timeline>
            <el-timeline-item
              v-for="(record, index) in timelineList"
              :key="index"
              :timestamp="record.time"
              :type="record.type"
            >
              <h4>{{ record.title }}</h4>
              <p>操作人：{{ record.operator }}</p>
              <p v-if="record.status">{{ record.status }}</p>
            </el-timeline-item>
          </el-timeline>
        </el-card>
      </div>
    </div>

    <!-- 床位选择弹窗：样式与【床位房型】一致，仅可选床位、不可增删改查；楼层 tab + 房间网格 + 仅空闲床位可点击 -->
    <el-dialog v-model="bedSelectVisible" title="床位选择" width="960px" destroy-on-close @close="onBedSelectClose" class="bed-select-dialog">
      <div class="bed-select-content">
        <el-tabs v-model="activeFloorId" type="card" @tab-click="onFloorTabChange">
          <el-tab-pane
            v-for="floor in floorList"
            :key="floor.id"
            :label="floor.name"
            :name="String(floor.id)"
          >
            <div v-loading="bedLoading" class="room-grid">
              <el-row :gutter="16">
                <el-col v-for="room in roomsOfActiveFloor" :key="room.id" :xs="24" :sm="12" :md="8" :lg="6">
                  <div class="room-card-select">
                    <div class="room-header-select">
                      <span class="room-code">{{ room.code }}</span>
                      <span class="room-type">{{ room.typeName }}</span>
                    </div>
                    <div class="bed-list-select">
                      <div
                        v-for="bed in bedsByRoomId(room.id)"
                        :key="bed.id"
                        class="bed-item-select"
                        :class="{ 'selected': currentBedRow && currentBedRow.id === bed.id }"
                        @click="onFreeBedClick(bed)"
                      >
                        <span class="bed-num">{{ bed.bedNumber }}</span>
                        <span class="bed-label">空闲床位</span>
                      </div>
                      <div v-if="bedsByRoomId(room.id).length === 0" class="no-free-bed">无空闲床位</div>
                    </div>
                  </div>
                </el-col>
              </el-row>
              <el-empty v-if="roomsOfActiveFloor.length === 0 && !bedLoading" description="该楼层暂无房间" :image-size="80" />
              <div v-else-if="bedLoaded && totalFreeBedsInFloor === 0" class="no-free-tip">该楼层暂无空闲床位</div>
            </div>
          </el-tab-pane>
        </el-tabs>
      </div>
      <template #footer>
        <el-button @click="bedSelectVisible = false">返回</el-button>
        <el-button type="primary" @click="confirmBedSelect">保存</el-button>
      </template>
    </el-dialog>

    <!-- 账单预览弹窗（图四：本期应付、账单金额等） -->
    <el-dialog v-model="billPreviewVisible" title="账单预览" width="560px" destroy-on-close>
      <div class="bill-preview">
        <div class="bill-info">
          <p>账单月份：{{ billPreview.billMonth }}</p>
          <p>老人姓名：{{ billPreview.elderName }}</p>
          <p>账单周期：{{ billPreview.costRange }}</p>
          <p>共计天数：{{ billPreview.days }}天</p>
        </div>
        <div class="bill-table-label">添加项</div>
        <el-table :data="billPreview.addItems" border size="small">
          <el-table-column label="类型" prop="type" width="100" />
          <el-table-column label="费用项目" prop="type" width="100" />
          <el-table-column label="服务内容" prop="name" />
          <el-table-column label="金额(元)" prop="amount" width="100" align="right" />
        </el-table>
        <div class="bill-summary">小计：{{ billPreview.addTotal }} 元</div>
        <div class="bill-table-label">扣减项</div>
        <el-table :data="billPreview.deductItems" border size="small">
          <el-table-column label="类型" prop="type" width="100" />
          <el-table-column label="费用项目" prop="type" width="100" />
          <el-table-column label="服务内容" prop="name" />
          <el-table-column label="金额(元)" prop="amount" width="100" align="right" />
        </el-table>
        <div class="bill-summary">小计：{{ billPreview.deductTotal }} 元</div>
        <div class="bill-total">
          <p>每月应付：{{ billPreview.monthlyPay }} 元</p>
          <p>本期应付：{{ billPreview.periodPay }} 元</p>
          <p>押金：{{ billPreview.deposit }} 元</p>
          <p>账单金额：{{ billPreview.billAmount }} 元</p>
        </div>
      </div>



    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, watch, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getCheckin } from '@/api/system/checkin.js'
import { submitCheckInConfig } from '@/api/system/checkInConfig.js'
// import { listBed } from '@/api/system/bed'
import { listFloor, listRoom, listBed } from '@/api/system/floor'
// import { listRoom } from '@/api/system/room'
import { listNursingLevel } from '@/api/code/nursingLevel'
import { listType } from '@/api/system/type'
import { ElMessage, ElMessageBox } from 'element-plus'

// 接收父组件传递的任务数据
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

const route = useRoute()
const router = useRouter()

const activeStep = ref(3)
const checkInId = ref(null) // 入住ID，也是业务ID
const businessId = ref(null) // 业务ID
const elderId = ref(null)
const elderName = ref('')
const taskId = ref(null) // 任务ID

const configFormRef = ref(null)
const configFormRef2 = ref(null)

const configForm = reactive({
  checkInDateRange: null,
  costDateRange: null,
  nursingLevelId: null,
  depositAmount: 3000,
  nursingCost: null,
  bedCost: null,
  otherCost: 0,
  medicalInsurancePayment: 0,
  governmentSubsidy: 0
})

const configRules = {
  checkInDateRange: [{ required: true, message: '请选择入住期限', trigger: 'change' }],
  nursingLevelId: [{ required: true, message: '请选择护理等级', trigger: 'change' }],
  bedId: [{ required: true, message: '请选择入住床位', trigger: 'change' }],
  costDateRange: [{ required: true, message: '请选择费用期限', trigger: 'change' }],
  nursingCost: [{ required: true, message: '请填写护理费用', trigger: 'blur' }],
  bedCost: [{ required: true, message: '请填写床位费用', trigger: 'blur' }]
}

const nursingLevelList = ref([])
const roomTypeList = ref([]) // 房型列表，用于床位费用自动带出
const originalNursingFee = ref(null)
const originalBedFee = ref(null)
const nursingCostError = ref('')
const bedCostError = ref('')

const canEditNursingCost = computed(() => configForm.nursingLevelId != null)
const canEditBedCost = computed(() => selectedBed.value != null)

const selectedBed = ref(null)
const floorList = ref([])
const roomList = ref([])
const freeBedList = ref([])
const bedSelectVisible = ref(false)
const currentBedRow = ref(null)
/** 床位选择弹窗：楼层 tab，与床位房型页面样式一致 */
const activeFloorId = ref('')
const floorLoading = ref(false)
const roomLoading = ref(false)
const bedLoading = ref(false)
const bedLoaded = ref(false)

/** 当前楼层下的房间列表 */
const roomsOfActiveFloor = computed(() => {
  if (!activeFloorId.value) return roomList.value
  return roomList.value.filter(r => String(r.floorId) === String(activeFloorId.value))
})

/** 当前楼层空闲床位数 */
const totalFreeBedsInFloor = computed(() => {
  const roomIds = roomsOfActiveFloor.value.map(r => r.id)
  return freeBedList.value.filter(b => roomIds.includes(b.roomId)).length
})

const timelineList = ref([
  { title: '发起申请-申请入住', operator: '-', status: '(已发起)', time: '-' },
  { title: '(角色) 处理-入住评估', operator: '-', status: '(已处理)', time: '-' }
])

const billPreviewVisible = ref(false)
const billPreview = reactive({
  billMonth: '',
  elderName: '',
  costRange: '',
  days: 0,
  addItems: [],
  deductItems: [],
  addTotal: '0.00',
  deductTotal: '0.00',
  monthlyPay: '0.00',
  periodPay: '0.00',
  deposit: '0.00',
  billAmount: '0.00'
})

function disabledStartDate(time) {
  return time.getTime() < Date.now() - 24 * 60 * 60 * 1000
}

function getFloorName(roomId) {
  const room = roomList.value.find(r => r.id === roomId)
  if (!room) return '-'
  const floor = floorList.value.find(f => f.id === room.floorId)
  return floor ? floor.name : '-'
}

/** 入住期限变更时同步费用期限（图三图四：保持一致） */
function onCheckInDateRangeChange(val) {
  if (val && val.length === 2) {
    configForm.costDateRange = [val[0], val[1]]
  }
}

/** 费用期限变更时同步入住期限 */
function onCostDateRangeChange(val) {
  if (val && val.length === 2) {
    configForm.checkInDateRange = [val[0], val[1]]
  }
}

/** 按房间ID取该房间下的空闲床位（用于床位选择弹窗） */
function bedsByRoomId(roomId) {
  return freeBedList.value.filter(b => b.roomId === roomId)
}

/** 点击空闲床位（仅空闲可点，已入住/请假中不展示在弹窗内） */
function onFreeBedClick(bed) {
  currentBedRow.value = bed
}

function onFloorTabChange() {
  currentBedRow.value = null
}

function loadCheckIn() {
  // 优先从props获取任务ID
  if (props.taskData && props.taskData.taskId) {
    taskId.value = props.taskData.taskId
  }
  // 其次从路由参数获取任务ID
  else if (route.query.taskId) {
    taskId.value = route.query.taskId
  }
  
  // 获取入住ID（业务ID）
  const id = props.taskData?.checkInId || props.taskData?.businessId || route.query.checkInId || route.params.checkInId || route.query.businessId || route.params.businessId
  if (!id) return
  
  // 设置入住ID和业务ID
  checkInId.value = id
  businessId.value = id
  
  getCheckin(id).then(res => {
    if (res.code === 200 && res.data) {
      elderId.value = res.data.elderId
      elderName.value = res.data.elderName || ''
      // 如果接口返回了任务ID，则更新
      if (res.data.taskId && !taskId.value) {
        taskId.value = res.data.taskId
      }
      if (res.data.configInfo) {
        Object.assign(configForm, res.data.configInfo)
      }
    }
  }).catch(() => {})
}

function loadNursingLevels() {
  listNursingLevel({ status: 0 }).then(res => {
    if (res.code === 200 && res.rows) {
      nursingLevelList.value = res.rows
    }
  }).catch(() => {})
}

/** 加载房型列表，用于选择床位后自动带出床位费用 */
function loadRoomTypes() {
  listType({ pageNum: 1, pageSize: 500 }).then(res => {
    if (res.code === 200 && res.rows) {
      roomTypeList.value = res.rows
    } else {
      roomTypeList.value = []
    }
  }).catch(() => { roomTypeList.value = [] })
}

function onNursingLevelChange(val) {
  const item = nursingLevelList.value.find(n => n.id === val)
  if (item && item.monthlyFee != null) {
    originalNursingFee.value = Number(item.monthlyFee)
    configForm.nursingCost = originalNursingFee.value
  } else {
    originalNursingFee.value = null
    configForm.nursingCost = null
  }
  nursingCostError.value = ''
}

/** 从数据库加载楼层列表（床位选择弹窗内 tab） */
function loadFloors() {
  floorLoading.value = true
  return listFloor({ pageNum: 1, pageSize: 500 }).then(res => {
    if (res.code === 200 && res.rows) {
      floorList.value = res.rows
      if (floorList.value.length > 0 && bedSelectVisible.value && !activeFloorId.value) {
        activeFloorId.value = String(floorList.value[0].id)
      }
    } else {
      floorList.value = []
    }
  }).catch(() => {
    floorList.value = []
  }).finally(() => {
    floorLoading.value = false
  })
}

/** 从数据库加载房间列表（床位选择弹窗内按楼层 tab 展示） */
function loadRooms() {
  roomLoading.value = true
  listRoom({ pageNum: 1, pageSize: 500 }).then(res => {
    if (res.code === 200 && res.rows) {
      roomList.value = res.rows
    } else {
      roomList.value = []
    }
  }).catch(() => {
    roomList.value = []
  }).finally(() => {
    roomLoading.value = false
  })
}

/** 从数据库加载空闲床位列表：bed 表 status=0 表示未入住，status=1 表示已入住 */
function loadFreeBeds() {
  bedLoading.value = true
  bedLoaded.value = false
  listBed({ statusQuery: 0, pageNum: 1, pageSize: 500 }).then(res => {
    if (res.code === 200 && res.rows) {
      freeBedList.value = res.rows
    } else {
      freeBedList.value = []
    }
  }).catch(() => {
    freeBedList.value = []
  }).finally(() => {
    bedLoading.value = false
    bedLoaded.value = true
  })
}

function openBedSelect() {
  roomList.value = []
  freeBedList.value = []
  bedLoaded.value = false
  currentBedRow.value = null
  bedSelectVisible.value = true
  activeFloorId.value = ''
  loadFloors().then(() => {
    if (floorList.value.length > 0 && !activeFloorId.value) {
      activeFloorId.value = String(floorList.value[0].id)
    }
  })
  loadRooms()
  loadFreeBeds()
}

function onBedSelectClose() {
  currentBedRow.value = null
}

function confirmBedSelect() {
  if (!currentBedRow.value) {
    ElMessage.warning('请选择一张床位')
    return
  }
  const r = currentBedRow.value
  const floorName = getFloorName(r.roomId)
  selectedBed.value = {
    id: r.id,
    roomId: r.roomId,
    roomCode: r.roomCode,
    bedNumber: r.bedNumber,
    roomTypeName: r.roomTypeName,
    displayText: `${r.bedNumber}`
  }
  /** 选择床位后自动带出床位费用（图三：根据房型价格，无需手填） */
  const roomType = roomTypeList.value.find(rt => rt.id === r.id)
  if (roomType && roomType.price != null) {
    const fee = Number(roomType.price)
    originalBedFee.value = fee
    configForm.bedCost = fee
  } else {
    originalBedFee.value = 0
    configForm.bedCost = 0
  }
  bedCostError.value = ''
  bedSelectVisible.value = false
}

watch(() => configForm.nursingCost, (val) => {
  if (originalNursingFee.value == null) return
  const v = Number(val)
  const low = originalNursingFee.value * 0.9
  const high = originalNursingFee.value * 1.1
  if (v < low || v > high) {
    nursingCostError.value = '该费用已超出/低于原费用10%，请重新输入'
  } else {
    nursingCostError.value = ''
  }
})

watch(() => configForm.bedCost, (val) => {
  if (originalBedFee.value == null) return
  const v = Number(val)
  const low = originalBedFee.value * 0.9
  const high = originalBedFee.value * 1.1
  if (v < low || v > high) {
    bedCostError.value = '该费用已超出/低于原费用10%，请重新输入'
  } else {
    bedCostError.value = ''
  }
})

function openBillPreview() {
  if (!configForm.costDateRange || configForm.costDateRange.length !== 2) {
    ElMessage.warning('请先选择费用期限')
    return
  }
  const [start, end] = configForm.costDateRange
  const startDate = new Date(start)
  const endDate = new Date(end)
  const startMonth = start ? String(start).slice(0, 7) : ''
  const startYear = startDate.getFullYear()
  const startMonthNum = startDate.getMonth()
  const endMonthNum = endDate.getMonth()
  const endYear = endDate.getFullYear()
  // 图四：若费用期限开始月与结束月相同，账单周期结束=费用结束日；否则=开始日期月份的最后一 day
  let periodEnd = end
  if (startMonthNum !== endMonthNum || startYear !== endYear) {
    const lastDay = new Date(startYear, startMonthNum + 1, 0)
    periodEnd = lastDay.getFullYear() + '-' + String(lastDay.getMonth() + 1).padStart(2, '0') + '-' + String(lastDay.getDate()).padStart(2, '0')
  }
  const periodStart = start
  const daysInMonth = new Date(startYear, startMonthNum + 1, 0).getDate()
  billPreview.billMonth = startMonth
  billPreview.elderName = elderName.value || '-'
  billPreview.costRange = `${periodStart}—${periodEnd}`
  billPreview.days = periodStart && periodEnd ? Math.floor((new Date(periodEnd) - new Date(periodStart)) / (24 * 60 * 60 * 1000)) + 1 : 0
  billPreview.addItems = [
    { type: '护理费用', name: nursingLevelList.value.find(n => n.id === configForm.nursingLevelId)?.name || '护理', amount: (configForm.nursingCost ?? 0).toFixed(2) },
    { type: '床位费用', name: selectedBed.value?.roomTypeName || '床位', amount: (configForm.bedCost ?? 0).toFixed(2) },
    { type: '其他费用', name: '其他', amount: (configForm.otherCost ?? 0).toFixed(2) }
  ]
  billPreview.deductItems = [
    { type: '医保支付', name: '—', amount: (configForm.medicalInsurancePayment ?? 0).toFixed(2) },
    { type: '政府补贴', name: '—', amount: (configForm.governmentSubsidy ?? 0).toFixed(2) }
  ]
  const addTotal = (configForm.nursingCost ?? 0) + (configForm.bedCost ?? 0) + (configForm.otherCost ?? 0)
  const deductTotal = (configForm.medicalInsurancePayment ?? 0) + (configForm.governmentSubsidy ?? 0)
  billPreview.addTotal = addTotal.toFixed(2)
  billPreview.deductTotal = deductTotal.toFixed(2)
  // 图四：若扣减项小计>添加项小计，每月应付显示 0.00
  const monthlyPay = Math.max(0, addTotal - deductTotal)
  billPreview.monthlyPay = monthlyPay.toFixed(2)
  // 本期应付 = 每月应付 * (共计天数/当月天数)
  const periodPay = daysInMonth > 0 ? (monthlyPay * billPreview.days / daysInMonth) : 0
  billPreview.periodPay = periodPay.toFixed(2)
  const deposit = Number(configForm.depositAmount ?? 3000)
  billPreview.deposit = deposit.toFixed(2)
  billPreview.billAmount = (periodPay + deposit).toFixed(2)
  billPreviewVisible.value = true
}

function submitConfig() {
  if (!selectedBed.value) {
    ElMessage.warning('请选择入住床位')
    return
  }
  if (!configForm.checkInDateRange || configForm.checkInDateRange.length !== 2) {
    ElMessage.warning('请选择入住期限')
    return
  }
  if (!configForm.costDateRange || configForm.costDateRange.length !== 2) {
    ElMessage.warning('请选择费用期限')
    return
  }
  if (configForm.nursingLevelId == null) {
    ElMessage.warning('请选择护理等级')
    return
  }
  if (nursingCostError.value || bedCostError.value) {
    ElMessage.warning('请修正费用后再提交')
    return
  }
  if (elderId.value == null) {
    ElMessage.warning('未获取到老人信息，无法提交')
    return
  }

  const [checkInStart, checkInEnd] = configForm.checkInDateRange
  const [costStart, costEnd] = configForm.costDateRange

  const data = {
    checkInId: checkInId.value,
    businessId: businessId.value, // 业务ID
    taskId: taskId.value, // 任务ID
    elderId: elderId.value,
    bedId: selectedBed.value.id,
    bedNo: selectedBed.value.displayText,
    checkInStartTime: checkInStart,
    checkInEndTime: checkInEnd,
    nursingLevelId: configForm.nursingLevelId,
    costStartTime: costStart,
    costEndTime: costEnd,
    depositAmount: configForm.depositAmount ?? 3000,
    nursingCost: configForm.nursingCost,
    bedCost: configForm.bedCost,
    otherCost: configForm.otherCost ?? 0,
    medicalInsurancePayment: configForm.medicalInsurancePayment ?? 0,
    governmentSubsidy: configForm.governmentSubsidy ?? 0
  }

  ElMessageBox.confirm('确认提交入住配置？提交后将占用所选床位。', '配置确认', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    submitCheckInConfig(data).then(res => {
      if (res.code === 200) {
        ElMessage.success('配置已保存，床位已占用')
        // 延迟跳转到任务列表，让用户看到成功提示
        setTimeout(() => {
          router.push('/system/checkin/list')
        }, 1500)
      } else {
        ElMessage.error(res.msg || '提交失败')
      }
    }).catch(() => {
      ElMessage.error('提交失败')
    })
  }).catch(() => {})
}

function goBack() {
  router.back()
}

onMounted(() => {
  loadCheckIn()
  loadNursingLevels()
  loadRoomTypes()
  loadFloors()
})
</script>

<style scoped>
.config-container {
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
  max-width: 1200px;
  margin: 0 auto;
}

.left-panel {
  flex: 1;
}

.right-panel {
  width: 320px;
}

.card-title {
  font-weight: bold;
  border-left: 4px solid #409eff;
  padding-left: 10px;
}

.section-title {
  font-weight: 600;
  margin: 20px 0 12px;
  color: #303133;
}

.section-title:first-of-type {
  margin-top: 0;
}

.config-form {
  padding: 10px 0;
  max-width: 560px;
}

.fee-tip {
  margin-bottom: 16px;
}

.bed-select-trigger .el-button {
  width: 100%;
}

.bed-selected {
  display: flex;
  align-items: center;
  gap: 12px;
}

.bed-text {
  color: #303133;
}

.form-error {
  color: #f56c6c;
  font-size: 12px;
  margin-top: 4px;
}

.bottom-btns {
  margin-top: 30px;
  padding-top: 20px;
  border-top: 1px solid #eee;
  text-align: right;
}

/* 床位选择弹窗：与床位房型页面样式一致，仅选床位 */
.bed-select-dialog .bed-select-content {
  min-height: 360px;
}
.bed-select-dialog .room-grid {
  padding: 12px 0;
}
.room-card-select {
  border: 1px solid #ebeef5;
  border-radius: 6px;
  padding: 12px;
  margin-bottom: 16px;
  background: #fafafa;
}
.room-header-select {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
  padding-bottom: 8px;
  border-bottom: 1px solid #eee;
}
.room-header-select .room-code {
  font-weight: 600;
  color: #303133;
}
.room-header-select .room-type {
  font-size: 12px;
  color: #909399;
}
.bed-list-select {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}
.bed-item-select {
  min-width: 80px;
  padding: 8px 12px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  background: #fff;
  cursor: pointer;
  text-align: center;
  transition: all 0.2s;
}
.bed-item-select:hover {
  border-color: #409eff;
  color: #409eff;
}
.bed-item-select.selected {
  border-color: #409eff;
  background: #ecf5ff;
  color: #409eff;
}
.bed-item-select .bed-num {
  display: block;
  font-weight: 500;
}
.bed-item-select .bed-label {
  display: block;
  font-size: 12px;
  color: #909399;
}
.bed-item-select.selected .bed-label {
  color: #409eff;
}
.no-free-bed,
.no-free-tip {
  font-size: 12px;
  color: #909399;
  padding: 8px 0;
}

.bill-table-label {
  margin-top: 12px;
  margin-bottom: 6px;
  font-weight: 600;
  color: #303133;
}
.bill-table-label:first-of-type {
  margin-top: 0;
}

.bill-preview {
  padding: 0 8px;
}

.bill-info p {
  margin: 8px 0;
  color: #606266;
}

.bill-summary {
  margin: 12px 0;
  text-align: right;
  font-weight: 600;
}

.bill-total {
  margin-top: 16px;
  padding-top: 12px;
  border-top: 1px solid #ebeef5;
}

.bill-total p {
  margin: 6px 0;
}

:deep(.el-tabs__nav-wrap::after) {
  height: 1px;
}
</style>
